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
public class GoodsReceiver extends User {
    public static final String RECEIVER = "Receiver";

    public GoodsReceiver() {
        super(103, RECEIVER, new Password(103, RECEIVER, 0, 0, Timestamp.valueOf(LocalDateTime.now().plusYears(2))),
                "", "", "", RECEIVER, "103", "", "103", Collections.singleton(Permission.MANAGE_WAREHOUSE), new NoRole());
    }

    public static boolean isGoodsReceiver(String username, char[] password) {
        boolean isReceiver = Objects.equals(username, RECEIVER) && Arrays.equals(password, RECEIVER.toCharArray());
        if (isReceiver) {
            PermissionService.setUser(new GoodsReceiver());
        }
        return isReceiver;
    }
}
