package com.ryblade.openbikebcn.Model;

import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alexmorral on 28/11/15.
 */
public class Route {

    private int idStationOrigin;
    private int idStationArrival;

    private GeoPoint startPoint;
    private GeoPoint endPoint;

    private List<LatLng> route;

    private double distance;

    private int time;


    public Route() {

    }

    public Route(GeoPoint start, GeoPoint end) {
        startPoint = start;
        endPoint = end;
    }

    public GeoPoint getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(GeoPoint startPoint) {
        this.startPoint = startPoint;
    }

    public GeoPoint getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(GeoPoint endPoint) {
        this.endPoint = endPoint;
    }

    public List<LatLng> getRoute() {
        return route;
    }

    public void setEncodedRoute(String encodedRoute) {
        route = decode(encodedRoute);
    }


    private static List<LatLng> decode(final String encodedPath) {
        int len = encodedPath.length();

        // For speed we preallocate to an upper bound on the final length, then
        // truncate the array before returning.
        final List<LatLng> path = new ArrayList<LatLng>();
        int index = 0;
        int lat = 0;
        int lng = 0;

        while (index < len) {
            int result = 1;
            int shift = 0;
            int b;
            do {
                b = encodedPath.charAt(index++) - 63 - 1;
                result += b << shift;
                shift += 5;
            } while (b >= 0x1f);
            lat += (result & 1) != 0 ? ~(result >> 1) : (result >> 1);

            result = 1;
            shift = 0;
            do {
                b = encodedPath.charAt(index++) - 63 - 1;
                result += b << shift;
                shift += 5;
            } while (b >= 0x1f);
            lng += (result & 1) != 0 ? ~(result >> 1) : (result >> 1);
            double auxLat, auxLon;

            auxLat = lat*0.000001;
            auxLon = lng*0.000001;
            path.add(new LatLng(auxLat, auxLon));
        }

        return path;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }


    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getIdStationOrigin() {
        return idStationOrigin;
    }

    public void setIdStationOrigin(int idStationOrigin) {
        this.idStationOrigin = idStationOrigin;
    }

    public int getIdStationArrival() {
        return idStationArrival;
    }

    public void setIdStationArrival(int idStationArrival) {
        this.idStationArrival = idStationArrival;
    }
}
