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
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
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
	private TextView resultAmountTextView;
	private Spinner initialCurrencySpinner;
	private Spinner resultCurrencySpinner;
	private String[] currencyArray;
	private Toast toast;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_currency_converter);

		initialAmountEditText = (EditText) findViewById(R.id.initialAmountEditText);
		resultAmountTextView = (TextView) findViewById(R.id.resultAmountTextView);
		initialCurrencySpinner = (Spinner) findViewById(R.id.initialCurrencySpinner);
		resultCurrencySpinner = (Spinner) findViewById(R.id.resultCurrencySpinner);

		initializeSpinner(initialCurrencySpinner, R.array.currencies);
		initializeSpinner(resultCurrencySpinner, R.array.currencies);

		currencyArray = getResources().getStringArray(R.array.currencies);

		// Restore a previously stored instance state
		if (savedInstanceState != null) {
			int initial_position = savedInstanceState.getInt(getResources().getString(R.string.init_position));
			int result_position = savedInstanceState.getInt(getResources().getString(R.string.result_position));
			String initial_amount = savedInstanceState.getString(getResources().getString(R.string.initial_amount));
			String result_amount = savedInstanceState.getString(getResources().getString(R.string.result_amount));

			initialCurrencySpinner.setSelection(initial_position);
			resultCurrencySpinner.setSelection(result_position);
			initialAmountEditText.setText(initial_amount);
			resultAmountTextView.setText(result_amount);

		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.currency_converter, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
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

	public void clearFields(View view) {
		Log.d(TAG, "Clearing");
		initialAmountEditText.setText("");
		resultAmountTextView.setText("");
	}

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

	private void getConversion() {
		cancelToast();
		toast = Toast.makeText(getApplicationContext(), null, Toast.LENGTH_SHORT);

		String initialCurrency = currencyArray[initialCurrencySpinner.getSelectedItemPosition()];
		String resultCurrency = currencyArray[resultCurrencySpinner.getSelectedItemPosition()];

		if (initialCurrency == resultCurrency) {
			resultAmountTextView.setText(initialAmountEditText.getText());
		} else {
			// check to see if we can get on the network
			ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
			if (networkInfo != null && networkInfo.isConnected()) {
				String url = "http://api.fixer.io/latest?base=" + initialCurrency + "&symbols=" + resultCurrency;
				new RetrieveConversion().execute(url);
			} else {
				toast.setText(R.string.connection_err_msg);
				toast.show();
			}
		}
	}

	public void swapCurrency(View view) {
		Log.d(TAG, "Swaping");

		int initialCurrencyPosition = initialCurrencySpinner.getSelectedItemPosition();
		int resultCurrencyPosition = resultCurrencySpinner.getSelectedItemPosition();

		initialCurrencySpinner.setSelection(resultCurrencyPosition);
		resultCurrencySpinner.setSelection(initialCurrencyPosition);
		resultAmountTextView.setText("");
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

		savedInstanceState.putInt((getResources().getString(R.string.init_position)),
				initialCurrencySpinner.getSelectedItemPosition());
		savedInstanceState.putInt((getResources().getString(R.string.result_position)),
				resultCurrencySpinner.getSelectedItemPosition());
		savedInstanceState.putString((getResources().getString(R.string.initial_amount)),
				initialAmountEditText.getText().toString());
		savedInstanceState.putString((getResources().getString(R.string.result_amount)),
				resultAmountTextView.getText().toString());
	}

	private class RetrieveConversion extends AsyncTask<String, Void, String> {
		private Toast toast;
		
		@Override
		protected void onPostExecute(String result) {
			resultAmountTextView.setText(result);
		}

		@Override
		protected String doInBackground(String... params) {
			//cancelToast();
			//toast = Toast.makeText(getApplicationContext(), null, Toast.LENGTH_SHORT);

			try {
				return downloadUrl(params[0]);
			} catch (IOException e) {
				//toast.setText(R.string.retrieve_err_msg);
				//toast.show();
				return "1";
			} catch (JSONException e) {
				//toast.setText(R.string.json_err_msg);
				//toast.show();
				return "2";
			}
		}
	}

	public String downloadUrl(String myurl) throws IOException, JSONException {
		String resultCurrency = currencyArray[resultCurrencySpinner.getSelectedItemPosition()];
		Double amount = Double.parseDouble(initialAmountEditText.getText().toString());
		InputStream is = null;
		HttpURLConnection conn = null;
		URL url = new URL(myurl);

		try {
			// create and open the connection
			conn = (HttpURLConnection) url.openConnection();

			/*
			 * set maximum time to wait for stream read read fails with
			 * SocketTimeoutException if elapses before connection established
			 * 
			 * in milliseconds
			 * 
			 * default: 0 - timeout disabled
			 */
			conn.setReadTimeout(10000);
			/*
			 * set maximum time to wait while connecting connect fails with
			 * SocketTimeoutException if elapses before connection established
			 * 
			 * in milliseconds
			 * 
			 * default: 0 - forces blocking connect timeout still occurs but
			 * VERY LONG wait ~several minutes
			 */
			conn.setConnectTimeout(15000 /* milliseconds */);
			/*
			 * HTTP Request method defined by protocol
			 * GET/POST/HEAD/POST/PUT/DELETE/TRACE/CONNECT
			 * 
			 * default: GET
			 */
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
			Double multiplier = getConversionFromJSON(jsonString, resultCurrency);
			Double result = Math.round((amount * multiplier) * 100.0) / 100.0;
			
			
			return String.valueOf(result);
		} catch (IOException e) {
			throw e;
		} catch (JSONException e) {
			throw e;
		} finally {
			/*
			 * Make sure that the InputStream is closed after the app is
			 * finished using it. Make sure the connection is closed after the
			 * app is finished using it.
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

	private Double getConversionFromJSON(String jsonString, String resultCurrency) throws JSONException {
		Log.d(TAG, "Currency: " + resultCurrency);
		Log.d(TAG, "JSON string: " + jsonString.trim());
		JSONObject json = new JSONObject(jsonString.trim());
		
		Log.d(TAG, "Convert: " + json.getJSONObject("rates").getDouble(resultCurrency));
		Double result = json.getJSONObject("rates").getDouble(resultCurrency);
		
		
		

		return result; // TODO CHANGE THIS
	}

	public String callURL(InputStream stream, int length) throws IOException, UnsupportedEncodingException {
		char[] buffer = new char[length];
		Reader reader = null;
		reader = new BufferedReader(new InputStreamReader(stream, "UTF-8"), length);
		// int count = reader.read(buffer);
		reader.read(buffer);

		return new String(buffer);
	}
}
