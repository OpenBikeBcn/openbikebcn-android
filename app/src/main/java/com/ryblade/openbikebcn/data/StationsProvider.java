package com.ryblade.openbikebcn.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

/**
 * Created by alexmorral on 21/11/15.
 */
public class StationsProvider extends ContentProvider {

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private StationsDbHelper mOpenHelper;
    private static final int STATION = 100;
    private static final int FAVOURITES = 101;
    private static final int JOIN_STATION_FAVOURITES = 102;
    private static final int JOIN_STATION_FAVOURITES_CONDITION = 103;


    private static UriMatcher buildUriMatcher() {
        // I know what you're thinking.  Why create a UriMatcher when you can use regular
        // expressions instead?  Because you're not crazy, that's why.

        // All paths added to the UriMatcher have a corresponding code to return when a match is
        // found.  The code passed into the constructor represents the code to return for the root
        // URI.  It's common to use NO_MATCH as the code for this case.
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = DBContract.CONTENT_AUTHORITY;

        // For each type of URI you want to add, create a corresponding code.
        matcher.addURI(authority, DBContract.PATH_STATIONS, STATION);
        matcher.addURI(authority, DBContract.PATH_STATIONS+ "/*", STATION);

        matcher.addURI(authority, DBContract.PATH_FAVOURITES, FAVOURITES);
        matcher.addURI(authority, DBContract.PATH_FAVOURITES+ "/*", FAVOURITES);

        matcher.addURI(authority, DBContract.PATH_JOIN, JOIN_STATION_FAVOURITES);
        matcher.addURI(authority, DBContract.PATH_JOIN+ "/*", JOIN_STATION_FAVOURITES);

        matcher.addURI(authority, DBContract.PATH_JOIN_CONDITION, JOIN_STATION_FAVOURITES_CONDITION);
        matcher.addURI(authority, DBContract.PATH_JOIN_CONDITION+ "/*", JOIN_STATION_FAVOURITES_CONDITION);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new StationsDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {
            // "STATION"
            case STATION: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        DBContract.StationsEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case FAVOURITES: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        DBContract.FavouritesEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case JOIN_STATION_FAVOURITES: {
                retCursor = mOpenHelper.getReadableDatabase().rawQuery("SELECT s.id, s.type, s.latitude, s.longitude, s.streetName, s.streetNumber, s.altitude, s.slots, s.bikes, s.nearbyStations, s.status FROM stations s, favourites f WHERE s.id = f.id", null);
                break;
            }
            case JOIN_STATION_FAVOURITES_CONDITION: {
                retCursor = mOpenHelper.getReadableDatabase().rawQuery("SELECT s.id, s.type, s.latitude, s.longitude, s.streetName, s.streetNumber, s.altitude, s.slots, s.bikes, s.nearbyStations, s.status FROM stations s, favourites f WHERE s.id = f.id AND (s.bikes < 5 OR s.slots < 5)", null);
                break;
            }

        default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case STATION:
                return DBContract.StationsEntry.CONTENT_TYPE;
            case FAVOURITES:
                return DBContract.FavouritesEntry.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case STATION: {
                long _id = db.insert(DBContract.StationsEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = DBContract.StationsEntry.buildStationUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case FAVOURITES: {
                long _id = db.insert(DBContract.FavouritesEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = DBContract.FavouritesEntry.buildStationUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;
        switch (match) {
            case STATION:
                rowsDeleted = db.delete(
                        DBContract.StationsEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case FAVOURITES:
                rowsDeleted = db.delete(
                        DBContract.FavouritesEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        // Because a null deletes all rows
        if (selection == null || rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(
            Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();;
        int rowsUpdated;

        switch (match) {
            case STATION:
                rowsUpdated = db.update(DBContract.StationsEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            case FAVOURITES:
                rowsUpdated = db.update(DBContract.FavouritesEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();;
        int returnCount;
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case STATION:
                db.beginTransaction();
                returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(DBContract.StationsEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            case FAVOURITES:
                db.beginTransaction();
                returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(DBContract.FavouritesEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            default:
                return super.bulkInsert(uri, values);
        }
    }

}
