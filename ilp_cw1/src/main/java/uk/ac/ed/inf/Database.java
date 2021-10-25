package uk.ac.ed.inf;

import java.sql.*;
import java.util.ArrayList;

public class Database {
    String dataBasePort;
    final String ordersQuery = "select * from orders";
    ArrayList<String> orderNo = new ArrayList<>();
    ArrayList<String> deliveryDate = new ArrayList<>();
    ArrayList<String> customer = new ArrayList<>();
    ArrayList<String> deliveryTo = new ArrayList<>();

    // Constructor
    Database(String dataBasePort){
        this.dataBasePort = dataBasePort;
    }

    // Methods
    public String getJDBCString(){
        return "jdbc:derby://localhost:" + dataBasePort + "/derbyDB";
    }

    public void readDataFromDatabase(String jdbcString) throws SQLException {
        Connection conn = DriverManager.getConnection(getJDBCString());
        PreparedStatement psOrdersQuery = conn.prepareStatement(ordersQuery);
        ResultSet rs = psOrdersQuery.executeQuery();
        while (rs.next()) {
            String order = rs.getString("orderNo");
            orderNo.add(order);
            String delivery = rs.getString("deliveryDate");
            deliveryDate.add(delivery);
            String cus = rs.getString("customer");
            customer.add(cus);
            String deliverT = rs.getString("deliverTo");
            deliveryTo.add(deliverT);
            System.out.println("orders table INFO: " + order + delivery + cus + deliverT);
        }
    }
}
