package uk.ac.ed.inf;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;

public class Menus {
    String machineFirstName = "";
    String port = "";
    private static final HttpClient client = HttpClient.newHttpClient();


    /*Constructor*/
    Menus(String machineFirstName, String port){
        this.machineFirstName = machineFirstName;
        this.port = port;
    }

    /*Returns the int cost in pence of having all of these items delivered by drone*/
     public int getDeliveryCost(String... items) throws IOException, InterruptedException {
         /*Set the urlString*/
         String urlString = "http://"+machineFirstName+":"+port+"/menus/menus.json";

         /*Create a request*/
         HttpRequest request = HttpRequest.newBuilder().uri(URI.create(urlString)).build();

         /*Create a response to receive data*/
         HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
         return items.length * 50;
     }

}
