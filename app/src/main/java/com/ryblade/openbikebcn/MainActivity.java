package com.ryblade.openbikebcn;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import org.osmdroid.util.BoundingBoxE6;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        MapView mapView = (MapView) findViewById(R.id.map);

        GeoPoint geoPoint1 = new GeoPoint(41, 2);
        GeoPoint geoPoint2 = new GeoPoint(41, 3);
        GeoPoint geoPoint3 = new GeoPoint(42, 2);
        GeoPoint geoPoint4 = new GeoPoint(42, 3);

        ArrayList<GeoPoint> arrayGeo = new ArrayList<>();
        arrayGeo.add(geoPoint1);
        arrayGeo.add(geoPoint2);
        arrayGeo.add(geoPoint3);
        arrayGeo.add(geoPoint4);

        BoundingBoxE6 bb = BoundingBoxE6.fromGeoPoints(arrayGeo);

        mapView.zoomToBoundingBox(bb);

        mapView.setMaxZoomLevel(5);
        mapView.setMinZoomLevel(5);
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
