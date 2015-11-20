package com.bob.travelproject;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class UnitConverterActivity extends Activity {
	
	/**
	 * SPINNER: DISTANCE VOLUME WEIGHT
	 * 
	 * TWO TEXT FIELDS EACH TEXT FIELD HAS A DROP DOWN LIST BESIDE IT
	 * DROP DOWN LIST CONTAINTS UNITS BASED ON CHOSEN CATEGORY IN SPINNER
	 */

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_unit_converter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.unit_converter, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
