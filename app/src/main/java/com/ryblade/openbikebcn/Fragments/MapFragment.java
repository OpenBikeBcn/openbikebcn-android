package com.ryblade.openbikebcn.Fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ryblade.openbikebcn.Model.Station;
import com.ryblade.openbikebcn.R;
import com.ryblade.openbikebcn.Utils;

import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.overlays.Marker;
import org.osmdroid.bonuspack.overlays.Polygon;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

import java.util.ArrayList;

/**
 * Created by alexmorral on 25/11/15.
 */
public class MapFragment extends Fragment implements LocationListener {


    private Marker currentPosition;
    private Polygon accuracyCircle;
    private IMapController mapController;
    private MapView mapView;
    private LinearLayout stationInfo;
    private boolean isStationInfoHiden;
    private Station stationSelected;

    private final double MAP_DEFAULT_LATITUDE = 41.38791700;
    private final double MAP_DEFAULT_LONGITUDE = 2.16991870;


    public MapFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.map_fragment, container, false);

        initMapView(rootView);
        initStationInfo(rootView);

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }



    private void initMapView(View rootView) {
        mapView = (MapView) rootView.findViewById(R.id.map);
        mapView.setMultiTouchControls(true);
        mapView.setClickable(true);

        isStationInfoHiden = true;

        mapView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(!isStationInfoHiden) {
                    stationInfo.animate().translationY(stationInfo.getHeight());
                    isStationInfoHiden = true;
                    stationSelected = null;
                }
                return false;
            }
        });

        currentPosition = new Marker(mapView);
        currentPosition.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER);
        currentPosition.setIcon(getResources().getDrawable(R.drawable.person));
        currentPosition.setTitle("My Position");

        accuracyCircle = new Polygon(getActivity());
        accuracyCircle.setFillColor(Color.argb(80, 135, 206, 250));
        accuracyCircle.setStrokeColor(Color.BLUE);
        accuracyCircle.setStrokeWidth(2);

        mapController = mapView.getController();
        mapController.setZoom(18);

        /* location manager */
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        Location location = null;

        if (getActivity().checkCallingOrSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && getActivity().checkCallingOrSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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

        ArrayList<Station> stations = Utils.getInstance().getAllStations(getActivity());

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
                    mapController.animateTo(marker.getPosition());
                    Station station = ((Station) marker.getRelatedObject());
                    ((TextView) stationInfo.findViewById(R.id.stationId)).setText(String.valueOf(station.getId()));
                    ((TextView) stationInfo.findViewById(R.id.stationAddress)).setText(String.format("%s, %s",
                            station.getStreetName(), String.valueOf(station.getStreetNumber())));
                    ((TextView) stationInfo.findViewById(R.id.stationBikes)).setText(String.valueOf(station.getBikes()));
                    ((TextView) stationInfo.findViewById(R.id.stationSlots)).setText(String.valueOf(station.getSlots()));
                    stationInfo.animate().translationY(-stationInfo.getHeight());
                    isStationInfoHiden = false;
                    stationSelected = station;
                    return false;
                }
            });
            mapView.getOverlays().add(stationMarker);
        }

        mapView.invalidate();
    }

    public void initStationInfo(View rootView) {
        stationInfo = ((LinearLayout) rootView.findViewById(R.id.stationInfo));
        stationInfo.setBackgroundColor(Color.LTGRAY);
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

    public Station getStationSelected() {
        return stationSelected;
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
