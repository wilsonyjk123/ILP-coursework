package uk.ac.ed.inf;
 import com.google.gson.Gson;
 import com.google.gson.reflect.TypeToken;

 import java.lang.reflect.Type;
 import java.sql.SQLException;
 import java.util.*;

public class Drone {
    // Fields
    DroneMap map;
    MenuParser menuParser;
    Database database;
    WordParser wordParser;
    LongLat startPosition;
    LongLat currentPosition;
    LongLat nextPosition;
    private static final int droneBattery = 1500;
    ArrayList<Integer> costs = new ArrayList<>();
    Map<String, Integer> dict = new HashMap<>();


    // Class Constructor
    Drone(DroneMap map, MenuParser menuParser, Database database){
        this.map = map;
        this.menuParser = menuParser; // get the location of stores
        this.database = database; // connect to database and get order details
    }
    public void nextMove() throws SQLException {
        // 订单排序
    }

    public int getDeliveryCost(ArrayList<String> strings){
        // Initiate a counter to calculate the total cost
        int totalCost = 0;
        ArrayList<MenuParser.Menu> menusList = menuParser.parseMenus();
        try{
            // Iterate each MenusInfo object incmdc the menuList
            for (MenuParser.Menu mi: menusList){

                // Iterate each item object in the menu
                for(MenuParser.Menu.Item i: mi.menu){

                    // Iterate each string in the given parameter - strings
                    for(String s:strings){

                        // Compare each of these two strings
                        if(i.item.equals(s)){
                            // Add the money to the counter if the two strings correspond
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
