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
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;

public class ItineraryActivity extends Activity {
	private static DBHelper dbh;
	private Cursor cursor;
	private int trip_id;
	private int location_id = 0;

	private SimpleCursorAdapter sca;
	private Context context;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_itinerary);
		context = this;
		
		Bundle bundle = getIntent().getExtras();
		if (bundle != null){
			trip_id = (Integer) bundle.getInt("trip_id");
			//location_id = (Integer) bundle.getInt("location_id");
		}
		String[] from = new String[] { DBHelper.COLUMN_ARRIVALDATE, DBHelper.COLUMN_CATEGORY, DBHelper.COLUMN_DESCRIPTION }; 	// THE DESIRED COLUMNS TO BE BOUND
		int[] to = new int[] {  R.id.display_itinerary_date,R.id.display_itinerary_category, R.id.display_itinerary_description  }; 	// THE XML DEFINED VIEWS WHICH THEDATA WILL BE BOUND TO
		ListView lv = (ListView) findViewById(R.id.displayItinaries);
		dbh = DBHelper.getDBHelper(this);

		
		cursor = dbh.getTripItineraries(trip_id);
		sca = new SimpleCursorAdapter(this, R.layout.list_itinaries, cursor, from, to, 0);			
		lv.setAdapter(sca);
		
		lv.setOnItemClickListener(editItem);
		lv.setOnItemLongClickListener(deleteItem);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_itinerary, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.addItineraryBtn) {
			Intent add = new Intent(this, AddItineraryActivity.class);
			add.putExtra("trip_id", trip_id);
			add.putExtra("location_id", location_id);

			startActivity(add);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	/**
	 * This method is executed when the activity(ItineraryActivity) restart.
	 * It calls the super and refreshView method. 
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
		cursor = dbh.getTripItineraries(trip_id);				// renew the cursor
		sca.changeCursor(cursor); 								// have the adapter use the new cursor, changeCursor closes old cursor too
		sca.notifyDataSetChanged(); 						// have the adapter tell the observers
	}
	
	/***
	 * 
	 * This is an Item Long Click Listener(Long Click), for use with the ListView.
	 * When an item in the listView is clicked, an alert dialog will pop up with two
	 * button:one to dismiss and one to confirm the delete. Deletes the
	 * corresponding record from the database .
	 * 
	 */
	public OnItemLongClickListener deleteItem = new OnItemLongClickListener() {
		public boolean onItemLongClick(AdapterView<?> parent, View view, int position, final long id) {
			// Creates/Displays an alert dialog
			AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
			alertDialog.setTitle("Delete Item "); 								// Setting Dialog Title
			alertDialog.setMessage("Are you sure you want to delete this?"); 	// Setting Dialog Message

			// if the yes button is clicked, delete the corresponding database record
			alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					dbh.deleteItinerary((int) id);
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
	 * 
	 * This is an Item Click Listener(Short Click), for use with the
	 * ListView. When an item in the list is clicked, the method creates an
	 * Intent object and starts the activity EditActivity.
	 * 
	 */
	public OnItemClickListener editItem = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, final long id) {
			Intent editIntent = new Intent(context, EditActivity.class);
			int intId = (int) id;
			editIntent.putExtra("itinerary_id", intId); 								// id of the item to be edit
			startActivity(editIntent);
		}
	};
}
