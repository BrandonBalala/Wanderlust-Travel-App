package com.wanderlust.travelproject;

import java.util.Currency;
import java.util.Locale;

import com.bob.travelproject.R;
import com.bob.travelproject.R.id;
import com.bob.travelproject.R.layout;
import com.bob.travelproject.R.menu;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class SettingsActivity extends Activity {
	private String username;
	private String password;
	private String fname;
	private String lname;
	private String cc;
	private String currency;
	private TextView tvUsername, tvFirstname, tvLastname, tvPassword, tvCc, tvCurrency;

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
		tvCurrency = (EditText) findViewById(R.id.settings_currency_text);

		username = (mSharedPreference.getString("username", ""));
		password = (mSharedPreference.getString("password", ""));
		fname = (mSharedPreference.getString("firstname", ""));
		lname = (mSharedPreference.getString("lastname", ""));
		cc = (mSharedPreference.getString("cc", ""));
		currency = (mSharedPreference.getString("currency", ""));

		tvUsername.setText(username);
		tvPassword.setText(password);
		tvFirstname.setText(fname);
		tvLastname.setText(lname);
		tvCc.setText(cc);
		tvCurrency.setText(currency);

	}

	public boolean validateInput() {
		boolean inputNotEmpty = true;

		EditText tvUsername = (EditText) findViewById(R.id.settings_username_text);
		EditText tvFirstname = (EditText) findViewById(R.id.settings_firstname_text);
		EditText tvLastname = (EditText) findViewById(R.id.settings_lastname_text);
		EditText tvPassword = (EditText) findViewById(R.id.settings_password_text);

		String username = tvUsername.getText().toString();
		String password = tvPassword.getText().toString();
		String fname = tvFirstname.getText().toString();
		String lname = tvLastname.getText().toString();

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
		return inputNotEmpty;
	}

	public void saveSettings(View view) {
		if (validateInput()) {
			/*
			 * These lines of code opens the preference Editor, adds three
			 * String-type preferences called username, firstname and lastName,
			 * and saves the change.
			 */

			// tvCc = (EditText) findViewById(R.id.settings_countrycode_text);
			// tvCurrency = (EditText)
			// findViewById(R.id.settings_currency_text);

			SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
			SharedPreferences.Editor editor = prefs.edit();

			editor.putString("username", tvUsername.getText().toString().trim());
			editor.putString("firstname", tvFirstname.getText().toString().trim());
			editor.putString("lastname", tvLastname.getText().toString().trim());
			editor.putString("password", tvPassword.getText().toString().trim());

			if (tvCc.getText().equals("") || tvCc.getText().equals(null)) {
				cc = Locale.getDefault().getCountry(); // country code
				editor.putString("cc", cc);
			} else
				editor.putString("cc", tvCc.getText().toString().trim().toUpperCase());
			if (tvCurrency.getText().equals("") || tvCurrency.getText().equals(null)) {
				currency = Currency.getInstance(getResources().getConfiguration().locale).getCurrencyCode(); // currency
				editor.putString("currency", currency);
			} else
				editor.putString("currency", tvCurrency.getText().toString().trim().toUpperCase());

			editor.commit();
			finish();
		}
		Intent i = new Intent(this, MainActivity.class);
		startActivity(i);
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
