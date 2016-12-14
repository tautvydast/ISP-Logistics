package com.blender.grape.orders.structs;

/**
 * Created by sponkau on 12/12/2016.
 */
public class orderTransport {
    private int id;
    private String marke;
    private String modelis;

    public orderTransport(int id, String marke, String modelis) {
        this.id = id;
        this.marke = marke;
        this.modelis = modelis;
    }

    @Override
    public String toString() {
        return marke + " " + modelis;
    }

    public int getID() { return id; }
}
