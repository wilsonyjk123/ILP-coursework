package uk.ac.ed.inf;

import javax.xml.crypto.Data;
import java.net.http.HttpResponse;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws SQLException {
//        WebConn connect = new WebConn("9898");
//        String URL = connect.getURLStringForNoFlyZones();
//        HttpResponse<String> response = connect.createResponse(connect.createRequest(URL));
//        String jsonText = response.body();
//        System.out.println(jsonText);



//        PreparedStatement psCourseQuery = conn.prepareStatement(coursesQuery);
//        psCourseQuery.setString(1, "s1967890");
        Database database = new Database("1527");
        database.readDataFromDatabase(database.getJDBCString());
        Map map = new Map("9898");
        map.connection();


    }
}
