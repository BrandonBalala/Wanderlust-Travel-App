package com.wanderlust.travelproject;

import com.bob.travelproject.R;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;


public class CurrentTripActivity extends Activity {

	private final static String TAG = "FRAG-SIMPLEACTIVITY";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_current_trip);
		Log.d(TAG, "onCreate()");
	}

	@Override
	protected void onStart() {
		super.onStart();
		Log.d(TAG, "onStart()");
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.d(TAG, "onResume()");
	}

	@Override
	protected void onPause() {
		super.onPause();
		Log.d(TAG, "onPause()");
	}

	@Override
	protected void onStop() {
		super.onStop();
		Log.d(TAG, "onStop()");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.d(TAG, "onDestroy()");
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		Log.d(TAG, "onRestart()");
	}
}