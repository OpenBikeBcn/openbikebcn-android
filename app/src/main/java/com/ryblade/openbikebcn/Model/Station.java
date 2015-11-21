package com.ryblade.openbikebcn.Model;

/**
 * Created by alexmorral on 21/11/15.
 */
public class Station {

    private int id;
    private String type;
    private Double latitude;
    private Double longitude;
    private String streetName;
    private String streetNumber;
    private Double altitude;
    private int slots;
    private int bikes;
    private String nearbyStations;
    private String status;

    public Station() {

    }

    public Station(int id) {
        this.id = id;
    }

    public Station(int id2, String type2, Double lat, Double lon, String strName, String strNum, Double alt, int slots2, int bikes2, String nearBy, String stat) {
        this.id = id2;
        this.type = type2;
        this.latitude = lat;
        this.longitude = lon;
        this.streetName = strName;
        this.streetNumber = strNum;
        this.altitude = alt;
        this.slots = slots2;
        this.bikes = bikes2;
        this.nearbyStations = nearBy;
        this.status = stat;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public String getStreetNumber() {
        return streetNumber;
    }

    public void setStreetNumber(String streetNumber) {
        this.streetNumber = streetNumber;
    }

    public Double getAltitude() {
        return altitude;
    }

    public void setAltitude(Double altitude) {
        this.altitude = altitude;
    }

    public int getSlots() {
        return slots;
    }

    public void setSlots(int slots) {
        this.slots = slots;
    }

    public int getBikes() {
        return bikes;
    }

    public void setBikes(int bikes) {
        this.bikes = bikes;
    }

    public String getNearbyStations() {
        return nearbyStations;
    }

    public void setNearbyStations(String nearbyStations) {
        this.nearbyStations = nearbyStations;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }




}
