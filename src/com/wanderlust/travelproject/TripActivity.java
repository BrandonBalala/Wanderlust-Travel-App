package com.wanderlust.travelproject;

import java.io.BufferedReader;
import java.io.IOException;
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

/**
 * This class will display all the trips that are saved in the device, this
 * class will also allow the user to sync its information to the web version of
 * the application via sync button.
 * 
 * @author Marvin Francisco and Marjorie Olano Morales
 *
 */
public class TripActivity extends Activity {

	public static final int SHOW_AS_ACTION_IF_ROOM = 1;
	private static DBHelper dbh;
	private final String TAG = "TRIP-ACTIVITY";
	static String myurl = "http://wanderlust-marjoriemorales.rhcloud.com/alltrips/";
	private Cursor cursor;
	private SimpleCursorAdapter sca;
	Context context;

	private int trip_id;
	private int budgeted_id;
	private int location_id;
	private int actualExpense_id;
	private String[] categoriesArray;
	private ListView lv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_trip);
		context = this;
		// make categories list
		categoriesArray = getResources().getStringArray(R.array.categories);
		String[] from = new String[] { DBHelper.COLUMN_NAME, DBHelper.COLUMN_DESCRIPTION, };
		int[] to = new int[] { R.id.nameTv, R.id.descriptionTv };
		dbh = DBHelper.getDBHelper(this);

		lv = (ListView) findViewById(R.id.displayTrips);

		cursor = dbh.getAllTrips();
		if (cursor.getCount() != 0) {
			sca = new SimpleCursorAdapter(this, R.layout.list_trips, cursor, from, to, 0);
			lv.setAdapter(sca);
			lv.setOnItemClickListener(showItinerary);
		} else
			Toast.makeText(this, R.string.no_trips, Toast.LENGTH_SHORT).show();
	}

	/**
	 * 
	 * This method gets called when a data is deleted, edited or created.
	 * Updates the activity to get the data back.
	 * 
	 */
	public void refreshView() {
	
		Intent intent = getIntent();
		finish();
		startActivity(intent);
	}

	public OnItemClickListener showItinerary = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			int trip_id = (int) id;
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
		if (password.equals("")) {
			Toast.makeText(this, R.string.credentials, Toast.LENGTH_SHORT).show();
			Intent i = new Intent(this, SettingsActivity.class);
			startActivity(i);
		}
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
				Toast.makeText(context, R.string.network, Toast.LENGTH_SHORT).show();

			// Toast.makeText(context, "a button to sync/download new trips from
			// the website:", Toast.LENGTH_SHORT).show();
			return true;
		}
		return super.onOptionsItemSelected(item);

	}

	/**
	 * This method will pull the information from the WEBAPI and return the
	 * string opject for the onPostExecute method to process.
	 * 
	 * @param email
	 * @param password
	 * @return
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	public String pullInfo(String email, String password) throws MalformedURLException, IOException {
		String newFeed = myurl + "?email=" + email + "&password=" + password;
		Log.v(TAG, "Email: " + email);
		Log.v(TAG, "Password: " + password);
		Log.v(TAG, "url:" + newFeed);
		StringBuilder response = new StringBuilder();
		URL url = new URL(newFeed);

		HttpURLConnection httpconn = (HttpURLConnection) url.openConnection();
		if (httpconn.getResponseCode() == HttpURLConnection.HTTP_OK) {
			BufferedReader input = new BufferedReader(new InputStreamReader(httpconn.getInputStream()), 8192);
			String strLine = null;
			while ((strLine = input.readLine()) != null) {
				response.append(strLine);
			}
			input.close();
			httpconn.disconnect(); // PMC
		}
		if (response.toString().equals("")) {
			Log.v(TAG, "code: " + httpconn.getResponseCode());
			if (httpconn.getResponseCode() == 302) {
				return "NoConnection";
			}
		}

		return response.toString();
	}

	/**
	 * This method that will convert the String resp to a JSON object and will
	 * save the information to the database.
	 * 
	 * @param resp
	 * @throws IllegalStateException
	 * @throws IOException
	 * @throws JSONException
	 * @throws NoSuchAlgorithmException
	 * @throws ParseException
	 */
	public void saveInfotoDatabase(String resp)
			throws IllegalStateException, IOException, JSONException, NoSuchAlgorithmException, ParseException {

		JSONObject jobj = new JSONObject(resp);

		// create an array of json objects
		JSONArray tripsObj = jobj.getJSONArray("trips");
		Log.v(TAG, "tripsObj size" + tripsObj.length());
		for (int i = 0; i < tripsObj.length(); i++) {
			JSONArray tripElements = tripsObj.getJSONArray(i);
			// loop to all of the trips
			for (int tripI = 0; tripI < tripElements.length(); tripI++) {
				JSONObject tripElement = tripElements.getJSONObject(tripI);
				// Log.v(TAG, "JSON TRIP Element" + tripElement.toString());
				if (tripElement.has("trip")) {
					// create a trip on database
					int web_trip_id = tripElement.getJSONObject("trip").getInt("id");
					String name = tripElement.getJSONObject("trip").getString("name");
					String description = tripElement.getJSONObject("trip").getString("description");
					// validation to see if there is the specific trip already
					trip_id = dbh.getIdWebTrip(web_trip_id);
					if (trip_id == 0) {
						trip_id = (int) dbh.createNewTrip(web_trip_id, name, description);
						Log.v("Trip", "trip created");
					}
				}
				if (tripElement.has("locations")) {
					JSONArray locations = tripElement.getJSONArray("locations");
					// loop to all of the locations
					for (int locationsI = 0; locationsI < locations.length(); locationsI++) {
						JSONArray locationElements = locations.getJSONArray(locationsI);
						for (int locationsElementsI = 0; locationsElementsI < locationElements
								.length(); locationsElementsI++) {

							JSONObject dbLocation = locationElements.getJSONObject(locationsElementsI);
							if (dbLocation.has("location")) {
								// create a new location
								String countryCode = dbLocation.getJSONObject("location").getString("countrycode");
								String city = dbLocation.getJSONObject("location").getString("city");
								String name = dbLocation.getJSONObject("location").getString("name");
								String description = dbLocation.getJSONObject("location").getString("description");
								// validation to see if there is the specific
								// location already
								location_id = dbh.getLocationId(name);
								if (location_id == 0) {
									location_id = (int) dbh.createNewLocation(name, description, city, countryCode);
								}
							}
							if (dbLocation.has("expenses")) {
								// expenses obj in the array locationsElement of
								// the index locationsElementsI
								JSONArray expensesObjs = dbLocation.getJSONArray("expenses");
								// make a new loop for all the expenses
								for (int x = 0; x < expensesObjs.length(); x++) {
									JSONArray expensesObj = expensesObjs.getJSONArray(x);

									for (int y = 0; y < expensesObj.length(); y++) {
										JSONObject itiniraryObj = expensesObj.getJSONObject(y);
										if (itiniraryObj.has("budgetedexpense")) {
											// Log.v("Budgeted",
											// itiniraryObj.getJSONObject("budgetedexpense").toString());
											JSONObject jsonElementBudgeted = itiniraryObj
													.getJSONObject("budgetedexpense");

											int web_budgeted_id = jsonElementBudgeted.getInt("id");
											String budgeteddescription = jsonElementBudgeted.getString("description");
											double amount = jsonElementBudgeted.getInt("amount");

											SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd",
													Locale.ENGLISH);

											String datearrive = jsonElementBudgeted.getString("planned_arrival_date");
											datearrive = datearrive.substring(0, 10);
											// Log.v("DATE arrival",
											// datearrive);
											Date parsedDate = dateFormat.parse(datearrive);
											Timestamp arrivalDate = new java.sql.Timestamp(parsedDate.getTime());

											String datedepart = jsonElementBudgeted.getString("planned_departure_date");
											datedepart = datedepart.substring(0, 10);
											// Log.v("DATE departure",
											// datedepart);
											Date departedDate = dateFormat.parse(datedepart);
											Timestamp departureDate = new java.sql.Timestamp(departedDate.getTime());

											int categoryPos = jsonElementBudgeted.getInt("category_id") - 1;
											String category = categoriesArray[categoryPos];

											String supplier_name = "", address = "";
											// if the budgeted_id is not there
											// save it to the database
											budgeted_id = dbh.getIdItinerariesFromweb(web_budgeted_id);
											if (budgeted_id == 0) {
												budgeted_id = (int) dbh.createNewItinerary(web_budgeted_id, location_id,
														trip_id, arrivalDate, departureDate, amount,
														budgeteddescription, category, supplier_name, address);
												Log.v("Itinerary", "Itinerary created");
											}

										}
										if (itiniraryObj.has("actualexpense")) {
											SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd",
													Locale.getDefault());
											JSONArray jsonElementsActual = itiniraryObj.getJSONArray("actualexpense");
											// loop on all of the Actual
											// elements
											for (int z = 0; z < jsonElementsActual.length(); z++) {
												JSONObject jsonElementActual = jsonElementsActual.getJSONObject(z);
												int web_actual_id = jsonElementActual.getInt("id");
												String description = jsonElementActual.getString("description");

												String datearriveActual = jsonElementActual
														.getString("actual_arrival_date");
												datearriveActual = datearriveActual.substring(0, 10);
												// Log.v("DATE 1",
												// datearriveActual);
												Date parsedArrivalDate = formatter.parse(datearriveActual);
												Timestamp arrivalDate = new java.sql.Timestamp(
														parsedArrivalDate.getTime());

												String datedepartActual = jsonElementActual
														.getString("actual_departure_date");
												datedepartActual = datedepartActual.substring(0, 10);
												// Log.v("DATE 2",
												// datedepartActual);
												Date parsedDepartureDate = formatter.parse(datedepartActual);
												Timestamp departureDate = new java.sql.Timestamp(
														parsedDepartureDate.getTime());
												int amount = jsonElementActual.getInt("amount");
												int categoryPosition = jsonElementActual.getInt("category_id") - 1;
												String category = categoriesArray[categoryPosition];
												String supplierName = jsonElementActual.getString("name_of_supplier");
												String address = jsonElementActual.getString("address");
												// if the actual_id is not there
												// save it to the database
												actualExpense_id = dbh.getIdActualExpenseFromweb(web_actual_id);
												if (actualExpense_id == 0) {
													dbh.createNewActualExpense(web_actual_id, budgeted_id, arrivalDate,
															departureDate, amount, description, category, supplierName,
															address);
													Log.v("ActualItinerary", "ActualItinerary created");

												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}

	}

	/**
	 * Asynctask that downloads the information in the WEB API (
	 * wanderlust-marjoriemorales.rhcloud.com)
	 * 
	 * @author theMarvin
	 *
	 */
	private class SyncInfo extends AsyncTask<String, Integer, String> {

		protected String doInBackground(String... searchKey) {

			String email = searchKey[0];
			String password = searchKey[1];
			try {
				return pullInfo(email, password);
			} catch (Exception e) {
				Log.v(TAG, "Exception:" + e.getMessage());
				return "";
			}
		}

		protected void onPostExecute(String result) {
			try {
				if (result.equals("NoConnection")) {
					Toast.makeText(getBaseContext(), R.string.test_connection, Toast.LENGTH_SHORT).show();
				} else {
					if (result.equals("")) {
						Toast.makeText(getBaseContext(), R.string.credentials, Toast.LENGTH_SHORT).show();
						Intent i = new Intent(getBaseContext(), SettingsActivity.class);
						startActivity(i);

					} else {
						saveInfotoDatabase(result);
						refreshView();

					}
				}

			} catch (Exception e) {
				Log.v(TAG, "Exception:" + e.getMessage());

			}
		}

	}

	/**
	 * This method will check if there is a valid connection for the device to
	 * sync the items in the WEB API
	 * 
	 * @return
	 */
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
