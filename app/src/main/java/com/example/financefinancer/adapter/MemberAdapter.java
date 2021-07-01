package com.example.financefinancer.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.financefinancer.model.Member;
import com.example.financefinancer.model.Trip;


public class MemberAdapter {

    public static final String TAG = "MemberAdapter";

    private SQLiteDatabase mDatabase;
    private Helper mDbHelper;
    private Context mContext;
    private String[] mAllColumns = {Helper.COLUMN_MEMBER_ID,
            Helper.COLUMN_MEMBER_NAME,
            Helper.COLUMN_MEMBER_BALANCE,
            Helper.COLUMN_MEMBER_TRIP};

    public MemberAdapter(Context context) {
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

    public Member createMember(String name, int tripID) {
        ContentValues values = new ContentValues();
        values.put(Helper.COLUMN_MEMBER_NAME, name);
        values.put(Helper.COLUMN_MEMBER_BALANCE, 0);
        values.put(Helper.COLUMN_MEMBER_TRIP, tripID);
        long insertId = mDatabase.insert(Helper.TABLE_MEMBER, null, values);
        Cursor cursor = mDatabase.query(Helper.TABLE_MEMBER, mAllColumns,
                Helper.COLUMN_MEMBER_ID + " = " + insertId, null, null,
                null, null);
        cursor.moveToFirst();
        Member newMember = cursorToMember(cursor);
        cursor.close();
        return newMember;
    }

    public Cursor getMember(String tripID) {
        Cursor cursor = mDatabase.rawQuery("SELECT * FROM " + Helper.TABLE_MEMBER + " WHERE " + Helper.COLUMN_MEMBER_TRIP + " = " + tripID + ";", null);
        Log.d("", "SELECT * FROM " + Helper.TABLE_MEMBER + " WHERE " + Helper.COLUMN_MEMBER_TRIP + " = " + tripID + ";");
        if (cursor != null)
            cursor.moveToFirst();
        return cursor;
    }

    public Member cursorToMember(Cursor cursor) {
        Member member = new Member();
        member.setId(cursor.getLong(0));
        member.setName(cursor.getString(1));
        member.setBalance(cursor.getDouble(2));
        long tripID = cursor.getLong(3);
        Log.d("Cursor Member", "" + cursor.getDouble(2));
        TripAdapter adapter = new TripAdapter(mContext);
        Trip trip = adapter.getTripById(tripID);
        if (trip != null)
            member.setTripId(trip.getId());
        return member;
    }

    public boolean deleteMemberById(String memberID) {
        return mDatabase.delete(Helper.TABLE_MEMBER, Helper.COLUMN_MEMBER_ID + " = ?", new String[]{memberID}) > 0;
    }

    public double getBalance(String tripId, String name) {
        String query = "SELECT " + Helper.COLUMN_MEMBER_BALANCE + " FROM member WHERE " + Helper.COLUMN_MEMBER_NAME + " = '" + name + "' and " + Helper.COLUMN_MEMBER_TRIP + " = " + tripId + ";";
        Cursor cursor = mDatabase.rawQuery(query, null);
        cursor.moveToFirst();
        double amt = cursor.getDouble(0);
        return amt;
    }


    public void updateBalance(String tripId, String name, double amount) {
        ContentValues cv = new ContentValues();
        cv.put(Helper.COLUMN_MEMBER_BALANCE, amount);
        mDatabase.update(Helper.TABLE_MEMBER, cv, Helper.COLUMN_MEMBER_NAME + "='" + name + "' and " + Helper.COLUMN_MEMBER_TRIP + "=" + tripId, null);
    }

}
