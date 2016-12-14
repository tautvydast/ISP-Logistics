package com.blender.grape.storage.controller;

import com.blender.grape.storage.model.Shipment;
import com.blender.grape.storage.model.SimpleUserModel;
import com.blender.grape.users.sql.SQLUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tautvydas Traška, IFF-4/2.
 */
public class ShipmentController {

    public List<Shipment> getList() {
        List<Shipment> shipmentList = new ArrayList<>();
        String query = "SELECT * FROM gp_krovinys";
        try (ResultSet rs = SQLUtil.executeQueryWithResult(query)) {
            while (rs != null && rs.next()) {
                int id = rs.getInt("krovinio_kodas");
                String arrivalDate = rs.getString("atvykimo_data");
                int weight = Integer.parseInt(rs.getString("svoris"));
                String country = rs.getString("salis");
                int driverId = Integer.parseInt(rs.getString("fk_vairuotojas"));

                Shipment temp = new Shipment(id, arrivalDate, weight, country, new SimpleUserModel(driverId, "", ""), null);
                shipmentList.add(temp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return shipmentList;
    }

    public boolean registerShipment(Shipment shipment) {
        String query = "INSERT INTO gp_krovinys" +
                " (atvykimo_data, svoris, salis, fk_vairuotojas)" +
                " VALUES" +
                " (" +
                "'" + shipment.getArrivalDate() + "', " +
                shipment.getWeight() +
                ", '" + shipment.getCountry() + "', " +
                shipment.getDriver().getId() +
                ")";
        int status = SQLUtil.execute(query);
        return status > 0;
    }

    public int getNextShipmentId() {
        String query = "SELECT `auto_increment` FROM INFORMATION_SCHEMA.TABLES\n" +
                "WHERE table_name = 'gp_krovinys'";
        ResultSet rs = SQLUtil.executeQueryWithResult(query);
        try {
            if (rs != null && rs.next()) {
                return rs.getInt("auto_increment");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
}
