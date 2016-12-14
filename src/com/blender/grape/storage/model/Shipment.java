package com.blender.grape.storage.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tautvydas Traška, IFF-4/2.
 */
public class Shipment {
    private int id;
    private String arrivalDate;
    private int weight; // gramais
    private String country;

    SimpleUserModel driver;
    private List<Commodity> commodities;

    public Shipment(int id, String arrivalDate, int weight, String country, SimpleUserModel driver, List<Commodity> commodities) {
        this.id = id;
        this.arrivalDate = arrivalDate;
        this.weight = weight;
        this.country = country;
        this.driver = driver;
        this.commodities = commodities;
    }

    public int getId() {
        return id;
    }

    public String getArrivalDate() {
        return arrivalDate;
    }

    public int getWeight() {
        return weight;
    }

    public String getCountry() {
        return country;
    }

    public SimpleUserModel getDriver() {
        return driver;
    }

    public List<Commodity> getCommodities() {
        return commodities;
    }

    @Override
    public String toString() {
        return country + " (" + arrivalDate + ")";
    }
}
