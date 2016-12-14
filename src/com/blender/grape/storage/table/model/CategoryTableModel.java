package com.blender.grape.storage.table.model;

import com.blender.grape.storage.model.Category;

import javax.swing.table.AbstractTableModel;
import java.util.List;

/**
 * Created by Tautvydas Traška, IFF-4/2.
 */
public class CategoryTableModel extends AbstractTableModel {
    private static final int TITLE_COLUMN = 0;
    private static final int COMMODITY_AMOUNT_COLUMN = 1;
    private static final int CREATION_DATE_COLUMN = 2;
    private final String[] column_names = {"Title", "Commodity amount", "Creation date"};
    private final List<Category> categories;

    public CategoryTableModel(List<Category> categories) {
        this.categories = categories;
    }

    @Override
    public int getRowCount() {
        return categories.size();
    }

    @Override
    public int getColumnCount() {
        return column_names.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Category section = categories.get(rowIndex);
        switch (columnIndex) {
            case TITLE_COLUMN:
                return " " + section.getTitle();
            case COMMODITY_AMOUNT_COLUMN:
                return " " + section.getCommodityCount();
            case CREATION_DATE_COLUMN:
                return " " + section.getCreationDate();
        }
        return "";
    }

    @Override
    public String getColumnName(int column) {
        return column_names[column];
    }

    public Category getItemModel(int rowIndex) {
        return categories.get(rowIndex);
    }

    public void removeRow(int rowIndex) {
        categories.remove(rowIndex);
    }
}
