package com.wanderlust.travelproject;

import java.text.DecimalFormat;

import com.bob.travelproject.R;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class TipCalculatorActivity extends Activity {
	private static final String TAG = "TipCalculatorActivity";
	private static final DecimalFormat df = new DecimalFormat("0.00");
	private static final double TIP10 = 0.10;
	private static final double TIP15 = 0.15;
	private static final double TIP20 = 0.20;

	private double tipPercent;
	private EditText amountEditText;
	private EditText divideEditText;
	private TextView tipTotalTextView;
	private TextView billTotalTextView;
	private TextView eachPersonTextView;
	private Toast toast;
	private double tipTotal;
	private double billTotal;
	private double splitAmount;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tip_calculator);

		// Initialize tip percent to first element in the radio button list
		tipPercent = TIP10;

		// Get all the necessary views
		amountEditText = (EditText) findViewById(R.id.amountEditText);
		divideEditText = (EditText) findViewById(R.id.divideEditText);
		tipTotalTextView = (TextView) findViewById(R.id.tipTotalTextView);
		billTotalTextView = (TextView) findViewById(R.id.billTotalTextView);
		eachPersonTextView = (TextView) findViewById(R.id.eachPersonTextView);

		// Restore a previously stored instance state
		if (savedInstanceState != null) {
			tipTotal = savedInstanceState.getDouble(getResources().getString(R.string.tip_total));
			billTotal = savedInstanceState.getDouble(getResources().getString(R.string.bill_total));
			splitAmount = savedInstanceState.getDouble(getResources().getString(R.string.split_amount));

			tipTotalTextView.setText(df.format(tipTotal));
			billTotalTextView.setText(df.format(billTotal));
			eachPersonTextView.setText(df.format(splitAmount));
		}
	}

	public void onRadioButtonClicked(View view) {
		cancelToast();
		// Is the button now checked?
		boolean checked = ((RadioButton) view).isChecked();

		// Check which radio button was clicked
		switch (view.getId()) {
		case R.id.tip10:
			if (checked)
				tipPercent = TIP10;
			break;
		case R.id.tip15:
			if (checked)
				tipPercent = TIP15;
			break;
		case R.id.tip20:
			if (checked)
				tipPercent = TIP20;
			break;
		}
	}

	public void calculateTip(View view) {
		cancelToast();
		toast = Toast.makeText(getApplicationContext(),null, Toast.LENGTH_SHORT);
		
		String amountStr = amountEditText.getText().toString().trim();
		double amount = -1;
		boolean isValid = false;
		
		if(!amountStr.isEmpty() && !amountStr.equals(".")){
			//Get bill amount and round it to 2 digits after decimal point
			amount = Math.round(Double.parseDouble(amountStr) * 100.0) / 100.0;
			
			//Check that amount is a positive number
			if (amount >= 0)
				isValid = true;
		
		}
		
		if(isValid) 
		{	
			//Calculate tip total and round it to 2 digits after decimal point
			tipTotal = Math.round((amount * tipPercent) * 100.0) / 100.0 ;
			
			//Calculate bill total
			billTotal = amount + tipTotal;
			
			//Get the number to split bill by in form of string
			String divideStr = divideEditText.getText().toString().trim();
			int numPeople = -1;
			
			//Checks that its a number using regex before parsing into double and check that it is not empty
			if (!divideStr.isEmpty()){
				//Get double equivalent of the number of people to split bill by and round down
				numPeople = Integer.parseInt(divideStr);
			}
			
			if(numPeople > 50){
				numPeople = 1;
				toast.setText(R.string.err_max_split);
				toast.show();
			}
			
			//Defaults to 1 person if it was an invalid input
			if(numPeople == -1 || numPeople == 0){
				numPeople = 1;
			}
			
			//Calculate bill total for one person and round it to 2 digits after decimal point
			splitAmount = Math.round((billTotal / numPeople) * 100.0) / 100.0 ;
			
			//Display results
			amountEditText.setText(df.format(amount));
			tipTotalTextView.setText(df.format(tipTotal));
			billTotalTextView.setText(df.format(billTotal));
			divideEditText.setText(Integer.toString(numPeople));
			eachPersonTextView.setText(df.format(splitAmount));
			
		}else {
			Log.d(TAG, "INVALID BILL AMOUNT");
			toast.setText(R.string.err_invalid_amount);
			toast.show();
		}
	}

	public void clearFields(View view) {
		cancelToast();
		amountEditText.setText("");
		divideEditText.setText("");
		tipTotalTextView.setText(R.string.defaultValue);
		billTotalTextView.setText(R.string.defaultValue);
		eachPersonTextView.setText(R.string.defaultValue);
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

		savedInstanceState.putDouble((getResources().getString(R.string.tip_total)), tipTotal);
		savedInstanceState.putDouble((getResources().getString(R.string.bill_total)), billTotal);
		savedInstanceState.putDouble((getResources().getString(R.string.split_amount)), splitAmount);
	}
}
