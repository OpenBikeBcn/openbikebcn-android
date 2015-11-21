package com.ryblade.openbikebcn;

import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;

import com.ryblade.openbikebcn.Model.Station;
import com.ryblade.openbikebcn.data.DBContract.StationsEntry;
import com.ryblade.openbikebcn.data.StationsProvider;

/**
 * Created by alexmorral on 21/11/15.
 */
public class Utils {
    private static Utils ourInstance = new Utils();

    public static Utils getInstance() {
        return ourInstance;
    }

    private Utils() {
    }


    public ArrayList<Station> getAllStations(Context context) {
        ArrayList<Station> stations = new ArrayList<>();

        Cursor allItems = context.getContentResolver().query(StationsEntry.CONTENT_URI, null, null, null, null);

        Station station;
        if (allItems != null) {
            while(allItems.moveToNext()) {

                station = new Station();

                int id = allItems.getInt(allItems.getColumnIndex(StationsEntry.COLUMN_ID));
                String type = allItems.getString(allItems.getColumnIndex(StationsEntry.COLUMN_TYPE));
                Double latitude = allItems.getDouble(allItems.getColumnIndex(StationsEntry.COLUMN_LATITUDE));
                Double longitude = allItems.getDouble(allItems.getColumnIndex(StationsEntry.COLUMN_LONGITUDE));
                String streetName = allItems.getString(allItems.getColumnIndex(StationsEntry.COLUMN_STREETNAME));
                String streetNum = allItems.getString(allItems.getColumnIndex(StationsEntry.COLUMN_STREETNUMBER));
                Double altitude = allItems.getDouble(allItems.getColumnIndex(StationsEntry.COLUMN_ALTITUDE));
                int slots = allItems.getInt(allItems.getColumnIndex(StationsEntry.COLUMN_SLOTS));
                int bikes = allItems.getInt(allItems.getColumnIndex(StationsEntry.COLUMN_BIKES));
                String nearbyStations = allItems.getString(allItems.getColumnIndex(StationsEntry.COLUMN_NEARBYSTATIONS));
                String status = allItems.getString(allItems.getColumnIndex(StationsEntry.COLUMN_STATUS));


                station.setId(id);
                station.setType(type);
                station.setLatitude(latitude);
                station.setLongitude(longitude);
                station.setStreetName(streetName);
                station.setStreetNumber(streetNum);
                station.setAltitude(altitude);
                station.setSlots(slots);
                station.setBikes(bikes);
                station.setNearbyStations(nearbyStations);
                station.setStatus(status);


                stations.add(station);
            }
            allItems.close();
        }

        return stations;
    }

}
