package com.blender.grape.storage.controller;

import com.blender.grape.storage.model.Category;
import com.blender.grape.users.sql.SQLUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Tautvydas Traška, IFF-4/2.
 */
public class CategoryController {
    private Category model;

    public void setModel(Category model) {
        this.model = model;
    }

    public Category getModel() {
        return model;
    }

    public List<Category> getList() {
        List<Category> categories = new ArrayList<>();
        String query = "SELECT * FROM gp_kategorija";
        try (ResultSet rs = SQLUtil.executeQueryWithResult(query)) {
            while (rs != null && rs.next()) {
                int id = rs.getInt("kategorijos_kodas");
                String title = rs.getString("pavadinimas");
                String description = rs.getString("aprasymas");
                String creationDate = rs.getString("sukurimo_data");
                int commodityAmount = rs.getInt("prekiu_kiekis");
                Category temp = new Category(id, title, description, creationDate, commodityAmount);
                categories.add(temp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categories;
    }

    public boolean insertIntoDatabase() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String query = "INSERT INTO gp_kategorija (pavadinimas, aprasymas, sukurimo_data, prekiu_kiekis)" +
                " VALUES ('" + model.getTitle() + "', '" + model.getDescription() + "', '" +  dateFormat.format(new Date()) + "', 0)";
        System.out.println(query);
        int status = SQLUtil.execute(query);
        return status > 0;
    }

    public boolean updateDatabaseModel() {
        String query = "UPDATE gp_kategorija SET " +
                "pavadinimas='" + model.getTitle() + "'" +
                ", aprasymas='" + model.getDescription() + "'" +
                ", prekiu_kiekis=" + model.getCommodityCount() +
                " WHERE kategorijos_kodas=" + model.getId();
        System.out.println(query);
        int status = SQLUtil.execute(query);
        return status > 0;
    }

    public boolean deleteFromDatabase(int id) {
        String query = "DELETE FROM gp_kategorija WHERE kategorijos_kodas=" + id;
        int status = SQLUtil.execute(query);
        return status > 0;
    }
}
