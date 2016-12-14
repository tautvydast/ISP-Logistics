package com.blender.grape.storage.table.model;

import com.blender.grape.resources.DialogResources;
import com.blender.grape.storage.model.Section;
import com.blender.grape.users.structures.role.Role;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by Tautvydas Traška, IFF-4/2.
 */
public class SectionTableModel extends AbstractTableModel {
    private static final int TITLE_COLUMN = 0;
    private static final int CAPACITY_COLUMN = 1;
    private static final int COMMODITY_AMOUNT_COLUMN = 2;
    private static final int RESP_PERS_COLUMN = 3;
    private static final int CREATION_DATE_COLUMN = 4;
    private static final int MANAGER_COLUMN = 5;
    private final String[] column_names = {"Title", "Capacity", "Commodity amount", "Responsible person", "Creation date", "Manager"};
    private final List<Section> sections;

    public SectionTableModel(List<Section> sections) {
        this.sections = sections;
    }

    @Override
    public int getRowCount() {
        return sections.size();
    }

    @Override
    public int getColumnCount() {
        return column_names.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Section section = sections.get(rowIndex);
        switch (columnIndex) {
            case TITLE_COLUMN:
                return " " + section.getTitle();
            case CAPACITY_COLUMN:
                return " " + section.getCapacity();
            case COMMODITY_AMOUNT_COLUMN:
                return " " + section.getCommodityAmount();
            case RESP_PERS_COLUMN:
                return " " + section.getResponsiblePerson();
            case CREATION_DATE_COLUMN:
                return " " + section.getCreationDate();
            case MANAGER_COLUMN:
                return " " + section.getManagerModel().toString();
        }
        return "";
    }

    @Override
    public String getColumnName(int column) {
        return column_names[column];
    }

    public Section getItemModel(int rowIndex) {
        return sections.get(rowIndex);
    }

    public void removeRow(int rowIndex) {
        sections.remove(rowIndex);
    }
}
