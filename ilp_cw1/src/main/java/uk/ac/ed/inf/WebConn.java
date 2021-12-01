package uk.ac.ed.inf;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;

public class WebConn {

    String webPort;

    WebConn(String webPort){
        this.webPort = webPort;
    }

    private static final HttpClient client = HttpClient.newHttpClient();
    // Create URL strings for accessing web server
    public String getURLStringForMenus(){
        return "http://localhost:" + webPort + "/menus/menus.json";
    }

    public String getURLStringForThreeWordsLocation(String threeWordLocation){
        String[] wordList = threeWordLocation.split("[.]");
        String threeWordURL = "/words/" + wordList[0] + "/" + wordList[1] + "/" + wordList[2] + "/details.json";
        return "http://localhost:" + webPort + threeWordURL;
    }

    // Methods for constructing web server connection
    public HttpRequest createRequest(String urlString){
        HttpRequest request = null;
        try{
            request = HttpRequest.newBuilder().uri(URI.create(urlString)).build();
        }catch (Exception e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }
        return request;
    }

    public HttpResponse<String> createResponse(HttpRequest request){
        HttpResponse<String> response = null;
        try{
            response = client.send(request, BodyHandlers.ofString());
        }catch (Exception e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }
        return response;
    }

}
