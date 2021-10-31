package uk.ac.ed.inf;
import java.sql.SQLException;
import java.util.*;
import java.awt.*;

public class Drone {
    // Fields
    DroneMap map;
    MenuParser menuParser;
    Database database;
    DroneMap droneMap;
    WordParser wordParser;
    LongLat apt = new LongLat(droneMap.getATLong(),droneMap.getATLat());
    LongLat startPosition;
    LongLat currentPosition;
    LongLat nextPosition;
    LongLat targetPosition;
    ArrayList<Order> orders;
    private int droneBattery = 1500;


    // Class Constructor
    Drone(DroneMap map, MenuParser menuParser, Database database, DroneMap droneMap) throws SQLException {
        this.map = map;
        this.menuParser = menuParser; // get the location of stores
        this.database = database; // connect to database and get order details
        this.droneMap = droneMap;
        this.orders = database.readOrdersFromDatabase();
    }
    public void nextMove() throws SQLException {

    }

    public void sortOrders() throws SQLException {
        Comparator<Order> c = Collections.reverseOrder();
        orders.sort(c);
    }
    public void findOrderShopLocations(){
        for(int i = 0;i<orders.size();i++){
            orders.get(i).orderShopLocations = new ArrayList<>();
            for(int j = 0;j<orders.get(i).item.size();j++){
                String name = orders.get(i).item.get(j);
                ArrayList<MenuParser.Menu> menusList = menuParser.parseMenus();
                try{
                    for (MenuParser.Menu mi: menusList){
                        for(MenuParser.Menu.Item k: mi.menu){
                            if(k.item.equals(name)){
                                if(orders.get(i).orderShopLocations.contains(mi.location)){
                                }else {
                                    orders.get(i).orderShopLocations.add(mi.location);
                                }
                            }
                        }
                    }
                }catch (IllegalArgumentException | NullPointerException e){
                    e.printStackTrace();
                    System.exit(1); // Unsuccessful termination
                }
            }
        }
    }

    public void getShopLongLat(){
        for (int i =0;i<orders.size();i++){
            orders.get(i).shopLongLats = new ArrayList<>();
            for(int j =0;j<orders.get(i).orderShopLocations.size();j++){
                String threeWord = orders.get(i).orderShopLocations.get(j);
                WordParser wordParser = new WordParser(menuParser.webPort);
                WordParser.Word word = wordParser.parseWord(threeWord);
                double lng = word.coordinates.lng;
                double lat = word.coordinates.lat;
                LongLat longLat = new LongLat(lng,lat);
                orders.get(i).shopLongLats.add(longLat);
            }
        }
    }

    public void getDeliverToLongLat(){
        for (Order order : orders) {
            String deliverTo = order.deliverTo;
            WordParser wordParser = new WordParser(menuParser.webPort);
            WordParser.Word word = wordParser.parseWord(deliverTo);
            double lng = word.coordinates.lng;
            double lat = word.coordinates.lat;
            order.deliverToLongLat = new LongLat(lng, lat);
        }
    }
    public void preparation() throws SQLException {
        sortOrders();
        findOrderShopLocations();
        getShopLongLat();
        getDeliverToLongLat();
    }
}
