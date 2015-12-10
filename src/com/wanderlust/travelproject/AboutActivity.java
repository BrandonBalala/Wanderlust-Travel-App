package com.wanderlust.travelproject;

import com.bob.travelproject.R;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class AboutActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
	}

	/**
	 * 
	 * This is an options menu. This method pragmatically creates two items and
	 * added to the menu: about fires an explicit intent to invoke About class
	 * that display information of the application and dawson fires an implicit
	 * intent to that launches the Dawson Computer Science web page.
	 * 
	 * @param menu
	 *            - Menu object
	 * @return boolean true
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.about_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.settingsMenuItem) {
			Intent intent = new Intent(this, SettingsActivity.class);
			startActivity(intent);
			return true;
		} else if (id == R.id.teamMenuItem) {
			Intent intent = new Intent(Intent.ACTION_VIEW,
					Uri.parse("https://sites.google.com/site/teamtravelbob/the-team"));
			startActivity(intent);
			return true;
		} else if (id == R.id.compsciPageMenuItem) {
			Intent intent = new Intent(Intent.ACTION_VIEW,
					Uri.parse("http://www.dawsoncollege.qc.ca/computer-science-technology/"));
			startActivity(intent);
			return true;
		}else if (id == R.id.dawsonMenuItem) {
			Intent intent = new Intent(Intent.ACTION_VIEW,
					Uri.parse("http://www.dawsoncollege.qc.ca/"));
			startActivity(intent);
			return true;
		}else if (id == R.id.webMenuItem) {
			Intent intent = new Intent(Intent.ACTION_VIEW,
					Uri.parse("http://wanderlust-marjoriemorales.rhcloud.com/"));
			startActivity(intent);
			return true;
		}
		

		return super.onOptionsItemSelected(item);
	}
}
