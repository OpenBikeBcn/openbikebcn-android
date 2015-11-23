package com.ryblade.openbikebcn;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;

import com.ryblade.openbikebcn.Model.Station;

import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.overlays.Marker;
import org.osmdroid.bonuspack.overlays.Polygon;
import org.osmdroid.events.MapListener;
import org.osmdroid.events.ScrollEvent;
import org.osmdroid.events.ZoomEvent;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

import java.util.ArrayList;

/**
 * Created by Pau on 21/11/2015.
 */
public class OsmActivity extends Activity implements LocationListener {

    private Marker currentPosition;
    private Polygon accuracyCircle;
    private IMapController mapController;
    private MapView mapView;

    private final double MAP_DEFAULT_LATITUDE = 41.38791700;
    private final double MAP_DEFAULT_LONGITUDE = 2.16991870;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_osm);

        mapView = (MapView) findViewById(R.id.map);
        mapView.setMultiTouchControls(true);
        mapView.setClickable(true);

        currentPosition = new Marker(mapView);
        currentPosition.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER);
        currentPosition.setIcon(getResources().getDrawable(R.drawable.person));
        currentPosition.setTitle("My Position");

        accuracyCircle = new Polygon(this);
        accuracyCircle.setFillColor(Color.argb(80,135,206,250));
        accuracyCircle.setStrokeColor(Color.BLUE);
        accuracyCircle.setStrokeWidth(2);

        mapController = mapView.getController();
        mapController.setZoom(18);

        /* location manager */
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Location location = null;

        if (checkCallingOrSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkCallingOrSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    public void requestPermissions(@NonNull String[] permissions, int requestCode)
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            return;
        }

        for (String provider : locationManager.getProviders(true)) {
            location = locationManager.getLastKnownLocation(provider);
            if (location != null)
            {
                updateCurrentLocation(new GeoPoint(location), location.getAccuracy(), true);
                locationManager.requestLocationUpdates(provider, 0, 0, this);
                break;
            }
        }

        //add car position
        if (location == null)
        {
            location = new Location(LocationManager.GPS_PROVIDER);
            location.setLatitude(MAP_DEFAULT_LATITUDE);
            location.setLongitude(MAP_DEFAULT_LONGITUDE);
            updateCurrentLocation(new GeoPoint(location), 0, true);
        }

        ArrayList<Station> stations = Utils.getInstance().getAllStations(this);

        Drawable darkRed = getResources().getDrawable(R.drawable.darkred_marker);
        Drawable green = getResources().getDrawable(R.drawable.green_marker);
        Drawable black = getResources().getDrawable(R.drawable.black_marker);
        Drawable red = getResources().getDrawable(R.drawable.red_marker);
        Drawable blue = getResources().getDrawable(R.drawable.blue_marker);

        for (Station sta : stations) {
            Drawable icon;
            if (sta.getBikes() == 0)
                icon = darkRed;
            else if (sta.getBikes() < 4)
                icon = red;
            else if (sta.getSlots() == 0)
                icon = black;
            else if (sta.getSlots() < 4)
                icon = blue;
            else
                icon = green;
            Marker stationMarker = new Marker(mapView);
            stationMarker.setPosition(new GeoPoint(sta.getLatitude(), sta.getLongitude()));
            stationMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
            stationMarker.setIcon(icon);
            stationMarker.setTitle(String.valueOf(sta.getId()));
            stationMarker.setSnippet(String.valueOf(sta.getBikes()));
            stationMarker.setRelatedObject(sta);
            stationMarker.setOnMarkerClickListener(new Marker.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker, MapView mapView) {
                    return false;
                }
            });
            mapView.getOverlays().add(stationMarker);
        }

        mapView.invalidate();
    }

    public void updateCurrentLocation(GeoPoint point, float accuracy, boolean center) {
        currentPosition.setPosition(point);
        accuracyCircle.setPoints(Polygon.pointsAsCircle(point, accuracy));
        if(!mapView.getOverlays().contains(currentPosition)) {
            mapView.getOverlays().add(currentPosition);
            mapView.getOverlays().add(accuracyCircle);
        }
        if(center)
            mapController.animateTo(point);
        mapView.invalidate();
    }

    @Override
    public void onLocationChanged(Location location)
    {
        int lat = (int) (location.getLatitude() * 1E6);
        int lng = (int) (location.getLongitude() * 1E6);
        GeoPoint point = new GeoPoint(lat, lng);
        this.updateCurrentLocation(point, location.getAccuracy(), false);
    }

    @Override
    public void onProviderDisabled(String provider)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderEnabled(String provider)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras)
    {
        // TODO Auto-generated method stub

    }

    public void goToLocation(View v) {
        mapController.animateTo(currentPosition.getPosition());
    }
}
