package com.wanderlust.travelproject;

import com.bob.travelproject.R;
import com.wanderlust.database.DBHelper;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class CurrentTripsActivity extends Activity {
	
	public static final int SHOW_AS_ACTION_IF_ROOM = 1;
	private static DBHelper dbh;

	private Cursor cursor;
	private SimpleCursorAdapter sca;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_current_trips);

		String[] from = new String[] { DBHelper.COLUMN_NAME, DBHelper.COLUMN_DESCRIPTION, DBHelper.COLUMN_CREATION }; 	// THE DESIRED COLUMNS TO BE BOUND
		int[] to = new int[] { R.id.nameTv, R.id.descriptionTv, R.id.creationTv }; 	// THE XML DEFINED VIEWS WHICH THEDATA WILL BE BOUND TO
		dbh = DBHelper.getDBHelper(this);

		ListView lv = (ListView) findViewById(R.id.current_trip);
		cursor = dbh.getAllTrips();
		if (cursor.getCount() != 0) {
			sca = new SimpleCursorAdapter(this, R.layout.list_current_trip, cursor, from, to, 0);
			lv.setAdapter(sca);
		} else
			Toast.makeText(this, "You have no saved trips", Toast.LENGTH_SHORT).show();
	}

}
