package com.wanderlust.travelproject;

import com.bob.travelproject.R;
import com.wanderlust.database.DBHelper;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;

/***
 * 
 * This activity is fired when a trip is clicked. This activity displays all the
 * itineraries(budgeted expenses) and actual expenses of that trip. 
 * 
 * @author Marjorie Morales, Rita Lazaar, Brandon Balala, Marvin Francisco
 *
 */
public class ItineraryActivity extends Activity {
	private int trip_id;
	private Cursor cursor;
	private Context context;
	private static DBHelper dbh;
	private ItineraryCursorAdapter itineraryAdapter;  		//Custom Cursor Adapter.

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_itinerary);
		context = this;
		
		// gets the trip_id send to the activity by the previous activity. 
		trip_id = (Integer) getIntent().getExtras().getInt("trip_id");

		ListView lv = (ListView) findViewById(R.id.displayItinaries);
		
		dbh = DBHelper.getDBHelper(this);
		// gets all the itineraries, actual expenses and location of a trip.
		cursor = dbh.getItineraryActualExpenseLocation(trip_id);
		itineraryAdapter = new ItineraryCursorAdapter(this, cursor, 0);
		
		lv.setAdapter(itineraryAdapter);
		lv.setOnItemClickListener(editItem);
		lv.setOnItemLongClickListener(deleteItem);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_itinerary, menu);
		return true;
	}
	
	/**
	 * 
	 * It will the set the menu items - one add : which will fire an intent
	 * for the add itinerary activity.
	 * 
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.addItineraryBtn) {
			Intent add = new Intent(this, AddItineraryActivity.class);
			add.putExtra("trip_id", trip_id);
			startActivity(add);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/***
	 * 
	 * This is an Item Long Click Listener(Long Click), for use with the
	 * ListView. When an item in the listView is long clicked, an alert dialog will
	 * pop up with two button:one to dismiss and one to confirm the delete.
	 * Deletes the corresponding record(an itinerary row) from the database .
	 * 
	 */
	public OnItemLongClickListener deleteItem = new OnItemLongClickListener() {
		public boolean onItemLongClick(AdapterView<?> parent, View view, int position, final long id) {
			// Creates/Displays an alert dialog
			AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
			alertDialog.setTitle("Delete Item "); // Setting Dialog Title
			alertDialog.setMessage("Are you sure you want to delete this?"); // Setting Dialog Message

			// if the yes button is clicked, delete the corresponding database record
			alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					int itinerary_id = (int) id;
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
	 * This method is executed when the activity(ItineraryActivity) restart. It
	 * calls the super and refreshView method.
	 */
	public void onResume() {
		super.onResume();
		refreshView();
	}

	/**
	 * 
	 * This method gets called when an itinerary is deleted, edited or created.
	 * Creates a new cursor and tell the adapter there is a new data.
	 * 
	 */
	public void refreshView() {
		cursor = dbh.getItineraryActualExpenseLocation(trip_id); // renew the cursor
		// have the adapter use the new cursor, changeCursor closes old cursor too
		itineraryAdapter.changeCursor(cursor); 
		itineraryAdapter.notifyDataSetChanged(); // have the adapter tell the observers
	}

	/**
	 * 
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
			editIntent.putExtra("itinerary_id", intId); // id of the item to be edit
			startActivity(editIntent);
		}
	};
}
