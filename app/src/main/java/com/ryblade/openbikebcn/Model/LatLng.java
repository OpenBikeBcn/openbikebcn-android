package com.ryblade.openbikebcn.Model;


/**
 * Created by alexmorral on 28/11/15.
 */
public class LatLng {

    private double Lat;
    private double Lon;

    public LatLng() {}

    public LatLng(double lat, double lon) {
        Lat = lat;
        Lon = lon;
    }

    public double getLat() {
        return Lat;
    }

    public double getLon() {
        return Lon;
    }
}
