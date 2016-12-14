package com.blender.grape.orders.sql;

import com.blender.grape.users.sql.SQLUtil;
import com.blender.grape.orders.structs.orderState;
import com.blender.grape.orders.structs.transport;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sponkau on 13/12/2016.
 */
public class TransportUtil {

    public static List<orderState> getAllTransportStates() {
        List<orderState> states = new ArrayList<>();
        String getAllStatesQuery = "SELECT * FROM gp_busena WHERE kam_skirta=2";
        try (ResultSet results = SQLUtil.executeQueryWithResult(getAllStatesQuery)) {
            while (results != null && results.next()) {
                int id = results.getInt("id");
                String type = results.getString("tipas");
                orderState state = new orderState(id, type);
                states.add(state);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return states;
    }

    public static void addTransport(transport Car) {
        try {
            Connection conn = SQLUtil.getConnection();
            Statement stmt = conn.createStatement
                    (ResultSet.TYPE_SCROLL_SENSITIVE,
                            ResultSet.CONCUR_UPDATABLE);
            String addTransportQuery = "INSERT INTO gp_transportas(marke,modelis,talpa,tempimo_galia,kuro_bako_dydis," +
                    "gedimas,spalva,sanaudos,tecnikinio_galiojimo_data,fk_vadybininkas) VALUES " +
                    "(" + Car.strOut() + ",'4')";
            String addStateQuery = "INSERT INTO gp_transporto_busena(fk_transportas,fk_vadybininkas,busena) VALUES " +
                    "(LAST_INSERT_ID(), '4', '" + Car.getBusena() + "')";
            conn.setAutoCommit(false);
            stmt.addBatch(addTransportQuery);
            stmt.addBatch(addStateQuery);
            stmt.executeBatch();
            conn.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<transport> getAllTransport() {
        List<transport> transports = new ArrayList<>();
        String getTransportQuery = "SELECT gp_transportas.id,gp_transportas.marke,gp_transportas.modelis,gp_busena.tipas FROM gp_transportas\n" +
                "INNER JOIN gp_transporto_busena\n" +
                "ON gp_transporto_busena.fk_transportas=gp_transportas.id\n" +
                "INNER JOIN gp_busena\n" +
                "ON gp_busena.id=gp_transporto_busena.busena";
        try (ResultSet results = SQLUtil.executeQueryWithResult(getTransportQuery)) {
            while (results != null && results.next()) {
                int id = results.getInt("id");
                String marke = results.getString("marke");
                String modelis = results.getString("modelis");
                String busenaStr = results.getString("tipas");
                transport Transport = new transport(id, marke, modelis, busenaStr);
                transports.add(Transport);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transports;
    }

    public static void editTransportState(int Truck, int State) {
        try {
            String editStateQuery = "UPDATE gp_transporto_busena SET busena=" + State + ",keitimo_data=now() WHERE fk_transportas=" + Truck;
            SQLUtil.executeQuery(editStateQuery);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
