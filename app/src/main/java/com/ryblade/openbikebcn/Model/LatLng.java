package com.ryblade.openbikebcn.Model;


/**
 * Created by alexmorral on 28/11/15.
 */
public class LatLng {

    private Double Lat;
    private Double Lon;

    public LatLng() {}

    public LatLng(double lat, double lon) {
        Lat = lat;
        Lon = lon;
    }

    public Double getLat() {
        return Lat;
    }

    public Double getLon() {
        return Lon;
    }
}
