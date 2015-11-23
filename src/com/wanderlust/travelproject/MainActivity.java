package com.wanderlust.travelproject;


import com.bob.travelproject.R;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends Activity {

	private static final int SHOW_AS_ACTION_IF_ROOM = 1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	/**
	 * 
	 * This is an options menu. This method pragmatically creates two items and
	 * added to the menu: about fires an explicit intent to invoke About class
	 * that display information of the application and dawson fires an implicit 
	 * intent to that launches the Dawson Computer Science web page.
	 * 
	 * @param menu - Menu object
	 * @return boolean true
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		// Inflate the menu; this adds items to the action bar if it is present.
		Intent about = new Intent(this, AboutActivity.class);
		menu.add(R.string.title_activity_about).setIntent(about).setShowAsAction(SHOW_AS_ACTION_IF_ROOM);
		
		Intent dawson = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.dawsoncollege.qc.ca/computer-science-technology/"));
		menu.add(R.string.dawson).setIntent(dawson).setShowAsAction(SHOW_AS_ACTION_IF_ROOM);
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
	public void tipCalculator(View view) {

		Intent i = new Intent(this, TipCalculatorActivity.class);
		startActivity(i);

	}
	public void conversion(View view) {

		Intent i = new Intent(this, UnitConverterActivity.class);
		startActivity(i);

	}
}
