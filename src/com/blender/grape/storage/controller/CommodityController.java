package com.blender.grape.storage.controller;

import com.blender.grape.storage.model.*;
import com.blender.grape.users.sql.SQLUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tautvydas Traška, IFF-4/2.
 */
public class CommodityController {

    public List<Commodity> getList(int stateValue, int type, int typeId, String pattern) {
        String busena = "busena";
        String fk_gamintojas = "fk_gamintojas";
        String fk_kategorija = "fk_kategorija";
        String fk_krovinys = "fk_krovinys";
        String fk_skyrius = "fk_skyrius";

        if (stateValue != -1) {
            busena = stateValue + "";
        }

        if (type != -1 && typeId != -1) {
            switch (type) {
                case 0:
                    fk_gamintojas = typeId + "";
                    break;
                case 1:
                    fk_kategorija = typeId + "";
                    break;
                case 2:
                    fk_krovinys = typeId + "";
                    break;
                case 3:
                    fk_skyrius = typeId + "";
                    break;
            }
        }

        List<Commodity> commodities = new ArrayList<>();
        String query =
                "SELECT " +
                "gp_preke.*, " +
                "gp_gamintojas.pavadinimas AS manTitle, gp_gamintojas.salis AS manCountry, " +
                "gp_kategorija.pavadinimas AS catTitle, " +
                "gp_krovinys.salis AS shiCountry, gp_krovinys.atvykimo_data AS shiDate, " +
                "gp_skyrius.pavadinimas AS secTitle " +
                "FROM " +
                "gp_preke, " +
                "gp_gamintojas, " +
                "gp_kategorija, " +
                "gp_krovinys, " +
                "gp_skyrius " +
                "WHERE " +
                "gp_preke.fk_gamintojas=gp_gamintojas.gamintojo_kodas AND " +
                "gp_preke.fk_kategorija=gp_kategorija.kategorijos_kodas AND " +
                "gp_preke.fk_krovinys=gp_krovinys.krovinio_kodas AND " +
                "gp_preke.fk_skyrius=gp_skyrius.skyriaus_kodas AND " +
                "gp_preke.busena=" + busena + " AND " +
                "gp_preke.fk_gamintojas=" + fk_gamintojas + " AND " +
                "gp_preke.fk_kategorija=" + fk_kategorija + " AND " +
                "gp_preke.fk_krovinys=" + fk_krovinys + " AND " +
                "gp_preke.fk_skyrius=" + fk_skyrius;
        if (!pattern.equals("")) {
            query += " AND gp_preke.pavadinimas LIKE '%" + pattern + "%'";
        }

        try (ResultSet rs = SQLUtil.executeQueryWithResult(query)) {
            while (rs != null && rs.next()) {
                int id = rs.getInt("prekes_kodas");
                String name = rs.getString("pavadinimas");
                String originCountry = rs.getString("kilmes_salis");
                String manufactureDate = rs.getString("pagaminimo_data");
                String state = rs.getString("busena");
                String comment = rs.getString("komentaras");
                float value = rs.getFloat("verte");
                int amount = rs.getInt("kiekis");
                String size = rs.getString("dydis");
                String color = rs.getString("spalva");
                String purpose = rs.getString("paskirtis");

                int manufacturerId = rs.getInt("fk_gamintojas");
                String manufacturerTitle = rs.getString("manTitle");
                String manufacturerCountry = rs.getString("manCountry");

                int categoryId = rs.getInt("fk_kategorija");
                String categoryTitle = rs.getString("catTitle");

                int shipmentId = rs.getInt("fk_krovinys");
                String shipmentCountry = rs.getString("shiCountry");
                String shipmentDate = rs.getString("shiDate");

                int sectionId = rs.getInt("fk_skyrius");
                String secTitle = rs.getString("secTitle");

                Commodity commodity = new Commodity(id, name, originCountry, manufactureDate, state, comment,
                        value, amount, size, color, purpose,
                        new Manufacturer(manufacturerId, manufacturerTitle, manufacturerCountry, "", ""),
                        new Category(categoryId, categoryTitle, "", "", 0),
                        new Shipment(shipmentId, shipmentDate, 0, shipmentCountry, null, null),
                        new Section(sectionId, secTitle, 0, 0, "", "", null));

                commodities.add(commodity);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return commodities;
    }

    public boolean addListToDatabase(List<Commodity> commodities, int shipmentId) {
        for (Commodity c : commodities) {
            String query = "INSERT INTO `gp_preke`(`pavadinimas`, `kilmes_salis`, `pagaminimo_data`," +
                    " `busena`, `komentaras`, `verte`, `kiekis`, `dydis`, `spalva`, `paskirtis`, `fk_gamintojas`," +
                    " `fk_kategorija`, `fk_krovinys`, `fk_skyrius`) VALUES " +
                    "(" +
                    "'" + c.getTitle() + "', " +
                    "'" + c.getOriginCountry() + "', " +
                    "'" + c.getCreationDate() + "', " +
                    0 + ", " +
                    "'" + c.getComment() + "', " +
                    "'" + c.getValue() + "', " +
                    "'" + c.getAmount() + "', " +
                    "'" + c.getSize() + "', " +
                    "'" + c.getColor() + "', " +
                    "'" + c.getPurpose() + "', " +
                    c.getManufacturerModel().getId() + ", " +
                    c.getCategoryModel().getId() + ", " +
                    shipmentId + ", " +
                    c.getSectionModel().getId() +
                    ")";
            int status = SQLUtil.execute(query);
            if (status == 0) {
                return false;
            }
        }
        return true;
    }

    public boolean updateStatus(int id, int updatedStatus, String comment) {
        String query = "UPDATE gp_preke SET busena=" + updatedStatus + ", komentaras='" + comment + "' WHERE prekes_kodas=" + id;
        int status = SQLUtil.execute(query);
        return status > 0;
    }

}
