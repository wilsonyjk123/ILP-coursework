package uk.ac.ed.inf;

import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.net.MulticastSocket;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        Menus menus = new Menus("localhost", "9898");
        menus.getResponse();
        String a = menus.getJsonText();
        System.out.println(a);
    }
}
