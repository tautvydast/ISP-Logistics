package com.blender.grape.users.structures;

import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * @author Tautvydas Ramanauskas IFF-4/2
 */
public class Password {
    private final LocalDateTime expirationDate;
    private int id;
    private String password;
    private int modificationCount;
    private int useCount;

    public Password() {
        expirationDate = LocalDateTime.now().plusYears(2);
        id = -1;
    }

    public Password(int id, String password, int modificationCount, int useCount, Timestamp expirationDate) {
        this.id = id;
        this.password = password;
        this.modificationCount = modificationCount;
        this.useCount = useCount;
        this.expirationDate = expirationDate.toLocalDateTime();
    }

    @Override
    public String toString() {
        return "\"" + password + "\", " + modificationCount + ", " + useCount;
    }

    public int getID() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public int getModificationCount() {
        return modificationCount;
    }

    public int getUseCount() {
        return useCount;
    }

    public LocalDateTime getExpirationDate() {
        return expirationDate;
    }

    public void setID(int id) {
        this.id = id;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setModificationCount(int modificationCount) {
        this.modificationCount = modificationCount;
    }

    public void setUseCount(int useCount) {
        this.useCount = useCount;
    }
}