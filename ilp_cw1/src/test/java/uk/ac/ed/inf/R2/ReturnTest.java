package uk.ac.ed.inf.R2;

import org.junit.Assert;
import org.junit.Test;
import uk.ac.ed.inf.*;
import uk.ac.ed.inf.Scaffolding.ScaffoldingUtils;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;

public class ReturnTest {
    @Test
    public void ReturnDetectionTest0() throws SQLException {
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
        drone.findPath(1000);
        ArrayList<FlightPath> result = drone.flightPaths;
        Point2D point2D = scaffoldingUtils.ToLastPoint2D(result);
        double lng = point2D.getX();
        double lat = point2D.getY();
        double newlng = scaffoldingUtils.RoundDown(lng);
        double newlat = scaffoldingUtils.RoundDown(lat);
        assert (Math.abs(newlng-(-3.1868))<0.0015) && (Math.abs(newlat-55.9445)<0.0015);
    }

    @Test
    public void ReturnDetectionTest1() throws SQLException {
        ScaffoldingUtils scaffoldingUtils = new ScaffoldingUtils();
        Database database = new Database("9898", "1527", "10", "12", "2023");
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
        Point2D point2D = scaffoldingUtils.ToLastPoint2D(result);
        double lng = point2D.getX();
        double lat = point2D.getY();
        double newlng = scaffoldingUtils.RoundDown(lng);
        double newlat = scaffoldingUtils.RoundDown(lat);
        assert (Math.abs(newlng-(-3.1868))<0.0015) && (Math.abs(newlat-55.9445)<0.0015);
    }

    @Test
    public void ReturnDetectionTest2() throws SQLException {
        ScaffoldingUtils scaffoldingUtils = new ScaffoldingUtils();
        Database database = new Database("9898", "1527", "09", "12", "2023");
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
        Point2D point2D = scaffoldingUtils.ToLastPoint2D(result);
        double lng = point2D.getX();
        double lat = point2D.getY();
        double newlng = scaffoldingUtils.RoundDown(lng);
        double newlat = scaffoldingUtils.RoundDown(lat);
        assert (Math.abs(newlng-(-3.1868))<0.0015) && (Math.abs(newlat-55.9445)<0.0015);
    }

    @Test
    public void ReturnDetectionTest3() throws SQLException {
        ScaffoldingUtils scaffoldingUtils = new ScaffoldingUtils();
        Database database = new Database("9898", "1527", "08", "12", "2023");
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
        ArrayList<FlightPath> result = drone.flightPaths;
        Point2D point2D = scaffoldingUtils.ToLastPoint2D(result);
        double lng = point2D.getX();
        double lat = point2D.getY();
        double newlng = scaffoldingUtils.RoundDown(lng);
        double newlat = scaffoldingUtils.RoundDown(lat);
        assert (Math.abs(newlng-(-3.1868))<0.0015) && (Math.abs(newlat-55.9445)<0.0015);
    }

    @Test
    public void ReturnDetectionTest4() throws SQLException {
        ScaffoldingUtils scaffoldingUtils = new ScaffoldingUtils();
        Database database = new Database("9898", "1527", "06", "12", "2023");
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
        ArrayList<FlightPath> result = drone.flightPaths;
        Point2D point2D = scaffoldingUtils.ToLastPoint2D(result);
        double lng = point2D.getX();
        double lat = point2D.getY();
        double newlng = scaffoldingUtils.RoundDown(lng);
        double newlat = scaffoldingUtils.RoundDown(lat);
        assert (Math.abs(newlng-(-3.1868))<0.0015) && (Math.abs(newlat-55.9445)<0.0015);
    }

    @Test
    public void ReturnDetectionTest5() throws SQLException {
        ScaffoldingUtils scaffoldingUtils = new ScaffoldingUtils();
        Database database = new Database("9898", "1527", "30", "06", "2023");
        MenuParser menuParser = new MenuParser("9898");
        DroneMap droneMap = new DroneMap("9898");

        // Initiate the drone
        Drone drone = new Drone( database, droneMap);

        // Prepare to initial drone's order information
        DroneUtils.sortOrders(drone.orders);
        DroneUtils.findOrderShopLocations(drone.orders,menuParser);
        DroneUtils.getRouteLongLat(drone.orders,menuParser);

        // find the flight path and record it
        drone.findPath(80000);
        ArrayList<FlightPath> result = drone.flightPaths;
        Point2D point2D = scaffoldingUtils.ToLastPoint2D(result);
        double lng = point2D.getX();
        double lat = point2D.getY();
        double newlng = scaffoldingUtils.RoundDown(lng);
        double newlat = scaffoldingUtils.RoundDown(lat);
        assert (Math.abs(newlng-(-3.1868))<0.0015) && (Math.abs(newlat-55.9445)<0.0015);
    }
}
