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

    public static void editTransport(transport Car) {
        try {
            Connection conn = SQLUtil.getConnection();
            Statement stmt = conn.createStatement
                    (ResultSet.TYPE_SCROLL_SENSITIVE,
                            ResultSet.CONCUR_UPDATABLE);
            String editTransportQuery = "UPDATE gp_transportas SET marke='" + Car.getMarke() + "',modelis='"
                    + Car.getModelis() + "',talpa='" + Car.getTalpa() + "',tempimo_galia='" + Car.getGalia() +
                    "',kuro_bako_dydis='" + Car.getBakas() + "',gedimas='" + Car.getGedimas() + "'," +
                    "spalva='" + Car.getSpalva() + "',sanaudos='" + Car.getSanaudos() + "'," +
                    "tecnikinio_galiojimo_data='" + Car.getTechnikine() + "' WHERE gp_transportas.id=" + Car.getID();
            String editStateQuery = "UPDATE gp_transporto_busena SET busena=" + Car.getBusena() + " WHERE " +
                    "gp_transporto_busena.fk_transportas=" + Car.getID();
            conn.setAutoCommit(false);
            stmt.addBatch(editTransportQuery);
            stmt.addBatch(editStateQuery);
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

    public static List<transport> getAllTransportFull() {
        List<transport> transports = new ArrayList<>();
        String getTransportQuery = "SELECT gp_transportas.*,gp_transporto_busena.busena,gp_busena.tipas FROM gp_transportas\n" +
                "INNER JOIN gp_transporto_busena\n" +
                "ON gp_transporto_busena.fk_transportas=gp_transportas.id\n" +
                "INNER JOIN gp_busena\n" +
                "ON gp_busena.id=gp_transporto_busena.busena";
        try (ResultSet results = SQLUtil.executeQueryWithResult(getTransportQuery)) {
            while (results != null && results.next()) {
                int id = results.getInt("id");
                String marke = results.getString("marke");
                String modelis = results.getString("modelis");
                String talpa = results.getString("talpa");
                String galia = results.getString("tempimo_galia");
                String bakas = results.getString("kuro_bako_dydis");
                String gedimas = results.getString("gedimas");
                String spalva = results.getString("spalva");
                String sanaudos = results.getString("sanaudos");
                String tech = results.getString("tecnikinio_galiojimo_data");
                String busenaStr = results.getString("tipas");
                int busena = results.getInt("busena");
                transport Transport = new transport(id, marke, modelis, talpa, galia, bakas, gedimas, spalva, sanaudos,
                        tech, busena, busenaStr);
                transports.add(Transport);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transports;
    }

    public static List<transport> getAllTransportNotOnOrder() {
        List<transport> transports = new ArrayList<>();
        String getTransportQuery = "SELECT gp_transportas.id,gp_transportas.marke,gp_transportas.modelis,gp_busena.tipas FROM gp_transportas\n" +
                "INNER JOIN gp_transporto_busena\n" +
                "ON gp_transporto_busena.fk_transportas=gp_transportas.id\n" +
                "INNER JOIN gp_busena\n" +
                "ON gp_busena.id=gp_transporto_busena.busena\n" +
                "WHERE gp_busena.id!=5";
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

    public static void deleteTransport(int Truck) {
        try {
            Connection conn = SQLUtil.getConnection();
            Statement stmt = conn.createStatement
                    (ResultSet.TYPE_SCROLL_SENSITIVE,
                            ResultSet.CONCUR_UPDATABLE);
            String deleteTransportStateQuery = "DELETE FROM gp_transporto_busena WHERE fk_transportas=" + Truck;
            String deleteTransportQuery = "DELETE FROM gp_transportas WHERE id=" + Truck;
            conn.setAutoCommit(false);
            stmt.addBatch(deleteTransportStateQuery);
            stmt.addBatch(deleteTransportQuery);
            stmt.executeBatch();
            conn.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
