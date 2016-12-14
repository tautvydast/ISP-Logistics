package com.blender.grape.users.sql;

import java.sql.*;

/**
 * @author Tautvydas Ramanauskas IFF-4/2
 */
@SuppressWarnings("SpellCheckingInspection")
public final class SQLUtil {
    private static final String IP2 = "jdbc:mysql://localhost:3306/isp";
    private static final String IP = "jdbc:mysql://db.if.ktu.lt/";
    private static final String USER_NAME = "tauram1";
    private static final String PASSWORD = "chae9za6eiHahDoh";
    private static final String DATABASE = "tauram1";
    private static Connection connection;

    private SQLUtil() {
        // disabled
    }

    public static ResultSet executeQueryWithResult(String query) {
        try {
            Connection connection = getConnection();
            Statement statement = connection.createStatement();
            return statement.executeQuery(query);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean executeQuery(String query) {
        try {
            Connection connection = getConnection();
            Statement statement = connection.createStatement();
            return statement.execute(query);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static int execute(String query) {
        try {
            Connection connection = getConnection();
            Statement statement = connection.createStatement();
            return statement.executeUpdate(query);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static void closeResultSet(ResultSet resultSet) {
        // TODO: 16/11/03 use autoclosable
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static Connection getConnection() throws SQLException, ClassNotFoundException {
        if (connection == null) {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(IP+DATABASE, USER_NAME, PASSWORD);
        }
        return connection;
    }
}
