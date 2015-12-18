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
    private JSONObject parametersPost;
    private JSONObject parametersDelete;
    private String method;
    private String idStation;

    public static String POST = "POST";
    public static String DELETE = "DELETE";

    public PostFavouriteAPITask(Context mContext, int idUser, int idStation, String method) {
        this.mContext = mContext;
        this.idStation = String.valueOf(idStation);
        parametersPost = new JSONObject();
        parametersDelete = new JSONObject();
        try {
            parametersPost.put("idStation", idStation);
            parametersPost.put("idUser", idUser);
            parametersDelete.put("idUser", idUser);
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
            String API_URL = "http://openbike.byte.cat/app_dev.php/api/stations";

            JSONObject parameters;

            if(method.equals(DELETE)) {
                parameters = parametersDelete;
                API_URL += "/"+idStation;
            }
            else
                parameters = parametersPost;

            URL url = new URL(API_URL);

            // Create the request to OpenWeatherMap, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoOutput(true);
            if(method.equals(DELETE))
                urlConnection.setRequestMethod(DELETE);
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
                Log.d("openbikebcn",method);
            }
        }
        return null;
    }
}
