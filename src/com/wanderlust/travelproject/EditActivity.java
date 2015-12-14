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
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
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
 * This activity is use to edit a budgeted and actual expense of a trip. This
 * method retrieves the budgeted and actual expense from the database and
 * display them to the field. The main purpose of this activity is to edit
 * budgeted expenses and actual expense.
 * 
 * @author Marjorie Morales
 *
 */
public class EditActivity extends Activity {
	private static DBHelper dbh;
	private int itinerary_id;
	private Cursor cursor;

	static final int DEPARTURE_DIALOG_ID = 0;
	static final int ARRIVAL_DIALOG_ID = 1;
	static final int ACTUAL_DEPARTURE_DIALOG_ID = 2;
	static final int ACTUAL_ARRIVAL_DIALOG_ID = 3;

	private int mYear;
	private int mMonth;
	private int mDay;

	public static SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
	public static Date date;

	private Button arrivalBtn;
	private Button departureBtn;
	private EditText descriptionEt;
	private EditText amountEt;
	private EditText categoryEt;
	private EditText supplier_nameEt;
	private EditText addressEt;
	private TextView departureDateError;
	private TextView arrivalDateError;
	private Spinner locationSpinner;

	private Button actualArrivalBtn;
	private Button actualDepartureBtn;
	private EditText actualDescriptionEt;
	private EditText actualAmountEt;
	private EditText actualCategoryEt;
	
	private EditText actualSupplier_nameEt;
	private EditText actualAddressEt;
	private TextView actualDepartureDateError;
	private TextView actualArrivalDateError;

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

	/**
	 * The lifecycle method, gets the budgeted expense and actual expense row to
	 * be edited. Then, display(put them back) to the EditText.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit);

		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			itinerary_id = (Integer) bundle.getInt("itinerary_id");
		}
		// capture budgeted expense View elements
		arrivalBtn = (Button) findViewById(R.id.editArrivalDate);
		departureBtn = (Button) findViewById(R.id.editDepartureDate);
		descriptionEt = (EditText) findViewById(R.id.edit_itinerary_description);
		amountEt = (EditText) findViewById(R.id.edit_itinerary_amount);
		categoryEt = (EditText) findViewById(R.id.edit_itinerary_category);
		supplier_nameEt = (EditText) findViewById(R.id.edit_itinerary_name_of_supplier);
		addressEt = (EditText) findViewById(R.id.edit_itinerary_address);
		departureDateError = (TextView) findViewById(R.id.editDepartureErrorTv);
		arrivalDateError = (TextView) findViewById(R.id.editArrivalErrorTv);
		locationSpinner = (Spinner) findViewById(R.id.edit_spinner_location);
		// capture actual expense View elements
		actualDepartureBtn = (Button) findViewById(R.id.editActualDepartureDate);
		actualArrivalBtn = (Button) findViewById(R.id.edtActualArrivalDate);
		actualDescriptionEt = (EditText) findViewById(R.id.edit_actual_description);
		actualAmountEt = (EditText) findViewById(R.id.edit_actual_amount);
		actualCategoryEt = (EditText) findViewById(R.id.edit_actual_category);
		actualSupplier_nameEt = (EditText) findViewById(R.id.edit_actual_name_of_supplier);
		actualAddressEt = (EditText) findViewById(R.id.edit_actual_address);
		actualDepartureDateError = (TextView) findViewById(R.id.editActualDepartureErrorTv);
		actualArrivalDateError = (TextView) findViewById(R.id.editActualArrivalErrorTv);

		dbh = DBHelper.getDBHelper(this);
		cursor = dbh.getItineraryActualExpense(itinerary_id);
		// display the budgeted informations to the fields
		while (cursor.moveToNext()) {
			String arrivalDateString = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_ARRIVALDATE));
			String departureDateString = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_DEPARTUREDATE));
			amount = cursor.getDouble(cursor.getColumnIndex(DBHelper.COLUMN_AMOUNT));
			description = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_DESCRIPTION));
			category = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_CATEGORY));
			nameOfSupplier = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_SUPPLIER_NAME));
			address = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_ADDRESS));

			String actualArrivalDateString = cursor
					.getString(cursor.getColumnIndex(DBHelper.COLUMN_ACTUAL_ARRIVALDATE));
			String actualDepartureDateString = cursor
					.getString(cursor.getColumnIndex(DBHelper.COLUMN_ACTUAL_DEPARTUREDATE));
			actualAmount = cursor.getDouble(cursor.getColumnIndex(DBHelper.COLUMN_ACTUAL_AMOUNT));
			actualDescription = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_ACTUAL_DESCRIPTION));
			actualCategory = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_ACTUAL_CATEGORY));
			actualNameOfSupplier = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_ACTUAL_SUPPLIER_NAME));
			actualAddress = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_ACTUAL_ADDRESS));

			arrivalBtn.setText(arrivalDateString);
			departureBtn.setText(departureDateString);
			descriptionEt.setText(description);
			amountEt.setText(String.valueOf(amount));
			categoryEt.setText(category);
			supplier_nameEt.setText(nameOfSupplier);
			addressEt.setText(address);

			actualDepartureBtn.setText(actualDepartureDateString);
			actualArrivalBtn.setText(actualArrivalDateString);
			actualDescriptionEt.setText(actualDescription);
			actualAmountEt.setText(String.valueOf(actualAmount));
			actualCategoryEt.setText(actualCategory);
			actualSupplier_nameEt.setText(actualNameOfSupplier);
			actualAddressEt.setText(actualAddress);

			// These lines of code converts a String format date into a
			// Timestamp type.
			SimpleDateFormat dateFormatTimestamp = new SimpleDateFormat("EEEE, MMM dd, yyyy");
			Date parsedArrivalDate = null;
			Date parsedDepartureDate = null;
			Date parsedActualArrivalDate = null;
			Date parsedActualDepartureDate = null;
			try {
				parsedArrivalDate = dateFormatTimestamp.parse(arrivalDateString);
				parsedDepartureDate = dateFormatTimestamp.parse(departureDateString);
				parsedActualArrivalDate = dateFormatTimestamp.parse(actualArrivalDateString);
				parsedActualDepartureDate = dateFormatTimestamp.parse(actualDepartureDateString);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			arrivalDate = new java.sql.Timestamp(parsedArrivalDate.getTime());
			departureDate = new java.sql.Timestamp(parsedDepartureDate.getTime());
			actualArrivalDate = new java.sql.Timestamp(parsedActualArrivalDate.getTime());
			actualDepartureDate = new java.sql.Timestamp(parsedActualDepartureDate.getTime());

		}

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

		// add a click listener to the button
		departureBtn.setOnClickListener(new View.OnClickListener() {
			@SuppressWarnings("deprecation")
			public void onClick(View v) {
				showDialog(DEPARTURE_DIALOG_ID);

			}
		});

		// add a click listener to the button
		arrivalBtn.setOnClickListener(new View.OnClickListener() {
			@SuppressWarnings("deprecation")
			public void onClick(View v) {
				showDialog(ARRIVAL_DIALOG_ID);
			}
		});

		// add a click listener to the button
		actualDepartureBtn.setOnClickListener(new View.OnClickListener() {
			@SuppressWarnings("deprecation")
			public void onClick(View v) {
				showDialog(ACTUAL_DEPARTURE_DIALOG_ID);

			}
		});

		// add a click listener to the button
		actualArrivalBtn.setOnClickListener(new View.OnClickListener() {
			@SuppressWarnings("deprecation")
			public void onClick(View v) {
				showDialog(ACTUAL_ARRIVAL_DIALOG_ID);
			}
		});

	}

	/**
	 * This method is fired when the save button is clicked. This method edit
	 * the budgeted expense and actual expense.
	 * 
	 * @param view
	 */
	public void editItinerary(View view) {
		if (validateBudgetedExpenseInput() && validateActualExpenseInput()) {
			// get the location id.
			String budgetedLocationName = locationSpinner.getSelectedItem().toString();
			int budgetedLocation_id = (int) dbh.getLocationId(budgetedLocationName);

			// create a budgeted expense row on the database. return the id of
			// the newly created itinerary(budgeted expense).
			dbh.updateItinerary(itinerary_id, budgetedLocation_id, arrivalDate, departureDate, amount, description,
					category, nameOfSupplier, address);
			dbh.updateActualExpense(itinerary_id, actualArrivalDate, actualDepartureDate, actualAmount,
					actualDescription, actualCategory, actualNameOfSupplier, actualAddress);
			finish();
		}
	}

	/**
	 * This method is called when the back button is clicked. This method this
	 * method display a dialog asking the user if they are sure they want to
	 * discard the changes.
	 */
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
			updateDisplay(departureBtn);
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
			updateDisplay(arrivalBtn);
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
			updateDisplay(actualDepartureBtn);
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
			updateDisplay(actualArrivalBtn);
			actualArrivalDate = new Timestamp(date.getTime());
			actualArrivalDateError.setVisibility(View.INVISIBLE);

		}
	};

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
	 *         the actual expense form. false - when the departure date is
	 *         before the arrival date.
	 */
	public boolean validateActualExpenseInput() {
		// getting input description
		actualDescription = actualDescriptionEt.getText().toString();
		// getting input amount
		String actualAmountString = actualAmountEt.getText().toString();
		// getting input category
		actualCategory = actualCategoryEt.getText().toString();
		// getting input name of supplier
		actualNameOfSupplier = actualSupplier_nameEt.getText().toString();
		// getting input name of supplier
		actualAddress = actualAddressEt.getText().toString();
		// if the user did not fill the actual arrival date, set it with the
		// budgeted arrival date.
		if (actualArrivalDate == null) {
			actualArrivalDate = arrivalDate;
		}
		// if the user did not fill the actual departure date, set it with the
		// budgeted departure date.
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
		// if the user did not fill the actual amount, set it with the budgeted
		// departure date.
		if (TextUtils.isEmpty(actualAmountString))
			actualAmount = 0.0;
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
		description = descriptionEt.getText().toString();
		// getting input amount
		String amountString = amountEt.getText().toString();
		// getting input category
		category = categoryEt.getText().toString();
		// getting input name of supplier
		nameOfSupplier = supplier_nameEt.getText().toString();
		// getting input name of supplier
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
			supplier_nameEt.setError("The name of the supplier cannot be empty.");
			valid = false;
		}
		if (TextUtils.isEmpty(address)) {
			addressEt.setError("The address cannot be empty.");
			valid = false;
		}
		return valid;
	}
}