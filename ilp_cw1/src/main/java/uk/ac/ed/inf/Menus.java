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
     * Calculate the total cost of the given strings in menus
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
             getResponse();
             ArrayList<MenusInfo> menusList;
             Type listType = new TypeToken<ArrayList<MenusInfo>>(){}.getType();
             menusList = new Gson().fromJson(this.jsonText,listType);
             for (MenusInfo m: menusList){
                 for(MenusInfo.Item i: m.menu){
                     for(String s:strings){
                         if(i.item.equals(s)){
                             totalCost += i.pence;
                         }

                     }
                 }
             }
         }catch (IllegalArgumentException|NullPointerException e){
             e.printStackTrace();
             System.exit(1);
         }
        return totalCost + 50; // 50 pence for extra delivery fee
     }

    /**
     * Connect to the target server and get the response of the request
     *
     * @throws IOException or InterruptedException If connecting to server is failed or A thread interruption operation has occurred
     */
     public void getResponse(){
         String urlString = getServerAddressToMenus();
         HttpRequest request = HttpRequest.newBuilder().uri(URI.create(urlString)).build();
         try{
             HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
             if(response.statusCode() == 200){
                 this.jsonText = response.body();
             }else{
                 System.out.println("Connection Failed with Response Code: "+response.statusCode());
                 System.exit(1);
             }
         }catch(IOException | InterruptedException e){
             e.printStackTrace();
             System.exit(1);
         }
     }
}
