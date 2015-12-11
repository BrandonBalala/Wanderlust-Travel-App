package com.wanderlust.travelproject;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.bob.travelproject.R;
import com.wanderlust.database.DBHelper;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class AddItineraryActivity extends Activity {
	private DBHelper dbh;

	static final int DEPARTURE_DIALOG_ID = 0;
	static final int ARRIVAL_DIALOG_ID = 1;
	static final int ACTUAL_DEPARTURE_DIALOG_ID = 2;
	static final int ACTUAL_ARRIVAL_DIALOG_ID = 3;

	private Button departurePickDate;
	private Button arrivalPickDate;
	private Button actualDeparturePickDate;
	private Button actualArrivalPickDate;
	private TextView departureDateError;
	private TextView arrivalDateError;
	private Spinner locationSpinner;

	private int mYear;
	private int mMonth;
	private int mDay;

	private Timestamp arrivalDate;
	private Timestamp departureDate;
	private Timestamp actualArrivalDate;
	private Timestamp actualDepartureDate;
	
	private Double amount;
	private String description;
	private String category;
	private String nameOfSupplier;
	private String address;
	
	private Double actualAmount = 0.0;
	private String actualDescription = "";
	private String actualCategory = "";
	private String actualNameOfSupplier = "";
	private String actualAddress = "";
	
	private int trip_id;
	private int budgetedLocation_id;
	private int budgetedExpense_id;

	public static SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
	public static Date date;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_itinerary);

		// capture our View elements
		departurePickDate = (Button) findViewById(R.id.addDepartureDate);
		arrivalPickDate = (Button) findViewById(R.id.addArrivalDate);
		actualDeparturePickDate = (Button) findViewById(R.id.addActualDepartureDate);
		actualArrivalPickDate = (Button) findViewById(R.id.addActualArrivalDate);
		departureDateError = (TextView) findViewById(R.id.departureErrorTv);
		arrivalDateError = (TextView) findViewById(R.id.arrivalErrorTv);
		locationSpinner = (Spinner) findViewById(R.id.spinner_location);

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

		// add a click listener to the button
		actualDeparturePickDate.setOnClickListener(new View.OnClickListener() {
			@SuppressWarnings("deprecation")
			public void onClick(View v) {
				showDialog(ACTUAL_DEPARTURE_DIALOG_ID);

			}
		});

		// add a click listener to the button
		actualArrivalPickDate.setOnClickListener(new View.OnClickListener() {
			@SuppressWarnings("deprecation")
			public void onClick(View v) {
				showDialog(ACTUAL_ARRIVAL_DIALOG_ID);
			}
		});

		Bundle bundle = getIntent().getExtras();
		if (bundle != null)
			trip_id = (Integer) bundle.getInt("trip_id");
		dbh = DBHelper.getDBHelper(this);

		Cursor locations = dbh.getAllLocations();
		List<String> list = new ArrayList<String>();

		while (locations.moveToNext()) {
			list.add(locations.getString(locations.getColumnIndex(DBHelper.COLUMN_NAME)));
		}

		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);	
		locationSpinner.setAdapter(dataAdapter);
		
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

		case ACTUAL_DEPARTURE_DIALOG_ID:
			DatePickerDialog da2 = new DatePickerDialog(this, actualDepartureDateSetListener, cYear, cMonth, cDay);
			da2.getDatePicker().setMinDate(newDate.getTime());
			return da2;

		case ACTUAL_ARRIVAL_DIALOG_ID:
			DatePickerDialog da3 = new DatePickerDialog(this, actualArrivalDateSetListener, cYear, cMonth, cDay);
			da3.getDatePicker().setMinDate(newDate.getTime());
			return da3;
		}
		return null;

	}

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

	// the callback received when the user "sets" the date in the dialog
	private DatePickerDialog.OnDateSetListener actualDepartureDateSetListener = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
			mYear = year;
			mMonth = monthOfYear;
			mDay = dayOfMonth;
			updateDisplay(actualDeparturePickDate);
			actualDepartureDate = new Timestamp(date.getTime());

		}
	};

	// the callback received when the user "sets" the date in the dialog
	private DatePickerDialog.OnDateSetListener actualArrivalDateSetListener = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
			mYear = year;
			mMonth = monthOfYear;
			mDay = dayOfMonth;
			updateDisplay(actualArrivalPickDate);
			actualArrivalDate = new Timestamp(date.getTime());

		}
	};

	public void addItinerary(View view) {
		// if the user input are all valid then create an itinerary on the
		// database.
		if (validateBudgetedExpenseInput()) {
			String budgetedLocationName = locationSpinner.getSelectedItem().toString();

			Cursor budgetedLocation = dbh.getLocation(budgetedLocationName);
			if (budgetedLocation.moveToFirst()) {
				budgetedLocation_id = budgetedLocation.getInt(budgetedLocation.getColumnIndex(DBHelper.COLUMN_ID));
			}
			
			budgetedExpense_id = (int) dbh.createNewItinerary(budgetedLocation_id, trip_id, arrivalDate, departureDate, amount, description, category,
					nameOfSupplier, address);
			validateActualExpenseInput();		
			finish();
		}
	}

	// updates the date we display in the Button text
	private void updateDisplay(Button btnDatePicker) {
		btnDatePicker.setText(new StringBuilder()
				// Month is 0 based so add 1
				.append(mMonth + 1).append("/").append(mDay).append("/").append(mYear).append(" "));
		String stringDate =(mMonth + 1) + "/" + mDay + "/" + mYear;
		try {
			date = dateFormat.parse(stringDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
 
	public void validateActualExpenseInput(){	
		// getting input description
		EditText actualDescriptionEt = (EditText) findViewById(R.id.actual_description);
		if(actualDescriptionEt.getText() != null)
			actualDescription = actualDescriptionEt.getText().toString();
		// getting input amount
		EditText actualAmountEt = (EditText) findViewById(R.id.actual_amount);
		if(actualAmountEt.getText() != null)
			actualAmount = Double.valueOf(actualAmountEt.getText().toString());
		 
		// getting input category
		EditText actualCategoryEt = (EditText) findViewById(R.id.actual_category);
		if(actualCategoryEt.getText() != null)
			actualCategory = actualCategoryEt.getText().toString();

		// getting input name of supplier
		EditText actualNameOfSupplierEt = (EditText) findViewById(R.id.actual_name_of_supplier);
		if(actualNameOfSupplierEt.getText() != null)
			actualNameOfSupplier = actualNameOfSupplierEt.getText().toString();

		// getting input name of supplier
		EditText actualAddressEt = (EditText) findViewById(R.id.actual_address);
		if(actualAddressEt.getText() != null)
			actualAddress = actualAddressEt.getText().toString();
		
		dbh.createNewActualExpense(budgetedExpense_id, actualArrivalDate, actualDepartureDate, actualAmount, actualDescription, actualCategory, actualNameOfSupplier, actualAddress);
		
	}
	
	public boolean validateBudgetedExpenseInput() {
		boolean valid = true;
		// verifies if the departure date is not in the past
		if (actualArrivalDate == null) {
			actualArrivalDate = arrivalDate;
		}

		if (actualDepartureDate == null) {
			actualDepartureDate = departureDate;
		} 
		// getting input description
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