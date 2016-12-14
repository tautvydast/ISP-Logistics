package com.blender.grape.users.structures.user;

import com.blender.grape.users.PermissionService;
import com.blender.grape.users.structures.Password;
import com.blender.grape.users.structures.Permission;
import com.blender.grape.users.structures.role.NoRole;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;

/**
 * @author Tautvydas Ramanauskas IFF-4/2
 */
public class Manager extends User {
    public static final String MANAGER = "Manager";

    public Manager() {
        super(102, MANAGER, new Password(102, MANAGER, 0, 0, Timestamp.valueOf(LocalDateTime.now().plusYears(2))),
                "", "", "", MANAGER, "102", "", "102", Collections.singleton(Permission.MANAGE_ORDERS), new NoRole());
    }

    public static boolean isManager(String username, char[] password) {
        boolean isManager = Objects.equals(username, MANAGER) && Arrays.equals(password, MANAGER.toCharArray());
        if (isManager) {
            PermissionService.setUser(new Manager());
        }
        return isManager;
    }
}
