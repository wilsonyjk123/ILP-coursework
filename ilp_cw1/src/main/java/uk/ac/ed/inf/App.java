package uk.ac.ed.inf;
import java.io.IOException;
import java.net.Inet4Address;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws SQLException, IOException {
        long startTime = System.nanoTime();

//        if (args.length != 5) {
//            throw new IllegalArgumentException("Please input the correct number of arguments");
//        }

        var day = "12";
        var month = "9";
        var year = "2023";
        var webPort = "9898";
        var databasePort = "1527";
        int monthCheck = Integer.parseInt(month);
        if(monthCheck<10){
            month = "0"+month;
        }

        Database database = new Database(webPort,databasePort,day,month, year);
        MenuParser menuParser = new MenuParser(webPort);
        DroneMap droneMap = new DroneMap(webPort);
        Drone drone = new Drone(droneMap,menuParser, database,droneMap);
        drone.preparation();
        drone.findPath();
        database.writeDeliveriesTable(drone.orders);
        database.writeFlightPathTable(drone.flightPaths);
        droneMap.printRoute(drone.pl,day,month,year);
    }
}
