package com.blender.grape.storage.model;

/**
 * Created by Tautvydas Traška, IFF-4/2.
 */
public class Commodity {
    private int id;
    private String title;
    private String originCountry;
    private String creationDate;
    private String state;
    private String comment;
    private float value;
    private int amount;
    private String size; // sutvarkyti DB tipa
    private String color;
    private String purpose;

    private Manufacturer manufacturerModel;
    private Category categoryModel;
    private Shipment shipmentModel;
    private Section sectionModel;

    public Commodity(int id, String title, String originCountry, String creationDate, String state, String comment,
                     float value, int amount, String size, String color, String purpose,
                     Manufacturer manufacturerModel, Category categoryModel, Shipment shipmentModel,
                     Section sectionModel) {
        this.id = id;
        this.title = title;
        this.originCountry = originCountry;
        this.creationDate = creationDate;
        this.state = state;
        this.comment = comment;
        this.value = value;
        this.amount = amount;
        this.size = size;
        this.color = color;
        this.purpose = purpose;
        this.manufacturerModel = manufacturerModel;
        this.categoryModel = categoryModel;
        this.shipmentModel = shipmentModel;
        this.sectionModel = sectionModel;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getOriginCountry() {
        return originCountry;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public String getState() {
        return state;
    }

    public String getComment() {
        return comment;
    }

    public float getValue() {
        return value;
    }

    public int getAmount() {
        return amount;
    }

    public String getSize() {
        return size;
    }

    public String getColor() {
        return color;
    }

    public String getPurpose() {
        return purpose;
    }

    public Manufacturer getManufacturerModel() {
        return manufacturerModel;
    }

    public Category getCategoryModel() {
        return categoryModel;
    }

    public Shipment getShipmentModel() {
        return shipmentModel;
    }

    public Section getSectionModel() {
        return sectionModel;
    }
}
