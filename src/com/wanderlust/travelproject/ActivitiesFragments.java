package com.wanderlust.travelproject;

import com.bob.travelproject.R;

import android.R.color;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;

public class ActivitiesFragments extends Fragment {
	private final static String TAG = "ActivitiesFragments";
	View view_a;
	private Cursor cursor;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		String[] tripTypes = getResources().getStringArray(R.array.trip_types);
		// Receive intent index of the current trips displayed.
		// Receive intent because the call is from a portrait view of the device
		Intent intent = getActivity().getIntent();
		int index = intent.getIntExtra("index", -1);

		// use of arguments is used when the call to the fragment is from a
		// landscape view.
		Bundle args = getArguments();
		if (index == -1)
			index = args.getInt("index", -1);

		// Line that will instantiate the database query of activities today.
		// DBHelper dbh = DBHelper.getDBHelper(this);
		// dbh.getItinirary(index); // method that will retrieve all the
		// activities or trips that are associated with the trip
		// from here, items will be added to the view based on the number of
		// items there are.

		// mock data for presentation purposes.
		// name, description, city, country code.

		LinearLayout ll = new LinearLayout(getActivity());
		TextView tv = new TextView(getActivity());
		
//		View separator = new View(this.getActivity());
//		separator.setMinimumWidth(ll.getWidth());
//		separator.setMinimumHeight(ll.getHeight());
//		separator.setBackgroundColor(color.darker_gray);
		
		tv.setText("You are going for a " + tripTypes[index] + " :");
		ll.addView(tv);
		
//		ll.addView(separator);
		
		
		LinearLayout trips = new LinearLayout(getActivity());
		trips.setOrientation(LinearLayout.VERTICAL);

		switch (index) {
		case 0:
			for (int i = 0; i <= 2; i++)
				trips.addView(addtextView(0, i));
			break;
		case 1:
			for (int i = 0; i <= 1; i++)
				trips.addView(addtextView(0, i));
			break;
		case 2:
			for (int i = 0; i <= 1; i++)
				trips.addView(addtextView(0, i));
			break;
		case 3:
			for (int i = 0; i <= 1; i++)
				trips.addView(addtextView(0, i));
			break;
		default:
			for (int i = 0; i <= 1; i++)
				trips.addView(addtextView(0, i));
			break;
		}

		ll.addView(trips);
		view_a = ll;

		return view_a;
	}

	private TextView addtextView(int index, int num) {
		TextView tv = new TextView(this.getActivity());

		String[] walktripsdesc = { "Long Walk", "short walks", "Lazy walk" };
		String[] walktripcode = { "US", "PH", "CN" };
		String[] walktripcity = { "New York", "Manila", "Montreal" };

		String[] flytripsdesc = { "33 Hours flight", "5 hour flight" };
		String[] flytripcode = { "PH", "US" };
		String[] flytripcity = { "Manila", "New York" };

		String[] boattripsdesc = { "Cross the river", "Cruise" };
		String[] boattripcode = { "PH", "CN" };
		String[] boattripcity = { "Manila", "Montreal" };

		String[] ferrytripsdesc = { "Staten Island crossing", "US - Canada Border" };
		String[] ferrytripcode = { "US", "CN" };
		String[] ferrytripcity = { "New York", "Montreal" };

		String[] cartripsdesc = { "Toronto - Montreal Long drive", "Atwater - Jean Talon drive" };
		String[] carttripcode = { "CN", "CN" };
		String[] carttripcity = { "Montreal", "Montreal" };

		String text = "You have a ";

		switch (index) {
		case 0:
			text = text + walktripsdesc[num] + " at " + walktripcity[num] + "," + walktripcode[num];
			break;
		case 1:
			text = text + flytripsdesc[num] + " at " + flytripcity[num] + "," + flytripcode[num];
			break;
		case 2:
			text = text + boattripsdesc[num] + " at " + boattripcity[num] + "," + boattripcode[num];
			break;
		case 3:
			text = text + ferrytripsdesc[num] + " at " + ferrytripcity[num] + "," + ferrytripcode[num];
			break;
		default:
			text = text + cartripsdesc[num] + " at " + carttripcity[num] + "," + carttripcode[num];
			break;
		}

		tv.setText(text);
		return tv;
	}

	public static ActivitiesFragments newInstance(int index) {
		Log.v(TAG, "Creating new instance: " + index);
		ActivitiesFragments fragment = new ActivitiesFragments();

		Bundle args = new Bundle();
		args.putInt("index", index);
		fragment.setArguments(args);
		return fragment;
	}

	public int getShownIndex() {
		int index = -1;
		Bundle args = getArguments();
		if (args != null) {
			index = args.getInt("index", -1);
		}
		if (index == -1) {
			Log.e(TAG, "Not an array index.");
		}

		return index;
	}

}