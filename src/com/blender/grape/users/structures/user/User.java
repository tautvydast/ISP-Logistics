package com.blender.grape.users.structures.user;

import com.blender.grape.users.structures.Password;
import com.blender.grape.users.structures.Permission;
import com.blender.grape.users.structures.role.Role;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * @author Tautvydas Ramanauskas IFF-4/2
 */
public class User {
    private int id;
    private String login;
    private Password password;
    private String name;
    private String lastName;
    private String email;
    private String position;
    private String phone;
    private String address;
    private String personCode;
    private Set<Permission> permissions;
    private Role role;

    public User() {
        id = -1;
        password = new Password();
        permissions = new HashSet<>();
        role = new Role();
    }

    public User(int id, String login, Password password, String name, String lastName, String email, String position,
                String phone, String address, String personCode, Set<Permission> permissions, Role role) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.position = position;
        this.phone = phone;
        this.address = address;
        this.personCode = personCode;
        this.permissions = permissions;
        this.role = role;
    }

    @Override
    public String toString() {
        return "\"" + login +
                "\", " + password.getID() +
                ", \"" + name +
                "\", \"" + lastName +
                "\", \"" + email +
                "\", \"" + position +
                "\", \"" + phone +
                "\", \"" + address +
                "\", \"" + personCode +
                "\", " + role.getID();
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof User) {
            User user = (User) object;
            return Objects.equals(login, user.getLogin());
        }
        return false;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPersonCode(String personCode) {
        this.personCode = personCode;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public int getID() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public Password getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPosition() {
        return position;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public String getPersonCode() {
        return personCode;
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }

    public Role getRole() {
        return role;
    }

    public boolean is(String username, char[] password) {
        boolean correctUsername = Objects.equals(username, this.login);
        boolean correctPassword = Arrays.equals(password, this.password.getPassword().toCharArray());
        return correctUsername && correctPassword;
    }
}