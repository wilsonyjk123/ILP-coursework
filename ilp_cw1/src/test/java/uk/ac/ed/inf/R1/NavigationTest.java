package uk.ac.ed.inf.R1;
import uk.ac.ed.inf.*;
import org.junit.Test;
import uk.ac.ed.inf.Scaffolding.ScaffoldingUtils;

import java.awt.geom.Line2D;
import java.sql.SQLException;
import java.util.ArrayList;

public class NavigationTest {
    @Test
    public void navigationTest1() throws SQLException {
        ScaffoldingUtils scaffoldingUtils = new ScaffoldingUtils();
        Database database = new Database("9898", "1527", "22", "06", "2023");
        MenuParser menuParser = new MenuParser("9898");
        DroneMap droneMap = new DroneMap("9898");

        // Initiate the drone
        Drone drone = new Drone( database, droneMap);

        // Prepare to initial drone's order information
        DroneUtils.sortOrders(drone.orders);
        DroneUtils.findOrderShopLocations(drone.orders,menuParser);
        DroneUtils.getRouteLongLat(drone.orders,menuParser);

        // find the flight path and record it
        drone.findPath(1200);
        ArrayList<FlightPath> result = drone.flightPaths;
        ArrayList<Line2D> line2DS = scaffoldingUtils.ToLine2D(result);
        boolean isCrossed = scaffoldingUtils.LinesCrossForTest(line2DS);
        assert isCrossed == Boolean.FALSE;
    }

    @Test
    public void navigationTest2() throws SQLException {
        ScaffoldingUtils scaffoldingUtils = new ScaffoldingUtils();
        Database database = new Database("9898", "1527", "22", "12", "2023");
        MenuParser menuParser = new MenuParser("9898");
        DroneMap droneMap = new DroneMap("9898");

        // Initiate the drone
        Drone drone = new Drone( database, droneMap);

        // Prepare to initial drone's order information
        DroneUtils.sortOrders(drone.orders);
        DroneUtils.findOrderShopLocations(drone.orders,menuParser);
        DroneUtils.getRouteLongLat(drone.orders,menuParser);

        // find the flight path and record it
        drone.findPath(1000);
        ArrayList<FlightPath> result = drone.flightPaths;
        ArrayList<Line2D> line2DS = scaffoldingUtils.ToLine2D(result);
        boolean isCrossed = scaffoldingUtils.LinesCrossForTest(line2DS);
        assert isCrossed == Boolean.FALSE;
    }

    @Test
    public void navigationTest3() throws SQLException {
        ScaffoldingUtils scaffoldingUtils = new ScaffoldingUtils();
        Database database = new Database("9898", "1527", "31", "12", "2022");
        MenuParser menuParser = new MenuParser("9898");
        DroneMap droneMap = new DroneMap("9898");

        // Initiate the drone
        Drone drone = new Drone( database, droneMap);

        // Prepare to initial drone's order information
        DroneUtils.sortOrders(drone.orders);
        DroneUtils.findOrderShopLocations(drone.orders,menuParser);
        DroneUtils.getRouteLongLat(drone.orders,menuParser);

        // find the flight path and record it
        drone.findPath(800);
        ArrayList<FlightPath> result = drone.flightPaths;
        ArrayList<Line2D> line2DS = scaffoldingUtils.ToLine2D(result);
        boolean isCrossed = scaffoldingUtils.LinesCrossForTest(line2DS);
        assert isCrossed == Boolean.FALSE;
    }

    @Test
    public void navigationTest4() throws SQLException {
        ScaffoldingUtils scaffoldingUtils = new ScaffoldingUtils();
        Database database = new Database("9898", "1527", "27", "06", "2022");
        MenuParser menuParser = new MenuParser("9898");
        DroneMap droneMap = new DroneMap("9898");

        // Initiate the drone
        Drone drone = new Drone( database, droneMap);

        // Prepare to initial drone's order information
        DroneUtils.sortOrders(drone.orders);
        DroneUtils.findOrderShopLocations(drone.orders,menuParser);
        DroneUtils.getRouteLongLat(drone.orders,menuParser);

        // find the flight path and record it
        drone.findPath(600);
        ArrayList<FlightPath> result = drone.flightPaths;
        ArrayList<Line2D> line2DS = scaffoldingUtils.ToLine2D(result);
        boolean isCrossed = scaffoldingUtils.LinesCrossForTest(line2DS);
        assert isCrossed == Boolean.FALSE;
    }

    @Test
    public void navigationTest5() throws SQLException {
        ScaffoldingUtils scaffoldingUtils = new ScaffoldingUtils();
        Database database = new Database("9898", "1527", "28", "02", "2022");
        MenuParser menuParser = new MenuParser("9898");
        DroneMap droneMap = new DroneMap("9898");

        // Initiate the drone
        Drone drone = new Drone( database, droneMap);

        // Prepare to initial drone's order information
        DroneUtils.sortOrders(drone.orders);
        DroneUtils.findOrderShopLocations(drone.orders,menuParser);
        DroneUtils.getRouteLongLat(drone.orders,menuParser);

        // find the flight path and record it
        drone.findPath(500);
        ArrayList<FlightPath> result = drone.flightPaths;
        ArrayList<Line2D> line2DS = scaffoldingUtils.ToLine2D(result);
        boolean isCrossed = scaffoldingUtils.LinesCrossForTest(line2DS);
        assert isCrossed == Boolean.FALSE;
    }

    @Test
    public void navigationTest6() throws SQLException {
        ScaffoldingUtils scaffoldingUtils = new ScaffoldingUtils();
        Database database = new Database("9898", "1527", "03", "11", "2023");
        MenuParser menuParser = new MenuParser("9898");
        DroneMap droneMap = new DroneMap("9898");

        // Initiate the drone
        Drone drone = new Drone( database, droneMap);

        // Prepare to initial drone's order information
        DroneUtils.sortOrders(drone.orders);
        DroneUtils.findOrderShopLocations(drone.orders,menuParser);
        DroneUtils.getRouteLongLat(drone.orders,menuParser);

        // find the flight path and record it
        drone.findPath(200);
        ArrayList<FlightPath> result = drone.flightPaths;
        ArrayList<Line2D> line2DS = scaffoldingUtils.ToLine2D(result);
        boolean isCrossed = scaffoldingUtils.LinesCrossForTest(line2DS);
        assert isCrossed == Boolean.FALSE;
    }
}
