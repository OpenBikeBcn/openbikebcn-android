package com.ryblade.openbikebcn.data;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by alexmorral on 21/11/15.
 */
public class DBContract  {

    public static final String CONTENT_AUTHORITY = "com.ryblade.openbikebcn";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_STATIONS = "stations";


    /* Inner class that defines the table contents of the location table */
    public static final class StationsEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_STATIONS).build();

        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + PATH_STATIONS;
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + CONTENT_AUTHORITY + "/" + PATH_STATIONS;

        // Table name
        public static final String TABLE_NAME = "stations";




        public static final String COLUMN_ID = "id";
        public static final String COLUMN_TYPE = "type";
        public static final String COLUMN_LATITUDE = "latitude";
        public static final String COLUMN_LONGITUDE = "longitude";
        public static final String COLUMN_STREETNAME = "streetName";
        public static final String COLUMN_STREETNUMBER = "streetNumber";
        public static final String COLUMN_ALTITUDE = "altitude";
        public static final String COLUMN_SLOTS = "slots";
        public static final String COLUMN_BIKES = "bikes";
        public static final String COLUMN_NEARBYSTATIONS = "nearbyStations";
        public static final String COLUMN_STATUS = "status";

        public static Uri buildStationUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

}
