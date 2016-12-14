package com.blender.grape.orders.sql;

import com.blender.grape.orders.structs.order;
import com.blender.grape.orders.structs.orderContent;
import com.blender.grape.orders.structs.orderState;
import com.blender.grape.orders.structs.orderTransport;
import com.blender.grape.users.sql.SQLUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sponkau on 10/12/2016.
 */
public class OrdersUtil {

    public static List<orderState> getAllOrderStates() {
        List<orderState> states = new ArrayList<>();
        String getAllStatesQuery = "SELECT * FROM gp_busena WHERE kam_skirta=1";
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

    public static List<orderContent> getAvailableContent() {
        List<orderContent> contents = new ArrayList<>();
        String getAvailableContentQuery = "SELECT * FROM gp_preke WHERE busena=2 AND kiekis>0";
        try (ResultSet results = SQLUtil.executeQueryWithResult(getAvailableContentQuery)) {
            while (results != null && results.next()) {
                int prekes_kodas = results.getInt("prekes_kodas");
                int kiekis = results.getInt("kiekis");
                String pavadinimas = results.getString("pavadinimas");
                orderContent content = new orderContent(prekes_kodas, kiekis, pavadinimas);
                contents.add(content);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return contents;
    }

    public static List<orderTransport> getAvailableTransport() {
        List<orderTransport> transports = new ArrayList<>();
        String getAvailableContentQuery = "SELECT * FROM gp_transportas\n" +
                "INNER JOIN gp_transporto_busena\n" +
                "ON gp_transporto_busena.fk_transportas=gp_transportas.id\n" +
                "WHERE gp_transporto_busena.busena=2";
        try (ResultSet results = SQLUtil.executeQueryWithResult(getAvailableContentQuery)) {
            while (results != null && results.next()) {
                int id = results.getInt("id");
                String marke = results.getString("marke");
                String modelis = results.getString("modelis");
                orderTransport transport = new orderTransport(id, marke, modelis);
                transports.add(transport);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transports;
    }

    public static void createOrder(order Uzsakymas) {

        try {
            Connection conn = SQLUtil.getConnection();
            Statement stmt = conn.createStatement
                    (ResultSet.TYPE_SCROLL_SENSITIVE,
                            ResultSet.CONCUR_UPDATABLE);
            String insertOrderStateQuery = "INSERT INTO gp_uzsakymo_busena(busena,fk_vadybininkas) VALUES ('" + Uzsakymas.getBusena() + "'," +
                    "'" + Uzsakymas.getVadybininkas() + "')";
            String insertOrderQuery = "INSERT INTO gp_uzsakymas(isvezimo_data, atvezimo_data, pristatymo_adresas" +
                    ", fk_vadybininkas, fk_uzsakymo_busena, fk_transportas) VALUES " +
                    "('" + Uzsakymas.getIsvezimoData() + "', '" + Uzsakymas.getAtvezimoData() + "'," +
                    " '" + Uzsakymas.getPristatymoAdresas() + "', '" + Uzsakymas.getVadybininkas() + "'," +
                    " LAST_INSERT_ID(), '" + Uzsakymas.getTransportas() + "')";
            String insertOrderContentQuery = "INSERT INTO gp_uzsakymas_preke(kiekis,fk_preke, fk_uzsakymas) VALUES " +
                    "('" + Uzsakymas.getPrekesKiekis() + "', '" + Uzsakymas.getPreke() + "', LAST_INSERT_ID())";
            String subtractContentQuery = "UPDATE gp_preke SET kiekis = kiekis - " + Uzsakymas.getPrekesKiekis() + " " +
                    "WHERE gp_preke.prekes_kodas=" +Uzsakymas.getPreke();
            String reserveTruckQuery = "UPDATE gp_transporto_busena SET busena=5,keitimo_data=now() " +
                    "WHERE fk_transportas=" + Uzsakymas.getTransportas();
            conn.setAutoCommit(false);
            stmt.addBatch(insertOrderStateQuery);
            stmt.addBatch(insertOrderQuery);
            stmt.addBatch(insertOrderContentQuery);
            stmt.addBatch(subtractContentQuery);
            stmt.addBatch(reserveTruckQuery);
            stmt.executeBatch();
            conn.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //System.out.println(id);

    }

    public static List<order> getAllOrders() {
        List<order> orders = new ArrayList<>();
        String getAllOrdersQuery = "SELECT gp_uzsakymas.numeris,gp_preke.pavadinimas,gp_busena.tipas,gp_uzsakymas_preke.kiekis,gp_uzsakymas.pristatymo_adresas,gp_uzsakymas.atvezimo_data,gp_uzsakymo_busena.id FROM gp_uzsakymas\n" +
                "INNER JOIN gp_uzsakymas_preke\n" +
                "ON gp_uzsakymas_preke.fk_uzsakymas=gp_uzsakymas.numeris\n" +
                "INNER JOIN gp_preke\n" +
                "ON gp_uzsakymas_preke.fk_preke=gp_preke.prekes_kodas\n" +
                "INNER JOIN gp_uzsakymo_busena\n" +
                "ON gp_uzsakymo_busena.id=gp_uzsakymas.fk_uzsakymo_busena\n" +
                "INNER JOIN gp_busena\n" +
                "ON gp_busena.id=gp_uzsakymo_busena.busena";
        try (ResultSet results = SQLUtil.executeQueryWithResult(getAllOrdersQuery)) {
            while (results != null && results.next()) {
                int UzsakymoId = results.getInt("numeris");
                int BusenaId = results.getInt("id");
                String pavadinimas = results.getString("pavadinimas");
                int kiekis = results.getInt("kiekis");
                String pristatymo_adresas = results.getString("pristatymo_adresas");
                String atvezimo_data = results.getString("atvezimo_data");
                String busena = results.getString("tipas");

                order Order = new order(BusenaId, pavadinimas, kiekis, pristatymo_adresas, atvezimo_data, busena);
                orders.add(Order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

    public static void editOrderState(int currentState, int stateId) {
        try {
            String updateOrderQuery = "UPDATE gp_uzsakymo_busena SET busena=" + stateId + ",keitimo_data=now() WHERE id=" + currentState;
            SQLUtil.executeQuery(updateOrderQuery);
            if(stateId == 6 || stateId == 7){
                String updateTransportQuery = "UPDATE gp_transporto_busena\n" +
                        "INNER JOIN gp_uzsakymas\n" +
                        "ON gp_transporto_busena.fk_transportas=gp_uzsakymas.fk_transportas\n" +
                        "INNER JOIN gp_uzsakymo_busena\n" +
                        "ON gp_uzsakymas. fk_uzsakymo_busena=gp_uzsakymo_busena.id\n" +
                        "SET gp_transporto_busena.busena=2\n" +
                        "WHERE gp_uzsakymo_busena.id=" + currentState;
                SQLUtil.executeQuery(updateTransportQuery);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
