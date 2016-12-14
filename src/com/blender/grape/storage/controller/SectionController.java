package com.blender.grape.storage.controller;

import com.blender.grape.storage.model.Section;
import com.blender.grape.storage.model.SimpleUserModel;
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
public class SectionController {
    private Section model;

    public void setModel(Section model) {
        this.model = model;
        System.out.println(model);
    }

    public Section getModel() {
        return model;
    }

    public List<Section> getList() {
        List<Section> sections = new ArrayList<>();
        String query = "SELECT gp_skyrius.*, gp_vartotojas.vardas, gp_vartotojas.pavarde FROM gp_skyrius, gp_vartotojas WHERE gp_skyrius.fk_vadovas=gp_vartotojas.id";
        try (ResultSet rs = SQLUtil.executeQueryWithResult(query)) {
            while (rs != null && rs.next()) {
                int id = rs.getInt("skyriaus_kodas");
                String title = rs.getString("pavadinimas");
                int capacity = rs.getInt("talpa");
                int commodityAmount = rs.getInt("prekiu_kiekis");
                String responsiblePerson = rs.getString("atsakingas_asmuo");
                String creationDate = rs.getString("ikurimo_data");

                int managerId = rs.getInt("fk_vadovas");
                String managerName = rs.getString("vardas");
                String managerLastName = rs.getString("pavarde");
                SimpleUserModel managerModel = new SimpleUserModel(managerId, managerName, managerLastName);
                Section temp = new Section(id, title, capacity, commodityAmount, responsiblePerson, creationDate, managerModel);
                sections.add(temp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sections;
    }

    public boolean insertIntoDatabase() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String query = "INSERT INTO gp_skyrius (pavadinimas, talpa, prekiu_kiekis, atsakingas_asmuo, ikurimo_data, fk_vadovas)" +
                " VALUES ('" + model.getTitle() + "', " + model.getCapacity() + ", 0, '" + model.getResponsiblePerson() + "'," +
                "'" + dateFormat.format(new Date()) + "', " + model.getManagerModel().getId() + ")";
        System.out.println(query);
        int status = SQLUtil.execute(query);
        return status > 0;
    }

    public boolean updateDatabaseModel() {
        String query = "UPDATE gp_skyrius SET " +
                "pavadinimas='" + model.getTitle() + "'" +
                ", talpa=" + model.getCapacity() +
                ", prekiu_kiekis=" + model.getCommodityAmount() +
                ", atsakingas_asmuo='" + model.getResponsiblePerson() + "'" +
                ", fk_vadovas=" + model.getManagerModel().getId() +
                " WHERE skyriaus_kodas=" + model.getId();
        System.out.println(query);
        int status = SQLUtil.execute(query);
        return status > 0;
    }

    public boolean deleteFromDatabase(int id) {
        String query = "DELETE FROM gp_skyrius WHERE skyriaus_kodas=" + id;
        int status = SQLUtil.execute(query);
        return status > 0;
    }
}
