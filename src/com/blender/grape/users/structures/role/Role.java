package com.blender.grape.users.structures.role;

import com.blender.grape.users.structures.Permission;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Tautvydas Ramanauskas IFF-4/2
 */
public class Role {
    private int id;
    private String name;
    private Set<Permission> permissions;

    public Role() {
        this(-1, "", new HashSet<>());
    }

    public Role(int id, String name, Set<Permission> permissions) {
        this.id = id;
        this.name = name;
        this.permissions = permissions;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof Role) {
            Role role = (Role) object;
            return id == role.getID();
        }
        return false;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPermissions(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    public String getName() {
        return name;
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }

    public int getID() {
        return id;
    }
}
