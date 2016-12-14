package com.blender.grape.users.structures.role;

import java.util.Collections;

/**
 * @author Tautvydas Ramanauskas IFF-4/2
 */
public class NoRole extends Role {
    private final static String ROLE = "role";

    public NoRole() {
        super(1, ROLE, Collections.emptySet());
    }
}
