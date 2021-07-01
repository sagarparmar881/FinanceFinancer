package com.example.financefinancer.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.financefinancer.model.Trip;

import java.util.ArrayList;
import java.util.List;


public class TripAdapter {

    public static final String TAG = "TripAdapter";

    private SQLiteDatabase mDatabase;
    private Helper mDbHelper;
    private Context mContext;
    private String[] mAllColumns = {Helper.COLUMN_TRIP_ID,
            Helper.COLUMN_TRIP_NAME};

    public TripAdapter(Context context) {
        this.mContext = context;
        mDbHelper = new Helper(context);
        try {
            open();
        } catch (SQLException e) {
            Log.e(TAG, "SQLException on opening database " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void open() throws SQLException {
        mDatabase = mDbHelper.getWritableDatabase();
    }

    public void close() {
        mDbHelper.close();
    }

    public Trip createTrip(String name) {
        open();
        ContentValues values = new ContentValues();
        values.put(Helper.COLUMN_TRIP_NAME, name);
        long insertId = mDatabase.insert(Helper.TABLE_TRIP, null, values);
        Cursor cursor = mDatabase.query(Helper.TABLE_TRIP, mAllColumns,
                Helper.COLUMN_TRIP_ID + " = " + insertId, null, null,
                null, null);
        cursor.moveToFirst();
        Trip newTrip = cursorToTrip(cursor);
        cursor.close();
        return newTrip;
    }

    public void deleteTrip(Trip trip) {
        long id = trip.getId();
        mDatabase.delete(Helper.TABLE_TRIP, Helper.COLUMN_TRIP_ID + " = " + id, null);
    }

    public List<Trip> getAllTrip() {
        List<Trip> listTrip = new ArrayList<Trip>();
        Cursor cursor = mDatabase.query(Helper.TABLE_TRIP, mAllColumns, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Trip trip = cursorToTrip(cursor);
                listTrip.add(trip);
                cursor.moveToNext();
            }
            cursor.close();
        }
        return listTrip;
    }

    public Cursor getTrip() {
        Cursor cursor = mDatabase.query(Helper.TABLE_TRIP, mAllColumns, null, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        return cursor;
    }

    public Trip getTripById(long id) {
        Cursor cursor = mDatabase.query(Helper.TABLE_TRIP, mAllColumns, Helper.COLUMN_TRIP_ID + " = ?", new String[]{String.valueOf(id)}, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        Trip trip = cursorToTrip(cursor);
        return trip;
    }

    private Trip cursorToTrip(Cursor cursor) {
        Trip trip = new Trip();
        trip.setId(cursor.getLong(0));
        trip.setName(cursor.getString(1));
        return trip;
    }

}
