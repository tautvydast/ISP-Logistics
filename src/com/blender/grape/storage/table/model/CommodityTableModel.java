package com.blender.grape.storage.table.model;

import com.blender.grape.storage.model.Commodity;

import javax.swing.table.AbstractTableModel;
import java.util.List;

/**
 * Created by Tautvydas Traška, IFF-4/2.
 */
public class CommodityTableModel extends AbstractTableModel {
    private static final int TITLE_COLUMN = 0;
    private static final int COUNTRY_COLUMN = 1;
    private static final int CREATION_DATE_COLUMN = 2;
    private static final int COMMENT_COLUMN = 3;
    private static final int VALUE_COLUMN = 4;
    private static final int AMOUNT_COLUMN = 5;
    private static final int SIZE_COLUMN = 6;
    private static final int COLOR_COLUMN = 7;
    private static final int PURPOSE_COLUMN = 8;
    private static final int MANUFACTURER_COLUMN = 9;
    private static final int CATEGORY_COLUMN = 10;
    private static final int SECTION_COLUMN = 11;
    private final String[] column_names = {"Title", "Origin country", "Manufacture date", "Comment", "Value", "Amount",
                                           "Size", "Color", "Purpose", "Manufacturer", "Category", "Section"};
    private final List<Commodity> commodities;

    public CommodityTableModel(List<Commodity> commodities) {
        this.commodities = commodities;
    }

    @Override
    public int getRowCount() {
        return commodities.size();
    }

    @Override
    public int getColumnCount() {
        return column_names.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Commodity commodity = commodities.get(rowIndex);
        switch (columnIndex) {
            case TITLE_COLUMN:
                return " " + commodity.getTitle();
            case COUNTRY_COLUMN:
                return " " + commodity.getOriginCountry();
            case CREATION_DATE_COLUMN:
                return " " + commodity.getCreationDate();
            case COMMENT_COLUMN:
                return " " + commodity.getComment();
            case VALUE_COLUMN:
                return " " + commodity.getValue();
            case AMOUNT_COLUMN:
                return " " + commodity.getAmount();
            case SIZE_COLUMN:
                return " " + commodity.getSize();
            case COLOR_COLUMN:
                return " " + commodity.getColor();
            case PURPOSE_COLUMN:
                return " " + commodity.getPurpose();
            case MANUFACTURER_COLUMN:
                return " " + commodity.getManufacturerModel();
            case CATEGORY_COLUMN:
                return " " + commodity.getCategoryModel();
            case SECTION_COLUMN:
                return " " + commodity.getSectionModel();
        }
        return "";
    }

    @Override
    public String getColumnName(int column) {
        return column_names[column];
    }

    public Commodity getItemModel(int rowIndex) {
        return commodities.get(rowIndex);
    }

    public void removeRow(int rowIndex) {
        commodities.remove(rowIndex);
    }
}