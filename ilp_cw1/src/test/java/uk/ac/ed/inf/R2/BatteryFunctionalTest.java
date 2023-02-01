package uk.ac.ed.inf.R2;

import org.junit.Test;
import uk.ac.ed.inf.*;
import uk.ac.ed.inf.Scaffolding.ScaffoldingUtils;

import java.sql.SQLException;

public class BatteryFunctionalTest {
    @Test
    public void functionalTesting0() throws SQLException {
        ScaffoldingUtils scaffoldingUtils = new ScaffoldingUtils();
        Database database = new Database("9898", "1527", "31", "12", "2023");
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
        System.out.println(drone.tp.longitude);
        System.out.println(drone.tp.latitude);
        assert scaffoldingUtils.RoundDown(drone.tp.longitude)-(-3.1868)<0.0015 && scaffoldingUtils.RoundDown(drone.tp.latitude)-55.9445<0.0015;
    }

    @Test
    public void functionalTesting1() throws SQLException {
        ScaffoldingUtils scaffoldingUtils = new ScaffoldingUtils();
        Database database = new Database("9898", "1527", "31", "12", "2023");
        MenuParser menuParser = new MenuParser("9898");
        DroneMap droneMap = new DroneMap("9898");

        // Initiate the drone
        Drone drone = new Drone( database, droneMap);

        // Prepare to initial drone's order information
        DroneUtils.sortOrders(drone.orders);
        DroneUtils.findOrderShopLocations(drone.orders,menuParser);
        DroneUtils.getRouteLongLat(drone.orders,menuParser);

        // find the flight path and record it
        drone.findPath(400);
        System.out.println(drone.tp.longitude);
        System.out.println(drone.tp.latitude);
        assert scaffoldingUtils.RoundDown(drone.tp.longitude)-(-3.1868)<0.0015 && scaffoldingUtils.RoundDown(drone.tp.latitude)-55.9445<0.0015;
    }

    @Test
    public void functionalTesting2() throws SQLException {
        ScaffoldingUtils scaffoldingUtils = new ScaffoldingUtils();
        Database database = new Database("9898", "1527", "31", "12", "2023");
        MenuParser menuParser = new MenuParser("9898");
        DroneMap droneMap = new DroneMap("9898");

        // Initiate the drone
        Drone drone = new Drone( database, droneMap);

        // Prepare to initial drone's order information
        DroneUtils.sortOrders(drone.orders);
        DroneUtils.findOrderShopLocations(drone.orders,menuParser);
        DroneUtils.getRouteLongLat(drone.orders,menuParser);

        // find the flight path and record it
        drone.findPath(300);
        System.out.println(drone.tp.longitude);
        System.out.println(drone.tp.latitude);
        assert scaffoldingUtils.RoundDown(drone.tp.longitude)-(-3.1868)<0.0015 && scaffoldingUtils.RoundDown(drone.tp.latitude)-55.9445<0.0015;
    }

    @Test
    public void functionalTesting3() throws SQLException {
        ScaffoldingUtils scaffoldingUtils = new ScaffoldingUtils();
        Database database = new Database("9898", "1527", "31", "12", "2023");
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
        System.out.println(drone.tp.longitude);
        System.out.println(drone.tp.latitude);
        assert scaffoldingUtils.RoundDown(drone.tp.longitude)-(-3.1868)<0.0015 && scaffoldingUtils.RoundDown(drone.tp.latitude)-55.9445<0.0015;
    }

    @Test
    public void functionalTesting4() throws SQLException {
        ScaffoldingUtils scaffoldingUtils = new ScaffoldingUtils();
        Database database = new Database("9898", "1527", "31", "12", "2023");
        MenuParser menuParser = new MenuParser("9898");
        DroneMap droneMap = new DroneMap("9898");

        // Initiate the drone
        Drone drone = new Drone( database, droneMap);

        // Prepare to initial drone's order information
        DroneUtils.sortOrders(drone.orders);
        DroneUtils.findOrderShopLocations(drone.orders,menuParser);
        DroneUtils.getRouteLongLat(drone.orders,menuParser);

        // find the flight path and record it
        drone.findPath(250);
        System.out.println(drone.tp.longitude);
        System.out.println(drone.tp.latitude);
        assert scaffoldingUtils.RoundDown(drone.tp.longitude)-(-3.1868)<0.0015 && scaffoldingUtils.RoundDown(drone.tp.latitude)-55.9445<0.0015;
    }

    @Test
    public void functionalTesting5() throws SQLException {
        ScaffoldingUtils scaffoldingUtils = new ScaffoldingUtils();
        Database database = new Database("9898", "1527", "31", "12", "2023");
        MenuParser menuParser = new MenuParser("9898");
        DroneMap droneMap = new DroneMap("9898");

        // Initiate the drone
        Drone drone = new Drone( database, droneMap);

        // Prepare to initial drone's order information
        DroneUtils.sortOrders(drone.orders);
        DroneUtils.findOrderShopLocations(drone.orders,menuParser);
        DroneUtils.getRouteLongLat(drone.orders,menuParser);

        // find the flight path and record it
        drone.findPath(100);
        System.out.println(drone.tp.longitude);
        System.out.println(drone.tp.latitude);
        assert scaffoldingUtils.RoundDown(drone.tp.longitude)-(-3.1868)<0.0015 && scaffoldingUtils.RoundDown(drone.tp.latitude)-55.9445<0.0015;
    }

    @Test
    public void functionalTesting6() throws SQLException {
        ScaffoldingUtils scaffoldingUtils = new ScaffoldingUtils();
        Database database = new Database("9898", "1527", "31", "12", "2023");
        MenuParser menuParser = new MenuParser("9898");
        DroneMap droneMap = new DroneMap("9898");

        // Initiate the drone
        Drone drone = new Drone( database, droneMap);

        // Prepare to initial drone's order information
        DroneUtils.sortOrders(drone.orders);
        DroneUtils.findOrderShopLocations(drone.orders,menuParser);
        DroneUtils.getRouteLongLat(drone.orders,menuParser);

        // find the flight path and record it
        drone.findPath(50);
        System.out.println(drone.tp.longitude);
        System.out.println(drone.tp.latitude);
        assert scaffoldingUtils.RoundDown(drone.tp.longitude)-(-3.1868)<0.0015 && scaffoldingUtils.RoundDown(drone.tp.latitude)-55.9445<0.0015;
    }
}
