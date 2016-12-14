package com.blender.grape.storage.controller;

import com.blender.grape.storage.model.Manufacturer;
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
public class ManufacturerController {
    private Manufacturer model;

    public void setModel(Manufacturer model) {
        this.model = model;
    }

    public Manufacturer getModel() {
        return model;
    }

    public List<Manufacturer> getList() {
        List<Manufacturer> manufacturers = new ArrayList<>();
        String query = "SELECT * FROM gp_gamintojas";
        try (ResultSet rs = SQLUtil.executeQueryWithResult(query)) {
            while (rs != null && rs.next()) {
                int id = rs.getInt("gamintojo_kodas");
                String title = rs.getString("pavadinimas");
                String country = rs.getString("salis");
                String creationDate = rs.getString("itraukimo_data");
                String phoneNumber = rs.getString("atstovo_tel_nr");
                Manufacturer temp = new Manufacturer(id, title, country, creationDate, phoneNumber);
                manufacturers.add(temp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return manufacturers;
    }

    public boolean insertIntoDatabase() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String query = "INSERT INTO gp_gamintojas (pavadinimas, salis, itraukimo_data, atstovo_tel_nr)" +
                " VALUES ('" + model.getName() + "', '" + model.getCountry() + "', '" +  dateFormat.format(new Date()) +
                "', '" + model.getPhoneNumber() + "')";
        System.out.println(query);
        int status = SQLUtil.execute(query);
        return status > 0;
    }

    public boolean updateDatabaseModel() {
        String query = "UPDATE gp_gamintojas SET " +
                "pavadinimas='" + model.getName() + "'" +
                ", salis='" + model.getCountry() + "'" +
                ", atstovo_tel_nr='" + model.getPhoneNumber() + "'" +
                " WHERE gamintojo_kodas=" + model.getId();
        System.out.println(query);
        int status = SQLUtil.execute(query);
        return status > 0;
    }

    public boolean deleteFromDatabase(int id) {
        String query = "DELETE FROM gp_gamintojas WHERE gamintojo_kodas=" + id;
        int status = SQLUtil.execute(query);
        return status > 0;
    }
}
