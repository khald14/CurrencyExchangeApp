package com.example.mychatapptutorial;

import android.net.Uri;

import java.util.ArrayList;
import java.util.HashMap;

public class UserModel implements Comparable<UserModel>{

    private String name;
    private double distance;
    private String id ;
    private String image;
    private HashMap<String,String> currencies;

    // Constructor
    public UserModel(String name, double distance, String image , String id, HashMap<String,String> currencies) {
        this.name = name;
        this.distance = distance;
        this.image = (image);
        this.id =id;
        this.currencies = currencies;
    }

    // Getter and Setter
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistane(Double distance) {
        this.distance = distance;
    }

    public String getimage() {
        return image;
    }

    public String getId() {
        return id;
    }

    public void setimage(String image) {
        this.image = image;
    }

    @Override
    public int compareTo(UserModel o) {
        int compareage = (int)(o.getDistance());

        //  For Ascending order
        return (int)this.distance - compareage;
    }

    public HashMap<String, String> getCurrencies() {
        return currencies;
    }

    public void setCurrencies(HashMap<String, String> currencies) {
        this.currencies = currencies;
    }
}
