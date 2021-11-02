package uk.ac.ed.inf;
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
    public static void main( String[] args ) throws SQLException {
//        String day = args[0];
//        String month = args[1];
//        String year = args[2];
//        String webPort = args[3];
//        String databasePort = args[4];
        Database database = new Database("9898","1527","14","8", "2022");
        MenuParser menuParser = new MenuParser("9898");
        DroneMap droneMap = new DroneMap("9898");
        Drone drone = new Drone(droneMap,menuParser, database,droneMap);
        drone.preparation();
        drone.Move();
        System.out.println(drone.printRoute());



    }
}
