package com.ryblade.openbikebcn;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import com.ryblade.openbikebcn.Fragments.FavoritesFragment;
import com.ryblade.openbikebcn.Model.Route;
import com.ryblade.openbikebcn.Model.Station;
import com.ryblade.openbikebcn.Model.User;
import com.ryblade.openbikebcn.data.DBContract;
import com.ryblade.openbikebcn.data.DBContract.StationsEntry;
import com.ryblade.openbikebcn.data.StationsProvider;

/**
 * Created by alexmorral on 21/11/15.
 */
public class Utils {
    private static Utils ourInstance = new Utils();

    public Boolean appInBackground = false;
    public User currentUser;



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

//    public void updateFavouriteStations(final Context context, final FavoritesFragment fragment) {
//        new FetchAPITask(context, FetchAPITask.FAVOURITES_API_URL).execute();
//    }

    public ArrayList<Station> getFavouriteStations(Context context) {
        ArrayList<Station> stations = new ArrayList<>();

        Cursor allItems = context.getContentResolver().query(StationsEntry.CONTENT_URI, null,
                StationsEntry.COLUMN_FAVORITE + " = 1", null, null);

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

    public void addToFavourites(Context context, Station station) {
        new PostFavouriteAPITask(context, 2, station.getId(), PostFavouriteAPITask.POST).execute();

//        ContentValues rankValues = new ContentValues();
//
//        rankValues.put(DBContract.FavouritesEntry.COLUMN_ID, station.getId());
//        rankValues.put(DBContract.FavouritesEntry.COLUMN_TYPE, station.getType());
//        rankValues.put(DBContract.FavouritesEntry.COLUMN_LATITUDE, station.getLatitude());
//        rankValues.put(DBContract.FavouritesEntry.COLUMN_LONGITUDE, station.getLongitude());
//        rankValues.put(DBContract.FavouritesEntry.COLUMN_STREETNAME, station.getStreetName());
//        rankValues.put(DBContract.FavouritesEntry.COLUMN_STREETNUMBER, station.getStreetNumber());
//        rankValues.put(DBContract.FavouritesEntry.COLUMN_ALTITUDE, station.getAltitude());
//        rankValues.put(DBContract.FavouritesEntry.COLUMN_SLOTS, station.getSlots());
//        rankValues.put(DBContract.FavouritesEntry.COLUMN_BIKES, station.getBikes());
//        rankValues.put(DBContract.FavouritesEntry.COLUMN_NEARBYSTATIONS, station.getNearbyStations());
//        rankValues.put(DBContract.FavouritesEntry.COLUMN_STATUS, station.getStatus());
//
//        context.getContentResolver().insert(DBContract.FavouritesEntry.CONTENT_URI,rankValues);
    }

    public void deleteFavourite(Context context, Station station) {
        new PostFavouriteAPITask(context, currentUser.getUserId(), station.getId(), PostFavouriteAPITask.DELETE).execute();
        ContentValues contentValues = new ContentValues();
        contentValues.put(StationsEntry.COLUMN_FAVORITE, 0);
        context.getContentResolver().update(StationsEntry.CONTENT_URI, contentValues,
                StationsEntry.COLUMN_ID + " = ?", new String[] {String.valueOf(station.getId())});
    }

    public ArrayList<Station> getNotificationStations(Context context) {
        Cursor allItems = context.getContentResolver().query(StationsEntry.CONTENT_URI, null,
                StationsEntry.COLUMN_FAVORITE + " = ? AND " + StationsEntry.COLUMN_BIKES + " < ?"  +
                        " = ? AND " + StationsEntry.COLUMN_SLOTS + " < ?", new String[] {"1","5","5"}, null);
        ArrayList<Station> stations = new ArrayList<>();
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


    public void postRoute(Context context, Route route) {
        new PostRouteAPITask(context, currentUser.getUserId(), route).execute();
    }
}
