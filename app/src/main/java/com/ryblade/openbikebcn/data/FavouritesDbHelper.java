package com.ryblade.openbikebcn.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ryblade.openbikebcn.data.DBContract.FavouritesEntry;

/**
 * Created by alexmorral on 21/11/15.
 */
public class FavouritesDbHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 1;

    public static final String DATABASE_NAME = "favourites.db";

    public FavouritesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String SQL_CREATE_RANKS_TABLE = "CREATE TABLE " + FavouritesEntry.TABLE_NAME + " (" +
                FavouritesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                FavouritesEntry.COLUMN_ID + " INTEGER NOT NULL, " +
                FavouritesEntry.COLUMN_TYPE + " TEXT NOT NULL, " +
                FavouritesEntry.COLUMN_LATITUDE + " DOUBLE NOT NULL, " +
                FavouritesEntry.COLUMN_LONGITUDE + " DOUBLE NOT NULL, " +
                FavouritesEntry.COLUMN_STREETNAME + " TEXT NOT NULL, " +
                FavouritesEntry.COLUMN_STREETNUMBER + " TEXT NOT NULL, " +
                FavouritesEntry.COLUMN_ALTITUDE + " DOUBLE NOT NULL, " +
                FavouritesEntry.COLUMN_SLOTS + " INTEGER NOT NULL, " +
                FavouritesEntry.COLUMN_BIKES + " INTEGER NOT NULL, " +
                FavouritesEntry.COLUMN_NEARBYSTATIONS + " TEXT NOT NULL, " +
                FavouritesEntry.COLUMN_STATUS+ " TEXT NOT NULL);";
        sqLiteDatabase.execSQL(SQL_CREATE_RANKS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + FavouritesEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }



}
