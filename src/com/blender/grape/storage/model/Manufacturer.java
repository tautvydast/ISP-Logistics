package com.blender.grape.storage.model;

/**
 * Created by Tautvydas Traška, IFF-4/2.
 */
public class Manufacturer {
    private int id;
    private String name;
    private String country;
    private String creationDate;
    private String phoneNumber;

    public Manufacturer(int id, String name, String country, String creationDate, String phoneNumber) {
        this.id = id;
        this.name = name;
        this.country = country;
        this.creationDate = creationDate;
        this.phoneNumber = phoneNumber;
    }

    public int getId() {
        return id;
    }

    public String getName() { return name; }

    public String getCountry() {
        return country;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    @Override
    public String toString() {
        return name + " (" + country + ")";
    }
}
