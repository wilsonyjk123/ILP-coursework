package uk.ac.ed.inf;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Database {
    String dataBasePort;
    final String ordersQuery = "select * from orders";
    final String orderDetailsQuery = "select * from orderDetails";
    ArrayList<String> orderNoInOrders = new ArrayList<>();
    ArrayList<String> orderNoInOrderDetails = new ArrayList<>();
    ArrayList<String> deliveryDate = new ArrayList<>();
    ArrayList<String> customer = new ArrayList<>();
    ArrayList<String> deliveryTo = new ArrayList<>();
    ArrayList<String> item = new ArrayList<>();
    Map<String, String> deliveryThreeWordAddress = new HashMap<>();
    Map<String, ArrayList<String>> menusRespectToOrderNo = new HashMap<>();
    ArrayList<String> buffer = new ArrayList<>();

    // Constructor
    Database(String dataBasePort){
        this.dataBasePort = dataBasePort;
    }

    // Methods
    public String getJDBCString(){
        return "jdbc:derby://localhost:" + dataBasePort + "/derbyDB";
    }

    public void readOrdersFromDatabase() throws SQLException {
        Connection conn = DriverManager.getConnection(getJDBCString());
        PreparedStatement psOrdersQuery = conn.prepareStatement(ordersQuery);
        ResultSet rs = psOrdersQuery.executeQuery();
        while (rs.next()) {
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
        for(int i = 0;i<orderNoInOrders.size();i++){
            deliveryThreeWordAddress.put(orderNoInOrders.get(i),deliveryTo.get(i));
        }

    }

    public void readOrderDetailsFromDatabase() throws SQLException {
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
        for(int i = 0;i<orderNoInOrderDetails.size();i++){
            if(menusRespectToOrderNo.containsKey(orderNoInOrderDetails.get(i))){
                //System.out.println(orderNoInOrderDetails.get(i));
                //buffer.add(item.get(i));
                menusRespectToOrderNo.get(orderNoInOrderDetails.get(i)).add(item.get(i));
                //menusRespectToOrderNo.put(orderNoInOrderDetails.get(i),item.get(i));
                //System.out.println(menusRespectToOrderNo);
            }else{
                //System.out.println(orderNoInOrderDetails.get(i));
                ArrayList<String> strs = new ArrayList<>();
                strs.add(item.get(i));
                menusRespectToOrderNo.put(orderNoInOrderDetails.get(i),strs);
                //System.out.println(menusRespectToOrderNo);
            }
        }
        System.out.println(menusRespectToOrderNo);
    }

}
