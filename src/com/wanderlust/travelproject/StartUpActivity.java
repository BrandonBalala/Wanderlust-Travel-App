package com.wanderlust.travelproject;

import java.util.Currency;
import java.util.Locale;
import com.bob.travelproject.R;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

/**
 * This activity is first launch when the application is newly install to the
 * tablet(android). This activity is fired by the MainActivity when the shared
 * preferences is empty. This activity asks for user name, first and last name
 * and saves it to the Shared Preferences.
 * 
 * @author Marjorie Morales, Rita Lazaar, Brandon Balala, Marvin Francisco
 *
 */
public class StartUpActivity extends Activity {
	private String fname;
	private String lname;
	private String username;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start_up);
	}

	/**
	 * The purpose of this method is to exit the application when the
	 * application back button is clicked. I've tested this with finish(), with
	 * finish when the back button is clicked, it goes back to this activity
	 * because the shared preference is empty therefore the main activity
	 */
	@Override
	public void onBackPressed() {
		moveTaskToBack(true);
	}

	/**
	 * This method validates the user input informations.
	 * 
	 * @return boolean - false when an EditText is empty. 
	 * 					 true when all the EditText is filled up
	 */
	public boolean validateInput() {
		boolean inputNotEmpty = true;

		EditText et1 = (EditText) findViewById(R.id.username);
		username = et1.getText().toString();
		EditText et2 = (EditText) findViewById(R.id.fname);
		fname = et2.getText().toString();
		EditText et3 = (EditText) findViewById(R.id.lname);
		lname = et3.getText().toString();

		if (TextUtils.isEmpty(username)) {
			et1.setError("You must enter your username");
			inputNotEmpty = false;
		}
		if (TextUtils.isEmpty(fname)) {
			et2.setError("You must enter your first name");
			inputNotEmpty = false;
		}
		if (TextUtils.isEmpty(lname)) {
			et3.setError("You must enter your last name");
			inputNotEmpty = false;
		}
		return inputNotEmpty;
	}

	/**
	 * This method is called when the enter button is clicked. This method puts
	 * the user's username, firstname, lastName, country code and the country's
	 * currency to the Shared Preference. This method close the activity and
	 * goes back to the MainActivity.
	 * 
	 * @param view
	 */
	public void enterBtn(View view) {

		if (validateInput()) {
			String cc = Locale.getDefault().getCountry(); // country code
			String currency = Currency.getInstance(getResources().getConfiguration().locale).getCurrencyCode(); // currency

			/*
			 * These lines of code opens the preference Editor, adds five
			 * String-type preferences called username, firstname, lastName,
			 * country code and the country's currency and saves then to the
			 * Shared Preferences
			 */
			SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
			SharedPreferences.Editor editor = prefs.edit();
			editor.putString("username", username);
			editor.putString("firstname", fname);
			editor.putString("lastname", lname);
			editor.putString("cc", cc);
			editor.putString("currency", currency);
			editor.commit();
			finish();
		}
	}
}
