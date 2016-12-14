package com.blender.grape.users.sql;

import com.blender.grape.users.PermissionService;
import com.blender.grape.users.structures.Password;
import com.blender.grape.users.structures.Permission;
import com.blender.grape.users.structures.role.Role;
import com.blender.grape.users.structures.user.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @author Tautvydas Ramanauskas IFF-4/2
 */
@SuppressWarnings("SpellCheckingInspection")
public final class UsersUtils {
    private UsersUtils() {
        // disabled
    }

    public static boolean containsLogin(String userName, String password) {
        String query = "SELECT * FROM gp_vartotojas WHERE prisijungimo_vardas = \"" + userName + "\"";
        try (ResultSet resultSet = SQLUtil.executeQueryWithResult(query)) {
            if (resultSet != null && resultSet.first()) {
                User user = getUser(resultSet);
                Password userPassword = user.getPassword();
                boolean isExpired = user.getPassword().getExpirationDate().isBefore(LocalDateTime.now());
                boolean isLogin = Objects.equals(userPassword.getPassword(), password);
                if (isLogin && !isExpired) {
                    userPassword.setUseCount(userPassword.getUseCount() + 1);
                    updatePassword(userPassword, false);
                    updateUserUseDate(user);
                    PermissionService.setUser(user);
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String query = "SELECT * FROM gp_vartotojas";
        try (ResultSet resultSet = SQLUtil.executeQueryWithResult(query)) {
            while (resultSet != null && resultSet.next()) {
                User user = getUser(resultSet);
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    public static void addUser(User user) {
        String addPasswordQuery = "INSERT INTO gp_slaptazodis (slaptazodis_text, modifikavimu_skaicius, " +
                "panaudojimu_skaicius, galiojimo_data) " +
                "VALUES (" + user.getPassword() + ", DATE_ADD(NOW(), INTERVAL 2 YEAR))";
        SQLUtil.executeQuery(addPasswordQuery);

        String getPasswordIDQuery = "SELECT * FROM gp_slaptazodis WHERE slaptazodis_text = \""
                + user.getPassword().getPassword() + "\"";
        try (ResultSet resultSet = SQLUtil.executeQueryWithResult(getPasswordIDQuery)) {
            if (resultSet != null && resultSet.first()) {
                int passwordID = resultSet.getInt("id");
                user.getPassword().setID(passwordID);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String addUserQuery = "INSERT INTO gp_vartotojas (prisijungimo_vardas, fk_slaptazodis, vardas, pavarde, " +
                "emailas, pareigos, telefonas, adresas, asmens_kodas, fk_role) VALUES (" + user + ")";
        SQLUtil.executeQuery(addUserQuery);

        PermissionService.logIt(Permission.CREATE_USERS);
    }

    public static void editUser(User user) {
        try {
            Password password = user.getPassword();
            password.setModificationCount(password.getModificationCount() + 1);
            updatePasswordIfNeed(password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String query = "UPDATE gp_vartotojas SET " +
                "prisijungimo_vardas = \"" + user.getLogin() +
                "\", vardas = \"" + user.getName() +
                "\", pavarde = \"" + user.getLastName() +
                "\", emailas = \"" + user.getEmail() +
                "\", pareigos = \"" + user.getPosition() +
                "\", telefonas = \"" + user.getPhone() +
                "\", adresas = \"" + user.getAddress() +
                "\", asmens_kodas = \"" + user.getPersonCode() +
                "\", fk_role = " + user.getRole().getID() +
                " WHERE id = " + user.getID();
        SQLUtil.executeQuery(query);

        PermissionService.logIt(Permission.EDIT_USERS);
    }

    public static void removeUser(User user) {
        String deletePermissionsQuery = "DELETE FROM gp_vartotojas_teise WHERE fk_vartotojas = " + user.getID();
        SQLUtil.executeQuery(deletePermissionsQuery);
        String deleteUserQuery = "DELETE FROM gp_vartotojas WHERE id = " + user.getID();
        SQLUtil.executeQuery(deleteUserQuery);
        String deleteRoleQuery = "DELETE FROM gp_slaptazodis WHERE id = " + user.getPassword().getID();
        SQLUtil.executeQuery(deleteRoleQuery);
        PermissionService.logIt(Permission.REMOVE_USERS);
    }

    public static void updatePermissions(User user, Set<Permission> permissions) {
        Set<Permission> unchangedPermissions = new HashSet<>(user.getPermissions());
        unchangedPermissions.retainAll(permissions);

        Set<Permission> deletedPermissions = new HashSet<>(user.getPermissions());
        deletedPermissions.removeAll(unchangedPermissions);
        for (Permission deletedPermission : deletedPermissions) {
            String query = "DELETE FROM gp_vartotojas_teise WHERE fk_vartotojas = " + user.getID()
                    + " AND fk_teise = " + deletedPermission.getID();
            SQLUtil.executeQuery(query);
        }

        Set<Permission> newPermissions = new HashSet<>(permissions);
        newPermissions.removeAll(unchangedPermissions);
        for (Permission newPermission : newPermissions) {
            String query = "INSERT INTO gp_vartotojas_teise (fk_vartotojas, fk_teise) VALUES (" + user.getID()
                    + ", " + newPermission.getID() + ")";
            SQLUtil.executeQuery(query);
        }

        PermissionService.logIt(Permission.EDIT_USER_PERMISSIONS);
    }

    public static User getUser(ResultSet resultSet) throws SQLException {
        int userID = resultSet.getInt("id");
        String userName = resultSet.getString("prisijungimo_vardas");
        String name = resultSet.getString("vardas");
        String lastName = resultSet.getString("pavarde");
        String occupation = resultSet.getString("pareigos");
        String phoneNumber = resultSet.getString("telefonas");
        String address = resultSet.getString("adresas");
        String personsCode = resultSet.getString("asmens_kodas");
        String eMail = resultSet.getString("emailas");
        int passwordID = resultSet.getInt("fk_slaptazodis");
        int roleID = resultSet.getInt("fk_role");
        Password password = getPassword(passwordID);
        Set<Permission> permissions = getPermissionsForUser(userID);
        Role role = getRole(roleID);
        return new User(userID, userName, password, name, lastName, eMail, occupation, phoneNumber, address,
                personsCode, permissions, role);
    }

    private static Password getPassword(int passwordID) throws SQLException {
        String query = "SELECT * FROM gp_slaptazodis WHERE id = " + passwordID;
        try (ResultSet resultSet = SQLUtil.executeQueryWithResult(query)) {
            if (resultSet != null && resultSet.first()) {
                String passwordText = resultSet.getString("slaptazodis_text");
                int modificationCount = resultSet.getInt("modifikavimu_skaicius");
                int useCount = resultSet.getInt("panaudojimu_skaicius");
                Timestamp expirationDate = resultSet.getTimestamp("galiojimo_data");
                return new Password(passwordID, passwordText, modificationCount, useCount, expirationDate);
            }
        }
        return new Password();
    }

    private static Role getRole(int roleID) throws SQLException {
        String query = "SELECT * FROM gp_role WHERE id = " + roleID;
        try (ResultSet resultSet = SQLUtil.executeQueryWithResult(query)) {
            if (resultSet != null && resultSet.first()) {
                String name = resultSet.getString("pavadinimas");
                Set<Permission> permissions = getPermissionsForRole(roleID);
                return new Role(roleID, name, permissions);
            }
        }
        return new Role();
    }

    private static Set<Permission> getPermissionsForUser(int userID) throws SQLException {
        String query = "SELECT * FROM gp_vartotojas_teise WHERE fk_vartotojas = " + userID;
        return getPermission(query);
    }

    private static Set<Permission> getPermissionsForRole(int roleID) throws SQLException {
        String query = "SELECT * FROM gp_role_teise WHERE fk_role = " + roleID;
        return getPermission(query);
    }

    private static Set<Permission> getPermission(String query) throws SQLException {
        Set<Permission> permissions = new HashSet<>();
        try (ResultSet resultSet = SQLUtil.executeQueryWithResult(query)) {
            while (resultSet != null && resultSet.next()) {
                int permissionID = resultSet.getInt("fk_teise");
                Permission permission = Permission.createPermissionFromID(permissionID);
                permissions.add(permission);
            }
        }
        return permissions;
    }

    private static void updateUserUseDate(User user) {
        String updateUserUseDate = "UPDATE gp_vartotojas " +
                "SET paskutinio_prisijungimo_data = NOW() " +
                "WHERE id = " + user.getID();
        SQLUtil.executeQuery(updateUserUseDate);
    }

    private static void updatePasswordIfNeed(Password password) throws SQLException {
        String getPasswordQuery = "SELECT * FROM gp_slaptazodis WHERE id = " + password.getID();
        try (ResultSet resultSet = SQLUtil.executeQueryWithResult(getPasswordQuery)) {
            if (resultSet != null && resultSet.first()) {
                String oldPassword = resultSet.getString("slaptazodis_text");
                if (!Objects.equals(password.getPassword(), oldPassword)) {
                    updatePassword(password, true);
                }
            }
        }
    }

    private static void updatePassword(Password password, boolean update) throws SQLException {
        String start = "UPDATE gp_slaptazodis " +
                "SET slaptazodis_text = \"" + password.getPassword() + "\", " +
                "panaudojimu_skaicius = " + (update ? 0 : password.getUseCount()) + ", " +
                "modifikavimu_skaicius = " + (update ? password.getModificationCount() : 0) + " ";
        String mid = update ? ", atnaujinimo_data = NOW(), galiojimo_data = DATE_ADD(NOW(), INTERVAL 2 YEAR) " : "";
        String end = "WHERE id = " + password.getID();
        String query = start + mid + end;
        SQLUtil.executeQuery(query);
    }
}