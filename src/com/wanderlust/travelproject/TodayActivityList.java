package com.wanderlust.travelproject;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

import com.bob.travelproject.R;
import com.wanderlust.database.DBHelper;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

/**
 * This class will allow the user to display the itineraries(budgeted and
 * actual) on a particular date that was returned by the TodayActivityFlash
 * class.
 * 
 * @author theMarvin
 *
 */
public class TodayActivityList extends Activity {
	private final static String TAG = "TODAY-ACTIVITY-LIST";
	private static DBHelper dbh;
	private Cursor cursor;
	private Timestamp ts;
	private ItineraryCursorAdapter sca;
	private Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_today_activity_list);
		dbh = DBHelper.getDBHelper(this);
		context = this;

		Intent intent = getIntent();
		if (intent == null)
			Toast.makeText(getApplicationContext(), "null", Toast.LENGTH_LONG).show();
		int year = intent.getIntExtra("year", 2015);
		int month = intent.getIntExtra("month", 11);
		int day = intent.getIntExtra("day", 29);
		Log.v(TAG, "Intent year " + year);
		Log.v(TAG, "Intent month " + month);
		Log.v(TAG, "Intent day " + day);

		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, month);
		cal.set(Calendar.DAY_OF_MONTH, day);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		Date searchDate = cal.getTime();
		ts = new Timestamp(searchDate.getTime());

		// checks if table is not empty for activities that are queried in
		// the
		// // DBHelper class

		Log.v(TAG, "There are trips on date," + ts.toString() + " : " + (dbh.getActivitiesToday(ts).getCount() < 1));

		if (dbh.getActivitiesToday(ts).getCount() < 1) {
			Toast.makeText(getApplicationContext(), R.string.no_trips + " at " + ts.toString(), Toast.LENGTH_LONG)
					.show();
			setResult(0);
			finish();
		} else {
			Toast.makeText(getApplicationContext(), R.string.trips + " at " + ts.toString(), Toast.LENGTH_LONG).show();
			ListView lv = (ListView) findViewById(R.id.displayItinaries);

			cursor = dbh.getActivitiesToday(ts);
			sca = new ItineraryCursorAdapter(this, cursor, 0);
			lv.setAdapter(sca);
			lv.setOnItemClickListener(editItem);
			lv.setOnItemLongClickListener(deleteItem);
		}
	}

	/**
	 * This method is executed when the activity(ItineraryActivity) restart. It
	 * calls the super and refreshView method.
	 */
	public void onResume() {
		super.onResume();
		refreshView();
	}

	/**
	 * 
	 * This method gets called when a data is deleted, edited or created.
	 * Creates a new cursor and tell the adapter there is a new data.
	 * 
	 */
	public void refreshView() {
		cursor = dbh.getActivitiesToday(ts); // renew the cursor
		sca.changeCursor(cursor); // have the adapter use the new cursor,
									// changeCursor closes old cursor too
		sca.notifyDataSetChanged(); // have the adapter tell the observers
	}

	/***
	 * 
	 * This is an Item Long Click Listener(Long Click), for use with the
	 * ListView. When an item in the listView is clicked, an alert dialog will
	 * pop up with two button:one to dismiss and one to confirm the delete.
	 * Deletes the corresponding record from the database .
	 * 
	 */
	public OnItemLongClickListener deleteItem = new OnItemLongClickListener() {
		public boolean onItemLongClick(AdapterView<?> parent, View view, int position, final long id) {
			final int itinerary_id = (int) id;
			// Creates/Displays an alert dialog
			AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
			alertDialog.setTitle("Delete Item "); // Setting
													// Dialog
													// Title
			alertDialog.setMessage("Are you sure you want to delete this?"); // Setting
																				// Dialog
																				// Message

			// if the yes button is clicked, delete the corresponding database
			// record
			alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					dbh.deleteItinerary(itinerary_id);
					refreshView();
				}
			});

			// if the No button is clicked, close the alert dialog.
			alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();
				}
			});

			alertDialog.show(); // Showing Alert Message
			return true;
		}
	};

	/**
	 * This is an Item Click Listener(Short Click), for use with the ListView.
	 * When an item in the list is clicked, the method creates an Intent object
	 * and starts the activity EditActivity.
	 * 
	 */
	public OnItemClickListener editItem = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, final long id) {

			Intent editIntent = new Intent(context, EditActivity.class);
			int intId = (int) id;
			editIntent.putExtra("itinerary_id", intId); // id of the item to be
														// edit
			startActivity(editIntent);

		}
	};
}
