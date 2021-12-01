package uk.ac.ed.inf;
import java.sql.*;
import java.util.ArrayList;

public class Database {
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
            String orderNo = rs.getString("orderNo");
            String delivery = rs.getString("deliveryDate");
            String cus = rs.getString("customer");
            String deliverT = rs.getString("deliverTo");
            ArrayList<String> items = readOrderDetailsFromDatabase(orderNo);
            int price = getDeliveryCost(items);
            Order order = new Order(orderNo,delivery,cus,deliverT,items,price);
            orders.add(order);
        }
        return orders;
    }

    public void writeDeliveriesTable(ArrayList<Order> orders) throws SQLException{
        //TODO We use the DatabaseMetaData to see if it has a students table.
        Connection conn = DriverManager.getConnection(getJDBCString());
        Statement statement = conn.createStatement();
        DatabaseMetaData databaseMetadata = conn.getMetaData();
        // Note: must capitalise STUDENTS in the call to getTables
        ResultSet resultSet =
                databaseMetadata.getTables(null, null, "DELIVERIES", null);
        // If the resultSet is not empty then the table exists, so we can drop it
        if (resultSet.next()) {
            statement.execute("drop table deliveries");
        }
        statement.execute("create table deliveries("+
                "orderNo char(8),"+
                "deliveredTo varchar(19)," +
                "costInPence int)");
        PreparedStatement psDeliveries = conn.prepareStatement(
                "insert into deliveries values (?, ?, ?)");
        for (Order order : orders) {
            if(order.getIsDelivered()){
                psDeliveries.setString(1, order.getOrderNo());
                psDeliveries.setString(2, order.getDeliverTo());
                psDeliveries.setInt(3, order.getPrice());
                psDeliveries.execute();
            }

        }

        resultSet =
                databaseMetadata.getTables(null, null, "FLIGHTPATH", null);
        // If the resultSet is not empty then the table exists, so we can drop it
        if (resultSet.next()) {
            statement.execute("drop table flightpath");
        }
        statement.execute("create table flightpath(orderNo char(8)," +
                "fromLongitude double," +
                "fromLatitude double," +
                "angle integer," +
                "toLongitude double," +
                "toLatitude double)");
        PreparedStatement psFlightpath = conn.prepareStatement(
                "insert into flightpath values (?, ?, ?, ?, ?, ?)");
        for (Order order : orders) {
            if(order.getIsDelivered()){
                psDeliveries.setString(1, order.getOrderNo());
                psDeliveries.setDouble(2, );
                psDeliveries.setDouble(3, );
                psDeliveries.setInt(4,);
                psDeliveries.setDouble(5, );
                psDeliveries.setDouble(6, );
                psDeliveries.execute();
            }

        }


    }

    public ArrayList<String> readOrderDetailsFromDatabase(String orderNo) throws SQLException {
        final String orderDetailsQuery = "select * from orderDetails where orderNo=(?)";
        Connection conn = DriverManager.getConnection(getJDBCString());
        PreparedStatement psOrdersQuery = conn.prepareStatement(orderDetailsQuery);
        psOrdersQuery.setString(1,orderNo);
        ResultSet rs = psOrdersQuery.executeQuery();
        ArrayList<String> str = new ArrayList<>();
        while (rs.next()){
            String order  = rs.getString("orderNo");
            String it = rs.getString("item");
            str.add(it);
        }
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
