package uk.ac.ed.inf;
import com.google.gson.Gson;
import com.google.gson.JsonElement;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.ArrayList;
import java.util.List;

public class Menus {
    String machineFirstName = "";
    String port = "";
    String jsonText = "";
    private static final HttpClient client = HttpClient.newHttpClient();


    /*Constructor*/
    Menus(String machineFirstName, String port){
        this.machineFirstName = machineFirstName;
        this.port = port;
    }

    /*Returns the int cost in pence of having all of these items delivered by drone*/
     public int getDeliveryCost(String... items){
         getResponse();
         MenusInfo menusInfo = new Gson().fromJson(this.jsonText, (Type) MenusInfo.class);
         List<item> list = new ArrayList<item>();
         return 1;
     }
     public String setUrlString(){
         /*Set the urlString*/
         String urlString = "http://" + machineFirstName + ":" + port+"/menus/menus.json";
         return urlString;
     }
     public HttpRequest buildRequest(){
         /*Create a request*/
         String urlString = setUrlString();
         HttpRequest request = HttpRequest.newBuilder().uri(URI.create(urlString)).build();
         return request;
     }

     public void getResponse(){
         HttpRequest request = buildRequest();
         try{
             HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
             if(response.statusCode() == 200){
                 this.jsonText = response.body();
             }else{
                 System.out.println("Connection Failed with Response Code: "+response.statusCode());
             }
         }catch(IOException e){
             e.printStackTrace();
             System.out.println("Connection Failed: IOException");
         }catch(InterruptedException e){
             e.printStackTrace();
             System.out.println("Connection Failed: InterruptedException");
         }
     }


     public class MenusInfo{
         String name;
         String location;
         List<item> menu;
     }
     public class item{
        String item;
        int pence;
     }
}
