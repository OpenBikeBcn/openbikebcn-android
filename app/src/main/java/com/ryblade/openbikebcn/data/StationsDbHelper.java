package com.ryblade.openbikebcn.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ryblade.openbikebcn.data.DBContract.StationsEntry;
/**
 * Created by alexmorral on 21/11/15.
 */
public class StationsDbHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 1;

    public static final String DATABASE_NAME = "stations.db";

    public StationsDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String SQL_CREATE_STATIONS_TABLE = "CREATE TABLE " + StationsEntry.TABLE_NAME + " (" +
                StationsEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                StationsEntry.COLUMN_ID + " INTEGER NOT NULL, " +
                StationsEntry.COLUMN_TYPE + " TEXT NOT NULL, " +
                StationsEntry.COLUMN_LATITUDE + " DOUBLE NOT NULL, " +
                StationsEntry.COLUMN_LONGITUDE + " DOUBLE NOT NULL, " +
                StationsEntry.COLUMN_STREETNAME + " TEXT NOT NULL, " +
                StationsEntry.COLUMN_STREETNUMBER + " TEXT NOT NULL, " +
                StationsEntry.COLUMN_ALTITUDE + " DOUBLE NOT NULL, " +
                StationsEntry.COLUMN_SLOTS + " INTEGER NOT NULL, " +
                StationsEntry.COLUMN_BIKES + " INTEGER NOT NULL, " +
                StationsEntry.COLUMN_NEARBYSTATIONS + " TEXT NOT NULL, " +
                StationsEntry.COLUMN_STATUS+ " TEXT NOT NULL);";

        final String SQL_CREATE_FAVOURITES_TABLE = "CREATE TABLE " + DBContract.FavouritesEntry.TABLE_NAME + " (" +
                DBContract.FavouritesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                DBContract.FavouritesEntry.COLUMN_ID + " INTEGER NOT NULL, " +
                DBContract.FavouritesEntry.COLUMN_LATITUDE + " DOUBLE NOT NULL, " +
                DBContract.FavouritesEntry.COLUMN_LONGITUDE + " DOUBLE NOT NULL, " +
                DBContract.FavouritesEntry.COLUMN_NAME + " TEXT NOT NULL);";

        sqLiteDatabase.execSQL(SQL_CREATE_STATIONS_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_FAVOURITES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + StationsEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DBContract.FavouritesEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }



}
