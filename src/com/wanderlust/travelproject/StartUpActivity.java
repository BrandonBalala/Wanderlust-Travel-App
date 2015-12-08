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

public class StartUpActivity extends Activity {
	private String username;
	private String fname;
	private String lname;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start_up);
	}

	@Override
	public void onBackPressed() {
		// i'VE TESTED IT WITH FINISH()
		moveTaskToBack(true);
	}

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

	public void enterBtn(View view) {
		if (validateInput()) {

			String cc = Locale.getDefault().getCountry(); // country code
			String currency = Currency.getInstance(getResources().getConfiguration().locale).getCurrencyCode(); // currency

			/*
			 * These lines of code opens the preference Editor, adds three
			 * String-type preferences called username, firstname and lastName,
			 * and saves the change.
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
