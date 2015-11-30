package com.ryblade.openbikebcn;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Pau on 30/11/2015.
 */
public class PostFavouriteAPITask extends AsyncTask <Void,Void,Void> {

    private final String LOG_TAG = FetchAPITask.class.getSimpleName();
    private final Context mContext;
    private JSONObject parameters;
    private String method;

    public static String POST = "POST";
    public static String DELETE = "DELETE";

    public PostFavouriteAPITask(Context mContext, int idUser, int idStation, String method) {
        this.mContext = mContext;
        parameters = new JSONObject();
        try {
            parameters.put("idUser", idUser);
            parameters.put("idStation", idStation);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        this.method = method;
    }

    @Override
    protected Void doInBackground(Void... params) {
        // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.
        HttpURLConnection urlConnection = null;

        try {
            final String API_URL = "http://openbike.byte.cat/app_dev.php/api/stations";

            URL url = new URL(API_URL);

            // Create the request to OpenWeatherMap, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            if(method.equals(POST))
                urlConnection.setDoOutput(true);
            else
                urlConnection.setRequestMethod(method);
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.connect();

            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(urlConnection.getOutputStream()));
            writer.write(parameters.toString());
            writer.flush();
            writer.close();

        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
            // If the code didn't successfully get the weather data, there's no point in attemping
            // to parse it.
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return null;
    }
}
