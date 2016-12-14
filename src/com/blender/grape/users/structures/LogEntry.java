package com.blender.grape.users.structures;

import com.blender.grape.users.structures.user.User;

import java.time.LocalDateTime;

/**
 * @author Tautvydas Ramanauskas IFF-4/2
 */
public class LogEntry {
    private final LocalDateTime dateTime;
    private final String comment;
    private final User user;
    private final Permission permission;

    public LogEntry(LocalDateTime dateTime, String comment, User user, Permission permission) {
        this.dateTime = dateTime;
        this.comment = comment;
        this.user = user;
        this.permission = permission;
    }

    @Override
    public String toString() {
        return "[" + dateTime + "] " + user.getLogin() + " used \"" + permission + "\" permission";
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public String getComment() {
        return comment;
    }

    public User getUser() {
        return user;
    }

    public Permission getPermission() {
        return permission;
    }
}
