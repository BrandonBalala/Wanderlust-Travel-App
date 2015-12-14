package com.wanderlust.travelproject;

import java.util.Locale;

import com.bob.travelproject.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

/**
 * This class will allow user to modify or add the information in the shared
 * preference of the user's device.
 * 
 * @author Marvin Francisco
 *
 */
public class SettingsActivity extends Activity {
	private String username;
	private String password;
	private String fname;
	private String lname;
	private String cc;
	private String city;
	private int theCurrency;
	private TextView tvUsername, tvFirstname, tvLastname, tvPassword, tvCc, tvCity;
	private Spinner myCurrencySpinner;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		SharedPreferences mSharedPreference = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

		tvUsername = (EditText) findViewById(R.id.settings_username_text);
		tvFirstname = (EditText) findViewById(R.id.settings_firstname_text);
		tvLastname = (EditText) findViewById(R.id.settings_lastname_text);
		tvPassword = (EditText) findViewById(R.id.settings_password_text);
		tvCc = (EditText) findViewById(R.id.settings_countrycode_text);
		myCurrencySpinner = (Spinner) findViewById(R.id.myCurrencySpinner);
		tvCity = (EditText) findViewById(R.id.settings_city_text);

		username = (mSharedPreference.getString("username", ""));
		password = (mSharedPreference.getString("password", ""));
		fname = (mSharedPreference.getString("firstname", ""));
		lname = (mSharedPreference.getString("lastname", ""));
		cc = (mSharedPreference.getString("cc", ""));
		theCurrency = (mSharedPreference.getInt("theCurrency", 0));
		city = (mSharedPreference.getString("city", ""));

		tvUsername.setText(username);
		tvPassword.setText(password);
		tvFirstname.setText(fname);
		tvLastname.setText(lname);
		tvCc.setText(cc);
		tvCity.setText(city);

		initializeSpinner(myCurrencySpinner, R.array.currencies);
		myCurrencySpinner.setSelection(theCurrency);
	}

	/**
	 * Change content of the given spinner with the array that has the specified
	 * resource id
	 * 
	 * @param spinner
	 * @param textArrayResId
	 */
	private void initializeSpinner(Spinner spinner, int textArrayResId) {
		// Create an ArrayAdapter using the string array and a default spinner
		// layout
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, textArrayResId,
				android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		spinner.setAdapter(adapter);
	}

	/**
	 * this method will validate the values input in the settings_activity.xml,
	 * fields should not be empty
	 * 
	 * @return
	 */
	public boolean validateInput() {
		boolean inputNotEmpty = true;

		EditText tvUsername = (EditText) findViewById(R.id.settings_username_text);
		EditText tvFirstname = (EditText) findViewById(R.id.settings_firstname_text);
		EditText tvLastname = (EditText) findViewById(R.id.settings_lastname_text);
		EditText tvPassword = (EditText) findViewById(R.id.settings_password_text);
		EditText tvCity = (EditText) findViewById(R.id.settings_city_text);

		String username = tvUsername.getText().toString();
		String password = tvPassword.getText().toString();
		String fname = tvFirstname.getText().toString();
		String lname = tvLastname.getText().toString();
		String city = tvCity.getText().toString();

		if (TextUtils.isEmpty(username)) {
			tvUsername.setError("You must enter your username");
			inputNotEmpty = false;
		}
		if (TextUtils.isEmpty(password)) {
			tvPassword.setError("You must enter your password");
			inputNotEmpty = false;
		}
		if (TextUtils.isEmpty(fname)) {
			tvFirstname.setError("You must enter your first name");
			inputNotEmpty = false;
		}
		if (TextUtils.isEmpty(lname)) {
			tvLastname.setError("You must enter your last name");
			inputNotEmpty = false;
		}
		if (TextUtils.isEmpty(city)) {
			tvLastname.setError("You must enter your a city name");
			inputNotEmpty = false;
		}
		return inputNotEmpty;
	}

	/**
	 * This method will save all the values inputed in the
	 * settings_activity.xml in the shared preference
	 * 
	 * @param view
	 */
	@SuppressLint("DefaultLocale")
	public void saveSettings(View view) {
		if (validateInput()) {
			/*
			 * These lines of code opens the preference Editor, adds three
			 * String-type preferences called username, firstname, lastname,
			 * city and password and saves the change.
			 */

			SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
			SharedPreferences.Editor editor = prefs.edit();

			editor.putString("username", tvUsername.getText().toString().trim());
			editor.putString("firstname", tvFirstname.getText().toString().trim());
			editor.putString("lastname", tvLastname.getText().toString().trim());
			editor.putString("city", tvCity.getText().toString().trim());
			editor.putString("password", tvPassword.getText().toString().trim());

			if (tvCc.getText().equals("") || tvCc.getText().equals(null)) {
				cc = Locale.getDefault().getCountry(); // country code
				editor.putString("cc", cc);
			} else
				editor.putString("cc", tvCc.getText().toString().trim().toUpperCase());
			editor.putInt("theCurrency", myCurrencySpinner.getSelectedItemPosition());

			editor.commit();
			finish();
		}
	}

	@Override
	public void onBackPressed() {
		// Creates/Displays an alert dialog
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
		alertDialog.setTitle("Delete Item "); // Setting Dialog Title
		alertDialog.setMessage("Are you sure you want to discard the changes?");

		// if the yes button is clicked, close the EditActivity
		alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				finish();
			}
		});

		// if the No button is clicked, close the alert dialog.
		alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});

		alertDialog.show(); // Showing Alert Message
	}

}
