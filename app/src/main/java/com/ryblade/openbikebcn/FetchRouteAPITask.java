package com.ryblade.openbikebcn;

import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.ryblade.openbikebcn.Model.Route;
import com.ryblade.openbikebcn.data.DBContract.StationsEntry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.util.GeoPoint;

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
public class FetchRouteAPITask extends AsyncTask <Void, Void, Void>{


    private final String LOG_TAG = FetchRouteAPITask.class.getSimpleName();
    private final Context mContext;

    public FetchRouteAPITask(Context context) {
        mContext = context;
    }


    private void getRouteDataFromJson(String routeJSONString) throws JSONException {


        Route route = new Route();


        JSONObject stationsObject = new JSONObject(routeJSONString);

        JSONObject tripObject = stationsObject.getJSONObject("trip");

        JSONArray legsJSONArray = tripObject.getJSONArray("legs");

        JSONObject routeObject = legsJSONArray.getJSONObject(0);

        String encodedRoute = routeObject.getString("shape");
        route.setEncodedRoute(encodedRoute);

        JSONArray locationsObject = tripObject.getJSONArray("locations");
        JSONObject locationStart = locationsObject.getJSONObject(0);
        JSONObject locationEnd = locationsObject.getJSONObject(1);

        double startLat = locationStart.getDouble("lat");
        double startLon = locationStart.getDouble("lon");

        double endLat = locationEnd.getDouble("lat");
        double endLon = locationEnd.getDouble("lon");
        route.setStartPoint(new GeoPoint(startLat, startLon));
        route.setEndPoint(new GeoPoint(endLat, endLon));


    }

    @Override
    protected Void doInBackground(Void... voids) {


        // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String routeJsonString = null;


        try {
            final String MAPZEN_API_URL = "valhalla.mapzen.com/route?api_key=valhalla-kn-nbBI";

            URL url = new URL(MAPZEN_API_URL);

            // Create the request to OpenWeatherMap, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
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
            routeJsonString = buffer.toString();
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
            getRouteDataFromJson(routeJsonString);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }
        // This will only happen if there was an error getting or parsing the forecast.
        return null;
    }


}

