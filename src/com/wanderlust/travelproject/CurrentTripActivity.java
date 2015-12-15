package com.wanderlust.travelproject;

import com.bob.travelproject.R;
import com.wanderlust.database.DBHelper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * 
 * This activity displays the last viewed trip and when the trip is clicked it
 * will fire the ItineraryActivity that displays all the budgeted and actual
 * expenses of that trip.
 * 
 * @author Marjorie Morales, Rita Lazaar, Brandon Balala, Marvin Francisco
 *
 */
public class CurrentTripActivity extends Activity {
	private Context context;
	private int lastTripViewedId;


	/**
	 * The method retrieves the id of the last viewed(clicked) trip from the
	 * shared preference, if the shared preference is empty the lastTripViewedId
	 * = 1 (the first trip on the database). This method display the trip on ListView.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_current_trip);
		context = this;

		String[] from = new String[] { DBHelper.COLUMN_NAME, DBHelper.COLUMN_DESCRIPTION, }; // BOUND
		int[] to = new int[] { R.id.nameTv, R.id.descriptionTv };
		DBHelper dbh = DBHelper.getDBHelper(this);

		// retrieves the saved informations from the shared preferences
		SharedPreferences mSharedPreference = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
		// default value is 1(the first trip on the database).
		lastTripViewedId = (mSharedPreference.getInt("lastTripViewedId", 1));

		ListView lv = (ListView) findViewById(R.id.currentTrip);
		Cursor cursor = dbh.getTrip(lastTripViewedId);
		SimpleCursorAdapter sca = new SimpleCursorAdapter(this, R.layout.list_trips, cursor, from, to, 0);
		lv.setAdapter(sca);

		lv.setOnItemClickListener(showItinerary);

	}

	/**
	 * This is a onclick lister(short click). When an item (a trip) on the
	 * listview is cliked, it fires the ItineraryActivity activity.
	 */
	public OnItemClickListener showItinerary = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			// Create an intent
			Intent intent = new Intent(context, ItineraryActivity.class);
			intent.putExtra("trip_id", lastTripViewedId);
			startActivity(intent);
		}
	};
}
