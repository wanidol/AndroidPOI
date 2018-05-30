package com.example.pc_wanidol.secondsessionpoi.Models;

/**
 * Created by PC-Wanidol on 09/08/2017.
 */

public class PlaceInterest {

    private int id ;
    private String placename;

    private float latitude ;
    private float longitude ;



    public PlaceInterest(){

    }

    public PlaceInterest(int id, String placename, float latitude, float longitude) {
        this.id = id;
        this.placename = placename;
        this.latitude = latitude;
        this.longitude = longitude;

    }

    public PlaceInterest(String firstname, String placename, float latitude, float longitude) {
        this.id = id;
        this.placename = placename;
        this.latitude = latitude;
        this.longitude = longitude;

    }




    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "" + id + ": " + placename  ;
    }

    public String getPlacename() {
        return placename;
    }

    public void setPlacename(String placename) {
        this.placename = placename;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }





}
