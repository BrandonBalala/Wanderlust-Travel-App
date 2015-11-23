package com.wanderlust.travelproject;

import com.wanderlust.database.DBHelper;

import android.app.Activity;
import android.app.ListFragment;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.app.FragmentManager;


public class FieldTripsListFragment extends ListFragment implements FragmentManager.OnBackStackChangedListener {

	int mCurPosition = -1;
	boolean mShowTwoFragments;
	Cursor cursor;
	SimpleCursorAdapter sca;	
	private static DBHelper dbh;


	private final static String TAG = "FRAG-LISTFRAGMENT";
	

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		
		
		// Gets trip lists from the database and displays using fragment display.
		String[] from = new String[] { DBHelper.COLUMN_NAME };
		dbh = DBHelper.getDBHelper(getActivity());
		cursor = dbh.getAllTrips();


		// Populate our ListView control within the Fragment
		setListAdapter(
				new SimpleCursorAdapter(getActivity(), android.R.layout.simple_list_item_activated_1, cursor, from, null,0));

		// Check which state we're in
		//View detailsFrame = getActivity().findViewById(R.id.fieldentry);
		//mShowTwoFragments = detailsFrame != null && detailsFrame.getVisibility() == View.VISIBLE;

		if (savedInstanceState != null) {
			mCurPosition = savedInstanceState.getInt("curChoice", 0);
		}

		if (mShowTwoFragments == true || mCurPosition != -1) {

		}

		// monitor back stack changes to update list view
		getFragmentManager().addOnBackStackChangedListener(this);

	}

	@Override
	public void onBackStackChanged() {
		//TODO


	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt("curChoice", mCurPosition);
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		//TODO
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

}