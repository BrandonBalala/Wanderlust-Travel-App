package com.wanderlust.travelproject;

import com.bob.travelproject.R;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class UnitConverterActivity extends Activity {
	private static final String TAG = "UnitConverterActivity";

	// inch <--> cm = 0.3937
	// cm <--> m = 100
	// m <--> km = 1000
	// km <--> mile = 1.6093
	private static final double[] DISTANCE_CONVERSION = { 0.3937, 100.0, 1000.0, 1.6093 };

	// ml <--> l = 1000
	// l <--> pint = 0.4732
	// pint <--> gallon = 8
	private static final double[] VOLUME_CONVERSION = { 1000, 0.4732, 8 };

	// kg <--> lbs = 0.4536
	private static final double[] WEIGHT_CONVERSION = { 0.4536 };
	private static final int CATEGORY_DISTANCE = 0;
	private static final int CATEGORY_VOLUME = 1;
	private static final int CATEGORY_WEIGHT = 2;
	private Spinner categorySpinner;
	private Spinner inititialUnitsSpinner;
	private Spinner conversionUnitSpinner;
	private EditText amountToConvertEditText;
	private TextView resultConversionTextView;
	private Toast toast;
	private int category;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_unit_converter);

		// Get all the necessary views
		categorySpinner = (Spinner) findViewById(R.id.categorySpinner);
		inititialUnitsSpinner = (Spinner) findViewById(R.id.initialUnitSpinner);
		conversionUnitSpinner = (Spinner) findViewById(R.id.conversionUnitSpinner);
		amountToConvertEditText = (EditText) findViewById(R.id.amountToConvertEditText);
		resultConversionTextView = (TextView) findViewById(R.id.resultConversionTextView);

		// Initialize the category spinner
		initializeSpinner(categorySpinner, R.array.conversion_categories);

		// Add on item selected listener
		categorySpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				Log.d(TAG + " - categorySpinner", " Position: " + position);
				// Change content of unit spinners
				category = position;
				changeSpinnerUnits(0, 0);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				Log.d(TAG, "Nothing Selected");
				// Nothing
			}

		});

		// Restore a previously stored instance state
		if (savedInstanceState != null) {
			category = savedInstanceState.getInt(getResources().getString(R.string.category));
			categorySpinner.setSelection(category);

			int initial = savedInstanceState.getInt(getResources().getString(R.string.initial_position));
			int convert = savedInstanceState.getInt(getResources().getString(R.string.conversion_position));
			Log.d(TAG, "initial saved: " + initial);
			Log.d(TAG, "conversion saved: " + convert);

			changeSpinnerUnits(initial, convert);
			inititialUnitsSpinner.setSelection(initial);
			conversionUnitSpinner.setSelection(convert);

			amountToConvertEditText
					.setText(savedInstanceState.getString(getResources().getString(R.string.convert_amount)));
			resultConversionTextView
					.setText(savedInstanceState.getString(getResources().getString(R.string.convert_result)));

		} else {
			category = CATEGORY_DISTANCE;
			changeSpinnerUnits(1, 1);
		}
	}

	/**
	 * Change the content of both unit spinners based on what category the user
	 * has chosen
	 * 
	 * @param convert
	 * @param initial
	 * 
	 * @param position
	 */
	private void changeSpinnerUnits(int initial, int convert) {
		switch (category) {
		case CATEGORY_DISTANCE:
			initializeSpinner(inititialUnitsSpinner, R.array.distance_units);
			initializeSpinner(conversionUnitSpinner, R.array.distance_units);
			break;
		case CATEGORY_VOLUME:
			initializeSpinner(inititialUnitsSpinner, R.array.volume_units);
			initializeSpinner(conversionUnitSpinner, R.array.volume_units);
			break;
		case CATEGORY_WEIGHT:
			initializeSpinner(inititialUnitsSpinner, R.array.weight_units);
			initializeSpinner(conversionUnitSpinner, R.array.weight_units);
			break;
		}

		//Log.d(TAG, "initial: " + initial);
		//Log.d(TAG, "conversion: " + convert);
		//inititialUnitsSpinner.setSelection(initial);
		//conversionUnitSpinner.setSelection(convert);
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.unit_converter, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * Clears the necessary views
	 * 
	 * @param view
	 */
	public void clearFields(View view) {
		Log.d(TAG, "Clearing fields");
		cancelToast();
		amountToConvertEditText.setText("");
		resultConversionTextView.setText("");
	}

	/**
	 * Method invoked as the user clicks on the convert button. Takes care of
	 * calculating the conversion that the user wants.
	 * 
	 * @param view
	 */
	public void convertUnit(View view) {
		Log.d(TAG, "Converting");
		cancelToast();
		toast = Toast.makeText(getApplicationContext(), null, Toast.LENGTH_SHORT);
		
		String amountStr = amountToConvertEditText.getText().toString().trim();
		double amount = -1;
		boolean isValid = false;
		
		if(!amountStr.isEmpty() && !amountStr.equals(".")){
			// Get amount to convert
			amount = Double.parseDouble(amountStr);
			
			// Check that amount is a positive number
			if (amount >= 0)
				isValid = true;
		}
		
		if(isValid) {
			// Get both initial position and the unit to convert to from
			// their respective spinners
			int initialUnitPosition = inititialUnitsSpinner.getSelectedItemPosition();
			int conversionUnitPosition = conversionUnitSpinner.getSelectedItemPosition();

			// Initialize necessary variables
			boolean toCalculate = true;
			boolean isMultiply = true;
			int start = -1;
			int end = -1;

			if (initialUnitPosition == conversionUnitPosition) {
				// Do nothing if both same position
				toCalculate = false;
			} // Set whether operation to use will be multiplication or
				// division, set starting counter and the limit/end
			else if (initialUnitPosition < conversionUnitPosition) {
				Log.d(TAG, "divide!");
				isMultiply = false;
				start = initialUnitPosition;
				end = conversionUnitPosition - 1;
			} else {
				Log.d(TAG, "multiply!");
				isMultiply = true;
				start = conversionUnitPosition;
				end = initialUnitPosition - 1;

			}

			// Initialize result as the amount to convert
			double result = amount;

			if (toCalculate) {
				// Filter down to which category was chosen
				switch (category) {
				case CATEGORY_DISTANCE:
					result = getConversion(isMultiply, result, start, end, DISTANCE_CONVERSION);
					break;
				case CATEGORY_VOLUME:
					result = getConversion(isMultiply, result, start, end, VOLUME_CONVERSION);
					break;
				case CATEGORY_WEIGHT:
					result = getConversion(isMultiply, result, start, end, WEIGHT_CONVERSION);
					break;
				}
			}

			// Show results of the conversion
			amountToConvertEditText.setText(Double.toString(amount));
			resultConversionTextView.setText(Double.toString(result));

		} else {
			Log.d(TAG, "INVALID AMOUNT");
			toast.setText(R.string.err_invalid_amount);
			toast.show();
		}
	}

	/**
	 * Gets the conversion based on the params
	 * 
	 * @param isMultiply
	 * @param result
	 * @param start
	 * @param end
	 * @param array_conversion
	 *            represents an array of multipliers/dividers from two adjacent
	 *            units
	 * @return result
	 */
	private double getConversion(boolean isMultiply, double result, int start, int end, double[] array_conversion) {
		Log.d(TAG, "isMultiply: " + isMultiply);
		Log.d(TAG, "start: " + start);
		Log.d(TAG, "end: " + end);

		for (int cntr = start; cntr <= end; cntr++) {
			if (isMultiply) {
				Log.d(TAG + " - MULTIPLY", Double.toString(array_conversion[cntr]));
				result *= array_conversion[cntr];
			} else {
				Log.d(TAG + " - DIVIDE", Double.toString(array_conversion[cntr]));
				result /= array_conversion[cntr];
			}
		}

		return result;
	}

	/**
	 * Swaps the chosen units from the initial unit spinner and the conversion
	 * unit spinner
	 * 
	 * @param view
	 */
	public void swapUnits(View view) {
		Log.d(TAG, "Swapping");

		int initialUnitPosition = inititialUnitsSpinner.getSelectedItemPosition();
		int conversionUnitPosition = conversionUnitSpinner.getSelectedItemPosition();

		inititialUnitsSpinner.setSelection(conversionUnitPosition);
		conversionUnitSpinner.setSelection(initialUnitPosition);
		resultConversionTextView.setText("");
	}

	/**
	 * Cancel any lingering toasts
	 */
	private void cancelToast() {
		if (toast != null)
			toast.cancel();
	}

	@Override
	protected void onSaveInstanceState(Bundle savedInstanceState) {
		super.onSaveInstanceState(savedInstanceState);

		savedInstanceState.putInt((getResources().getString(R.string.category)), category);
		savedInstanceState.putInt((getResources().getString(R.string.initial_position)),
				inititialUnitsSpinner.getSelectedItemPosition());

		Log.d(TAG, "saving initial: " + inititialUnitsSpinner.getSelectedItemPosition());
		savedInstanceState.putInt((getResources().getString(R.string.conversion_position)),
				conversionUnitSpinner.getSelectedItemPosition());

		Log.d(TAG, "saving conversion: " + conversionUnitSpinner.getSelectedItemPosition());
		savedInstanceState.putString((getResources().getString(R.string.convert_amount)),
				amountToConvertEditText.getText().toString());
		savedInstanceState.putString((getResources().getString(R.string.convert_result)),
				resultConversionTextView.getText().toString());
	}

}
