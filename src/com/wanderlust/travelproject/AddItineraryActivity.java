package com.wanderlust.travelproject;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.bob.travelproject.R;
import com.wanderlust.database.DBHelper;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AddItineraryActivity extends Activity {
	static final int DEPARTURE_DIALOG_ID = 0;
	static final int ARRIVAL_DIALOG_ID = 1;

	private Button departurePickDate;
	private Button arrivalPickDate;
	private TextView departureDateError;
	private TextView arrivalDateError;

	private int mYear;
	private int mMonth;
	private int mDay;

	private Timestamp arrivalDate;
	private Timestamp departureDate;
	private Double amount;
	private String description;
	private String category;
	private String nameOfSupplier;
	private String address;
	private int trip_id;
	private int location_id = 1;

	public static SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
	public static Date date;

	// the callback received when the user "sets" the date in the dialog
	private DatePickerDialog.OnDateSetListener departureDateSetListener = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
			mYear = year;
			mMonth = monthOfYear;
			mDay = dayOfMonth;
			updateDisplay(departurePickDate);
			departureDate = new Timestamp(date.getTime());
			departureDateError.setVisibility(View.INVISIBLE);

		}
	};

	// the callback received when the user "sets" the date in the dialog
	private DatePickerDialog.OnDateSetListener arrivalDateSetListener = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
			mYear = year;
			mMonth = monthOfYear;
			mDay = dayOfMonth;
			updateDisplay(arrivalPickDate);
			arrivalDate = new Timestamp(date.getTime());
			arrivalDateError.setVisibility(View.INVISIBLE);

		}
	};

	public void addItinerary(View view) {
		// if the user input are all valid then create an itinerary on the
		// database.
		if (validateInput()) {
			DBHelper dbh = DBHelper.getDBHelper(this);
			dbh.createNewItinerary(location_id, trip_id, arrivalDate, departureDate, amount, description, category,
					nameOfSupplier, address);
			finish();
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_itinerary);

		// capture our View elements
		departurePickDate = (Button) findViewById(R.id.addDepartureDate);
		arrivalPickDate = (Button) findViewById(R.id.addArrivalDate);
		departureDateError = (TextView) findViewById(R.id.departureErrorTv);
		arrivalDateError = (TextView) findViewById(R.id.arrivalErrorTv);

		// add a click listener to the button
		departurePickDate.setOnClickListener(new View.OnClickListener() {
			@SuppressWarnings("deprecation")
			public void onClick(View v) {
				showDialog(DEPARTURE_DIALOG_ID);

			}
		});

		// add a click listener to the button
		arrivalPickDate.setOnClickListener(new View.OnClickListener() {
			@SuppressWarnings("deprecation")
			public void onClick(View v) {
				showDialog(ARRIVAL_DIALOG_ID);
			}
		});
		
		Bundle bundle = getIntent().getExtras();
		if (bundle != null)
			trip_id = (Integer) bundle.getInt("trip_id");
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		// getting the current date
		final Calendar c = Calendar.getInstance();
		int cYear = c.get(Calendar.YEAR);
		int cMonth = c.get(Calendar.MONTH);
		int cDay = c.get(Calendar.DAY_OF_MONTH);

		c.add(Calendar.DATE, 1);
		
		Date newDate = new Date();		

		switch (id) {
		case DEPARTURE_DIALOG_ID:
			DatePickerDialog da = new DatePickerDialog(this, departureDateSetListener, cYear, cMonth, cDay);
			da.getDatePicker().setMinDate(newDate.getTime());
			return da;

		case ARRIVAL_DIALOG_ID:
			DatePickerDialog da1 = new DatePickerDialog(this, arrivalDateSetListener, cYear, cMonth, cDay);
			da1.getDatePicker().setMinDate(newDate.getTime());
			return da1;
		}
		return null;

	}

	// updates the date we display in the Button text
	private void updateDisplay(Button btnDatePicker) {
		btnDatePicker.setText(new StringBuilder()
				// Month is 0 based so add 1
				.append(mMonth + 1).append("/").append(mDay).append("/").append(mYear).append(" "));

		try {
			date = dateFormat.parse((mMonth + 1) + "/" + mDay + "/" + mYear);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	public boolean validateInput() {
		boolean valid = true;
		// verifies if the departure date is not in the past
		// getting input category
		EditText descriptionEt = (EditText) findViewById(R.id.itinerary_description);
		description = descriptionEt.getText().toString();
		// getting input amount
		EditText amountEt = (EditText) findViewById(R.id.itinerary_amount);
		String amountString = amountEt.getText().toString();
		// getting input category
		EditText categoryEt = (EditText) findViewById(R.id.itinerary_category);
		category = categoryEt.getText().toString();

		// getting input name of supplier
		EditText nameOfSupplierEt = (EditText) findViewById(R.id.itinerary_name_of_supplier);
		nameOfSupplier = nameOfSupplierEt.getText().toString();

		// getting input name of supplier
		EditText addressEt = (EditText) findViewById(R.id.itinerary_address);
		address = addressEt.getText().toString();

		if (arrivalDate == null) {
			arrivalDateError.setVisibility(View.VISIBLE);
			valid = false;
		}

		if (departureDate == null) {
			departureDateError.setVisibility(View.VISIBLE);
			valid = false;
		} else {
			if (!departureDate.after(arrivalDate)) {
				Toast.makeText(this, "The departure date should not be before the arrival date", Toast.LENGTH_SHORT)
						.show();
				valid = false;
				departureDateError.setVisibility(View.VISIBLE);
			}
		}
		if (TextUtils.isEmpty(amountString)) {
			amountEt.setError("The amount cannot be empty.");
			valid = false;
		} else
			amount = Double.valueOf(amountString);

		if (TextUtils.isEmpty(description)) {
			descriptionEt.setError("The itinerary description cannot be empty.");
			valid = false;
		}

		if (TextUtils.isEmpty(category)) {
			categoryEt.setError("The itinerary category cannot be empty.");
			valid = false;
		}
		if (TextUtils.isEmpty(nameOfSupplier)) {
			nameOfSupplierEt.setError("The name of the supplier cannot be empty.");
			valid = false;
		}
		if (TextUtils.isEmpty(address)) {
			addressEt.setError("The address cannot be empty.");
			valid = false;
		}
		return valid;
	}
}