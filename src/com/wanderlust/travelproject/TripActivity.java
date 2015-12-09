package com.wanderlust.travelproject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.bob.travelproject.R;
import com.wanderlust.database.DBHelper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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

	public static final int SHOW_AS_ACTION_IF_ROOM = 1;
	private static DBHelper dbh;
	private final String TAG = "TRIP-ACTIVITY";
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

	public OnItemClickListener showItinerary = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			// TODO Auto-generated method stub
			int trip__id = (int) id;
			// save the clicked trip id into the SharedPreferences
			SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
			SharedPreferences.Editor editor = prefs.edit();
			editor.putInt("lastTripViewedId", trip__id);
			editor.commit();

			Intent intent = new Intent(context, ItineraryActivity.class);
			intent.putExtra("trip_id", trip__id);
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
		InputStream is = null;
		String myurl = "www.facebook.com";
		int response, len = 500;
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.sync) {

			/*
			 * Always check if the network is connected before sending data.
			 */
			ConnectivityManager connMgr;
			NetworkInfo netInfo;
			connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
			HttpURLConnection conn = null;
			// here we don’t care if it is WiFi or Mobile
			// we just want to use the active network

			netInfo = connMgr.getActiveNetworkInfo();

			if (netInfo != null && netInfo.isConnected()) {

				try {
					URL url = new URL(myurl);
					conn = (HttpURLConnection) url.openConnection();
					conn.setDoInput(true);
					conn.setRequestMethod("GET");
					conn.setReadTimeout(10000);
					conn.setConnectTimeout(15000 /* milliseconds */);
					conn.connect();
					response = conn.getResponseCode();
					if (response != HttpURLConnection.HTTP_OK) {
						Log.d(TAG, "Server returned: " + response + " aborting read.");
						Toast.makeText(context, "the response: " + response, Toast.LENGTH_SHORT).show();
					}
					is = conn.getInputStream();
					Log.d(TAG, "Server returned: " + is.toString() + " aborting read.");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
					if (is != null) {
						try {
							is.close();
						} catch (IOException ignore) {
							/* ignore */ }
						if (conn != null)
							try {
								conn.disconnect();
							} catch (IllegalStateException ignore) {
								/* ignore */ }
					}
				}

				Toast.makeText(context, "Download something", Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(context, "No download possible", Toast.LENGTH_SHORT).show();
			}

			// Toast.makeText(context, "a button to sync/download new trips from
			// the website:", Toast.LENGTH_SHORT).show();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
