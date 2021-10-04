package uk.ac.ed.inf;
import com.google.gson.Gson;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.reflect.TypeToken;

public class Menus {
    private String machineFirstName = "";
    private String port = "";
    private String jsonText = "";

    // Initiate a HttpClient when an object is created
    private static final HttpClient client = HttpClient.newHttpClient();

    /**
    * Menus constructor
    *
    * @param machineFirstName   - Server IP: {@code localhost}
    * @param port               - The port that user needs to connect to the server
    */
    Menus(String machineFirstName, String port){
        this.machineFirstName = machineFirstName;
        this.port = port;
    }

    /**
     * Inner class to access the parsed-data in json form
     */
    public static class MenusInfo{
        String name;
        String location;
        List<Item> menu;
        public static class Item{
            String item;
            int pence;
        }
    }

    // Getters
    public String getJsonText(){ return this.jsonText; }

    public String getServerAddressToMenus(){ return "http://" + machineFirstName + ":" + port+"/menus/menus.json"; }

    // Methods
    /**
     * Calculate the total cost of the given strings
     *
     * @param strings - A list of strings concern the dishes in the menus
     * @throws IllegalArgumentException If strings is not given as the correct type
     * @throws IllegalArgumentException if strings has no visible content
     * @throws NullPointerException If strings is null
     * @return the total cost of dishes cost and extra delivery fee
     * */
     public int getDeliveryCost(String... strings){
         // Initiate a counter to calculate the total cost
         int totalCost = 0;
         try{
             // Get the response body into jsonText field
             getResponse();

             // Parsing the text in json form and create an object of the corresponding inner class to access the attributes
             Type listType = new TypeToken<ArrayList<MenusInfo>>(){}.getType();
             ArrayList<MenusInfo> menusList = new Gson().fromJson(this.jsonText,listType);

             // Iterate each MenusInfo object in the menuList
             for (MenusInfo mi: menusList){

                 // Iterate each item object in the menu
                 for(MenusInfo.Item i: mi.menu){

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

    /**
     * Connect to the target server and get the response of the request
     *
     * @throws  IOException or InterruptedException If a send request is failed
     */
     public void getResponse(){
         // Set the urlString by using the getter
         String urlString = getServerAddressToMenus();

         // Create a GET request with the urlString
         HttpRequest request = HttpRequest.newBuilder().uri(URI.create(urlString)).build();

         try{
             // Send the request to the target server
             HttpResponse<String> response = client.send(request, BodyHandlers.ofString());

             // If the status code of the response is 200, then we can use response.body()
             if(response.statusCode() == 200){
                 this.jsonText = response.body();
             }else{
                 System.out.println("Connection Failed with Response Code: " + response.statusCode());
                 System.exit(1); // Unsuccessful termination
             }
         }catch(IOException | InterruptedException e){
             e.printStackTrace();
             System.exit(1); // Unsuccessful termination
         }
     }
}
