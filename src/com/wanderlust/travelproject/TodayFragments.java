package com.wanderlust.travelproject;

import com.bob.travelproject.R;

import android.app.Activity;
import android.os.Bundle;

public class TodayFragments extends Activity {
	/**
	 * This method will instantiate the activity today xml with only a list view on it.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_today);
	}
}