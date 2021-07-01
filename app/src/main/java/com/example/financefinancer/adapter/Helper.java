package com.example.financefinancer.adapter;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class Helper extends SQLiteOpenHelper {

    public static final String TAG = "Helper";

    public static final String TABLE_TRIP = "trip";
    public static final String COLUMN_TRIP_ID = "_id";
    public static final String COLUMN_TRIP_NAME = "trip_name";

    public static final String TABLE_MEMBER = "member";
    public static final String COLUMN_MEMBER_ID = "_id";
    public static final String COLUMN_MEMBER_NAME = "member_name";
    public static final String COLUMN_MEMBER_BALANCE = "member_balance";
    public static final String COLUMN_MEMBER_TRIP = "trip_id";

    public static final String TABLE_BILL = "bill";
    public static final String COLUMN_BILL_ID = "_id";
    public static final String COLUMN_BILL_NAME = "bill_name";
    public static final String COLUMN_BILL_AMOUNT = "bill_amount";
    public static final String COLUMN_BILL_SHARE = "bill_share";
    public static final String COLUMN_BILL_MEMBERS = "bill_members";
    public static final String COLUMN_BILL_TRIP = "trip_id";

    private static final String DATABASE_NAME = "bill.db";
    private static final int DATABASE_VERSION = 1;

    private static final String SQL_CREATE_TABLE_TRIP = "CREATE TABLE " + TABLE_TRIP + "("
            + COLUMN_TRIP_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_TRIP_NAME + " TEXT NOT NULL "
            + ");";

    private static final String SQL_CREATE_TABLE_MEMBER = "CREATE TABLE " + TABLE_MEMBER + "("
            + COLUMN_MEMBER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_MEMBER_NAME + " TEXT NOT NULL, "
            + COLUMN_MEMBER_BALANCE + " TEXT NOT NULL, "
            + COLUMN_MEMBER_TRIP + " INTEGER NOT NULL "
            + ");";

    private static final String SQL_CREATE_TABLE_BILL = "CREATE TABLE " + TABLE_BILL + "("
            + COLUMN_BILL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_BILL_NAME + " TEXT NOT NULL, "
            + COLUMN_BILL_AMOUNT + " TEXT NOT NULL, "
            + COLUMN_BILL_SHARE + " TEXT NOT NULL, "
            + COLUMN_BILL_MEMBERS + " TEXT NOT NULL, "
            + COLUMN_BILL_TRIP + " INTEGER NOT NULL "
            + ");";

    Helper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE_TRIP);
        db.execSQL(SQL_CREATE_TABLE_MEMBER);
        db.execSQL(SQL_CREATE_TABLE_BILL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(TAG, "Upgrading database from version " + oldVersion + " to " + newVersion);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRIP);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MEMBER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BILL);
        onCreate(db);
    }


}
