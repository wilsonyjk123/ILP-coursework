package uk.ac.ed.inf;
import com.google.gson.Gson;
import com.google.gson.JsonElement;

import java.awt.*;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.ArrayList;
import java.util.List;
import java.lang.reflect.Type;
import com.google.gson.reflect.TypeToken;

public class Menus {
    private String machineFirstName = "";
    private String port = "";
    private String jsonText = "";
    private static final HttpClient client = HttpClient.newHttpClient();

    /*Constructor*/
    Menus(String machineFirstName, String port){
        this.machineFirstName = machineFirstName;
        this.port = port;
    }

    public static class MenusInfo{
        String name;
        String location;
        List<Item> menu;
        public static class Item{
            String item;
            int pence;
        }
    }
    public String getJsonText(){
        return this.jsonText;
    }
    public String getServerAddress(){
        return "http://" + machineFirstName + ":" + port;
    }
    /*Returns the int cost in pence of having all of these items delivered by drone*/
     public int getDeliveryCost(String... str){
         int cost = 0;
         try{
             getResponse();
             ArrayList<MenusInfo> menusList;
             Type listType = new TypeToken<ArrayList<MenusInfo>>(){}.getType();
             menusList = new Gson().fromJson(this.jsonText,listType);
             for (MenusInfo m: menusList){
                 for(MenusInfo.Item i: m.menu){
                     for(String s:str){
                         if(i.item.equals(s)){
                             cost += i.pence;
                         }

                     }
                 }
             }
         }catch (Exception e){
             e.printStackTrace();
             System.exit(1);
         }
        return cost+50; // 50p for delivery fee
     }

     public String setUrlStringToMenus(){
         /*Set the urlString*/
         return "http://" + machineFirstName + ":" + port+"/menus/menus.json";
     }

     public HttpRequest buildRequest(){
         /*Create a request*/
         String urlString = setUrlStringToMenus();
         return HttpRequest.newBuilder().uri(URI.create(urlString)).build();
     }

     public void getResponse(){
         HttpRequest request = buildRequest();
         try{
             HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
             if(response.statusCode() == 200){
                 this.jsonText = response.body();
             }else{
                 System.out.println("Connection Failed with Response Code: "+response.statusCode());
                 System.exit(1);
             }
         }catch(IOException e){
             e.printStackTrace();
             System.out.println("Connection Failed: IOException");
         }catch(InterruptedException e){
             e.printStackTrace();
             System.out.println("Connection Failed: InterruptedException");
         }
     }
}
