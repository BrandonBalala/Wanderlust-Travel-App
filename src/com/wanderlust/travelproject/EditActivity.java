package com.wanderlust.travelproject;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.bob.travelproject.R;
import com.wanderlust.database.DBHelper;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class EditActivity extends Activity {
	private static DBHelper dbh;
	private int itinerary_id;
	private Cursor cursor;
	static final int DEPARTURE_DIALOG_ID = 0;
	static final int ARRIVAL_DIALOG_ID = 1;

	private int mYear;
	private int mMonth;
	private int mDay;
	
	private Timestamp arrivalDate;
	private Timestamp departureDate;
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

	private String arrival;
	private String amount;
	private String departure;
	private String description;
	private String category;
	private String supplier_name;
	private String address;
	 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit);

		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			itinerary_id = (Integer) bundle.getInt("itinerary_id");
		}

		arrivalBtn = (Button) findViewById(R.id.editArrivalDate);
		departureBtn = (Button) findViewById(R.id.editDepartureDate);
		descriptionEt = (EditText) findViewById(R.id.edit_itinerary_description);
		amountEt = (EditText) findViewById(R.id.edit_itinerary_amount);
		categoryEt = (EditText) findViewById(R.id.edit_itinerary_category);
		supplier_nameEt = (EditText) findViewById(R.id.edit_itinerary_name_of_supplier);
		addressEt = (EditText) findViewById(R.id.edit_itinerary_address);
	    departureDateError = (TextView) findViewById(R.id.departureErrorTv);
		arrivalDateError = (TextView) findViewById(R.id.arrivalErrorTv);


		dbh = DBHelper.getDBHelper(this);
		cursor = dbh.getItinerary(itinerary_id);
		while (cursor.moveToNext()) {
			arrival = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_ARRIVALDATE));
			departure = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_DEPARTUREDATE));	
			
			amount = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_AMOUNT));
			description = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_DESCRIPTION));
			category = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_CATEGORY));
			supplier_name = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_SUPPLIER_NAME));
			address = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_ADDRESS));

			arrivalBtn.setText(arrival);
			departureBtn.setText(departure);
			descriptionEt.setText(description);
			amountEt.setText(amount);
			categoryEt.setText(category);
			supplier_nameEt.setText(supplier_name);
			addressEt.setText(address);

				SimpleDateFormat dateFormatTimestamp = new SimpleDateFormat("EEEE, MMM dd, yyyy");
			    Date parsedArrivalDate = null;
				try {
					parsedArrivalDate = dateFormatTimestamp.parse(arrival);
				} catch (ParseException e) {
					e.printStackTrace();
				}
			    Date parsedDepartureDate = null;
				try {
					parsedDepartureDate = dateFormatTimestamp.parse(departure);
				} catch (ParseException e) {
					e.printStackTrace();
				}
			    arrivalDate = new java.sql.Timestamp(parsedArrivalDate.getTime());
			    departureDate = new java.sql.Timestamp(parsedDepartureDate.getTime());
		}

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
	}

	public void editItinerary(View view) {
		if (validateInput()) {

			Double newAmount = Double.parseDouble(amountEt.getText().toString());
			description = descriptionEt.getText().toString();
			category = categoryEt.getText().toString();
			supplier_name = supplier_nameEt.getText().toString();
			address = addressEt.getText().toString();
	
			dbh.updateItinerary(itinerary_id, arrivalDate, departureDate, newAmount, description, category, supplier_name, address);
			finish();
		}
	}
	public boolean validateInput() {		
		boolean valid = true;
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
		return valid;
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
}
