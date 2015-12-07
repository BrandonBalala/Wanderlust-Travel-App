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

public class CurrentTripActivity extends Activity {
	private Context context;
	private int lastTripViewedId;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_current_trip);
		context = this;
		
		String[] from = new String[] { DBHelper.COLUMN_NAME, DBHelper.COLUMN_DESCRIPTION, }; 	// THE DESIRED COLUMNS TO BE BOUND
		int[] to = new int[] { R.id.nameTv, R.id.descriptionTv }; 	// THE XML DEFINED VIEWS WHICH THEDATA WILL BE BOUND TO
		DBHelper dbh = DBHelper.getDBHelper(this);

		// retrieves the saved informations from the shared preferences
		SharedPreferences mSharedPreference = PreferenceManager.getDefaultSharedPreferences(getBaseContext());		
		//default value is 1(the first trip on the database).
		lastTripViewedId = (mSharedPreference.getInt("lastTripViewedId", 1));
		
		ListView lv = (ListView) findViewById(R.id.currentTrip);
		Cursor cursor = dbh.getTrip(lastTripViewedId);
		SimpleCursorAdapter sca = new SimpleCursorAdapter(this, R.layout.list_trips, cursor, from, to, 0);
		lv.setAdapter(sca);
		
		lv.setOnItemClickListener(showItinerary);
		
	}
	
	public OnItemClickListener showItinerary = new OnItemClickListener(){
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {		
			Intent intent = new Intent(context, ItineraryActivity.class);
			intent.putExtra("trip_id", lastTripViewedId);
			startActivity(intent);
		}
	};
}
