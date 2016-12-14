package com.blender.grape.storage.model;

/**
 * Created by Tautvydas Traška, IFF-4/2.
 */
public class Category {
    private int id;
    private String title;
    private String description;
    private String creationDate;
    private int commodityCount;

    public Category(int id, String title, String description, String creationDate, int commodityCount) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.creationDate = creationDate;
        this.commodityCount = commodityCount;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public int getCommodityCount() {
        return commodityCount;
    }

    public void decreaseCommodityCount(int value) {
        this.commodityCount -= value;
    }

    public void increaseCommodityCount(int value) {
        this.commodityCount += value;
    }

    @Override
    public String toString() {
        return title;
    }
}
