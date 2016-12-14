package com.blender.grape.users;

import com.blender.grape.users.sql.SQLUtil;
import com.blender.grape.users.structures.Permission;
import com.blender.grape.users.structures.user.User;

import java.util.Set;

/**
 * @author Tautvydas Ramanauskas IFF-4/2
 */
@SuppressWarnings("SpellCheckingInspection")
public final class PermissionService {
    private static PermissionService INSTANCE;
    private User user;

    private PermissionService() {

    }

    public static void setUser(User user) {
        getInstance().user = user;
    }

    public static User getUser() {
        return getInstance().user;
    }

    public static boolean hasRightTo(Permission permission) {
        return getInstance().user.getPermissions().contains(permission);
    }

    public static boolean isUserManager() {
        PermissionService permissionService = getInstance();
        Set<Permission> permissions = permissionService.user.getPermissions();
        return permissions.contains(Permission.CREATE_USERS) ||
                permissions.contains(Permission.EDIT_USERS) ||
                permissions.contains(Permission.REMOVE_USERS) ||
                permissions.contains(Permission.EDIT_USER_PERMISSIONS);
    }

    public static boolean isRoleManager() {
        PermissionService permissionService = getInstance();
        Set<Permission> permissions = permissionService.user.getPermissions();
        return permissions.contains(Permission.CREATE_ROLE) ||
                permissions.contains(Permission.EDIT_ROLE) ||
                permissions.contains(Permission.REMOVE_ROLE);
    }

    public static void logIt(Permission permission) {
        int userID = getInstance().user.getID();
        String logEntry = "INSERT INTO gp_registras (laikas, komentaras, data, fk_teise, fk_vartotojas) VALUES " +
                "(CURTIME(), " + "\"\", " + "CURDATE()" + ", " + permission.getID() + ", " + userID + ")";
        SQLUtil.executeQuery(logEntry);
    }

    private static PermissionService getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new PermissionService();
        }
        return INSTANCE;
    }
}
