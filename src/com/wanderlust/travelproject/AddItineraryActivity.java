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

/**
 * 
 * This activity is use to add a budgeted and actual expense to a trip. This
 * activity handles the user input validation and the saves the data to the
 * database. The main purpose of this activity is to create a new budgeted
 * expenses and actual expense.
 * 
 * @author Marjorie Morales, Rita Lazaar, Brandon Balala, Marvin Francisco
 *
 */
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
	private TextView actualDepartureDateError;
	private TextView actualArrivalDateError;
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

	private Double actualAmount;
	private String actualDescription;
	private String actualCategory;
	private String actualNameOfSupplier;
	private String actualAddress;

	private int trip_id;
	private int budgetedExpense_id;
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
		actualDepartureDateError = (TextView) findViewById(R.id.actualDepartureErrorTv);
		actualArrivalDateError = (TextView) findViewById(R.id.actualArrivalErrorTv);
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

		// gets the trip_id send to the activity by the previous activity.
		trip_id = (Integer) getIntent().getExtras().getInt("trip_id");
		dbh = DBHelper.getDBHelper(this);

		/**
		 * These lines of code gets all the location on the database. Then, put
		 * then to an ArrayList<String> in order to put on an ArrayAdapter with
		 * a Spinner View.
		 */
		Cursor locations = dbh.getAllLocations();
		List<String> list = new ArrayList<String>();
		while (locations.moveToNext()) {
			list.add(locations.getString(locations.getColumnIndex(DBHelper.COLUMN_NAME)));
		}
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		locationSpinner.setAdapter(dataAdapter);

	}

	/**
	 * This method creates a dialog that managed(saved and restores) of the
	 * date.
	 */
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
			actualDepartureDateError.setVisibility(View.INVISIBLE);

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
			actualArrivalDateError.setVisibility(View.INVISIBLE);

		}
	};

	/**
	 * 
	 * This method is fired when the add button is clicked. This method add the
	 * user input informations to the database.
	 * 
	 * @param view
	 */
	public void addItinerary(View view) {
		// if the user input are all valid then create an itinerary on the
		// database.
		if (validateBudgetedExpenseInput() && validateActualExpenseInput()) {
			// get the location id.
			String budgetedLocationName = locationSpinner.getSelectedItem().toString();
			int budgetedLocation_id = (int) dbh.getLocationId(budgetedLocationName);

			// create a budgeted expense row on the database. return the id of
			// the newly created itinerary(budgeted expense).
			budgetedExpense_id = (int) dbh.createNewItinerary(budgetedLocation_id, trip_id, arrivalDate, departureDate,
					amount, description, category, nameOfSupplier, address);
			dbh.createNewActualExpense(budgetedExpense_id, actualArrivalDate, actualDepartureDate, actualAmount,
					actualDescription, actualCategory, actualNameOfSupplier, actualAddress);
			finish();
		}
	}

	/**
	 * This method updates the date displayed on the Button for arrival date and
	 * departure date(budgeted expense and actual expense).
	 * 
	 * @param btnDatePicker
	 */
	private void updateDisplay(Button btnDatePicker) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		btnDatePicker.setText(new StringBuilder()
				// Month is 0 based so add 1
				.append(mMonth + 1).append("/").append(mDay).append("/").append(mYear).append(" "));
		String stringDate = (mMonth + 1) + "/" + mDay + "/" + mYear;
		try {
			date = dateFormat.parse(stringDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method validates the user input informations for the actual
	 * expenses. This method initialize the actual expense variables.
	 * 
	 * @return true - because the user is not obligated to fill up all fields on
	 *         the actual expense form.
	 *         false - when the departure date is before the arrival date.
	 */
	public boolean validateActualExpenseInput() {
		// getting input description
		EditText actualDescriptionEt = (EditText) findViewById(R.id.actual_description);
		actualDescription = actualDescriptionEt.getText().toString();
		// getting input amount
		EditText actualAmountEt = (EditText) findViewById(R.id.actual_amount);
		String actualAmountString = actualAmountEt.getText().toString();
		// getting input category
		EditText actualCategoryEt = (EditText) findViewById(R.id.actual_category);
		actualCategory = actualCategoryEt.getText().toString();
		// getting input name of supplier
		EditText actualNameOfSupplierEt = (EditText) findViewById(R.id.actual_name_of_supplier);
		actualNameOfSupplier = actualNameOfSupplierEt.getText().toString();
		// getting input name of supplier
		EditText actualAddressEt = (EditText) findViewById(R.id.actual_address);
		actualAddress = actualAddressEt.getText().toString();
		// if the user did not fill the actual arrival date, set it with the
		// budgeted arrival date.
		if (actualArrivalDate == null) {
			actualArrivalDate = arrivalDate;
		}
		// if the user did not fill the actual departure date, set it with the budgeted departure date.
		if (actualDepartureDate == null) {
			actualDepartureDate = departureDate;
		} else {
			if (!actualDepartureDate.equals(actualArrivalDate) && !actualDepartureDate.after(actualArrivalDate)) {
				Toast.makeText(this, "The departure date should not be before the arrival date", Toast.LENGTH_SHORT)
						.show();
				actualDepartureDateError.setVisibility(View.VISIBLE);
				return false;
			}
		}
		// if the user did not fill the actual amount, set it with the budgeted departure date.
		if (TextUtils.isEmpty(actualAmountString)) 
			actualAmount =0.0;
		else
			actualAmount = Double.valueOf(actualAmountString);
		return true;
	}

	/**
	 * This method validates the user input informations for the budgeted
	 * expenses. Return false when user did not fill a field.
	 * 
	 * @return boolean - false when an EditText is empty. true when all the
	 *         EditText is filled up
	 */
	public boolean validateBudgetedExpenseInput() {
		boolean valid = true;

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
			if (!departureDate.equals(arrivalDate) && !departureDate.after(arrivalDate)) {
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