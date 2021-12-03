package uk.ac.ed.inf;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class MenuParser {
    // Fields
    String jsonText;
    String webPort;
    MenuParser(String webPort){
        this.webPort = webPort;
    }
    ArrayList<Menu> menuList = null;

    // An inner class to parse the JSON file
    public static class Menu {
        String name;
        String location;
        List<Item> menu;
        public static class Item {
            String item;
            int pence;
        }
    }

    /**
     * parse the menus on the website
     *
     * @return a list of Menu object that we defined as inner class Menu
     * @throws Exception if the Http connection is not failed
     * */
    public ArrayList<Menu> parseMenus (){
        WebConn webConn = new WebConn(webPort);
        try{
            HttpResponse<String> response = webConn.createResponse(webConn.createRequest(webConn.getURLStringForMenus()));
            if(response.statusCode() == 200){
                this.jsonText = response.body();
            }else{
                System.out.println("Connection Failed with Response Code: " + response.statusCode());
                System.exit(1);
            }
        }catch (Exception e){
            e.printStackTrace();
            System.exit(1);
        }
        Type listType = new TypeToken<ArrayList<Menu>>(){}.getType();
        menuList = new Gson().fromJson(this.jsonText, listType);
        return menuList;
    }

    /**
     * Get the price of a list of strings according to the pence attributes in the static class Menu
     *
     * @param strings - a list of strings that shows the order's contents
     * @return an Integer-type price by the given list of string
     * @throws IllegalArgumentException if the argument is not correct
     * @throws NullPointerException if the reference of the given string is null
     * */
    public Integer getDeliveryCost(ArrayList<String> strings){
        int totalCost = 0;
        ArrayList<MenuParser.Menu> menusList = parseMenus();
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