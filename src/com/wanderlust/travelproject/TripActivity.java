package com.wanderlust.travelproject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.bob.travelproject.R;
import com.wanderlust.database.DBHelper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class TripActivity extends Activity {
	public static SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, MMM dd, yyyy HH:mm:ss z",
			Locale.getDefault());
	public static final int SHOW_AS_ACTION_IF_ROOM = 1;
	private static DBHelper dbh;
	private final String TAG = "TRIP-ACTIVITY";
	static String myurl = "http://wanderlust-marjoriemorales.rhcloud.com/alltrips/";
	private Cursor cursor;
	private SimpleCursorAdapter sca;
	Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_trip);
		context = this;

		String[] from = new String[] { DBHelper.COLUMN_NAME, DBHelper.COLUMN_DESCRIPTION, }; // THE
																								// DESIRED
																								// COLUMNS
																								// TO
																								// BE
																								// BOUND
		int[] to = new int[] { R.id.nameTv, R.id.descriptionTv }; // THE XML
																	// DEFINED
																	// VIEWS
																	// WHICH
																	// THEDATA
																	// WILL BE
																	// BOUND TO
		dbh = DBHelper.getDBHelper(this);

		ListView lv = (ListView) findViewById(R.id.displayTrips);

		cursor = dbh.getAllTrips();
		if (cursor.getCount() != 0) {
			sca = new SimpleCursorAdapter(this, R.layout.list_trips, cursor, from, to, 0);
			lv.setAdapter(sca);
			lv.setOnItemClickListener(showItinerary);
		} else
			Toast.makeText(this, "You have no saved trips", Toast.LENGTH_SHORT).show();
	}

	/**
	 * 
	 * This method gets called when a data is deleted, edited or created.
	 * Creates a new cursor and tell the adapter there is a new data.
	 * 
	 */
	public void refreshView() {
		cursor = dbh.getAllTrips(); // renew the cursor
		sca.changeCursor(cursor); // have the adapter use the new cursor,
									// changeCursor closes old cursor too
		sca.notifyDataSetChanged(); // have the adapter tell the observers
	}

	public OnItemClickListener showItinerary = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			ListView lv = (ListView) findViewById(R.id.displayTrips);
			int trip_id = ((SimpleCursorAdapter) lv.getAdapter()).getCursor().getInt(1);
			// save the clicked trip id into the SharedPreferences
			SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
			SharedPreferences.Editor editor = prefs.edit();
			editor.putInt("lastTripViewedId", trip_id);
			editor.commit();
			Log.v("TRIP_ID", "the id is " + trip_id);
			Intent intent = new Intent(context, ItineraryActivity.class);
			intent.putExtra("trip_id", trip_id);
			startActivity(intent);
		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.trip, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		SharedPreferences mSharedPreference = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

		String email = (mSharedPreference.getString("username", ""));
		String password = (mSharedPreference.getString("password", ""));
		InputStream is = null;
		int response, len = 500;
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.sync) {

			Boolean netup = netIsUp();
			if (netup) {
				try {
					// send the username in the new SyncInfo class to download
					// appropriate trips for that user.
					new SyncInfo().execute(email, password);

				} catch (Exception e) {
					Log.v(TAG, "Exception:" + e.getMessage());
				}
			} else
				Toast.makeText(context, "Check your network connection", Toast.LENGTH_SHORT).show();

			// Toast.makeText(context, "a button to sync/download new trips from
			// the website:", Toast.LENGTH_SHORT).show();
			return true;
		}
		return super.onOptionsItemSelected(item);

	}

	public String SearchRequest(String email, String password) throws MalformedURLException, IOException {
		String newFeed = myurl + "?email=" + email + "&password=" + password;
		Log.v(TAG, "Email: " + email);
		Log.v(TAG, "Password: " + password);
		// "/?email=marvs298@yahoo.com&password=testing";
		StringBuilder response = new StringBuilder();
		Log.v(TAG, "url:" + newFeed);
		URL url = new URL(newFeed);

		HttpURLConnection httpconn = (HttpURLConnection) url.openConnection();
		Log.v(TAG, "code: " + httpconn.getResponseCode());
		if (httpconn.getResponseCode() == HttpURLConnection.HTTP_OK) {
			BufferedReader input = new BufferedReader(new InputStreamReader(httpconn.getInputStream()), 8192);
			String strLine = null;
			while ((strLine = input.readLine()) != null) {
				response.append(strLine);
			}
			input.close();
			httpconn.disconnect(); // PMC
		} else {
			Toast.makeText(this, "Invalid credentials", Toast.LENGTH_LONG).show();
			Intent i = new Intent(this, SettingsActivity.class);
			startActivity(i);
		}
		return response.toString();
	}

	/*
	 * In order to read this consider the JSON format the api has chosen
	 * https://developers.google.com/feed/v1/jsondevguide match the code to the
	 * returned data, Try https://developers.google.com/feed/v1/jsondevguide Try
	 * http://ajax.googleapis.com/ajax/services/search/web?v=1.0&q=dogs in
	 * jsoneditoronline.org and compare it to the code:
	 */
	public void ProcessResponse(String resp)
			throws IllegalStateException, IOException, JSONException, NoSuchAlgorithmException, ParseException {

		// create an array of json objects
		JSONArray array = new JSONArray(resp);
		Log.v(TAG, "array size" + array.length());
		for (int i = 0; i < array.length(); i++) {
			JSONObject jsonElement = array.getJSONObject(i);
			Log.v(TAG, "json" + jsonElement.toString());

			if (jsonElement.has("trip")) {
				// create a trip on database
				int trip_id = jsonElement.getJSONObject("trip").getInt("id");
				String name = jsonElement.getJSONObject("trip").getString("name");
				String description = jsonElement.getJSONObject("trip").getString("description");
				Cursor thistrip = dbh.getTrip(trip_id);
				Log.v("TAG", "this trips id on sync" + trip_id);
				//validation to see if there is the specific trip already
				if (thistrip.getCount() < 1)
					dbh.createNewTrip(trip_id, name, description);
			}
			if (jsonElement.has("location")) {
				int location_id = jsonElement.getJSONObject("location").getInt("id");
				int trip_id = jsonElement.getJSONObject("location").getInt("trip_id");
				String countryCode = jsonElement.getJSONObject("location").getString("countrycode");
				String city = jsonElement.getJSONObject("location").getString("city");
				String name = jsonElement.getJSONObject("location").getString("name");
				String description = jsonElement.getJSONObject("location").getString("description");
				Cursor thislocation = dbh.getLocations(location_id);
				//validation to see if there is the specific location already
				if (thislocation.getCount() < 1)
					dbh.createNewLocation(name, description, city, countryCode);

				JSONArray budgetedArray = jsonElement.getJSONArray("budgetedexpenses");
				Log.v(TAG, "budgetedArray size " + budgetedArray.length());

				for (int x = 0; x < budgetedArray.length(); x++) {
					JSONObject jsonElementBudgeted = budgetedArray.getJSONObject(x);
					Log.v(TAG, "jsonElementBudgeted " + jsonElementBudgeted.toString());
					int budgeted_id = jsonElementBudgeted.getInt("id");
					int location_id_budgeted = jsonElementBudgeted.getInt("location_id");
					String budgeteddescription = jsonElementBudgeted.getString("description");
					double amount = jsonElementBudgeted.getInt("amount");

					SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

					String datearrive = jsonElementBudgeted.getString("planned_arrival_date");
					datearrive = datearrive.substring(0, 10);
					// Log.v("DATE arrival", datearrive);
					Date parsedDate = dateFormat.parse(datearrive);
					Timestamp arrivalDate = new java.sql.Timestamp(parsedDate.getTime());

					String datedepart = jsonElementBudgeted.getString("planned_departure_date");
					datedepart = datedepart.substring(0, 10);
					// Log.v("DATE departure", datedepart);
					Date departedDate = dateFormat.parse(datedepart);
					Timestamp departureDate = new java.sql.Timestamp(departedDate.getTime());

					String category = String.valueOf(jsonElementBudgeted.getInt("category_id"));
					String supplier_name = "", address = "";
					// if the budgeted_id is not there save it to the database
					if (dbh.getItinerary(budgeted_id).getCount() < 1)
						dbh.createNewItinerary(location_id_budgeted, trip_id, arrivalDate, departureDate, amount,
								budgeteddescription, category, supplier_name, address);
				}

				JSONArray actualArray = jsonElement.getJSONArray("actualexpenses");
				Log.v(TAG, "actualArray size " + actualArray.length());
				for (int x = 0; x < actualArray.length(); x++) {
					JSONObject jsonElementActual = actualArray.getJSONObject(x);
					Log.v(TAG, "jsonElementActual " + jsonElementActual.toString());
					int actual_id = jsonElementActual.getInt("id");
					String arrivalDate = jsonElementActual.getString("actual_arrival_date");
					String departureDate = jsonElementActual.getString("actual_departure_date");
					int amount = jsonElementActual.getInt("amount");
					String category = String.valueOf(jsonElementActual.getInt("category_id"));
					String supplierName = jsonElementActual.getString("name_of_supplier");
					String address = jsonElementActual.getString("address");
					// if the actual_id is not there save it to the database
					if (dbh.getActualExpense(actual_id).getCount() < 1)
						dbh.createNewActualExpense(actual_id, arrivalDate, departureDate, amount, description, category,
								supplierName, address);

				}
			}

		}

	}

	/**
	 * Asynctask that downloads the information in
	 * 
	 * @author theMarvin
	 *
	 */
	private class SyncInfo extends AsyncTask<String, Integer, String> {

		protected String doInBackground(String... searchKey) {

			String email = searchKey[0];
			String password = searchKey[1];
			try {
				return SearchRequest(email, password);
			} catch (Exception e) {
				Log.v(TAG, "Exception:" + e.getMessage());
				return "";
			}
		}

		protected void onPostExecute(String result) {
			try {
				// Log.v(TAG, "the result: " + result);
				ProcessResponse(result);

			} catch (Exception e) {
				Log.v(TAG, "Exception:" + e.getMessage());

			}
		}

	}

	private boolean netIsUp() {
		ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		// getActiveNetworkInfo() each time as the network may swap as the
		// device moves
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		// ALWAYS check isConnected() before initiating network traffic
		if (networkInfo != null)
			return networkInfo.isConnected();
		else
			return false;
	} // netIsUp()

}
