package com.wanderlust.travelproject;

import com.bob.travelproject.R;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

/**
 * This class fires all the intents necessary when clicking on the main menu. It
 * will also prompt a register activity, so the user can sign in to the
 * application if he did not already. If he already is signed in, it displays a
 * Welcome message and allows him to use the application.
 * 
 * @author Rita Lazaar, Brandon Balala, Marjorie Morales, Marvin Francisco
 *
 */
public class MainActivity extends Activity {

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
	 * @param menu
	 *            - Menu object
	 * @return booleanE true
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.shared_menu, menu);
		return true;
	}

	/**
	 * 
	 * It will the set the menu items - one settings : which will fire an intent
	 * for the settings activity and the about one, which will fire the About
	 * activity.
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.settingsMenuItem) {
			Intent intent = new Intent(this, SettingsActivity.class);
			startActivity(intent);
			return true;
		} else if (id == R.id.aboutMenuItem) {
			Intent intent = new Intent(this, AboutActivity.class);
			startActivity(intent);
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	/**
	 * This will fire the intent for the TipCalculator activity
	 * 
	 * @param view
	 */
	public void tipCalculator(View view) {

		Intent i = new Intent(this, TipCalculatorActivity.class);
		startActivity(i);

	}

	/**
	 * This will fire the intent for the UnitConverterActivity activity
	 * 
	 * @param view
	 */
	public void conversion(View view) {

		Intent i = new Intent(this, UnitConverterActivity.class);
		startActivity(i);

	}
	
	/**
	 * This will fire the intent for the ToBeCreatedActivity activity
	 * 
	 * @param view
	 */
	public void toBeCreated(View view) {

		Intent intent = new Intent(this, ToBeCreatedActivity.class);
		startActivity(intent);

	}

	/**
	 * This will fire the intent for the TodayActivityFlash activity
	 * 
	 * @param view
	 */
	public void launchToday(View view) {
		Intent i = new Intent(this, TodayActivtyFlash.class);
		startActivity(i);
	}

	/**
	 * This will fire the intent for the CurrentTripActivity activity
	 * 
	 * @param view
	 */
	public void currentTrip(View view) {
		Intent currentTrips = new Intent(this, CurrentTripActivity.class);
		startActivity(currentTrips);
	}

	/**
	 * This will fire the intent for the WeatherActivity activity
	 * 
	 * @param view
	 */
	public void weather(View view) {
		Intent weather = new Intent(this, WeatherActivity.class);
		startActivity(weather);
	}

	/**
	 * This will fire the intent for the TripActivity activity
	 * 
	 * @param view
	 */
	public void manageTrips(View view) {
		Intent allTrips = new Intent(this, TripActivity.class);
		startActivity(allTrips);
	}

	/**
	 * This will fire the intent for the AboutActivity activity
	 * 
	 * @param view
	 */
	public void about(View view) {
		Intent about = new Intent(this, AboutActivity.class);
		startActivity(about);
	}

	/**
	 * This will fire the intent for the CurrencyConverterActivity activity
	 * 
	 * @param view
	 */
	public void launchCurrencyConverter(View view) {
		Intent intent = new Intent(this, CurrencyConverterActivity.class);
		startActivity(intent);
	}
}
