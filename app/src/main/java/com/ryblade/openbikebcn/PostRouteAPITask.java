package com.ryblade.openbikebcn;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.ryblade.openbikebcn.Model.Route;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by alexmorral on 18/12/15.
 */


public class PostRouteAPITask extends AsyncTask<Void,Void,Void> {

    private final String LOG_TAG = FetchAPITask.class.getSimpleName();
    private final Context mContext;
    private JSONObject parametersPost;

    public static String POST = "POST";

    public PostRouteAPITask(Context mContext, Route route) {
        this.mContext = mContext;
        parametersPost = new JSONObject();
        try {
            parametersPost.put("idUser", 2);
            parametersPost.put("idStationOrigin", route.getIdStationOrigin());
            parametersPost.put("idStationArrival", route.getIdStationArrival());
            parametersPost.put("distance", route.getDistance());
            parametersPost.put("time", route.getTime());

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected Void doInBackground(Void... params) {
        // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.
        HttpURLConnection urlConnection = null;

        try {
            String API_URL = "http://openbike.byte.cat/app_dev.php/api/rutas";

            JSONObject parameters;

            parameters = parametersPost;

            URL url = new URL(API_URL);

            // Create the request to OpenWeatherMap, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoOutput(true);
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.connect();

            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(urlConnection.getOutputStream()));
            writer.write(parameters.toString());
            writer.flush();
            writer.close();

            Log.d("openbikebcn", String.valueOf(urlConnection.getResponseCode()));

        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
            // If the code didn't successfully get the weather data, there's no point in attemping
            // to parse it.
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
                Log.d("openbikebcn", "POST Success");
            }
        }
        return null;
    }
}