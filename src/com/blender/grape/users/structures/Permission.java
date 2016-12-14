package com.blender.grape.users.structures;

import com.blender.grape.resources.DialogResources;

import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Tautvydas Ramanauskas IFF-4/2
 */
public enum Permission {
    NONE(0, "PERMISSION_NONE"),
    CREATE_USERS(1, "PERMISSION_CREATE_USERS"),
    EDIT_USERS(2, "PERMISSION_EDIT_USERS"),
    REMOVE_USERS(3, "PERMISSION_REMOVE_USERS"),
    EDIT_USER_PERMISSIONS(4, "PERMISSION_EDIT_USER_PERMISSIONS"),
    CREATE_ROLE(5, "PERMISSION_CREATE_ROLE"),
    EDIT_ROLE(6, "PERMISSION_EDIT_ROLE"),
    REMOVE_ROLE(7, "PERMISSION_REMOVE_ROLE"),
    OPEN_LOG(8, "PERMISSION_OPEN_LOG"),
    MANAGE_WAREHOUSE(9, "PERMISSION_WAREHOUSE"),
    MANAGE_ORDERS(10, "PERMISSION_ORDERS");

    private final int id;
    private final String title;

    Permission(int id, String title) {
        this.id = id;
        this.title = DialogResources.getString(title);
    }

    @Override
    public String toString() {
        return title;
    }

    public int getID() {
        return id;
    }

    public static Permission createPermissionFromID(int permissionID) {
        for (Permission permission : values()) {
            if (permission.getID() == permissionID) {
                return permission;
            }
        }
        return NONE;
    }

    public static Set<Permission> getAll() {
        return Stream.of(values()).collect(Collectors.toSet());
    }
}
