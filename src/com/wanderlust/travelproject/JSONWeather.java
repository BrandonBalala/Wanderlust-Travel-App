package com.wanderlust.travelproject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class is used to parse the data from the weather API which is in a JSON
 * format and convert it to an actual weather object.
 * 
 * @author Rita Lazaar, Brandon Balala, Marjorie Morales, Marvin Francisco
 *
 */
public class JSONWeather {

	/**
	 * This method is to parse the data received and set it as an appropriate
	 * weather object.
	 * 
	 * @param data
	 * @return
	 * @throws JSONException
	 *             if something is not present in the JSON data
	 */

	public static Weather getWeather(String data) throws JSONException {
		Weather weather = new Weather();

		// JSONObject witht the String sent
		JSONObject jObj = new JSONObject(data);

		// We start extracting the info
		Location loc = new Location();

		JSONObject coordObj = getObject("coord", jObj);
		loc.setLatitude(getFloat("lat", coordObj));
		loc.setLongitude(getFloat("lon", coordObj));

		JSONObject sysObj = getObject("sys", jObj);
		loc.setCountry(getString("country", sysObj));
		loc.setSunrise(getInt("sunrise", sysObj));
		loc.setSunset(getInt("sunset", sysObj));
		loc.setCity(getString("name", jObj));

		weather.location = loc;

		// We get weather info (This is an array)
		JSONArray jArr = jObj.getJSONArray("weather");

		// We use only the first value
		JSONObject JSONWeather = jArr.getJSONObject(0);

		weather.currentCondition.setWeatherId(getInt("id", JSONWeather));
		weather.currentCondition.setDescr(getString("description", JSONWeather));
		weather.currentCondition.setCondition(getString("main", JSONWeather));
		weather.currentCondition.setIcon(getString("icon", JSONWeather));

		JSONObject mainObj = getObject("main", jObj);

		weather.currentCondition.setHumidity(getInt("humidity", mainObj));
		weather.currentCondition.setPressure(getInt("pressure", mainObj));
		weather.temperature.setMaxTemp(getFloat("temp_max", mainObj));
		weather.temperature.setMinTemp(getFloat("temp_min", mainObj));
		weather.temperature.setTemp(getFloat("temp", mainObj));

		// Wind
		JSONObject wObj = getObject("wind", jObj);
		weather.wind.setSpeed(getFloat("speed", wObj));
		weather.wind.setDeg(getFloat("deg", wObj));

		// Clouds
		JSONObject cObj = getObject("clouds", jObj);
		weather.clouds.setPerc(getInt("all", cObj));

		return weather;
	}

	private static JSONObject getObject(String tagName, JSONObject jObj) throws JSONException {
		JSONObject subObj = jObj.getJSONObject(tagName);
		return subObj;
	}

	private static String getString(String tagName, JSONObject jObj) throws JSONException {
		return jObj.getString(tagName);
	}

	private static float getFloat(String tagName, JSONObject jObj) throws JSONException {
		return (float) jObj.getDouble(tagName);
	}

	private static int getInt(String tagName, JSONObject jObj) throws JSONException {
		return jObj.getInt(tagName);
	}

}
