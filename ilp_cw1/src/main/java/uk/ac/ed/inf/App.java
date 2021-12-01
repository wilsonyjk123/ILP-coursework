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
        //
        long startTime = System.nanoTime();

        if (args.length != 5) {
            throw new IllegalArgumentException("Please input the correct number of arguments");
        }

        var day = args[0];
        var month = args[1];
        var year = args[2];
        var webPort = args[3];
        var databasePort = args[4];

        // prepare for
        Database database = new Database(webPort, databasePort, day, month, year);
        MenuParser menuParser = new MenuParser(webPort);
        DroneMap droneMap = new DroneMap(webPort);
        Drone drone = new Drone(droneMap, menuParser, database, droneMap);


        drone.preparation();
        drone.findPath();
        database.writeDeliveriesTable(drone.orders);
        database.writeFlightPathTable(drone.flightPaths);
        System.out.println(drone.cost);
        droneMap.printRoute(drone.pl, day, month, year);

        // print the total time used
        long endTime = System.nanoTime();
        long totalTime = endTime - startTime;
        System.out.println("Total Time used in seconds: " + totalTime/1000000000.0 + " seconds");
    }

}
