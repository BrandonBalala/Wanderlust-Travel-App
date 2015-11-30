package com.wanderlust.travelproject;

import com.bob.travelproject.R;

import android.app.Activity;
import android.os.Bundle;

public class TodayActivityFragments extends Activity {
	private final static String TAG = "TodayActivityFragments";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_today);
	}
}
