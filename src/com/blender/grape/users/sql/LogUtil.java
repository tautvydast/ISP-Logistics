package com.blender.grape.users.sql;

import com.blender.grape.users.PermissionService;
import com.blender.grape.users.structures.LogEntry;
import com.blender.grape.users.structures.Permission;
import com.blender.grape.users.structures.user.User;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Tautvydas Ramanauskas IFF-4/2
 */
@SuppressWarnings("SpellCheckingInspection")
public final class LogUtil {
    private LogUtil() {
        // disabled
    }

    public static List<LogEntry> getLog() {
        List<LogEntry> logEntries = new ArrayList<>();
        String getLogQuery = "SELECT * FROM gp_registras";
        try (ResultSet results = SQLUtil.executeQueryWithResult(getLogQuery)) {
            while (results != null && results.next()) {
                Date date = results.getDate("data");
                Time time = results.getTime("laikas");
                String comment = results.getString("komentaras");
                User user = getUser(results.getInt("fk_vartotojas"));
                Permission permission = Permission.createPermissionFromID(results.getInt("fk_teise"));
                LocalDateTime localDateTime = LocalDateTime.of(date.toLocalDate(), time.toLocalTime());
                LogEntry logEntry = new LogEntry(localDateTime, comment, user, permission);
                logEntries.add(0, logEntry);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        PermissionService.logIt(Permission.OPEN_LOG);
        return logEntries;
    }

    private static User getUser(int userID) throws SQLException {
        String query = "SELECT * FROM gp_vartotojas WHERE id = " + userID;
        ResultSet resultSet = SQLUtil.executeQueryWithResult(query);
        if (resultSet != null && resultSet.first()) {
            return UsersUtils.getUser(resultSet);
        }
        return null;
    }
}
