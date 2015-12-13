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

/**
 * The DBHelper is responsible for creating the database. This class is use to
 * create, read, update and delete values on the database in the Android
 * application.
 * 
 * @author Marjorie Morales, Rita Lazaar, Brandon Balala, Marvin Francisco
 *
 */
public class DBHelper extends SQLiteOpenHelper {

	public static SimpleDateFormat dateFormat = new SimpleDateFormat("EE, MMM dd, yyyy", Locale.getDefault());
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

	// Trips table field names COLUMN names
	public static final String COLUMN_TRIP_ID = "trip_id";
	public static final String COLUMN_CREATION = "createdate";
	public static final String COLUMN_UPDATE = "updatedate";
	public static final String COLUMN_CLOSE = "closedate";

	// Locations table field names COLUMN names
	public static final String COLUMN_CITY = "city";
	public static final String COLUMN_COUNTRYCODE = "countrycode";
	public static final String COLUMN_LOCATION_DESCRIPTION = "location_description";

	// ACTUALEXPENSE table field names COLUMN names
	public static final String COLUMN_BUDGETED_ID = "budgeted_id";
	public static final String COLUMN_ACTUAL_ARRIVALDATE = "actual_arrivaldate";
	public static final String COLUMN_ACTUAL_DEPARTUREDATE = "actual_departuredate";
	public static final String COLUMN_ACTUAL_AMOUNT = "actual_amount";
	public static final String COLUMN_ACTUAL_DESCRIPTION = "actual_description";
	public static final String COLUMN_ACTUAL_CATEGORY = "actual_category";
	public static final String COLUMN_ACTUAL_SUPPLIER_NAME = "actual_supplier_name";
	public static final String COLUMN_ACTUAL_ADDRESS = "actual_address";

	// ITINERARY table field names COLUMN names
	public static final String COLUMN_LOCATION_ID = "location_id";
	public static final String COLUMN_ARRIVALDATE = "arrivaldate";
	public static final String COLUMN_DEPARTUREDATE = "departuredate";
	public static final String COLUMN_AMOUNT = "amount";
	public static final String COLUMN_DESCRIPTION = "description";
	public static final String COLUMN_CATEGORY = "category";
	public static final String COLUMN_SUPPLIER_NAME = "supplier_name";
	public static final String COLUMN_ADDRESS = "address";

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
			+ " integer primary key autoincrement, " + COLUMN_NAME + " text not null, " + COLUMN_LOCATION_DESCRIPTION
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
			+ " integer primary key autoincrement, " + COLUMN_ACTUAL_ARRIVALDATE
			+ " TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " + COLUMN_ACTUAL_DEPARTUREDATE
			+ " TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " + COLUMN_ACTUAL_AMOUNT + " real, " + COLUMN_ACTUAL_DESCRIPTION
			+ " text not null," + COLUMN_ACTUAL_CATEGORY + " text not null," + COLUMN_ACTUAL_SUPPLIER_NAME
			+ " text not null," + COLUMN_ACTUAL_ADDRESS + " text not null," + COLUMN_BUDGETED_ID + " integer,"
			+ " FOREIGN KEY (" + COLUMN_BUDGETED_ID + ") REFERENCES " + TABLE_ITINERARY + "(" + COLUMN_ID
			+ ") ON DELETE CASCADE);";

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
	 * This method is creates all the tables needed : trips, locations, actual
	 * expense and itinerary and it adds test data for those tables.
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
		location1.put(COLUMN_LOCATION_DESCRIPTION, "Eating at Rita's Restaurant");
		location1.put(COLUMN_CITY, "MONTREAL");
		location1.put(COLUMN_COUNTRYCODE, "CAN");
		db.insert(TABLE_LOCATIONS, null, location1);

		ContentValues location2 = new ContentValues();
		location2.put(COLUMN_NAME, "Marvin's Restaurant");
		location2.put(COLUMN_LOCATION_DESCRIPTION, "Eating lunch at Marvin's Restaurant");
		location2.put(COLUMN_CITY, "MONTREAL");
		location2.put(COLUMN_COUNTRYCODE, "CAN");
		db.insert(TABLE_LOCATIONS, null, location2);

	}

	/**
	 * This is used to update the version of the database by dropping and
	 * recreating the needed tables.
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

	/***
	 * This method opens the database
	 */
	@Override
	public void onOpen(SQLiteDatabase database) {
		Log.i(TAG, "onOpen()");
	}

	// ------------------------ "trips" table methods ----------------//

	/*
	 * CREATE - This method creates a new trip row in the Trips table.
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
	 * 
	 * This method returns the trip related to that trip_id.
	 * 
	 * @param _id
	 * @return Cursor
	 */
	public Cursor getTrip(int _id) {

		return getReadableDatabase().query(TABLE_TRIPS, null, COLUMN_ID + " = ?", new String[] { String.valueOf(_id) },
				null, null, null);
	}

	/**
	 * This method gets all trips on the database.
	 * 
	 * @return Cursor
	 */
	public Cursor getAllTrips() {

		return getReadableDatabase().query(TABLE_TRIPS, null, null, null, null, null, null);
	}

	// ------------------------ "location" table methods ----------------//

	/*
	 * CREATE - This method creates a new location row in the Locations table.
	 */
	public void createNewLocation(String name, String description, String city, String countryCode) {

		ContentValues values = new ContentValues();
		values.put(COLUMN_NAME, name);
		values.put(COLUMN_LOCATION_DESCRIPTION, description);
		values.put(COLUMN_CITY, city);
		values.put(COLUMN_COUNTRYCODE, countryCode);
		// insert row
		getWritableDatabase().insert(TABLE_LOCATIONS, null, values);
	}

	/**
	 * This method gets all locations on the database.
	 * 
	 * @return Cursor
	 */
	public Cursor getAllLocations() {

		return getReadableDatabase().query(TABLE_LOCATIONS, null, null, null, null, null, null);

	}

	/**
	 * 
	 * This method is getting the location with that specified location_id.
	 * 
	 * @param location_id
	 * @return Cursor
	 */
	public Cursor getLocation(int location_id) {

		return getReadableDatabase().query(TABLE_LOCATIONS, null, COLUMN_ID + " = ?",
				new String[] { String.valueOf(location_id) }, null, null, null);

	}

	/**
	 * 
	 * This method is getting a location with the specified name.
	 * 
	 * @param name
	 * @return int - the id of the location
	 */
	public int getLocationId(String name) {

		Cursor cursor = getReadableDatabase().query(TABLE_LOCATIONS, new String[] { COLUMN_ID }, COLUMN_NAME + " = ?",
				new String[] { name }, null, null, null);
		if (cursor.moveToNext())
			return cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
		return 0;
	}

	/**
	 * 
	 * This method is getting the location's name of the specified location_id.
	 * 
	 * @param name
	 * @return string - the location's name
	 */
	public String getLocationName(int location_id) {

		Cursor cursor = getReadableDatabase().query(TABLE_LOCATIONS, new String[] { COLUMN_NAME }, COLUMN_ID + " = ?",
				new String[] { String.valueOf(location_id) }, null, null, null);

		if (cursor.moveToNext())
			return cursor.getString(cursor.getColumnIndex(COLUMN_NAME));
		return "";
	}

	// ------------------------ "BUDGETEDEXPENSE" table methods
	// ----------------//

	/*
	 * CREATE - This method creates a new itinerary(budgeted expense) row in the
	 * Itinerary table.
	 *
	 * @param location_id
	 * 
	 * @param trip_id
	 * 
	 * @param arrivalDate
	 * 
	 * @param departureDate
	 * 
	 * @param amount
	 * 
	 * @param description
	 * 
	 * @param category
	 * 
	 * @param supplier_name
	 * 
	 * @param address
	 * 
	 * @return long - the id of the newly created itinerary.
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
	 * 
	 * This method gets a budgeted expense(itinerary) row from the database
	 * using its id.
	 * 
	 * @return Cursor
	 */
	public Cursor getItinerary(int itinerary_id) {

		return getReadableDatabase().query(TABLE_ITINERARY, null, COLUMN_ID + " = ?",
				new String[] { String.valueOf(itinerary_id) }, null, null, null);

	}

	/**
	 * 
	 * This method returns all the itineraries of a specified trip id .
	 * 
	 * @param trip_id
	 * @return Cursor
	 */
	public Cursor getTripItineraries(int trip_id) {

		return getReadableDatabase().query(TABLE_ITINERARY, null, COLUMN_TRIP_ID + " = ?",
				new String[] { String.valueOf(trip_id) }, null, null, COLUMN_ARRIVALDATE);

	}

	/*
	 * UPDATE - This method is updating a specific itinerary based on the id
	 * sent and updates all the values being sent in the parameter list.
	 */
	public void updateItinerary(int itinerary_id, int location_id, Timestamp arrivalDate, Timestamp departureDate,
			double amount, String description, String category, String supplier_name, String address) {

		ContentValues values = new ContentValues();
		values.put(COLUMN_LOCATION_ID, location_id);
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

	/*
	 * DELETE - This method deletes a budgeted expense (itinerary) row in the
	 * Itinerary table.
	 */
	public void deleteItinerary(int itinerary) {
		getWritableDatabase().delete(TABLE_ITINERARY, COLUMN_ID + " = ?", new String[] { String.valueOf(itinerary) });
	}

	// ------------------------ "Itinerary" table methods
	// ----------------//

	/*
	 * CREATE - This method creates an actual expense row in the ActualExpenses
	 * table .
	 */
	public void createNewActualExpense(int budgeted_id, Timestamp arrivalDate, Timestamp departureDate, double amount,
			String description, String category, String supplierName, String address) {

		ContentValues values = new ContentValues();
		values.put(COLUMN_BUDGETED_ID, budgeted_id);
		values.put(COLUMN_ACTUAL_ARRIVALDATE, dateFormat.format(arrivalDate));
		values.put(COLUMN_ACTUAL_DEPARTUREDATE, dateFormat.format(departureDate));
		values.put(COLUMN_ACTUAL_AMOUNT, amount);
		values.put(COLUMN_ACTUAL_DESCRIPTION, description);
		values.put(COLUMN_ACTUAL_CATEGORY, category);
		values.put(COLUMN_ACTUAL_SUPPLIER_NAME, supplierName);
		values.put(COLUMN_ACTUAL_ADDRESS, address);

		// insert row
		getWritableDatabase().insert(TABLE_ACTUALEXPENSE, null, values);
	}

	/**
	 * This method gets an actual expense row from the database using its id.
	 * 
	 * @param actualExpense_id
	 * @return Cursor
	 */
	public Cursor getActualExpense(int actualExpense_id) {

		return getReadableDatabase().query(TABLE_ACTUALEXPENSE, null, COLUMN_ID + " = ?",
				new String[] { String.valueOf(actualExpense_id) }, null, null, null);

	}

	/**
	 * This method gets all actual expenses on the database.
	 * 
	 * @return Cursor
	 */
	public Cursor getAllActualExpense() {

		return getReadableDatabase().query(TABLE_ACTUALEXPENSE, null, null, null, null, null, null);

	}

	/*
	 * UPDATE - This method is updating the values of a specific actual expense
	 * row with the ones being sent in the parameter list.
	 */
	public void updateActualExpense(int itinerary_id, Timestamp arrivalDate, Timestamp departureDate, double amount,
			String description, String category, String supplierName, String address) {

		ContentValues values = new ContentValues();
		values.put(COLUMN_ACTUAL_ARRIVALDATE, dateFormat.format(arrivalDate));
		values.put(COLUMN_ACTUAL_DEPARTUREDATE, dateFormat.format(departureDate));
		values.put(COLUMN_ACTUAL_AMOUNT, amount);
		values.put(COLUMN_ACTUAL_DESCRIPTION, description);
		values.put(COLUMN_ACTUAL_CATEGORY, category);
		values.put(COLUMN_ACTUAL_SUPPLIER_NAME, supplierName);
		values.put(COLUMN_ACTUAL_ADDRESS, address);

		// updating row
		getWritableDatabase().update(TABLE_ACTUALEXPENSE, values, COLUMN_BUDGETED_ID + " = ?",
				new String[] { String.valueOf(itinerary_id) });
	}

	/**
	 * DELETE - This method deletes an actual expense row in the ActualExpenses
	 * table.
	 */
	public void deleteActualExpense(int actualExpense_id) {
		getWritableDatabase().delete(TABLE_ACTUALEXPENSE, COLUMN_ID + " = ?",
				new String[] { String.valueOf(actualExpense_id) });
	}

	/**
	 * This method gets all the budgeted expenses for the date specified.
	 * 
	 * @param selectedDate
	 * @return Cursor
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

		String searchSelect = "SELECT * FROM " + TABLE_ITINERARY + " WHERE itinerary.arrivaldate =  ? ";
		Log.v(TAG, "Query statement: " + searchSelect);
		return getReadableDatabase().rawQuery(searchSelect, new String[] { date });

	}

	/********************************************************************
	 * JOIN TABLE
	 ********************************************************************/

	/**
	 * This method gets all the joins a location , a budgeted expense and actual
	 * expense of a trip.
	 * 
	 * @param trip_id
	 * @return Cursor
	 */
	public Cursor getItineraryActualExpense(int budgeted_id) {

		String selectQuery = "SELECT * "
				+ "FROM itinerary INNER JOIN actualexpense ON itinerary._id=actualexpense.budgeted_id"
				+ " WHERE itinerary._id=?";

		return getReadableDatabase().rawQuery(selectQuery, new String[] { String.valueOf(budgeted_id) });
	}

	/**
	 * This method gets all the joins a location, a budgeted expense and actual
	 * expense of a trip.
	 * 
	 * @param trip_id
	 * @return Cursor
	 */
	public Cursor getItineraryActualExpenseLocation(int trip_id) {

		String selectQuery = "SELECT * " + "FROM locations INNER JOIN itinerary ON locations._id=itinerary.location_id "
				+ "INNER JOIN actualexpense ON itinerary._id=actualexpense.budgeted_id" + " WHERE trip_id=?";

		return getReadableDatabase().rawQuery(selectQuery, new String[] { String.valueOf(trip_id) });
	}
}