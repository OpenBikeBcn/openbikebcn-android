package com.ryblade.openbikebcn;

import android.app.LoaderManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import org.osmdroid.api.IMapController;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

import com.ryblade.openbikebcn.FetchAPITask;
import com.ryblade.openbikebcn.Model.Station;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private Utils utilsManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        utilsManager = Utils.getInstance();

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        MapView mapView = (MapView) findViewById(R.id.map);
        IMapController mapController = mapView.getController();
        mapController.setZoom(15);

        GeoPoint startPoint = new GeoPoint(41.3833, 2.1833);
        mapController.setCenter(startPoint);


        this.updateBikesDatabase();



        ArrayList<Station> listOfStations = utilsManager.getAllStations(this);

        for (Station stat : listOfStations) {

            System.out.println(stat.getId() + " - " + stat.getStreetName());

        }

    }

    private void updateBikesDatabase() {
        new FetchAPITask(this).execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return false;
        }

        return super.onOptionsItemSelected(item);
    }


}
