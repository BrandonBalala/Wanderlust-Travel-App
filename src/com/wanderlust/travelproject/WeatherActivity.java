package com.wanderlust.travelproject;

import org.json.JSONException;

import com.bob.travelproject.R;
import com.bob.travelproject.R.layout;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * This is the weather activity. It displays all weather data related to a
 * specific city retrieved from the database.
 * 
 * @author Rita Lazaar, Brandon Balala, Marjorie Morales, Marvin Francisco
 *
 */
public class WeatherActivity extends Activity {

	private TextView cityText;
	private TextView condDescr;
	private TextView temp;
	private TextView press;
	private TextView windSpeed;
	private TextView windDeg;
	private TextView hum;
	private String city;

	/**
	 * This is being executed when the activity is created. It sets all the
	 * TextViews and calls the inner class which runs an Async Task to retrieve
	 * data and set it to the textViews.
	 * 
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_weather);

		SharedPreferences mSharedPreference = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
		city = (mSharedPreference.getString("city", "Montreal"));
		
		//String city = "Montreal,CA";

		cityText = (TextView) findViewById(R.id.cityText);
		condDescr = (TextView) findViewById(R.id.condDescr);
		temp = (TextView) findViewById(R.id.temp);
		hum = (TextView) findViewById(R.id.hum);
		press = (TextView) findViewById(R.id.press);
		windSpeed = (TextView) findViewById(R.id.windSpeed);
		windDeg = (TextView) findViewById(R.id.windDeg);

		JSONWeatherTask task = new JSONWeatherTask();
		task.execute(new String[] { city });
	}

	/**
	 * This is an AsyncTask which creates in the background the connection to
	 * the API and retrieves the data from it. It then send the data to a JSON
	 * parser class to retrieve the actual weather.
	 * 
	 * @author Rita Lazaar, Brandon Balala, Marjorie Morales, Marvin Francisco
	 *
	 */
	private class JSONWeatherTask extends AsyncTask<String, Void, Weather> {

		/**
		 * This is done in background. Connectiong to the OpenWeather API and
		 * getting the data as a string.
		 * 
		 */
		@Override
		protected Weather doInBackground(String... params) {
			Weather weather = new Weather();
			String data = ((new WeatherConnection()).getWeatherData(params[0]));

			try {
				// converting the string to a Weather object
				weather = JSONWeather.getWeather(data);

			} catch (JSONException e) {
				e.getMessage();
			}
			return weather;

		}

		/**
		 * When task is done being executed, the newly created weather object is
		 * being used to set the textViews values.
		 */
		@Override
		protected void onPostExecute(Weather weather) {
			super.onPostExecute(weather);

			cityText.setText(weather.location.getCity() + "," + weather.location.getCountry());
			condDescr
					.setText(weather.currentCondition.getCondition() + "(" + weather.currentCondition.getDescr() + ")");
			temp.setText("" + Math.round((weather.temperature.getTemp() - 273.15)) + "C");
			hum.setText("" + weather.currentCondition.getHumidity() + "%");
			press.setText("" + weather.currentCondition.getPressure() + " hPa");
			windSpeed.setText("" + weather.wind.getSpeed() + " mps");
			windDeg.setText("" + weather.wind.getDeg());

		}

	}
}
