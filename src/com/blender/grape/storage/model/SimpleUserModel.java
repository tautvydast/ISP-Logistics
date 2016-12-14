package com.blender.grape.storage.model;

/**
 * Created by Tautvydas Traška, IFF-4/2.
 */
public class SimpleUserModel {
    private int id;
    private String name;
    private String lastName;

    public SimpleUserModel(int id, String name, String lastName) {
        this.id = id;
        this.name = name;
        this.lastName = lastName;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLastName() {
        return lastName;
    }

    @Override
    public String toString() {
        return name + " " + lastName;
    }
}
