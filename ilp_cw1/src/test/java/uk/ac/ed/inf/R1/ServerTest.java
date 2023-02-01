package uk.ac.ed.inf.R1;

import org.junit.Test;
import uk.ac.ed.inf.*;
import uk.ac.ed.inf.Scaffolding.ScaffoldingUtils;

import java.sql.SQLException;

public class ServerTest {
    @Test
    public void responseTimeTestForWebServer1() throws SQLException {
        ScaffoldingUtils scaffoldingUtils = new ScaffoldingUtils();
        Database database = new Database("9898", "1527", "03", "11", "2023");
        MenuParser menuParser = new MenuParser("9898");
        DroneMap droneMap = new DroneMap("9898");

        // Initiate the drone
        Drone drone = new Drone( database, droneMap);
        // Prepare to initial drone's order information
        DroneUtils.sortOrders(drone.orders);
        long start = System.currentTimeMillis();
        DroneUtils.findOrderShopLocations(drone.orders,menuParser);
        long end = System.currentTimeMillis();
        long time = end - start;
        assert time<500;
    }

    @Test
    public void responseTimeTestForWebServer2() throws SQLException {
        ScaffoldingUtils scaffoldingUtils = new ScaffoldingUtils();
        Database database = new Database("9898", "1527", "01", "12", "2023");
        MenuParser menuParser = new MenuParser("9898");
        DroneMap droneMap = new DroneMap("9898");

        // Initiate the drone
        Drone drone = new Drone( database, droneMap);
        // Prepare to initial drone's order information
        DroneUtils.sortOrders(drone.orders);
        long start = System.currentTimeMillis();
        DroneUtils.findOrderShopLocations(drone.orders,menuParser);
        long end = System.currentTimeMillis();
        long time = end - start;
        assert time<300;
    }

    @Test
    public void responseTimeTestForWebServer3() throws SQLException {
        ScaffoldingUtils scaffoldingUtils = new ScaffoldingUtils();
        Database database = new Database("9898", "1527", "31", "12", "2023");
        MenuParser menuParser = new MenuParser("9898");
        DroneMap droneMap = new DroneMap("9898");

        // Initiate the drone
        Drone drone = new Drone( database, droneMap);
        // Prepare to initial drone's order information
        DroneUtils.sortOrders(drone.orders);
        long start = System.currentTimeMillis();
        DroneUtils.findOrderShopLocations(drone.orders,menuParser);
        long end = System.currentTimeMillis();
        long time = end - start;
        assert time<100;
    }
}
