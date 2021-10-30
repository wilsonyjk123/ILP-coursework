package uk.ac.ed.inf;
import java.sql.SQLException;
import java.util.*;
import java.awt.*;

public class Drone {
    // Fields
    DroneMap map;
    MenuParser menuParser;
    Database database;
    WordParser wordParser;
    LongLat startPosition;
    LongLat currentPosition;
    LongLat nextPosition;
    ArrayList<Order> orders;
    private static final int droneBattery = 1500;


    // Class Constructor
    Drone(DroneMap map, MenuParser menuParser, Database database) throws SQLException {
        this.map = map;
        this.menuParser = menuParser; // get the location of stores
        this.database = database; // connect to database and get order details
        this.orders = database.readOrdersFromDatabase();
    }
    public void nextMove() throws SQLException {

    }

    public void sortOrders() throws SQLException {
        Comparator<Order> c = Collections.reverseOrder();
        for(int i =0;i<orders.size();i++){
            //System.out.println(orders.get(i).price);
        }
        orders.sort(c);
        for(int i =0;i<orders.size();i++){
            //System.out.println(orders.get(i).price);
        }
    }
    public void findOrderShopLocations(){
        for(int i = 0;i<orders.size();i++){
            System.out.println(orders.get(i));//
            for(int j = 0;j<orders.get(i).item.size();j++){
                String name = orders.get(i).item.get(j);
                System.out.println(name);//
                ArrayList<MenuParser.Menu> menusList = menuParser.parseMenus();
                try{
                    for (MenuParser.Menu mi: menusList){
                        for(MenuParser.Menu.Item k: mi.menu){
                            if(k.item.equals(name)){
                                ArrayList<String> shop = orders.get(i).orderShopLocations;
                                System.out.println(mi.location);//
                                System.out.println(shop);//
                            }
                        }
                    }
                }catch (IllegalArgumentException | NullPointerException e){
                    e.printStackTrace();
                    System.exit(1); // Unsuccessful termination
                }
            }
        }
        for(int i = 0;i<orders.size();i++){
            //System.out.println(orders.get(i).orderShopLocations);
        }
    }
}
