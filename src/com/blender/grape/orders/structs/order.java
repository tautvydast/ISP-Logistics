package com.blender.grape.orders.structs;

/**
 * Created by sponkau on 12/12/2016.
 */
public class order {
    private int prekesKiekis;
    private int vadybininkas;
    private int busena;
    private int preke;
    private int transportas;
    private String pristatymoAdresas;
    private String isvezimoData;
    private String atvezimoData;
    private String busenaStr;
    private String prekesPav;

    public order() { }

    public order(int prekesKiekis, int vadybininkas, int busena, int preke, int transportas,
                 String pristatymoAdresas, String isvezimoData, String atvezimoData){
        this.prekesKiekis = prekesKiekis;
        this.vadybininkas = vadybininkas;
        this.busena = busena;
        this.preke = preke;
        this.transportas = transportas;
        this.pristatymoAdresas = pristatymoAdresas;
        this.isvezimoData = isvezimoData;
        this.atvezimoData = atvezimoData;
    }

    public order(int busena, String prekesPav, int prekesKiekis, String pristatymoAdresas, String atvezimoData, String busenaStr){
        this.busena = busena;
        this.prekesKiekis = prekesKiekis;
        this.busenaStr = busenaStr;
        this.pristatymoAdresas = pristatymoAdresas;
        this.atvezimoData = atvezimoData;
        this.prekesPav = prekesPav;
    }

    @Override
    public String toString() {
        return prekesPav + " | " + prekesKiekis + " | " + pristatymoAdresas + " | " + atvezimoData + " | " + busenaStr;
    }

    public int getPrekesKiekis() { return prekesKiekis; }
    public int getVadybininkas() { return vadybininkas; }
    public int getBusena() { return busena; }
    public int getPreke() { return preke; }
    public int getTransportas() { return transportas; }
    public String getPristatymoAdresas() { return pristatymoAdresas; }
    public String getIsvezimoData() { return isvezimoData; }
    public String getAtvezimoData() { return atvezimoData; }

}
