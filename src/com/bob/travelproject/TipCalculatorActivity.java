package com.bob.travelproject;

import java.text.DecimalFormat;

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
	private static final String ERR_INVALID_AMOUNT  = "Error: Bill amount is invalid, must be positive number";
	private static final String NUM_PEOPLE_DEFAULT  = "Warning: Number of people to split the bill has been defaulted to 1";
	private static final String TIP_TOTAL = "tipTotal";
	private static final String BILL_TOTAL = "billTotal";
	private static final String SPLIT_AMOUNT = "splitAmount";
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

		//Initialize tip percent to first element in the radio button list
		tipPercent = TIP10;
		
		//Get all the necessary views
		amountEditText = (EditText) findViewById(R.id.amountEditText);
		divideEditText = (EditText) findViewById(R.id.divideEditText);
		tipTotalTextView = (TextView) findViewById(R.id.tipTotalTextView);
		billTotalTextView = (TextView) findViewById(R.id.billTotalTextView);
		eachPersonTextView = (TextView) findViewById(R.id.eachPersonTextView);
		
		//Restore a previously stored instance state
		if (savedInstanceState != null){
			tipTotal = savedInstanceState.getDouble(TIP_TOTAL);
			billTotal = savedInstanceState.getDouble(BILL_TOTAL);
			splitAmount = savedInstanceState.getDouble(SPLIT_AMOUNT);
			
			tipTotalTextView.setText(df.format(tipTotal));
			billTotalTextView.setText(df.format(billTotal));
			eachPersonTextView.setText(df.format(splitAmount));	
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.tip_calculator, menu);
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
		//DecimalFormat df = new DecimalFormat("0.00");
		
		try {
			//Get bill amount and round it to 2 digits after decimal point
			double amount = Math.round(Double.parseDouble(amountEditText.getText().toString().trim()) * 100.0) / 100.0;
			
			//Check that amount is a positive number
			if (amount >= 0) {
				//Calculate tip total and round it to 2 digits after decimal point
				tipTotal = Math.round((amount * tipPercent) * 100.0) / 100.0 ;
				
				//Calculate bill total
				billTotal = amount + tipTotal;
				
				//Get the number to split bill by in form of string
				String divideStr = divideEditText.getText().toString().trim();
				
				int numPeople = -1;
				//Checks that its a number using regex before parsing into double and check that it is not empty
				if (divideStr.matches("[0-9]*(\\.[0-9]*)?") && !divideStr.isEmpty()){
					//Get double equivalent of the number of people to split bill by and round down
					numPeople = (int) Math.floor(Double.parseDouble(divideStr));
				}
				
				//Defaults to 1 person if it was an invalid input
				if(numPeople == -1 || numPeople == 0){
					toast.setText(NUM_PEOPLE_DEFAULT);
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
			} else {
				throw new NumberFormatException();
			}
			
		} catch (NumberFormatException e) {
			Log.d(TAG, "INVALID BILL AMOUNT");
			toast.setText(ERR_INVALID_AMOUNT);
		}
		toast.show();
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
	 *  Cancel any lingering toasts
	 */
	private void cancelToast()
	{
		if(toast != null)
			toast.cancel();
	}
	
    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
    	super.onSaveInstanceState(savedInstanceState);

    	savedInstanceState.putDouble(TIP_TOTAL, tipTotal);
    	savedInstanceState.putDouble(BILL_TOTAL, billTotal);
    	savedInstanceState.putDouble(SPLIT_AMOUNT, splitAmount);
    }
}
