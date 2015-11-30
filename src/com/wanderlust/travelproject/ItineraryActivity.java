package com.wanderlust.travelproject;

import com.bob.travelproject.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class ItineraryActivity extends Activity {
	private int trip_id = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_itinerary);
		
		Bundle bundle = getIntent().getExtras();
		if (bundle != null)
			trip_id = (Integer) bundle.getInt("trip_id");
			

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_itinerary, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.addItineraryBtn) {
			Intent add = new Intent(this, AddItineraryActivity.class);
			add.putExtra("trip_id", trip_id);
			startActivity(add);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
