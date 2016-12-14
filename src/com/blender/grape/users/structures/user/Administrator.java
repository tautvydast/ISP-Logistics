package com.blender.grape.users.structures.user;

import com.blender.grape.users.PermissionService;
import com.blender.grape.users.structures.Password;
import com.blender.grape.users.structures.Permission;
import com.blender.grape.users.structures.role.NoRole;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Objects;

/**
 * @author Tautvydas Ramanauskas IFF-4/2
 */
public final class Administrator extends User {
    public static final String ADMIN = "Admin";

    public Administrator() {
        super(101, ADMIN, new Password(101, ADMIN, 0, 0, Timestamp.valueOf(LocalDateTime.now().plusYears(2))),
                "", "", "", ADMIN, "101", "", "101", Permission.getAll(), new NoRole());
    }

    public static boolean isAdmin(String username, char[] password) {
        boolean isAdmin = Objects.equals(username, ADMIN) && Arrays.equals(password, ADMIN.toCharArray());
        if (isAdmin) {
            PermissionService.setUser(new Administrator());
        }
        return isAdmin;
    }
}