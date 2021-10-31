package uk.ac.ed.inf;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Database {
    String webPort;
    String dataBasePort;
    String day;
    String month;
    String year;
    String dateString;
    MenuParser menuParser;

    // Constructor
    Database(String webPort,String dataBasePort, String day, String month, String year){
        this.dataBasePort = dataBasePort;
        this.day = day;
        this.month = month;
        this.year = year;
        this.dateString = this.year + "-" + this.month + "-" + this.day;
        this.menuParser = new MenuParser(webPort);
        menuParser.parseMenus();
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
            String orderNo = rs.getString("orderNo");
            String delivery = rs.getString("deliveryDate");
            String cus = rs.getString("customer");
            String deliverT = rs.getString("deliverTo");
            ArrayList<String> items = readOrderDetailsFromDatabase(orderNo);
            int price = getDeliveryCost(items);
            Order order = new Order(orderNo,delivery,cus,deliverT,items,price);
            orders.add(order);
            //System.out.println("orders table INFO: " + order + "||" + delivery + "||" + cus + "||" + deliverT);
        }
        return orders;
    }

    public ArrayList<String> readOrderDetailsFromDatabase(String orderNo) throws SQLException {
        final String orderDetailsQuery = "select * from orderDetails where orderNo=(?)";
        Connection conn = DriverManager.getConnection(getJDBCString());
        PreparedStatement psOrdersQuery = conn.prepareStatement(orderDetailsQuery);
        psOrdersQuery.setString(1,orderNo);
        ResultSet rs = psOrdersQuery.executeQuery();
        ArrayList<String> str = new ArrayList<>();
        while (rs.next()){
            String order = rs.getString("orderNo");
            String it = rs.getString("item");
            str.add(it);
            //System.out.println("orders table INFO: " + order + "||" + it);
        }
        //System.out.println(str);
        return str;
    }

    public Integer getDeliveryCost(ArrayList<String> strings){
        int totalCost = 0;
        ArrayList<MenuParser.Menu> menusList = menuParser.parseMenus();
        try{
            for (MenuParser.Menu mi: menusList){
                for(MenuParser.Menu.Item i: mi.menu){
                    for(String s:strings){
                        if(i.item.equals(s)){
                            totalCost += i.pence;
                        }

                    }
                }
            }
        }catch (IllegalArgumentException | NullPointerException e){
            e.printStackTrace();
            System.exit(1); // Unsuccessful termination
        }
        return totalCost + 50; // 50 pence for extra delivery fee
    }

}
