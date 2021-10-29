package uk.ac.ed.inf;

import java.sql.SQLException;


/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws SQLException {
        Database database = new Database("1527");
        DroneMap map = new DroneMap("9898");
        MenuParser menuParser = new MenuParser("9898");
        
        database.readOrderDetailsFromDatabase();
        Drone drone = new Drone(map,menuParser,database);
        drone.nextMove();
    }
}
