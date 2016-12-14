package com.blender.grape.orders.structs;

/**
 * Created by sponkau on 10/12/2016.
 */
public class orderState {
    private int id;
    private String type;

    public orderState(int id, String type) {
        this.id = id;
        this.type = type;
    }

    @Override
    public String toString() {
        return type;
    }

    public String getType() {
        return type;
    }

    public int getID() {
        return id;
    }

}
