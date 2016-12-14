package com.blender.grape.orders.structs;

/**
 * Created by sponkau on 11/12/2016.
 */
public class orderContent {
    private int kiekis;
    private int prekes_kodas;
    private String pavadinimas;

    public orderContent(int prekes_kodas, int kiekis, String pavadinimas) {
        this.prekes_kodas = prekes_kodas;
        this.kiekis = kiekis;
        this.pavadinimas = pavadinimas;
    }

    @Override
    public String toString() {
        return pavadinimas + " | " + kiekis;
    }

    public int getPrekes_kodas() { return prekes_kodas; }
}
