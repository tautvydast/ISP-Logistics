package com.blender.grape.users.ui;

import com.blender.grape.resources.DialogResources;
import com.blender.grape.users.structures.Permission;

import javax.swing.table.AbstractTableModel;
import java.util.Map;

/**
 * @author Tautvydas Ramanauskas IFF-4/2
 */
public class PermissionsTableModel extends AbstractTableModel {
    public final static int PERMISSION_COLUMN = 0;
    public final static int PERMISSION_STATUS_COLUMN = 1;
    private final String[] titles = {"PERMISSIONS_TABLE_STATUS", "PERMISSIONS_TABLE_PERMISSION"};
    private final Map<Permission, Boolean> permissions;

    public PermissionsTableModel(Map<Permission, Boolean> permissions) {
        this.permissions = permissions;
    }

    @Override
    public int getRowCount() {
        return permissions.size() - 1;
    }

    @Override
    public int getColumnCount() {
        return titles.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Permission permission = Permission.values()[rowIndex + 1];
        switch (columnIndex) {
            case PERMISSION_COLUMN:
                return permission;
            case PERMISSION_STATUS_COLUMN:
                Boolean contains = permissions.get(permission);
                return contains == null || contains;
        }
        return "";
    }

    @Override
    public String getColumnName(int column) {
        return DialogResources.getString(titles[column]);
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case PERMISSION_COLUMN:
                return Permission.class;
            case PERMISSION_STATUS_COLUMN:
                return Boolean.class;
        }
        return super.getColumnClass(columnIndex);
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        if (columnIndex == PERMISSION_STATUS_COLUMN) {
            Object value = getValueAt(rowIndex, PERMISSION_COLUMN);
            if (value instanceof Permission) {
                Boolean contains = permissions.get(value);
                return contains != null;
            }
        }
        return false;
    }

    @Override
    public void setValueAt(Object value, int rowIndex, int columnIndex) {
        if (columnIndex == PERMISSION_STATUS_COLUMN) {
            Object valueAt = getValueAt(rowIndex, PERMISSION_COLUMN);
            if (valueAt instanceof Permission) {
                Boolean contains = permissions.get(valueAt);
                if (contains != null) {
                    permissions.put(Permission.values()[rowIndex + 1], (Boolean) value);
                }
            }
        }
    }
}
