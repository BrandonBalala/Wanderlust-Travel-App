package com.wanderlust.travelproject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import com.bob.travelproject.R;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class CurrencyConverterActivity extends Activity {
	private static final String TAG = "CurrencyConverterActivity";
	private static final int MAXBYTES = 60;
	private EditText initialAmountEditText;
	private TextView resultAmountTextView, initialCurrencyTextView;
	private Spinner resultCurrencySpinner;
	private String[] currencyArray;
	private Toast toast;
	private int theCurrencyPosition;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_currency_converter);

		SharedPreferences mSharedPreference = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
		theCurrencyPosition = (mSharedPreference.getInt("theCurrency", 0));
		
		initialAmountEditText = (EditText) findViewById(R.id.initialAmountEditText);
		resultAmountTextView = (TextView) findViewById(R.id.resultAmountTextView);
		resultCurrencySpinner = (Spinner) findViewById(R.id.resultCurrencySpinner);
		initialCurrencyTextView = (TextView) findViewById(R.id.initialCurrencyTextView);

		initializeSpinner(resultCurrencySpinner, R.array.currencies);

		currencyArray = getResources().getStringArray(R.array.currencies);
		initialCurrencyTextView.setText(currencyArray[theCurrencyPosition]);

		// Restore a previously stored instance state
		if (savedInstanceState != null) {
			int result_position = savedInstanceState.getInt(getResources().getString(R.string.result_position));
			String initial_amount = savedInstanceState.getString(getResources().getString(R.string.initial_amount));
			String result_amount = savedInstanceState.getString(getResources().getString(R.string.result_amount));

			resultCurrencySpinner.setSelection(result_position);
			initialAmountEditText.setText(initial_amount);
			resultAmountTextView.setText(result_amount);

		}
	}

	/**
	 * Change content of the given spinner with the array that has the specified
	 * resource id
	 * 
	 * @param spinner
	 * @param textArrayResId
	 */
	private void initializeSpinner(Spinner spinner, int textArrayResId) {
		// Create an ArrayAdapter using the string array and a default spinner
		// layout
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, textArrayResId,
				android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		spinner.setAdapter(adapter);
	}

	/**
	 * Clears the fields
	 * 
	 * @param view
	 */
	public void clearFields(View view) {
		Log.d(TAG, "Clearing");
		initialAmountEditText.setText("");
		resultAmountTextView.setText("");
	}

	/**
	 * Triggered as the user clicks on the convert button. This method validates
	 * the edit text, if everything is fine, it tries to get the conversion.
	 * Otherwise, it displays an error message for user to try again.
	 * 
	 * @param view
	 */
	public void convertCurrency(View view) {
		Log.d(TAG, "Converting");
		cancelToast();
		toast = Toast.makeText(getApplicationContext(), null, Toast.LENGTH_SHORT);

		String amountStr = initialAmountEditText.getText().toString().trim();
		double amount = -1;
		boolean isValid = false;

		if (!amountStr.isEmpty() && !amountStr.equals(".")) {
			// Get amount and round it to 2 digits after decimal point
			amount = Math.round(Double.parseDouble(amountStr) * 100.0) / 100.0;

			// Check that amount is a positive number
			if (amount >= 0) {
				isValid = true;
				// re-display with proper format
				initialAmountEditText.setText(String.valueOf(amount));
			}
		}

		if (isValid) {
			getConversion();
		} else {
			Log.d(TAG, "INVALID AMOUNT");
			toast.setText(R.string.err_invalid_amount);
			toast.show();
		}
	}

	/**
	 * Method tries to get the conversion. Beforehand, it checks whether user is
	 * connected to wifi. If the device is connected, it starts an async task
	 * else it simply displays an error saying that you have to be connected.
	 */
	private void getConversion() {
		cancelToast();
		toast = Toast.makeText(getApplicationContext(), null, Toast.LENGTH_SHORT);

		String initialCurrency = initialCurrencyTextView.getText().toString().trim();
		String resultCurrency = currencyArray[resultCurrencySpinner.getSelectedItemPosition()];

		if (initialCurrency == resultCurrency) {
			resultAmountTextView.setText(initialAmountEditText.getText());
		} else {
			// check to see if we can get on the network
			ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
			if (networkInfo != null && networkInfo.isConnected()) {
				// the url to get the multiplier
				String url = "http://api.fixer.io/latest?base=" + initialCurrency + "&symbols=" + resultCurrency;

				// invoke the AsyncTask to do the work in the background
				new RetrieveConversion().execute(url);
			} else {
				toast.setText(R.string.connection_err_msg);
				toast.show();
			}
		}
	}

	/**
	 * Cancel any lingering toasts
	 */
	private void cancelToast() {
		if (toast != null)
			toast.cancel();
	}

	@Override
	protected void onSaveInstanceState(Bundle savedInstanceState) {
		super.onSaveInstanceState(savedInstanceState);

		savedInstanceState.putInt((getResources().getString(R.string.result_position)),
				resultCurrencySpinner.getSelectedItemPosition());
		savedInstanceState.putString((getResources().getString(R.string.initial_amount)),
				initialAmountEditText.getText().toString());
		savedInstanceState.putString((getResources().getString(R.string.result_amount)),
				resultAmountTextView.getText().toString());
	}

	/**
	 * AsyncTask which tries to retrieve the conversion rate from one currency
	 * to another based on the url that was passed to it
	 */
	private class RetrieveConversion extends AsyncTask<String, Void, String> {

		@Override
		protected void onPostExecute(String result) {
			resultAmountTextView.setText(result);
		}

		@Override
		protected String doInBackground(String... params) {
			try {
				return downloadUrl(params[0]);
			} catch (Exception e) {
				return "";
			}
		}
	}

	/**
	 * Establishes an HTTP connection to the given URL
	 * 
	 * @param myurl
	 * @return
	 * @throws IOException
	 * @throws JSONException
	 */
	public String downloadUrl(String myurl) throws IOException, JSONException {
		String resultCurrency = currencyArray[resultCurrencySpinner.getSelectedItemPosition()];
		Double amount = Double.parseDouble(initialAmountEditText.getText().toString());
		InputStream is = null;
		HttpURLConnection conn = null;
		URL url = new URL(myurl);

		try {
			// create and open the connection
			conn = (HttpURLConnection) url.openConnection();

			//set maximum time to wait for stream read read fails
			conn.setReadTimeout(10000);
			
			//set maximum time to wait while connecting connect fails
			conn.setConnectTimeout(15000);
			
			//Set HTTP Request method 
			conn.setRequestMethod("GET");
			// specifies whether this connection allows receiving data
			conn.setDoInput(true);
			// Starts the query
			conn.connect();

			int response = conn.getResponseCode();
			Log.d(TAG, "Server returned: " + response + " aborting read.");

			/*
			 * check the status code HTTP_OK = 200 anything else we didn't get
			 * what we want in the data.
			 */
			if (response != HttpURLConnection.HTTP_OK)
				return "Server returned: " + response + " aborting read.";

			// get the stream for the data from the website
			is = conn.getInputStream();

			// read the stream (max len bytes)
			String jsonString = callURL(is, MAXBYTES);

			// Get the conversion rate
			Double multiplier = getConversionRateFromJSON(jsonString, resultCurrency);

			// Get the result of the conversion and rounds it to 2 numbers after
			// decimal point
			Double result = Math.round((amount * multiplier) * 100.0) / 100.0;

			return String.valueOf(result);
		} catch (IOException e) {
			throw e;
		} catch (JSONException e) {
			throw e;
		} finally {
			/*
			 * Close InputStream after app finished using it.
			 * Make sure connection is closed after done with it too..
			 */
			if (is != null) {
				try {
					is.close();
				} catch (IOException ignore) {
				}
				if (conn != null)
					try {
						conn.disconnect();
					} catch (IllegalStateException ignore) {
					}
			}
		}
	}

	/**
	 * Get the conversion rate from the JSON object
	 * 
	 * @param jsonString
	 * @param resultCurrency
	 * @return
	 * @throws JSONException
	 */
	private Double getConversionRateFromJSON(String jsonString, String resultCurrency) throws JSONException {
		Log.d(TAG, "Currency: " + resultCurrency);
		Log.d(TAG, "JSON string: " + jsonString.trim());

		// Create new json object out of the given string
		JSONObject json = new JSONObject(jsonString.trim());

		Log.d(TAG, "Convert: " + json.getJSONObject("rates").getDouble(resultCurrency));
		// Gets the conversion rate by finding it in JSONObject by the currency
		// name
		Double result = json.getJSONObject("rates").getDouble(resultCurrency);

		return result;
	}

	/**
	 * Reads the stream from HTTP connection and converts it into a String
	 * 
	 * @param stream
	 * @param length
	 * @return
	 * @throws IOException
	 * @throws UnsupportedEncodingException
	 */
	public String callURL(InputStream stream, int length) throws IOException, UnsupportedEncodingException {
		char[] buffer = new char[length];
		Reader reader = null;
		reader = new BufferedReader(new InputStreamReader(stream, "UTF-8"), length);
		reader.read(buffer);

		return new String(buffer);
	}
}
