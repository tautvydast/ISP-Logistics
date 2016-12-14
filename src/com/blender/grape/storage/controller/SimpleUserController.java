package com.blender.grape.storage.controller;

import com.blender.grape.storage.model.SimpleUserModel;
import com.blender.grape.users.sql.SQLUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tautvydas Traška, IFF-4/2.
 */
public class SimpleUserController {
    public List<SimpleUserModel> getList() {
        List<SimpleUserModel> users = new ArrayList<>();
        String query = "SELECT * FROM gp_vartotojas";
        try (ResultSet rs = SQLUtil.executeQueryWithResult(query)) {
            while (rs != null && rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("vardas");
                String lastName = rs.getString("pavarde");
                SimpleUserModel model = new SimpleUserModel(id, name, lastName);
                users.add(model);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }
}
