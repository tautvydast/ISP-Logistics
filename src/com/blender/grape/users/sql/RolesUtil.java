package com.blender.grape.users.sql;

import com.blender.grape.users.PermissionService;
import com.blender.grape.users.structures.Permission;
import com.blender.grape.users.structures.role.Role;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Tautvydas Ramanauskas IFF-4/2
 */
@SuppressWarnings("SpellCheckingInspection")
public final class RolesUtil {
    private RolesUtil() {
        // disabled
    }

    public static List<Role> getAllRoles() {
        List<Role> roles = new ArrayList<>();
        String getAllRolesQuery = "SELECT * FROM gp_role";
        try (ResultSet results = SQLUtil.executeQueryWithResult(getAllRolesQuery)) {
            while (results != null && results.next()) {
                int id = results.getInt("id");
                String name = results.getString("pavadinimas");
                Set<Permission> permissions = getPermissions(id);
                Role role = new Role(id, name, permissions);
                roles.add(role);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return roles;
    }

    public static void addRole(Role role) {
        String addRoleQuery = "INSERT INTO gp_role (pavadinimas) VALUES (\"" + role + "\")";
        SQLUtil.executeQuery(addRoleQuery);

        String getRoleIDQuery = "SELECT * FROM gp_role WHERE pavadinimas = \"" + role.getName() + "\"";
        try (ResultSet results = SQLUtil.executeQueryWithResult(getRoleIDQuery)) {
            if (results != null && results.first()) {
                int roleID = results.getInt("id");
                for (Permission permission : role.getPermissions()) {
                    String addRolePermissionQuery = "INSERT INTO gp_role_teise (fk_role, fk_teise) VALUES (" +
                            roleID + ", " + permission.getID() + ")";
                    SQLUtil.executeQuery(addRolePermissionQuery);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        PermissionService.logIt(Permission.CREATE_ROLE);
    }

    public static void editRole(Role role) {
        String updateName = "UPDATE gp_role SET pavadinimas = \"" + role.getName() + "\", atnaujinimo_data = NOW() " +
                "WHERE id = " + role.getID();
        SQLUtil.executeQuery(updateName);

        String deleteOldPermissions = "DELETE FROM gp_role_teise WHERE fk_role = " + role.getID();
        SQLUtil.executeQuery(deleteOldPermissions);

        for (Permission permission : role.getPermissions()) {
            String addRolePermission = "INSERT INTO gp_role_teise (fk_role, fk_teise) " +
                    "VALUES (" + role.getID() + ", " + permission.getID() + ")";
            SQLUtil.executeQuery(addRolePermission);
        }

        PermissionService.logIt(Permission.EDIT_ROLE);
    }

    public static void removeRole(Role role) {
        String deleteRolePermissions = "DELETE FROM gp_role_teise WHERE fk_role = " + role.getID();
        SQLUtil.executeQuery(deleteRolePermissions);

        String deleteRole = "DELETE FROM gp_role WHERE id = " + role.getID();
        SQLUtil.executeQuery(deleteRole);

        PermissionService.logIt(Permission.REMOVE_ROLE);
    }

    private static Set<Permission> getPermissions(int roleID) {
        Set<Permission> permissions = new HashSet<>();
        String getRolePermissionsQuery = "SELECT * FROM gp_role_teise WHERE fk_role = " + roleID;
        try (ResultSet results = SQLUtil.executeQueryWithResult(getRolePermissionsQuery)) {
            while (results != null && results.next()) {
                int permissionsID = results.getInt("fk_teise");
                Permission permission = Permission.createPermissionFromID(permissionsID);
                permissions.add(permission);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return permissions;
    }
}
