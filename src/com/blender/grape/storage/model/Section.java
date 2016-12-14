package com.blender.grape.storage.model;

/**
 * Created by Tautvydas Traška, IFF-4/2.
 */
public class Section {
    private int id;
    private String title;
    private int capacity;
    private int commodityAmount;
    private String responsiblePerson;
    private String creationDate;
    private SimpleUserModel managerModel;

    public Section(int id, String title, int capacity, int commodityAmount, String responsiblePerson,
                   String creationDate, SimpleUserModel managerModel) {
        this.id = id;
        this.title = title;
        this.capacity = capacity;
        this.commodityAmount = commodityAmount;
        this.responsiblePerson = responsiblePerson;
        this.creationDate = creationDate;
        this.managerModel = managerModel;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getCommodityAmount() { return commodityAmount; }

    public String getResponsiblePerson() {
        return responsiblePerson;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public SimpleUserModel getManagerModel() {
        return managerModel;
    }

    public void decreaseCapacity(int capacity) {
        this.capacity -= capacity;
        this.commodityAmount += capacity;
    }

    public void increaseCapacity(int capacity) {
        this.capacity += capacity;
        this.commodityAmount -= capacity;
    }

    @Override
    public String toString() {
        return title;
    }
}
