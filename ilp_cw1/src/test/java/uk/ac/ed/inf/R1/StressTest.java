package uk.ac.ed.inf.R1;

import org.junit.Test;
import uk.ac.ed.inf.*;
import uk.ac.ed.inf.Scaffolding.ScaffoldingUtils;

import java.awt.geom.Line2D;
import java.sql.SQLException;
import java.util.ArrayList;

public class StressTest {
    @Test
    public void StressTesting1() throws SQLException {
        ScaffoldingUtils scaffoldingUtils = new ScaffoldingUtils();
        Database database = new Database("9898", "1527", "22", "06", "2023");
        MenuParser menuParser = new MenuParser("9898");
        DroneMap droneMap = new DroneMap("9898");

        // Initiate the drone
        Drone drone = new Drone(database, droneMap);

        // Prepare to initial drone's order information
        DroneUtils.sortOrders(drone.orders);
        DroneUtils.findOrderShopLocations(drone.orders, menuParser);
        DroneUtils.getRouteLongLat(drone.orders, menuParser);

        // find the flight path and record it
        LongLat longLat = new LongLat(-3.1628, 55.9177);
        drone.findPath(15000);

    }

    @Test
    public void StressTesting2() throws SQLException {
        ScaffoldingUtils scaffoldingUtils = new ScaffoldingUtils();
        Database database = new Database("9898", "1527", "22", "06", "2023");
        MenuParser menuParser = new MenuParser("9898");
        DroneMap droneMap = new DroneMap("9898");

        // Initiate the drone
        Drone drone = new Drone(database, droneMap);

        // Prepare to initial drone's order information
        DroneUtils.sortOrders(drone.orders);
        DroneUtils.findOrderShopLocations(drone.orders, menuParser);
        DroneUtils.getRouteLongLat(drone.orders, menuParser);

        // find the flight path and record it
        LongLat longLat = new LongLat(-5.2222, 55.9177);
        drone.findPath(800000);

    }

    @Test
    public void StressTesting3() throws SQLException {
        ScaffoldingUtils scaffoldingUtils = new ScaffoldingUtils();
        Database database = new Database("9898", "1527", "22", "06", "2023");
        MenuParser menuParser = new MenuParser("9898");
        DroneMap droneMap = new DroneMap("9898");

        // Initiate the drone
        Drone drone = new Drone(database, droneMap);

        // Prepare to initial drone's order information
        DroneUtils.sortOrders(drone.orders);
        DroneUtils.findOrderShopLocations(drone.orders, menuParser);
        DroneUtils.getRouteLongLat(drone.orders, menuParser);

        // find the flight path and record it
        LongLat longLat = new LongLat(-0.1628, 55.9177);
        drone.findPath(60000);

    }

    @Test
    public void StressTesting4() throws SQLException {
        ScaffoldingUtils scaffoldingUtils = new ScaffoldingUtils();
        Database database = new Database("9898", "1527", "22", "06", "2023");
        MenuParser menuParser = new MenuParser("9898");
        DroneMap droneMap = new DroneMap("9898");

        // Initiate the drone
        Drone drone = new Drone(database, droneMap);

        // Prepare to initial drone's order information
        DroneUtils.sortOrders(drone.orders);
        DroneUtils.findOrderShopLocations(drone.orders, menuParser);
        DroneUtils.getRouteLongLat(drone.orders, menuParser);

        // find the flight path and record it
        LongLat longLat = new LongLat(-3.1628, 59.9177);
        drone.findPath(80000);

    }
}
