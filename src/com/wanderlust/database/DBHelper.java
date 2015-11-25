package com.wanderlust.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {
	// TAG for logcat debugging
	public static final String TAG = "DBHelper Info";
	
	// table names
	public static final String TABLE_TRIPS = "trips";
	public static final String TABLE_LOCATIONS = "locations";
	public static final String TABLE_BUDGETEDEXPENSE = "budgetedexpense";
	public static final String TABLE_ACTUALEXPENSE = "actualexpense";

	// Shared column names
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_DESCRIPTION = "description";

	// Trips table field names COLUMN names
	public static final String COLUMN_TRIP_ID = "trip_id";
	public static final String COLUMN_CREATION = "creation";
	public static final String COLUMN_UPDATE = "update";
	public static final String COLUMN_CLOSE = "close";

	// Locations table field names COLUMN names
	public static final String COLUMN_CITY = "city";
	public static final String COLUMN_COUNTRYCODE = "countrycode";

	// Shared column names between BUDGETEDEXPENSE table & ACTUALEXPENSE table
	public static final String COLUMN_LOCATION_ID = "location_id";
	public static final String COLUMN_ARRIVALDATE = "arrivaldate";
	public static final String COLUMN_DEPARTUREDATE = "departuredate";
	public static final String COLUMN_AMOUNT = "amount";
	public static final String COLUMN_CATEGORY = "category";
	public static final String COLUMN_SUPPLIER_NAME = "supplier_name";
	public static final String COLUMN_ADDRESS = "address";
	
	// BUDGETEDEXPENSE table field names COLUMN names
	public static final String COLUMN_ACTUAL_ID = "actual_id";

	// file name
	private static final String DATABASE_NAME = "travel.db";
	// database version
	private static final int DATABASE_VERSION = 1;

	// static instance to share DBHelper
	private static DBHelper dbh = null;

	// Table Create Statements
	// TRIPS table create statement
	private static final String DATABASE_TRIPS_TABLE = "create table " + TABLE_TRIPS + "(" 
														+ COLUMN_ID + " integer primary key autoincrement, " 
														+ COLUMN_TRIP_ID + " integer, " 
														+ COLUMN_CREATION + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " 
														+ COLUMN_UPDATE + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP, "
														+ COLUMN_CLOSE + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " 
														+ COLUMN_NAME + " text not null, "
														+ COLUMN_DESCRIPTION + " text not null);";

	// LOCATION table create statement
	private static final String DATABASE_LOCATIONS_TABLE = "create table " + TABLE_LOCATIONS + "(" 
															+ COLUMN_ID + " integer primary key autoincrement, " 
															+ COLUMN_NAME + " text not null, "
															+ COLUMN_DESCRIPTION + " text not null, " 
															+ COLUMN_CITY + " text not null, " 
															+ COLUMN_COUNTRYCODE + " text not null);";

	// BUDGETEDEXPENSE table create statement
	private static final String DATABASE_BUDGETEDEXPENSE_TABLE = "create table " + TABLE_BUDGETEDEXPENSE + "("
																+ COLUMN_ID + " integer primary key autoincrement, " 
																+ COLUMN_LOCATION_ID + " integer," + " FOREIGN KEY (" + COLUMN_LOCATION_ID 
																+ ") REFERENCES " + TABLE_LOCATIONS + " (" + COLUMN_ID + "), " 
																+ COLUMN_ACTUAL_ID + " integer," + " FOREIGN KEY (" + COLUMN_ACTUAL_ID 
																+ ") REFERENCES " + TABLE_ACTUALEXPENSE + " (" + COLUMN_ID + "), "
																+ COLUMN_ARRIVALDATE + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " 
																+ COLUMN_DEPARTUREDATE + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP, "
																+ COLUMN_AMOUNT + " real, " 
																+ COLUMN_DESCRIPTION + " text not null, " 
																+ COLUMN_CATEGORY + " text not null), "														
																+ COLUMN_SUPPLIER_NAME + " text not null, " 
																+ COLUMN_ADDRESS + " text not null);";

	// BUDGETEDEXPENSE table create statement
	private static final String DATABASE_ACTUALEXPENSE_TABLE = "create table " + TABLE_ACTUALEXPENSE + "(" 
																+ COLUMN_ID + " integer primary key autoincrement, " 
																+ COLUMN_ARRIVALDATE + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " 
																+ COLUMN_DEPARTUREDATE + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP, "
																+ COLUMN_AMOUNT + " real, " 
																+ COLUMN_DESCRIPTION + " text not null, " 
																+ COLUMN_CATEGORY + " text not null, "
																+ COLUMN_SUPPLIER_NAME + " text not null, " 
																+ COLUMN_ADDRESS + " text not null);";

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
		db.execSQL(DATABASE_BUDGETEDEXPENSE_TABLE);

		Log.i(TAG, "onCreate()");
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
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_BUDGETEDEXPENSE);
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
	 * CREATE Creating a trips
	 */
	public long createNewTrip(int trip_id, String dateCreated, String dateUpdated, String dateClose, String name,
			String description) {

		ContentValues values = new ContentValues();

		values.put(COLUMN_TRIP_ID, trip_id);
		values.put(COLUMN_CREATION, dateCreated);
		values.put(COLUMN_UPDATE, dateUpdated);
		values.put(COLUMN_CLOSE, dateClose);
		values.put(COLUMN_NAME, name);
		values.put(COLUMN_DESCRIPTION, description);

		// insert row
		long code = getWritableDatabase().insert(TABLE_TRIPS, null, values);

		return code;
	}

	/*
	 * getting all trips
	 */
	public Cursor getAllTrips() {

		Cursor cursor = getReadableDatabase().query(TABLE_TRIPS, null, null, null, null, null, null);
		return cursor;
	}

	/*
	 * UPDATE Updating a trip TODO
	 */
	public void updateTrip(int id, String name, String description) {

		ContentValues values = new ContentValues();
		values.put(COLUMN_NAME, name);
		values.put(COLUMN_DESCRIPTION, description);

		// updating row
		getWritableDatabase().update(TABLE_TRIPS, values, COLUMN_ID + " = ?", new String[] { String.valueOf(id) });
	}

	/*
	 * Deleting a trip
	 */
	public void deleteTrip(int trip_id) {
		getWritableDatabase().delete(TABLE_TRIPS, COLUMN_ID + " = ?", new String[] { String.valueOf(trip_id) });
	}

	// ------------------------ "location" table methods ----------------//

	/*
	 * CREATE Creating a location
	 */
	public long createNewLocation(String name, String description, String city, String countryCode) {

		ContentValues values = new ContentValues();
		values.put(COLUMN_NAME, name);
		values.put(COLUMN_DESCRIPTION, description);
		values.put(COLUMN_CITY, city);
		values.put(COLUMN_COUNTRYCODE, countryCode);
		// insert row
		long code = getWritableDatabase().insert(TABLE_LOCATIONS, null, values);

		return code;
	}

	/*
	 * getting all locations
	 */
	public Cursor getAllLocations() {

		return getReadableDatabase().query(TABLE_LOCATIONS, null, null, null, null, null, null);

	}

	/*
	 * UPDATE Updating a location
	 */
	public void updateLocation(int id, String name, String description, String city, String countryCode) {

		ContentValues values = new ContentValues();
		values.put(COLUMN_NAME, name);
		values.put(COLUMN_DESCRIPTION, description);
		values.put(COLUMN_CITY, city);
		values.put(COLUMN_COUNTRYCODE, countryCode);

		// updating row
		getWritableDatabase().update(TABLE_LOCATIONS, values, COLUMN_ID + " = ?", new String[] { String.valueOf(id) });
	}

	/*
	 * DELETE Deleting a location
	 */
	public void deleteLocation(int trip_id) {
		getWritableDatabase().delete(TABLE_LOCATIONS, COLUMN_ID + " = ?", new String[] { String.valueOf(trip_id) });
	}

	// ------------------------ "BUDGETEDEXPENSE" table methods
	// ----------------//

	/*
	 * CREATE Creating a budgeted expense
	 */
	public long createNewBudgetedExpense(int location_id, int actual_id, String arrivalDate, String departureDate, double amount,
			String description, String category, String supplier_name, String address) {

		ContentValues values = new ContentValues();
		values.put(COLUMN_TRIP_ID, location_id);
		values.put(COLUMN_ACTUAL_ID, actual_id);
		values.put(COLUMN_NAME, arrivalDate);
		values.put(COLUMN_NAME, departureDate);
		values.put(COLUMN_AMOUNT, amount);
		values.put(COLUMN_DESCRIPTION, description);
		values.put(COLUMN_CATEGORY, category);
		values.put(COLUMN_SUPPLIER_NAME, supplier_name);
		values.put(COLUMN_ADDRESS, address);

		// insert row
		long code = getWritableDatabase().insert(TABLE_BUDGETEDEXPENSE, null, values);

		return code;
	}

	/*
	 * getting all budgeted expense
	 */
	public Cursor getAllBudgetedExpense() {

		return getReadableDatabase().query(TABLE_BUDGETEDEXPENSE, null, null, null, null, null, null);

	}

	/*
	 * UPDATE Updating a budgeted expense
	 */
	public void updateBudgetedExpense(int budgetedExpense_id, int location_id, int actual_id, String arrivalDate, String departureDate,
			double amount, String description, String category, String supplier_name, String address) {

		ContentValues values = new ContentValues();
		values.put(COLUMN_TRIP_ID, location_id);
		values.put(COLUMN_ACTUAL_ID, actual_id);
		values.put(COLUMN_NAME, arrivalDate);
		values.put(COLUMN_NAME, departureDate);
		values.put(COLUMN_AMOUNT, amount);
		values.put(COLUMN_DESCRIPTION, description);
		values.put(COLUMN_CATEGORY, category);
		values.put(COLUMN_SUPPLIER_NAME, supplier_name);
		values.put(COLUMN_ADDRESS, address);

		// updating row
		getWritableDatabase().update(TABLE_BUDGETEDEXPENSE, values, COLUMN_ID + " = ?",
				new String[] { String.valueOf(budgetedExpense_id) });
	}

	/*
	 * DELETE Deleting a budgeted expense
	 */
	public void deleteBudgetedExpense(int budgetedExpense_id) {
		getWritableDatabase().delete(TABLE_BUDGETEDEXPENSE, COLUMN_ID + " = ?",
				new String[] { String.valueOf(budgetedExpense_id) });
	}

	// ------------------------ "BUDGETEDEXPENSE" table methods
	// ----------------//

	/*
	 * CREATE Creating a actual expense
	 */
	public long createNewActualExpense(String arrivalDate, String departureDate, double amount,
			String description, String category, String supplierName, String address) {

		ContentValues values = new ContentValues();
		values.put(COLUMN_NAME, arrivalDate);
		values.put(COLUMN_NAME, departureDate);
		values.put(COLUMN_AMOUNT, amount);
		values.put(COLUMN_DESCRIPTION, description);
		values.put(COLUMN_CATEGORY, category);
		values.put(COLUMN_CATEGORY, supplierName);
		values.put(COLUMN_CATEGORY, address);

		// insert row
		long code = getWritableDatabase().insert(TABLE_ACTUALEXPENSE, null, values);

		return code;
	}

	/*
	 * getting all actual expense
	 */
	public Cursor getAllActualExpense() {

		return getReadableDatabase().query(TABLE_ACTUALEXPENSE, null, null, null, null, null, null);

	}

	/*
	 * UPDATE Updating a actual expense
	 */
	public void updateActualExpense(int actualExpense_id, String arrivalDate, String departureDate,
			double amount, String description, String category, String supplierName, String address) {

		ContentValues values = new ContentValues();
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
}