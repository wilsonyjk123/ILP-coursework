package uk.ac.ed.inf;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Database {
    String dataBasePort;
    String day;
    String month;
    String year;
    String dateString;
    ArrayList<String> orderNoInOrders = new ArrayList<>();
    ArrayList<String> orderNoInOrderDetails = new ArrayList<>();
    ArrayList<String> deliveryDate = new ArrayList<>();
    ArrayList<String> customer = new ArrayList<>();
    ArrayList<String> deliveryTo = new ArrayList<>();
    ArrayList<String> item = new ArrayList<>();

    // Constructor
    Database(String dataBasePort, String day, String month, String year){
        this.dataBasePort = dataBasePort;
        this.day = day;
        this.month = month;
        this.year = year;
        this.dateString = this.year + "-" + this.month + "-" + this.day;
    }

    // Methods
    public String getJDBCString(){
        return "jdbc:derby://localhost:" + dataBasePort + "/derbyDB";
    }

    public ArrayList<Order> readOrdersFromDatabase() throws SQLException {
        ArrayList<Order> orders = new ArrayList<>();
        final String ordersQuery = "select * from orders where deliveryDate='" + dateString + "'";
        Connection conn = DriverManager.getConnection(getJDBCString());
        PreparedStatement psOrdersQuery = conn.prepareStatement(ordersQuery);
        ResultSet rs = psOrdersQuery.executeQuery();
        while (rs.next()) {
            //TODO 数据库每读一行建立一个order对象
            String order = rs.getString("orderNo");
            orderNoInOrders.add(order);
            String delivery = rs.getString("deliveryDate");
            deliveryDate.add(delivery);
            String cus = rs.getString("customer");
            customer.add(cus);
            String deliverT = rs.getString("deliverTo");
            deliveryTo.add(deliverT);
            System.out.println("orders table INFO: " + order + "||" + delivery + "||" + cus + "||" + deliverT);
        }
        return orders;
    }

    public void readOrderDetailsFromDatabase() throws SQLException {
        final String orderDetailsQuery = "select * from orderDetails";
        Connection conn = DriverManager.getConnection(getJDBCString());
        PreparedStatement psOrdersQuery = conn.prepareStatement(orderDetailsQuery);
        ResultSet rs = psOrdersQuery.executeQuery();
        while (rs.next()){
            String order = rs.getString("orderNo");
            orderNoInOrderDetails.add(order);
            String it = rs.getString("item");
            item.add(it);
            //System.out.println("orders table INFO: " + order + "||" + it);
        }
//        for(int i = 0;i<10;i++){
//            System.out.println(orderNoInOrderDetails.get(i));
//        }
//        for(int i = 0;i<10;i++){
//            System.out.println(item.get(i));
//        }

    }

}
