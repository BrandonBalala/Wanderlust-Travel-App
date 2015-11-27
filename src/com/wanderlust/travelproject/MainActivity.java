package com.wanderlust.travelproject;


import com.bob.travelproject.R;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity {

	private static final int SHOW_AS_ACTION_IF_ROOM = 1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public void onStart() {	
		super.onStart();
		
		// retrieves the saved informations from the shared preferences
		SharedPreferences mSharedPreference = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
		String username = (mSharedPreference.getString("username", ""));
		String fname = (mSharedPreference.getString("firstname", ""));
		String lname = (mSharedPreference.getString("lastname", ""));
		
		if (username.equals("") && fname.equals("") && lname.equals("")) {
			Intent startUp = new Intent(this, StartUpActivity.class);
			startActivity(startUp);
		}
		
		TextView tv = (TextView) findViewById(R.id.userFullName);
		tv.setText("Hello " + fname + " " + lname);	
	}
	
	/**
	 * 
	 * This is an options menu. This method pragmatically creates two items and
	 * added to the menu: about fires an explicit intent to invoke About class
	 * that display information of the application and dawson fires an implicit 
	 * intent to that launches the Dawson Computer Science web page.
	 * 
	 * @param menu - Menu object
	 * @return boolean true
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		// Inflate the menu; this adds items to the action bar if it is present.
		Intent about = new Intent(this, AboutActivity.class);
		menu.add(R.string.title_activity_about).setIntent(about).setShowAsAction(SHOW_AS_ACTION_IF_ROOM);
		
		Intent dawson = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.dawsoncollege.qc.ca/computer-science-technology/"));
		menu.add(R.string.dawson).setIntent(dawson).setShowAsAction(SHOW_AS_ACTION_IF_ROOM);
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
	public void tipCalculator(View view) {

		Intent i = new Intent(this, TipCalculatorActivity.class);
		startActivity(i);

	}
	public void conversion(View view) {

		Intent i = new Intent(this, UnitConverterActivity.class);
		startActivity(i);

	}
	
	public void toBeCreated(View view) {

		Intent i = new Intent(this, UnitConverterActivity.class);
		startActivity(i);

	}
	
	/** Called when the user clicks the Send button */
	public void launchToday(View view) {
		Intent i = new Intent(this, TodayFragments.class);
		startActivity(i);
	}
	
	public void currentTrip(View view){
		Intent currentTrips = new Intent(this, CurrentTripActivity.class);
		startActivity(currentTrips);
	}
	
	public void manageTrips(View view){
		Intent allTrips = new Intent(this, TripActivity.class);
		startActivity(allTrips);
	}
}
