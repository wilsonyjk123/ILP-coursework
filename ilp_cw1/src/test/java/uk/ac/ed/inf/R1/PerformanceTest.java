package uk.ac.ed.inf.R1;

import org.junit.Test;
import uk.ac.ed.inf.*;
import uk.ac.ed.inf.Scaffolding.ScaffoldingUtils;

import java.awt.geom.Line2D;
import java.sql.SQLException;
import java.util.ArrayList;

public class PerformanceTest {
    @Test
    public void performanceTest1() throws SQLException {
        ScaffoldingUtils scaffoldingUtils = new ScaffoldingUtils();
        Database database = new Database("9898", "1527", "03", "11", "2023");
        MenuParser menuParser = new MenuParser("9898");
        DroneMap droneMap = new DroneMap("9898");

        // Initiate the drone
        Drone drone = new Drone( database, droneMap);
        long start = System.currentTimeMillis();
        // Prepare to initial drone's order information
        DroneUtils.sortOrders(drone.orders);
        DroneUtils.findOrderShopLocations(drone.orders,menuParser);
        DroneUtils.getRouteLongLat(drone.orders,menuParser);

        // find the flight path and record it
        drone.findPath(2000);
        long end = System.currentTimeMillis();
        long responseTime = end - start;
        assert(responseTime<10000);
    }

    @Test
    public void performanceTest2() throws SQLException {
        ScaffoldingUtils scaffoldingUtils = new ScaffoldingUtils();
        Database database = new Database("9898", "1527", "23", "12", "2023");
        MenuParser menuParser = new MenuParser("9898");
        DroneMap droneMap = new DroneMap("9898");

        // Initiate the drone
        Drone drone = new Drone( database, droneMap);
        long start = System.currentTimeMillis();
        // Prepare to initial drone's order information
        DroneUtils.sortOrders(drone.orders);
        DroneUtils.findOrderShopLocations(drone.orders,menuParser);
        DroneUtils.getRouteLongLat(drone.orders,menuParser);

        // find the flight path and record it
        drone.findPath(2000);
        long end = System.currentTimeMillis();
        long responseTime = end - start;
        assert(responseTime<6000);
    }
}
