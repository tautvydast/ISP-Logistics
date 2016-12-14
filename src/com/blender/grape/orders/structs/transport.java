package com.blender.grape.orders.structs;

/**
 * Created by sponkau on 13/12/2016.
 */
public class transport {
    private int id;
    private String marke;
    private String modelis;
    private String talpa;
    private String tempimo_galia;
    private String kuro_bakas;
    private String gedimas;
    private String spalva;
    private String sanaudos;
    private String technikine;
    private String busenaStr;
    private int busena;

    public transport(String marke, String modelis, String talpa, String tempimo_galia, String kuro_bakas, String gedimas
    , String spalva, String sanaudos, String technikine, int busena){
        this.marke = marke;
        this.modelis = modelis;
        this.talpa = talpa;
        this.tempimo_galia = tempimo_galia;
        this.kuro_bakas = kuro_bakas;
        this.gedimas = gedimas;
        this.spalva = spalva;
        this.sanaudos = sanaudos;
        this.technikine = technikine;
        this.busena = busena;
    }

    public transport(int id, String marke, String modelis, String busenaStr){
        this.id = id;
        this.marke = marke;
        this.modelis = modelis;
        this.busenaStr = busenaStr;
    }

    public transport(int id, String marke, String modelis, String talpa, String tempimo_galia, String kuro_bakas, String gedimas
            , String spalva, String sanaudos, String technikine, int busena, String busenaStr){
        this.id = id;
        this.marke = marke;
        this.modelis = modelis;
        this.talpa = talpa;
        this.tempimo_galia = tempimo_galia;
        this.kuro_bakas = kuro_bakas;
        this.gedimas = gedimas;
        this.spalva = spalva;
        this.sanaudos = sanaudos;
        this.technikine = technikine;
        this.busena = busena;
        this.busenaStr = busenaStr;
    }

    @Override
    public String toString() {
        return marke + " " + modelis + " " + busenaStr;
    }

    public String strOut() {
        return "'" + marke + "', '" + modelis + "', '" + talpa + "', '" + tempimo_galia + "', '" + kuro_bakas + "'" +
                ", '" + gedimas + "', '" + spalva + "', '" + sanaudos + "', '" + technikine + "'";
    }

    public int getID() { return id; }
    public int getBusena() { return busena; }
    public String getMarke() { return marke; }
    public String getModelis() { return modelis; }
    public String getBusenaStr() { return busenaStr; }
    public String getTalpa() { return talpa; }
    public String getGalia() { return tempimo_galia; }
    public String getBakas() { return kuro_bakas; }
    public String getGedimas() { return gedimas; }
    public String getSpalva() { return spalva; }
    public String getSanaudos() { return sanaudos; }
    public String getTechnikine() { return technikine; }

    public transport getTransport() { return this; }
}
