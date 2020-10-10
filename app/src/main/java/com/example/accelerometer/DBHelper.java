package com.example.accelerometer;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

   /* Set table and row names */
public class DBHelper extends SQLiteOpenHelper {

    public static final int DB_VERSION = 1;
    public static final String DB_NAME = "Sensor_Data.db";

    public static final String TABLE_ONE_NAME = "Accelerometer_Data_Road1";
    public static final String ROAD1_COL1 = "AX";
    public static final String ROAD1_COL2 = "AY";
    public static final String ROAD1_COL3 = "AZ";
    public static final String ROAD1_COL4 = "LAT";
    public static final String ROAD1_COL5 = "LON";
    public static final String ROAD1_COL6 = "SPEED";

    public static final String TABLE_TWO_NAME = "Accelerometer_Data_Road2";
    public static final String ROAD2_COL1 = "AX";
    public static final String ROAD2_COL2 = "AY";
    public static final String ROAD2_COL3 = "AZ";
    public static final String ROAD2_COL4 = "LAT";
    public static final String ROAD2_COL5 = "LON";
    public static final String ROAD2_COL6 = "SPEED";


    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    /* Create Database with following tables once app is opened and created */
    /* Data is stored internally on phone. location is data/data/com.example.accelerometer/databases */
    @Override
    public void onCreate(SQLiteDatabase db) {

        String createTable1 = "CREATE TABLE " + TABLE_ONE_NAME + " (AX REAL, AY REAL, AZ REAL, LAT REAL, LON REAL, SPEED REAL) ";
        String createTable2 = "CREATE TABLE " + TABLE_TWO_NAME + " (AX REAL, AY REAL, AZ REAL, LAT REAL, LON REAL, SPEED REAL) ";

        db.execSQL(createTable1);
        db.execSQL(createTable2);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ONE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TWO_NAME);

        onCreate(db);

    }

    public void addDataRoad1(float AX, float AY, float AZ, double LAT, double LON, float SPEED){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ROAD1_COL1, AX);
        contentValues.put(ROAD1_COL2, AY);
        contentValues.put(ROAD1_COL3, AZ);
        contentValues.put(ROAD1_COL4, LAT);
        contentValues.put(ROAD1_COL5, LON);
        contentValues.put(ROAD1_COL6, SPEED);

        db.insert(TABLE_ONE_NAME, null, contentValues);
    }

    public void addDataRoad2(float AX, float AY, float AZ, double LAT, double LON, float SPEED){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ROAD2_COL1, AX);
        contentValues.put(ROAD2_COL2, AY);
        contentValues.put(ROAD2_COL3, AZ);
        contentValues.put(ROAD2_COL4, LAT);
        contentValues.put(ROAD2_COL5, LON);
        contentValues.put(ROAD2_COL6, SPEED);

        db.insert(TABLE_TWO_NAME, null, contentValues);
    }


}
