package com.blender.grape.storage.table.model;

import com.blender.grape.storage.model.Manufacturer;

import javax.swing.table.AbstractTableModel;
import java.util.List;

/**
 * Created by Tautvydas Traška, IFF-4/2.
 */
public class ManufacturerTableModel extends AbstractTableModel {
    private static final int TITLE_COLUMN = 0;
    private static final int COUNTRY_COLUMN = 1;
    private static final int CREATION_DATE_COLUMN = 2;
    private static final int PHONE_COLUMN = 3;
    private final String[] column_names = {"Title", "Country", "Creation date", "Phone"};
    private final List<Manufacturer> manufacturers;

    public ManufacturerTableModel(List<Manufacturer> manufacturers) {
        this.manufacturers = manufacturers;
    }

    @Override
    public int getRowCount() {
        return manufacturers.size();
    }

    @Override
    public int getColumnCount() {
        return column_names.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Manufacturer manufacturer = manufacturers.get(rowIndex);
        switch (columnIndex) {
            case TITLE_COLUMN:
                return " " + manufacturer.getName();
            case COUNTRY_COLUMN:
                return " " + manufacturer.getCountry();
            case CREATION_DATE_COLUMN:
                return " " + manufacturer.getCreationDate();
            case PHONE_COLUMN:
                return " " + manufacturer.getPhoneNumber();
        }
        return "";
    }

    @Override
    public String getColumnName(int column) {
        return column_names[column];
    }

    public Manufacturer getItemModel(int rowIndex) {
        return manufacturers.get(rowIndex);
    }

    public void removeRow(int rowIndex) {
        manufacturers.remove(rowIndex);
    }
}
