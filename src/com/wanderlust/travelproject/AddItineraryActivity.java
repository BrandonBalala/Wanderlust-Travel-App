package com.wanderlust.travelproject;

import java.util.Calendar;

import com.bob.travelproject.R;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

public class AddItineraryActivity extends Activity {
	private Button departurePickDate;
	private Button arrivalPickDate;

	private int mYear;
	private int mMonth;
	private int mDay;

	static final int DEPARTURE_DIALOG_ID = 0;
	static final int ARRIVAL_DIALOG_ID = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_itinerary);

		// capture our View elements
		departurePickDate = (Button) findViewById(R.id.addDepartureDate);
		arrivalPickDate = (Button) findViewById(R.id.addArrivalDate);

		// add a click listener to the button
		departurePickDate.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				showDialog(DEPARTURE_DIALOG_ID);

			}
		});

		// add a click listener to the button
		arrivalPickDate.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				showDialog(ARRIVAL_DIALOG_ID);
			}
		});

		// get the current date
		final Calendar c = Calendar.getInstance();
		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH);
		mDay = c.get(Calendar.DAY_OF_MONTH);

		// display the current date (this method is below)
		updateDisplay(departurePickDate);
		updateDisplay(arrivalPickDate);

	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DEPARTURE_DIALOG_ID:
			return new DatePickerDialog(this, departureDateSetListener, mYear, mMonth, mDay);

		case ARRIVAL_DIALOG_ID:
			return new DatePickerDialog(this, arrivalDateSetListener, mYear, mMonth, mDay);
		}
		return null;

	}

	// updates the date we display in the Button text
	private void updateDisplay(Button btnDatePicker) {
		btnDatePicker.setText(new StringBuilder()
				// Month is 0 based so add 1
				.append(mMonth + 1).append("-").append(mDay).append("-").append(mYear).append(" "));

	}

	// the callback received when the user "sets" the date in the dialog
	private DatePickerDialog.OnDateSetListener departureDateSetListener = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
			mYear = year;
			mMonth = monthOfYear;
			mDay = dayOfMonth;
			updateDisplay(departurePickDate);
		}
	};
	// the callback received when the user "sets" the date in the dialog
	private DatePickerDialog.OnDateSetListener arrivalDateSetListener = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
			mYear = year;
			mMonth = monthOfYear;
			mDay = dayOfMonth;
			updateDisplay(arrivalPickDate);
		}
	};
	public void addItinerary(View view){
		
	}
	
}