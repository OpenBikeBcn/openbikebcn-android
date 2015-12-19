package com.ryblade.openbikebcn.Fragments;

import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.ryblade.openbikebcn.Model.Station;
import com.ryblade.openbikebcn.R;
import com.ryblade.openbikebcn.StationArrayAdapter;
import com.ryblade.openbikebcn.Utils;
import com.ryblade.openbikebcn.data.DBContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by alexmorral on 25/11/15.
 */
public class FavoritesFragment extends Fragment {

    private ListView stationsList;
    public static final int UPDATE_FAVORITES = 0;

    public Handler _handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == FavoritesFragment.UPDATE_FAVORITES)
                updateFavorites();
        }

    };

    public FavoritesFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.favorites_fragment, container, false);
        Log.d("openbikebcn","Favorites fragment created");
        init(rootView);

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    public void init(View v) {
        stationsList = ((ListView) v.findViewById(R.id.favouritesList));
        new FetchFavoritesAPITask(getActivity(), Utils.getInstance().currentUser.getUserId()).execute();
    }

    public void updateFavorites() {
        ArrayList<Station> stations = Utils.getInstance().getFavouriteStations(getActivity());
        StationArrayAdapter saa = new StationArrayAdapter(getContext(), stations, this);
        stationsList.setAdapter(saa);
    }

    private class FetchFavoritesAPITask extends AsyncTask<Void, Void, Void> {


        private final String LOG_TAG = FetchFavoritesAPITask.class.getSimpleName();
        private final Context mContext;

        public String FAVOURITES_API_URL = "http://openbike.byte.cat/app_dev.php/api/stations?filters[userId]=";

        public FetchFavoritesAPITask(Context context, int userId) {
            mContext = context;
            FAVOURITES_API_URL += userId;
        }

        private void getFavouritesDataFromJson(String stationsJSONString) throws JSONException {

            // These are the names of the JSON objects that need to be extracted.

//        "id": "1",
//        "id_bicing": "2",
//        "name": "1 - Gran Via Corts Catalanes - 760",
//        "latitude":41.393699,
//        "longitude":2.181137

            Log.v(LOG_TAG, "Fetching favourites!!!");
            final String ID_STATION = "id_bicing";
            final String NAME_STATION = "name";
            final String LATITUDE_STATION = "latitude";
            final String LONGITUDE_STATION = "longitude";

            JSONObject stationsObject = new JSONObject(stationsJSONString);

            JSONArray stationsJSONArray = stationsObject.getJSONArray("stations");
            String[] idsArray = new String[stationsJSONArray.length()];
            String where = " IN(";

            for(int i = 0; i < stationsJSONArray.length(); i++) {
                // These are the values that will be collected.

                String idString, latitudeString, longitudeString, name;

                // Get the JSON object representing the player
                JSONObject stationObject = stationsJSONArray.getJSONObject(i);

                idString = stationObject.getString(ID_STATION);
                latitudeString = stationObject.getString(LATITUDE_STATION);
                double latitude = Double.parseDouble(latitudeString);
                longitudeString = stationObject.getString(LONGITUDE_STATION);
                double longitude = Double.parseDouble(longitudeString);
                name = stationObject.getString(NAME_STATION);

                idsArray[i] = idString;
                where += "?";
                if(i < stationsJSONArray.length()-1)
                    where += ",";
            }
            if (idsArray.length > 0) {
                where += ")";
                ContentValues rankValues = new ContentValues();
                rankValues.put(DBContract.StationsEntry.COLUMN_FAVORITE, 1);
                int result = mContext.getContentResolver().update(DBContract.StationsEntry.CONTENT_URI, rankValues,
                        DBContract.StationsEntry.COLUMN_ID + where, idsArray);
                Log.d("openbikebcn", "num of results " + result);
                Message msg = Message.obtain();
                msg.what = FavoritesFragment.UPDATE_FAVORITES;
                FavoritesFragment.this._handler.sendMessage(msg);
            }
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
                Log.d(LOG_TAG, "Start fetching favorites");
                URL url = new URL(this.FAVOURITES_API_URL);

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
                getFavouritesDataFromJson(stationJsonString);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }
            // This will only happen if there was an error getting or parsing the forecast.
            return null;
        }


    }
}
