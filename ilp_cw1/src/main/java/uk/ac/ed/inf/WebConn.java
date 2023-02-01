package uk.ac.ed.inf;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;

public class WebConn {
    // field
    public String webPort;
    private static final HttpClient client = HttpClient.newHttpClient();

    // Constructor
    WebConn(String webPort){
        this.webPort = webPort;
    }

    /**
     * Get the url stirng for parsing the Menu
     *
     * @return the url stirng for parsing the Menu
     * */
    public String getURLStringForMenus(){
        return "http://localhost:" + webPort + "/menus/menus.json";
    }

    /**
     * Get the url string for accessing three-word location on the website
     *
     * @param threeWordLocation - A string that contains the three-word of a location
     * @return the url string for accessing three-word location on the website
     * */
    public String getURLStringForThreeWordsLocation(String threeWordLocation){
        String[] wordList = threeWordLocation.split("[.]");
        String threeWordURL = "/words/" + wordList[0] + "/" + wordList[1] + "/" + wordList[2] + "/details.json";
        return "http://localhost:" + webPort + threeWordURL;
    }

    /**
     * Get the HttpRequest
     *
     * @param urlString - the input url string
     * @return the HttpRequest of the given input url string
     */
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

    /**
     * Get the HttpResponse
     *
     * @param request - the input request
     * @return the HttpResponse of the given input request
     * */
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
