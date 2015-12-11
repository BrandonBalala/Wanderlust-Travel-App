package com.wanderlust.database;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {

	public static SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, MMM dd, yyyy", Locale.getDefault());
	public static Date date = new Date();

	// TAG for logcat debugging
	public static final String TAG = "DBInfo";
	// table names
	public static final String TABLE_TRIPS = "trips";
	public static final String TABLE_LOCATIONS = "locations";
	public static final String TABLE_ITINERARY = "itinerary";
	public static final String TABLE_ACTUALEXPENSE = "actualexpense";

	// Shared column names
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_DESCRIPTION = "description";

	// Trips table field names COLUMN names
	public static final String COLUMN_TRIP_ID = "trip_id";
	public static final String COLUMN_CREATION = "createdate";
	public static final String COLUMN_UPDATE = "updatedate";
	public static final String COLUMN_CLOSE = "closedate";

	// Locations table field names COLUMN names
	public static final String COLUMN_CITY = "city";
	public static final String COLUMN_COUNTRYCODE = "countrycode";

	// Shared column names between ITINERARY table & ACTUALEXPENSE table
	public static final String COLUMN_ARRIVALDATE = "arrivaldate";
	public static final String COLUMN_DEPARTUREDATE = "departuredate";
	public static final String COLUMN_AMOUNT = "amount";
	public static final String COLUMN_CATEGORY = "category";
	public static final String COLUMN_SUPPLIER_NAME = "supplier_name";
	public static final String COLUMN_ADDRESS = "address";

	// ACTUALEXPENSE table field names COLUMN names
	public static final String COLUMN_BUDGETED_ID = "budgeted_id";

	// ITINERARY table field names COLUMN names
	public static final String COLUMN_LOCATION_ID = "location_id";

	// file name
	private static final String DATABASE_NAME = "travel.db";
	// database version
	private static final int DATABASE_VERSION = 1;

	// static instance to share DBHelper
	private static DBHelper dbh = null;

	// Table Create Statements
	// TRIPS table create statement
	private static final String DATABASE_TRIPS_TABLE = "create table " + TABLE_TRIPS + "(" + COLUMN_ID
			+ " integer primary key autoincrement, " + COLUMN_TRIP_ID + " integer, " + COLUMN_CREATION
			+ " TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " + COLUMN_UPDATE + " timestamp , " + COLUMN_CLOSE + " timestamp, "
			+ COLUMN_NAME + " text not null, " + COLUMN_DESCRIPTION + " text not null);";

	// LOCATION table create statement
	private static final String DATABASE_LOCATIONS_TABLE = "create table " + TABLE_LOCATIONS + "(" + COLUMN_ID
			+ " integer primary key autoincrement, " + COLUMN_NAME + " text not null, " + COLUMN_DESCRIPTION
			+ " text not null," + COLUMN_CITY + " text not null, " + COLUMN_COUNTRYCODE + " text not null);";

	// ITINERARY table create statement
	private static final String DATABASE_ITINERARY_TABLE = "create table " + TABLE_ITINERARY + "(" + COLUMN_ID
			+ " integer primary key autoincrement, " + COLUMN_ARRIVALDATE + " TIMESTAMP, " + COLUMN_DEPARTUREDATE
			+ " TIMESTAMP, " + COLUMN_AMOUNT + " real, " + COLUMN_DESCRIPTION + " text not null," + COLUMN_CATEGORY
			+ " text not null, " + COLUMN_SUPPLIER_NAME + " text not null," + COLUMN_ADDRESS + " text not null, "
			+ COLUMN_LOCATION_ID + " integer," + COLUMN_TRIP_ID + " integer," + " FOREIGN KEY (" + COLUMN_LOCATION_ID
			+ ") REFERENCES " + TABLE_LOCATIONS + "(" + COLUMN_ID + ")," + " FOREIGN KEY (" + COLUMN_TRIP_ID
			+ ") REFERENCES " + TABLE_TRIPS + "(" + COLUMN_ID + "));";

	// ACTUALEXPENSE table create statement
	private static final String DATABASE_ACTUALEXPENSE_TABLE = "create table " + TABLE_ACTUALEXPENSE + "(" + COLUMN_ID
			+ " integer primary key autoincrement, " + COLUMN_ARRIVALDATE + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP, "
			+ COLUMN_DEPARTUREDATE + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " + COLUMN_AMOUNT + " real, "
			+ COLUMN_DESCRIPTION + " text not null," + COLUMN_CATEGORY + " text not null," + COLUMN_SUPPLIER_NAME
			+ " text not null," + COLUMN_ADDRESS + " text not null," + COLUMN_BUDGETED_ID + " integer,"
			+ " FOREIGN KEY (" + COLUMN_BUDGETED_ID + ") REFERENCES " + TABLE_ITINERARY + "(" + COLUMN_ID + "));";

	/**
	 * Constructor
	 * 
	 * super.SQLiteOpenHelper: public SQLiteOpenHelper (Context context, String
	 * name, SQLiteDatabase.CursorFactory factory, int version)
	 * 
	 * "Create a helper object to create, open, and/or manage a database."
	 * Remember the database is not actually created or opened until one of
	 * getWritableDatabase() or getReadableDatabase() is called.
	 * 
	 * constructor is private to prevent direct instantiation only getDBHelper()
	 * can be called and it will create or return existing
	 * 
	 * @param context
	 */
	public DBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);

	}

	/**
	 * getDBHelper(Context)
	 * 
	 * The static factory method getDBHelper make's sure that only one database
	 * helper exists across the app's lifecycle
	 * 
	 * A factory is an object for creating other objects. It is an abstraction
	 * of a constructor.
	 *
	 * @param context
	 * @return
	 */
	public static DBHelper getDBHelper(Context context) {
		if (dbh == null) {
			dbh = new DBHelper(context.getApplicationContext());
			Log.i(TAG, "getDBHelper, dbh == null");
		}
		return dbh;
	}

	/**
	 * SQLiteOpenHelper lifecycle method. This method creates an empty database
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		// creating required tables
		db.execSQL(DATABASE_TRIPS_TABLE);
		db.execSQL(DATABASE_LOCATIONS_TABLE);
		db.execSQL(DATABASE_ACTUALEXPENSE_TABLE);
		db.execSQL(DATABASE_ITINERARY_TABLE);

		Log.i(TAG, "onCreate()");

		ContentValues trip = new ContentValues();
		trip.put(COLUMN_CREATION, dateFormat.format(date));
		trip.put(COLUMN_TRIP_ID, 0);
		trip.put(COLUMN_NAME, "Test 1");
		trip.put(COLUMN_DESCRIPTION, "This is a test on the data");
		db.insert(TABLE_TRIPS, null, trip);

		ContentValues trip2 = new ContentValues();
		trip2.put(COLUMN_CREATION, dateFormat.format(date));
		trip2.put(COLUMN_TRIP_ID, 0);
		trip2.put(COLUMN_NAME, "Test 2 ");
		trip2.put(COLUMN_DESCRIPTION, "This is a second test on the data");
		db.insert(TABLE_TRIPS, null, trip2);

		ContentValues location1 = new ContentValues();
		location1.put(COLUMN_NAME, "Rita's Restaurant");
		location1.put(COLUMN_DESCRIPTION, "Eating at Rita's Restaurant");
		location1.put(COLUMN_CITY, "MONTREAL");
		location1.put(COLUMN_COUNTRYCODE, "CAN");
		db.insert(TABLE_LOCATIONS, null, location1);

		ContentValues location2 = new ContentValues();
		location2.put(COLUMN_NAME, "Marvin's Restaurant");
		location2.put(COLUMN_DESCRIPTION, "Eating lunch at Marvin's Restaurant");
		location2.put(COLUMN_CITY, "MONTREAL");
		location2.put(COLUMN_COUNTRYCODE, "CAN");
		db.insert(TABLE_LOCATIONS, null, location2);
		
	}

	/**
	 * This method upgrades the version of the database
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(TAG, DBHelper.class.getName() + "Upgrading database from version " + oldVersion + " to " + newVersion
				+ ", which will destroy all old data");
		// on upgrade drop older tables
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRIPS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOCATIONS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITINERARY);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACTUALEXPENSE);
		onCreate(db);
		Log.i(TAG, "onUpgrade()");
	}

	/**
	 * This method opens the database
	 */
	@Override
	public void onOpen(SQLiteDatabase database) {
		Log.i(TAG, "onOpen()");
	}

	// ------------------------ "trips" table methods ----------------//

	/**
	 * CREATE Creating a trips
	 */
	public void createNewTrip(int trip_id, String name, String description) {

		ContentValues values = new ContentValues();

		values.put(COLUMN_TRIP_ID, trip_id);
		values.put(COLUMN_CREATION, dateFormat.format(date));
		values.put(COLUMN_UPDATE, dateFormat.format(date));
		values.put(COLUMN_CLOSE, dateFormat.format(date));
		values.put(COLUMN_NAME, name);
		values.put(COLUMN_DESCRIPTION, description);

		getWritableDatabase().insert(TABLE_TRIPS, null, values);

	}

	/**
	 * getting a trip
	 */
	public Cursor getTrip(int trip_id) {

		return getReadableDatabase().query(TABLE_TRIPS, null, COLUMN_TRIP_ID + " = ?",
				new String[] { String.valueOf(trip_id) }, null, null, null);
	}

	/**
	 * getting all trips
	 */
	public Cursor getAllTrips() {

		return getReadableDatabase().query(TABLE_TRIPS, null, null, null, null, null, null);
	}

	/**
	 * UPDATE Updating a trip TODO
	 */
	public void updateTrip(int id, String name, String description) {

		ContentValues values = new ContentValues();
		values.put(COLUMN_NAME, name);
		values.put(COLUMN_DESCRIPTION, description);
		values.put(COLUMN_UPDATE, dateFormat.format(date));

		// updating row
		getWritableDatabase().update(TABLE_TRIPS, values, COLUMN_ID + " = ?", new String[] { String.valueOf(id) });
	}
	

	// ------------------------ "location" table methods ----------------//

	/**
	 * CREATE Creating a location
	 */
	public void createNewLocation(String name, String description, String city, String countryCode) {

		ContentValues values = new ContentValues();
		values.put(COLUMN_NAME, name);
		values.put(COLUMN_DESCRIPTION, description);
		values.put(COLUMN_CITY, city);
		values.put(COLUMN_COUNTRYCODE, countryCode);
		// insert row
		getWritableDatabase().insert(TABLE_LOCATIONS, null, values);
	}

	/**
	 * getting all locations
	 */
	public Cursor getAllLocations() {

		return getReadableDatabase().query(TABLE_LOCATIONS, null, null, null, null, null, null);

	}

	/**
	 * getting a location
	 */
	public Cursor getLocations(int location_id) {

		return getReadableDatabase().query(TABLE_LOCATIONS, null, COLUMN_ID + " = ?",
				new String[] { String.valueOf(location_id) }, null, null, null);

	}

	/**
	 * getting a location
	 */
	public Cursor getLocation(String name) {

		return getReadableDatabase().query(TABLE_LOCATIONS, null, COLUMN_NAME + " = ?",
				new String[] { name }, null, null, null);

	}

	

	/**
	 * CREATE Creating an itinerary
	 */
	public long createNewItinerary(int location_id, int trip_id, Timestamp arrivalDate, Timestamp departureDate,
			double amount, String description, String category, String supplier_name, String address) {

		ContentValues values = new ContentValues();
		values.put(COLUMN_LOCATION_ID, location_id);
		values.put(COLUMN_TRIP_ID, trip_id);
		values.put(COLUMN_ARRIVALDATE, dateFormat.format(arrivalDate));
		values.put(COLUMN_DEPARTUREDATE, dateFormat.format(departureDate));
		values.put(COLUMN_AMOUNT, amount);
		values.put(COLUMN_DESCRIPTION, description);
		values.put(COLUMN_CATEGORY, category);
		values.put(COLUMN_SUPPLIER_NAME, supplier_name);
		values.put(COLUMN_ADDRESS, address);

		// insert row
		long code = getWritableDatabase().insert(TABLE_ITINERARY, null, values);

		return code;
	}

	/**
	 * getting an itinerary
	 */
	public Cursor getItinerary(int itinerary_id) {

		return getReadableDatabase().query(TABLE_ITINERARY, null, COLUMN_ID + " = ?",
				new String[] { String.valueOf(itinerary_id) }, null, null, null);

	}

	/**
	 * getting all itineraries of a trip
	 */
	public Cursor getTripItineraries(int trip_id) {

		return getReadableDatabase().query(TABLE_ITINERARY, null, COLUMN_TRIP_ID + " = ?",
				new String[] { String.valueOf(trip_id) }, null, null, COLUMN_ARRIVALDATE);

	}

	/**
	 * UPDATE Updating an itinerary
	 */
	public void updateItinerary(int itinerary_id, Timestamp arrivalDate, Timestamp departureDate, double amount,
			String description, String category, String supplier_name, String address) {

		ContentValues values = new ContentValues();
		values.put(COLUMN_ARRIVALDATE, dateFormat.format(arrivalDate));
		values.put(COLUMN_DEPARTUREDATE, dateFormat.format(departureDate));
		values.put(COLUMN_AMOUNT, amount);
		values.put(COLUMN_DESCRIPTION, description);
		values.put(COLUMN_CATEGORY, category);
		values.put(COLUMN_SUPPLIER_NAME, supplier_name);
		values.put(COLUMN_ADDRESS, address);

		// updating row
		getWritableDatabase().update(TABLE_ITINERARY, values, COLUMN_ID + " = ?",
				new String[] { String.valueOf(itinerary_id) });
	}

	/**
	 * DELETE Deleting an itinerary
	 */
	public void deleteItinerary(int itinerary) {
		getWritableDatabase().delete(TABLE_ITINERARY, COLUMN_ID + " = ?", new String[] { String.valueOf(itinerary) });
	}

	// ------------------------ "Itinerary" table methods
	// ----------------//

	/**
	 * CREATE Creating a actual expense
	 */
	public long createNewActualExpense(int budgeted_id, Timestamp arrivalDate, Timestamp departureDate, double amount,
			String description, String category, String supplierName, String address) {

		ContentValues values = new ContentValues();
		values.put(COLUMN_BUDGETED_ID, budgeted_id);
		values.put(COLUMN_ARRIVALDATE, dateFormat.format(arrivalDate));
		values.put(COLUMN_DEPARTUREDATE, dateFormat.format(departureDate));
		values.put(COLUMN_AMOUNT, amount);
		values.put(COLUMN_DESCRIPTION, description);
		values.put(COLUMN_CATEGORY, category);
		values.put(COLUMN_SUPPLIER_NAME, supplierName);
		values.put(COLUMN_ADDRESS, address);

		// insert row
		long code = getWritableDatabase().insert(TABLE_ACTUALEXPENSE, null, values);

		return code;
	}

	/**
	 * getting an actual expense
	 */
	public Cursor getActualExpense(int actualExpense_id) {

		return getReadableDatabase().query(TABLE_ACTUALEXPENSE, null, COLUMN_ID + " = ?",
				new String[] { String.valueOf(actualExpense_id) }, null, null, null);

	}

	/**
	 * 
	 * getting all actual expense
	 */
	public Cursor getAllActualExpense() {

		return getReadableDatabase().query(TABLE_ACTUALEXPENSE, null, null, null, null, null, null);

	}

	/**
	 * UPDATE Updating a actual expense
	 */
	public void updateActualExpense(int actualExpense_id, int budgeted_id, String arrivalDate, String departureDate,
			double amount, String description, String category, String supplierName, String address) {

		ContentValues values = new ContentValues();
		values.put(COLUMN_BUDGETED_ID, budgeted_id);
		values.put(COLUMN_NAME, arrivalDate);
		values.put(COLUMN_NAME, departureDate);
		values.put(COLUMN_AMOUNT, amount);
		values.put(COLUMN_DESCRIPTION, description);
		values.put(COLUMN_CATEGORY, category);
		values.put(COLUMN_CATEGORY, supplierName);
		values.put(COLUMN_CATEGORY, address);

		// updating row
		getWritableDatabase().update(TABLE_ACTUALEXPENSE, values, COLUMN_ID + " = ?",
				new String[] { String.valueOf(actualExpense_id) });
	}

	/*
	 * DELETE Deleting a actual expense
	 */
	public void deleteActualExpense(int actualExpense_id) {
		getWritableDatabase().delete(TABLE_ACTUALEXPENSE, COLUMN_ID + " = ?",
				new String[] { String.valueOf(actualExpense_id) });
	}

	/**
	 * 
	 * getting all the activities that are scheduled today.
	 */
	public Cursor getActivitiesToday(Timestamp selectedDate) {

		// Create an instance of SimpleDateFormat used for formatting
		// the string representation of date (month/day/year)
		Log.v(TAG, selectedDate + " entered date");

		// Using DateFormat format method we can create a string
		// representation of a date with the defined format.
		String date = dateFormat.format(selectedDate);

		Log.v(TAG, date + " entered date");

		// return getReadableDatabase().query(TABLE_ITINERARY, null,
		// COLUMN_DEPARTUREDATE + " = ? ", new String[] { date },
		// null, null, null);

		String searchSelect = "SELECT * FROM " + TABLE_ITINERARY 
				+ " WHERE itinerary.arrivaldate =  ? ";
		Log.v(TAG, "Query statement: " + searchSelect);
		return getReadableDatabase().rawQuery(searchSelect, new String[] { date });

	}

	/********************************************************************
	 * JOIN TABLE
	 ********************************************************************/
	public Cursor getItineraryLocation(int trip_id){
		
		String selectQuery = "SELECT * FROM locations INNER JOIN itinerary ON locations._id=itinerary.location_id WHERE trip_id=?";
		return getReadableDatabase().rawQuery(selectQuery, new String[] {String.valueOf(trip_id)});
	}
}