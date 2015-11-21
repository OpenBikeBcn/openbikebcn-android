package com.ryblade.openbikebcn;

import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Vector;

/**
 * Created by alexmorral on 21/11/15.
 */
public class FetchAPITask extends AsyncTask <Void, Void, Void>{


    private final String LOG_TAG = FetchAPITask.class.getSimpleName();
    private final Context mContext;

    public FetchAPITask(Context context) {
        mContext = context;
    }


    private void getStationsDataFromJson(String stationsJSONString) throws JSONException {

        // These are the names of the JSON objects that need to be extracted.

//        "id": "1",
//        "type": "BIKE",
//                "latitude": "41.397952",
//                "longitude": "2.180042",
//                "streetName": "Gran Via Corts Catalanes",
//                "streetNumber": "760",
//                "altitude": "21",
//                "slots": "11",
//                "bikes": "12",
//                "nearbyStations": "24, 369, 387, 426",
//                "status": "OPN"


        final String ID_STATION = "id";
        final String TYPE_STATION = "type";
        final String LATITUDE_STATION = "latitude";
        final String LONGITUDE_STATION = "longitude";
        final String STREETNAME_STATION = "streetName";
        final String STREETNUMBER_STATION = "streetNumber";
        final String ALTITUDE_STATION = "altitude";
        final String SLOTS_STATION = "slots";
        final String BIKES_STATION = "bikes";
        final String NEARBY_STATIONS = "nearbyStations";
        final String STATUS_STATION = "status";

        JSONObject stationsObject = new JSONObject(stationsJSONString);

        JSONArray stationsJSONArray = stationsObject.getJSONArray("stations");
        Vector<ContentValues> cVVector = new Vector<ContentValues>(stationsJSONArray.length());

        for(int i = 0; i < stationsJSONArray.length(); i++) {
            // These are the values that will be collected.

            String idString, type, latitudeString, longitudeString, streetName, streetNumber, altitudeString, slotsString, bikesString, nearbyStations, status;

            // Get the JSON object representing the player
            JSONObject stationObject = stationsJSONArray.getJSONObject(i);

            idString = stationObject.getString(ID_STATION);
            int id = Integer.parseInt(idString);
            type = stationObject.getString(TYPE_STATION);
            latitudeString = stationObject.getString(LATITUDE_STATION);
            double latitude = Double.parseDouble(latitudeString);
            longitudeString = stationObject.getString(LONGITUDE_STATION);
            double longitude = Double.parseDouble(longitudeString);
            streetName = stationObject.getString(STREETNAME_STATION);
            streetNumber = stationObject.getString(STREETNUMBER_STATION);
            altitudeString = stationObject.getString(ALTITUDE_STATION);
            int altitude = Integer.parseInt(altitudeString);
            slotsString = stationObject.getString(SLOTS_STATION);
            int slots = Integer.parseInt(slotsString);
            bikesString = stationObject.getString(BIKES_STATION);
            int bikes = Integer.parseInt(bikesString);
            nearbyStations = stationObject.getString(NEARBY_STATIONS);
            status = stationObject.getString(STATUS_STATION);

            /* Database
            ContentValues rankValues = new ContentValues();
            rankValues.put(RanksEntry.COLUMN_POSITION, pos);
            rankValues.put(RanksEntry.COLUMN_NAME, name);
            rankValues.put(RanksEntry.COLUMN_COUNTRY, country);
            rankValues.put(RanksEntry.COLUMN_POINTS, points);
            rankValues.put(RanksEntry.COLUMN_EARNINGS, earnings);
            rankValues.put(RanksEntry.COLUMN_HEIGHT, height);
            rankValues.put(RanksEntry.COLUMN_WEIGHT, weight);
            rankValues.put(RanksEntry.COLUMN_BORN, born);
            rankValues.put(RanksEntry.COLUMN_HAND, hand);
            rankValues.put(RanksEntry.COLUMN_BIRTHPLACE, birthplace);

            cVVector.add(rankValues);
            */
        }
        /* Database
        if (cVVector.size() > 0) {
            ContentValues[] cvArray = new ContentValues[cVVector.size()];
            cVVector.toArray(cvArray);
            mContext.getContentResolver().delete(RanksEntry.CONTENT_URI, null, null);
            mContext.getContentResolver().bulkInsert(RanksEntry.CONTENT_URI, cvArray);

        }*/
    }

    @Override
    protected Void doInBackground(Void... voids) {


        // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String stationJsonString = null;


        try {
            final String BICING_API_URL = "http://wservice.viabicing.cat/v2/stations";

            URL url = new URL(BICING_API_URL);

            // Create the request to OpenWeatherMap, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }
            stationJsonString = buffer.toString();
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
            // If the code didn't successfully get the weather data, there's no point in attemping
            // to parse it.
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }

        try {
            getStationsDataFromJson(stationJsonString);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }
        // This will only happen if there was an error getting or parsing the forecast.
        return null;
    }


}

