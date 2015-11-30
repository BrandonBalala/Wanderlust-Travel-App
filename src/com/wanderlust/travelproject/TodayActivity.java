package com.wanderlust.travelproject;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

import com.bob.travelproject.R;
import com.wanderlust.database.DBHelper;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.content.Intent;
import android.database.Cursor;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

/**
 * This is a class that handles the activities / trips that are supposed to be
 * done today
 * 
 * @author Marvin Francisco
 *
 */
public class TodayActivity extends ListFragment implements FragmentManager.OnBackStackChangedListener {

	private final static String TAG = "TODAY-ACTIVITY";
	int mCurPosition = -1;
	boolean mShowTwoFragments;
	private DBHelper dbh;
	private Cursor cursor;
	private SimpleCursorAdapter sca;
	boolean returnHit = false;

	@SuppressLint("SimpleDateFormat")
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		// Line that will instantiate the database query of activities today.
		dbh = DBHelper.getDBHelper(getActivity());

		Timestamp ts;
		Intent intent = getActivity().getIntent();
		int year = intent.getIntExtra("year", 2015);
		int month = intent.getIntExtra("month", 11);
		int day = intent.getIntExtra("day", 29);
		Log.v(TAG, "Intent year" + year);
		Log.v(TAG, "Intent month" + month);
		Log.v(TAG, "Intent day" + day);

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

		Log.v(TAG, "String of the timestamp" + ts);

		// checks if table is not empty for activities that are queried in
		// the
		// // DBHelper class
		if (dbh.getActivitiesToday(ts).getCount() < 1) {
			Log.v(TAG, "the database is empty with this date" + ts);
			Toast.makeText(getActivity().getApplicationContext(), "There are no trips for" + ts.toString(),
					Toast.LENGTH_LONG).show();
			getActivity().setResult(0);
			getActivity().finish();

		} else {
			getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);

			// todayTrips will be pulled out from the local database using
			// SQLite
			String[] todayTrips = new String[dbh.getActivitiesToday(ts).getCount()];
			cursor = dbh.getActivitiesToday(ts);

			cursor.moveToFirst();
			int i = 0;
			while (!cursor.isAfterLast()) {
				todayTrips[i] = cursor.getString(4);
				i++;
				cursor.moveToNext();
			}
			setListAdapter(
					new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_activated_1, todayTrips));

			// Check which state we're in
			View detailsFrame = getActivity().findViewById(R.id.fieldentry);

			mShowTwoFragments = detailsFrame != null && detailsFrame.getVisibility() == View.VISIBLE;
			if (savedInstanceState != null) {
				mCurPosition = savedInstanceState.getInt("curChoice", 0);
			}
			if (mShowTwoFragments == true || mCurPosition != -1) {
				viewActivityInfo(mCurPosition);
			}
			getFragmentManager().addOnBackStackChangedListener(this);
		}

	}

	@Override
	public void onBackStackChanged() {
		Log.d(TAG, "onBackStackChanged() ");
		// update position
		ActivitiesFragments details = (ActivitiesFragments) getFragmentManager().findFragmentById(R.id.fieldentry);
		if (details != null) {
			mCurPosition = details.getShownIndex();
			getListView().setItemChecked(mCurPosition, true);

			// if we're in single pane, then we need to switch forward to the
			// viewer
			if (!mShowTwoFragments) {
				viewActivityInfo(mCurPosition);
			}
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt("curChoice", mCurPosition);
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		Cursor cursortemp = (Cursor) this.getListView().getItemAtPosition(position);
		int idDB = cursortemp.getInt(0);
		viewActivityInfo(idDB);
	}

	void viewActivityInfo(int index) {
		mCurPosition = index;
		Log.d(TAG, "viewActivity() " + index);

		if (mShowTwoFragments == true) {
			// Check what fragment is currently shown, replace if needed.
			ActivitiesFragments details = (ActivitiesFragments) getFragmentManager().findFragmentById(R.id.fieldentry);
			if (details == null || details.getShownIndex() != index) {

				// Make new fragment to show this selection.
				ActivitiesFragments newDetails = ActivitiesFragments.newInstance(index);

				// Execute a transaction, replacing any existing fragment
				// with this one inside the frame.
				FragmentManager fm = getFragmentManager();
				FragmentTransaction ft = fm.beginTransaction();
				ft.replace(R.id.fieldentry, newDetails);
				// Add this fragment instance to the back-stack of the Activity
				// so we can backtrack through our field notes
				if (index != -1) {
					String[] fieldNotes = getResources().getStringArray(R.array.trip_types);
					String strBackStackTagName = fieldNotes[index];
					ft.addToBackStack(strBackStackTagName);
				}
				// Fade between Urls
				ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
				ft.commit();
			}

		} else {
			// Otherwise we need to launch a new activity to display
			// the dialog fragment with selected text.
			Intent intent = new Intent();
			intent.setClass(getActivity(), TodayActivitiesNodeFragments.class);
			intent.putExtra("index", index);
			startActivity(intent);
		}
	}

	@Override
	public void onAttach(Activity activity) {
		Log.d(TAG, "FRAGMENT onAttach()");
		super.onAttach(activity);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, "FRAGMENT onCreate()");
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onDestroy() {
		Log.d(TAG, "FRAGMENT onDestroy()");
		super.onDestroy();
	}

	@Override
	public void onDetach() {
		Log.d(TAG, "FRAGMENT onDetach()");
		super.onDetach();
	}

	@Override
	public void onPause() {
		Log.d(TAG, "FRAGMENT onPause()");
		super.onPause();
	}

	@Override
	public void onResume() {
		Log.d(TAG, "FRAGMENT onResume(): " + mCurPosition);
		super.onResume();
	}

	@Override
	public void onStart() {
		Log.d(TAG, "FRAGMENT onStart()");
		super.onStart();
	}

	@Override
	public void onStop() {
		Log.d(TAG, "FRAGMENT onStop()");
		super.onStop();
	}

	public void logIt(String msg) {
		final String TAG = "DBCURSOR";
		Log.d(TAG, msg);
	}
}

/**
 * This method is executed when the activity starts. This method generates a
 * 
 */
// @Override
// protected void onCreate(Bundle savedInstanceState) {
// super.onCreate(savedInstanceState);
// DBHelper dbh = DBHelper.getDBHelper(this);
//
// // checks if table is not empty. `
// if (dbh.getNumberOfEntry() != 0) {
//
// setContentView(R.layout.activity_today);
// int id = (int) (Math.random() * 20); // Generates a random id.
// boolean found = false;
// // while an item is not found.
// while (!found) {
// cursor = dbh.getItem(id);
// // checks it cursor is not null, the id exist on the database
// if (cursor.moveToNext()) {
// // display the text on the TextView.
// String todayItem =
// cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_ITEM));
// TextView tv = (TextView) findViewById(R.id.todayTv);
// tv.setText(todayItem);
// found = true;
// }
// // generates a new random id
// else
// id = (int) (Math.random() * 10);
// }
// }
// else
// Toast.makeText(this, "Todo list today is empty.",
// Toast.LENGTH_LONG).show();
// }
