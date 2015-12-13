package com.wanderlust.travelproject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * This class is used to make the connection with the OpenWeather API and
 * retrieve the necessary data from it to create the weather activity.
 * 
 * @author Rita Lazaar, Brandon Balala, Marjorie Morales, Marvin Francisco
 *
 */
public class WeatherConnection {
	private static String WEATHER_URL = "http://api.openweathermap.org/data/2.5/weather?q=";
	private static String API_KEY = "&APPID=0e22b726187895f15784bdd14fbe83ec";

	/**
	 * This method is used to get the weather data related to the location,
	 * which is a city and returns it as a string.
	 * 
	 * @param location
	 * @return
	 */
	public String getWeatherData(String location) {
		HttpURLConnection con = null;
		InputStream is = null;

		try {
			con = (HttpURLConnection) (new URL(WEATHER_URL + location + API_KEY)).openConnection();
			con.setRequestMethod("GET");
			con.setDoInput(true);
			con.setDoOutput(true);
			con.connect();

			// Let's read the response
			StringBuffer buffer = new StringBuffer();
			is = con.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			String line = null;
			while ((line = br.readLine()) != null)
				buffer.append(line + "\r\n");

			is.close();
			con.disconnect();
			return buffer.toString();

		} catch (Throwable t) {
			t.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (Throwable t) {
			}
			try {
				con.disconnect();
			} catch (Throwable t) {
			}
		}

		return null;

	}

}
