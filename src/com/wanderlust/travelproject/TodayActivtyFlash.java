package com.wanderlust.travelproject;

import java.util.Calendar;

import com.bob.travelproject.R;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.DatePicker;

/**
 * This is a class that will allow user to pick a date for the TodayAcivityList
 * to query
 * 
 * @author Marvin Francisco
 *
 */
public class TodayActivtyFlash extends Activity implements OnClickListener {

	private final static String TAG = "TODAY-ACTIVITY";
	int mCurPosition = -1;
	boolean mShowTwoFragments;
	Calendar myCalendar = Calendar.getInstance();
	private int year;
	private int month;
	private int day;
	private Dialog dialog;

	/**
	 * This method will instantiate the activity today xml with only a list view
	 * on it.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// initialize the calendar date picker.
		Calendar cal = Calendar.getInstance();
		year = cal.get(Calendar.YEAR);
		month = cal.get(Calendar.MONTH);
		day = cal.get(Calendar.DATE);
		dialog = onCreateDialog();
		dialog.setTitle(R.string.selectdate);
		dialog.show();
	}

	/**
	 * This method will create a date picker dialog
	 * 
	 * @return
	 */
	private Dialog onCreateDialog() {
		// open datepicker dialog.
		// set date picker for current date
		// add pickerListener listner to date picker
		DatePickerDialog dpd = new DatePickerDialog(this, pickerListener, year, month, day);
		dpd.setButton(DatePickerDialog.BUTTON_NEGATIVE, "", this);
		dpd.setOnKeyListener(new Dialog.OnKeyListener() {
			@Override
			public boolean onKey(DialogInterface arg0, int keyCode, KeyEvent event) {
				// TODO Auto-generated method stub
				if (keyCode == KeyEvent.KEYCODE_BACK) {
					Intent i = new Intent();
					i.setClass(getApplication(), MainActivity.class);
					startActivity(i);
				}
				return true;
			}
		});
		return dpd;

	}

	/**
	 * This is an OnDateSetListener that will allow the date picker call the
	 * TodayActivityList class when the date in the date picker has been changed
	 */
	private DatePickerDialog.OnDateSetListener pickerListener = new DatePickerDialog.OnDateSetListener() {
		// when dialog box is closed, below method will be called.
		@Override
		public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {

			year = selectedYear;
			month = selectedMonth;
			day = selectedDay;

			Log.v(TAG, "Year" + year);
			Log.v(TAG, "Month" + month);
			Log.v(TAG, "Day" + day);
			Intent intent = new Intent();
			intent.setClass(getApplication(), TodayActivityList.class);
			intent.putExtra("year", year);
			intent.putExtra("month", month);
			intent.putExtra("day", day);
			// request code = 0, only one activity used
			startActivityForResult(intent, 0);
		}
	};

	/**
	 * Lifecycle method called when an activity you launched exits and will
	 * simply recall the date picker object
	 *
	 * @param request
	 *            int originally supplied to startActivityForResult()
	 * @param result
	 *            int returned by the child activity through its setResult().
	 * @param i
	 *            Intent can be used to return (extras) result data to caller
	 */
	protected void onActivityResult(int request, int result, Intent i) {
		switch (result) {
		case 0:
			dialog = onCreateDialog();
			dialog.show();
			break;
		}
	} // onActivityResult()

	/**
	 * Simple wrapper method for Log.d()
	 * 
	 * @param msg
	 *            string to be logged
	 */
	public static void logIt(String msg) {
		final String TAG = "INTDATA1";
		Log.d(TAG, msg);
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {

	}

}