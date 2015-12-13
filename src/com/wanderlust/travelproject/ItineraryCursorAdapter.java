package com.wanderlust.travelproject;

import com.bob.travelproject.R;
import com.wanderlust.database.DBHelper;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * This is a Custom Cursor Adapter. The purpose of this method is to display all
 * the columns of join tables in the ListView. With the SimpleCursorAdapter, it
 * can display only columns of one of three joined tables
 * 
 * @author Marjorie Morales, Rita Lazaar, Brandon Balala, Marvin Francisco
 *
 */
public class ItineraryCursorAdapter extends CursorAdapter {
	private LayoutInflater mInflater;

	public ItineraryCursorAdapter(Context context, Cursor cursor, int flags) {
		super(context, cursor, flags);
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		// Location TextFields
		TextView locationNameTv = (TextView) view.findViewById(R.id.display_location_name);
		TextView locationDescriptionTv = (TextView) view.findViewById(R.id.display_location_description);
		TextView locationCityTv = (TextView) view.findViewById(R.id.display_location_city);
		TextView locationCodeTv = (TextView) view.findViewById(R.id.display_location_code);

		// itinerary TextView
		TextView arrivaldateTv = (TextView) view.findViewById(R.id.display_itinerary_arrival);
		TextView departuredateTv = (TextView) view.findViewById(R.id.display_itinerary_departure);
		TextView descriptionTv = (TextView) view.findViewById(R.id.display_itinerary_description);
		TextView categoryTv = (TextView) view.findViewById(R.id.display_itinerary_category);
		TextView amountTv = (TextView) view.findViewById(R.id.display_itinerary_amount);
		TextView nameOfSupplierTv = (TextView) view.findViewById(R.id.display_itinerary_name_of_supplier);
		TextView addressTv = (TextView) view.findViewById(R.id.display_itinerary_address);

		// actual expenses TextView
		TextView actualArrivaldateTv = (TextView) view.findViewById(R.id.display_actual_arrival);
		TextView actualDeparturedateTv = (TextView) view.findViewById(R.id.display_actual_departure);
		TextView actualDescriptionTv = (TextView) view.findViewById(R.id.display_actual_description);
		TextView actualCategoryTv = (TextView) view.findViewById(R.id.display_actual_category);
		TextView actualAmountTv = (TextView) view.findViewById(R.id.display_actual_amount);
		TextView actualNameOfSupplierTv = (TextView) view.findViewById(R.id.display_actual_name_of_supplier);
		TextView actualAddressTv = (TextView) view.findViewById(R.id.display_actual_address);

		String locationName = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_NAME));
		String locationDescription = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_LOCATION_DESCRIPTION));
		String locationCity = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_CITY));
		String locationCode = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_COUNTRYCODE));

		// itinerary variables
		String arrivaldate = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_ARRIVALDATE));
		String departuredate = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_DEPARTUREDATE));
		String description = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_DESCRIPTION));
		String category = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_CATEGORY));
		String amount = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_AMOUNT));
		String nameOfSupplier = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_SUPPLIER_NAME));
		String address = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_ADDRESS));

		// actual expense variables
		String actualArrivaldate = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_ACTUAL_ARRIVALDATE));
		String actualDeparturedate = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_ACTUAL_DEPARTUREDATE));
		String actualDescription = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_ACTUAL_DESCRIPTION));
		String actualCategory = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_ACTUAL_CATEGORY));
		String actualAmount = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_ACTUAL_AMOUNT));
		String actualNameOfSupplier = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_ACTUAL_SUPPLIER_NAME));
		String actualAddress = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_ACTUAL_ADDRESS));
		locationNameTv.setText(locationName);
		locationDescriptionTv.setText(locationDescription);
		locationCityTv.setText(locationCity);
		locationCodeTv.setText(locationCode);

		arrivaldateTv.setText(arrivaldate);
		departuredateTv.setText(departuredate);
		categoryTv.setText(category);
		descriptionTv.setText(description);
		amountTv.setText(amount);
		nameOfSupplierTv.setText(nameOfSupplier);
		addressTv.setText(address);

		actualArrivaldateTv.setText(actualArrivaldate);
		actualDeparturedateTv.setText(actualDeparturedate);
		actualDescriptionTv.setText(actualDescription);
		actualCategoryTv.setText(actualCategory);
		actualAmountTv.setText(actualAmount);
		actualNameOfSupplierTv.setText(actualNameOfSupplier);
		actualAddressTv.setText(actualAddress);
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		return mInflater.inflate(R.layout.list_itinaries, parent, false);
	}

}
