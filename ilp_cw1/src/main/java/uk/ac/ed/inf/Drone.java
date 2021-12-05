package uk.ac.ed.inf;
import com.mapbox.geojson.Point;

import java.sql.SQLException;
import java.util.*;

public class Drone {
    // Fields
    public Database database;
    public DroneMap droneMap;
    public DroneUtils droneUtils;
    public LongLat cp;
    public LongLat tp;
    private final ArrayList<Order> orders;
    private ArrayList<Point> pl;
    private ArrayList<FlightPath> flightPaths;
    int cost = 0;
    int batteryLimit = 1450;
    int[] shift = new int[]{10,-10,20,-20,30,-30,40,-40,50,-50,60,-60,70,-70,80,-80,90,-90,100,-100,110,-110,120,-120,130,-130,140,-140,150,-150,160,-160,170,-170,180,-180,190,-190,200,-200,210,-210,220,-220,230,-230,240,-240,250,-250,260,-260,270,-270,280,-280,290,-290,300,-300,310,-310,320,-320,330,-330,340,-340,350,-350};

    // Class Constructor
    Drone(Database database,DroneMap droneMap) throws SQLException {
        this.droneMap = droneMap;
        this.database = database; // connect to database and get order details
        this.orders = database.readOrdersFromDatabase();
        this.droneUtils = new DroneUtils(droneMap);
    }

    /**
     * Find the flightpath and record the information of each move
     * When the drone's battery is under 50, the drone will directly go back to Appleton Tower from the current position
     *
     * */
    public void findPath(){
        pl = new ArrayList<>();
        flightPaths = new ArrayList<>();
        cp = droneMap.setAPT();
        pl.add(Point.fromLngLat(droneMap.getATLong(),droneMap.getATLat()));
        int orderCounter = 0;
        outerloop:
        for(Order order:orders){
            if(order.getRouteLongLat().size()>2){
                if(cp.distanceTo(order.getRouteLongLat().get(0))>cp.distanceTo(order.getRouteLongLat().get(1))){
                    ArrayList<LongLat> longLats = new ArrayList<>();
                    longLats.add(order.getRouteLongLat().get(1));
                    longLats.add(order.getRouteLongLat().get(0));
                    longLats.add(order.getRouteLongLat().get(2));
                    order.setRouteLongLat(longLats);
                }
            }
            for(LongLat target: order.getRouteLongLat()){
                tp = target;
                if(droneUtils.isNoFlyZone(cp.getLongitude() , cp.getLatitude() , tp.getLongitude() , tp.getLatitude())&& droneUtils.isConfinementArea(cp.getLongitude() , cp.getLatitude() , tp.getLongitude() , tp.getLatitude())){
                    LongLat closestLandmark  = droneUtils.getClosestLandmark(tp,cp);
                    while (!cp.closeTo(closestLandmark)){
                        if(cost>= batteryLimit){
                            break outerloop;
                        }
                        if(!droneUtils.isNoFlyZone(cp.getLongitude() , cp.getLatitude() , tp.getLongitude() , tp.getLatitude())){
                            break;
                        }
                        if(!droneUtils.isNoFlyZone(cp.nextPosition(droneUtils.getAngle(cp,closestLandmark)).getLongitude() , cp.nextPosition(droneUtils.getAngle(cp,closestLandmark)).getLatitude() , cp.getLongitude() , cp.getLatitude())
                                && !droneUtils.isConfinementArea(cp.nextPosition(droneUtils.getAngle(cp,closestLandmark)).getLongitude() , cp.nextPosition(droneUtils.getAngle(cp,closestLandmark)).getLatitude() , cp.getLongitude() , cp.getLatitude())){
                            FlightPath flightPath = new FlightPath(order.getOrderNo(),cp.getLongitude(),cp.getLatitude(),droneUtils.getAngle(cp,closestLandmark),cp.nextPosition(droneUtils.getAngle(cp,closestLandmark)).getLongitude(),cp.nextPosition(droneUtils.getAngle(cp,closestLandmark)).getLatitude());
                            flightPaths.add(flightPath);
                            cp = cp.nextPosition(droneUtils.getAngle(cp,closestLandmark));
                            pl.add(Point.fromLngLat(cp.getLongitude(),cp.getLatitude()));
                            cost+=1;
                        }else{
                            FlightPath flightPath = new FlightPath(order.getOrderNo(),cp.getLongitude(),cp.getLatitude(),droneUtils.getShiftAngle(cp,closestLandmark,shift),cp.nextPosition(droneUtils.getAngle(cp,closestLandmark)).getLongitude(),cp.nextPosition(droneUtils.getAngle(cp,closestLandmark)).getLatitude());
                            flightPaths.add(flightPath);
                            cp = cp.nextPosition(droneUtils.getShiftAngle(cp,closestLandmark,shift));
                            pl.add(Point.fromLngLat(cp.getLongitude(),cp.getLatitude()));
                            cost+=1;
                        }
                    }
                    while(!cp.closeTo(tp)){
                        if(cost>= batteryLimit){
                            break outerloop;
                        }
                        if(!droneUtils.isNoFlyZone(cp.nextPosition(droneUtils.getAngle(cp, tp)).getLongitude() , cp.nextPosition(droneUtils.getAngle(cp, tp)).getLatitude() , cp.getLongitude() , cp.getLatitude())
                                && !droneUtils.isConfinementArea(cp.nextPosition(droneUtils.getAngle(cp, tp)).getLongitude() , cp.nextPosition(droneUtils.getAngle(cp, tp)).getLatitude() , cp.getLongitude() , cp.getLatitude())){
                            FlightPath flightPath = new FlightPath(order.getOrderNo(),cp.getLongitude(),cp.getLatitude(),droneUtils.getAngle(cp, tp),cp.nextPosition(droneUtils.getAngle(cp, tp)).getLongitude(),cp.nextPosition(droneUtils.getAngle(cp, tp)).getLatitude());
                            flightPaths.add(flightPath);
                            cp = cp.nextPosition(droneUtils.getAngle(cp, tp));
                            pl.add(Point.fromLngLat(cp.getLongitude(),cp.getLatitude()));
                            cost+=1;
                        }else{
                            FlightPath flightPath = new FlightPath(order.getOrderNo(),cp.getLongitude(),cp.getLatitude(),droneUtils.getShiftAngle(cp, tp,shift),cp.nextPosition(droneUtils.getAngle(cp, tp)).getLongitude(),cp.nextPosition(droneUtils.getAngle(cp, tp)).getLatitude());
                            flightPaths.add(flightPath);
                            cp = cp.nextPosition(droneUtils.getShiftAngle(cp, tp,shift));
                            pl.add(Point.fromLngLat(cp.getLongitude(),cp.getLatitude()));
                            cost += 1;
                        }
                    }
                    if(cp.closeTo(tp)) {
                        order.routeCounter += 1;
                        cost += 1;
                        FlightPath flightPath = new FlightPath(order.getOrderNo(),cp.getLongitude(),cp.getLatitude(),-999,cp.getLongitude(),cp.getLatitude());
                        flightPaths.add(flightPath);
                    }
                }else{
                    while(!cp.closeTo(tp)){
                        if(cost>= batteryLimit){
                            break outerloop;
                        }
                        if(!droneUtils.isNoFlyZone(cp.nextPosition(droneUtils.getAngle(cp, tp)).getLongitude() , cp.nextPosition(droneUtils.getAngle(cp, tp)).getLatitude() , cp.getLongitude() , cp.getLatitude())
                                &&!droneUtils.isConfinementArea(cp.nextPosition(droneUtils.getAngle(cp, tp)).getLongitude() , cp.nextPosition(droneUtils.getAngle(cp, tp)).getLatitude() , cp.getLongitude() , cp.getLatitude())){
                            FlightPath flightPath = new FlightPath(order.getOrderNo(),cp.getLongitude(),cp.getLatitude(),droneUtils.getAngle(cp, tp),cp.nextPosition(droneUtils.getAngle(cp, tp)).getLongitude(),cp.nextPosition(droneUtils.getAngle(cp, tp)).getLatitude());
                            flightPaths.add(flightPath);
                            cp = cp.nextPosition(droneUtils.getAngle(cp, tp));
                            pl.add(Point.fromLngLat(cp.getLongitude(),cp.getLatitude()));
                            cost+=1;
                        }
                        else{
                            FlightPath flightPath = new FlightPath(order.getOrderNo(),cp.getLongitude(),cp.getLatitude(),droneUtils.getShiftAngle(cp, tp,shift),cp.nextPosition(droneUtils.getAngle(cp, tp)).getLongitude(),cp.nextPosition(droneUtils.getAngle(cp, tp)).getLatitude());
                            flightPaths.add(flightPath);
                            cp = cp.nextPosition(droneUtils.getShiftAngle(cp, tp,shift));
                            pl.add(Point.fromLngLat(cp.getLongitude(),cp.getLatitude()));
                            cost += 1;
                        }
                    }
                    if(cp.closeTo(tp)) {
                        order.routeCounter += 1;
                        cost += 1;
                        FlightPath flightPath = new FlightPath(order.getOrderNo(),cp.getLongitude(),cp.getLatitude(),-999,cp.getLongitude(),cp.getLatitude());
                        flightPaths.add(flightPath);
                    }
                }
            }
            if(order.routeCounter == order.getRouteLongLat().size()){
                orderCounter+=1;
                order.setIsDelivered(true);
                System.out.println("The order("+order.getOrderNo()+") has been finished");
            }
        }
        System.out.println("Total number of orders: "+orders.size());
        System.out.println("Actual number of Delivered orders: " +orderCounter);
        tp = droneMap.setAPT();
        backAPT();
    }

    /**
     * When the battery of the drone is under 50 or all the orders has been finished, the drone will go back to Appleton Tower
     * */
    public void backAPT(){
        if(droneUtils.isNoFlyZone(cp.getLongitude() , cp.getLatitude() , tp.getLongitude() , tp.getLatitude())&& droneUtils.isConfinementArea(cp.getLongitude() , cp.getLatitude() , tp.getLongitude() , tp.getLatitude())){
            LongLat closestLandmark  = droneUtils.getClosestLandmark(tp,cp);
            //不接近终点就不停
            while (!cp.closeTo(closestLandmark)){
                //如果当前move没有进禁飞区
                if(!droneUtils.isNoFlyZone(cp.getLongitude() , cp.getLatitude() , tp.getLongitude() , tp.getLatitude())){
                    break;
                }
                if(!droneUtils.isNoFlyZone(cp.nextPosition(droneUtils.getAngle(cp,closestLandmark)).getLongitude() , cp.nextPosition(droneUtils.getAngle(cp,closestLandmark)).getLatitude() , cp.getLongitude() , cp.getLatitude())
                        && !droneUtils.isConfinementArea(cp.nextPosition(droneUtils.getAngle(cp,closestLandmark)).getLongitude() , cp.nextPosition(droneUtils.getAngle(cp,closestLandmark)).getLatitude() , cp.getLongitude() , cp.getLatitude())){
                    FlightPath flightPath = new FlightPath(null,cp.getLongitude(),cp.getLatitude(),droneUtils.getAngle(cp,closestLandmark),cp.nextPosition(droneUtils.getAngle(cp,closestLandmark)).getLongitude(),cp.nextPosition(droneUtils.getAngle(cp,closestLandmark)).getLatitude());
                    flightPaths.add(flightPath);
                    cp = cp.nextPosition(droneUtils.getAngle(cp,closestLandmark));
                    pl.add(Point.fromLngLat(cp.getLongitude(),cp.getLatitude()));
                    cost+=1;
                }else{
                    FlightPath flightPath = new FlightPath(null,cp.getLongitude(),cp.getLatitude(),droneUtils.getShiftAngle(cp,closestLandmark,shift),cp.nextPosition(droneUtils.getAngle(cp,closestLandmark)).getLongitude(),cp.nextPosition(droneUtils.getAngle(cp,closestLandmark)).getLatitude());
                    flightPaths.add(flightPath);
                    cp = cp.nextPosition(droneUtils.getShiftAngle(cp,closestLandmark,shift));
                    pl.add(Point.fromLngLat(cp.getLongitude(),cp.getLatitude()));
                    cost+=1;
                }
            }
            while(!cp.closeTo(tp)){
                if(!droneUtils.isNoFlyZone(cp.nextPosition(droneUtils.getAngle(cp, tp)).getLongitude() , cp.nextPosition(droneUtils.getAngle(cp, tp)).getLatitude() , cp.getLongitude() , cp.getLatitude())
                        && !droneUtils.isConfinementArea(cp.nextPosition(droneUtils.getAngle(cp, tp)).getLongitude() , cp.nextPosition(droneUtils.getAngle(cp, tp)).getLatitude() , cp.getLongitude() , cp.getLatitude())){
                    FlightPath flightPath = new FlightPath(null,cp.getLongitude(),cp.getLatitude(),droneUtils.getAngle(cp, tp),cp.nextPosition(droneUtils.getAngle(cp, tp)).getLongitude(),cp.nextPosition(droneUtils.getAngle(cp, tp)).getLatitude());
                    flightPaths.add(flightPath);
                    cp = cp.nextPosition(droneUtils.getAngle(cp, tp));
                    pl.add(Point.fromLngLat(cp.getLongitude(),cp.getLatitude()));
                    cost+=1;
                }else{
                    FlightPath flightPath = new FlightPath(null,cp.getLongitude(),cp.getLatitude(),droneUtils.getShiftAngle(cp, tp,shift),cp.nextPosition(droneUtils.getAngle(cp, tp)).getLongitude(),cp.nextPosition(droneUtils.getAngle(cp, tp)).getLatitude());
                    flightPaths.add(flightPath);
                    cp = cp.nextPosition(droneUtils.getShiftAngle(cp, tp,shift));
                    pl.add(Point.fromLngLat(cp.getLongitude(),cp.getLatitude()));
                    cost += 1;
                }
            }
            if(cp.closeTo(tp)) {
                cost += 1;
                FlightPath flightPath = new FlightPath(null,cp.getLongitude(),cp.getLatitude(),-999,cp.getLongitude(),cp.getLatitude());
                flightPaths.add(flightPath);
                System.out.println("The Drone has returned to APT");
            }
        }else{
            while(!cp.closeTo(tp)){
                if(!droneUtils.isNoFlyZone(cp.nextPosition(droneUtils.getAngle(cp, tp)).getLongitude() , cp.nextPosition(droneUtils.getAngle(cp, tp)).getLatitude() , cp.getLongitude() , cp.getLatitude())
                        && !droneUtils.isConfinementArea(cp.nextPosition(droneUtils.getAngle(cp, tp)).getLongitude() , cp.nextPosition(droneUtils.getAngle(cp, tp)).getLatitude() , cp.getLongitude() , cp.getLatitude())){
                    FlightPath flightPath = new FlightPath(null,cp.getLongitude(),cp.getLatitude(),droneUtils.getAngle(cp, tp),cp.nextPosition(droneUtils.getAngle(cp, tp)).getLongitude(),cp.nextPosition(droneUtils.getAngle(cp, tp)).getLatitude());
                    flightPaths.add(flightPath);
                    cp = cp.nextPosition(droneUtils.getAngle(cp, tp));
                    pl.add(Point.fromLngLat(cp.getLongitude(),cp.getLatitude()));
                    cost+=1;
                }
                else{
                    FlightPath flightPath = new FlightPath(null,cp.getLongitude(),cp.getLatitude(),droneUtils.getShiftAngle(cp, tp,shift),cp.nextPosition(droneUtils.getAngle(cp, tp)).getLongitude(),cp.nextPosition(droneUtils.getAngle(cp, tp)).getLatitude());
                    flightPaths.add(flightPath);
                    cp = cp.nextPosition(droneUtils.getShiftAngle(cp, tp,shift));
                    pl.add(Point.fromLngLat(cp.getLongitude(),cp.getLatitude()));
                    cost += 1;
                }
            }
            if(cp.closeTo(tp)) {
                cost += 1;
                FlightPath flightPath = new FlightPath(null,cp.getLongitude(),cp.getLatitude(),-999,cp.getLongitude(),cp.getLatitude());
                flightPaths.add(flightPath);
                System.out.println("The Drone has returned to APT");
            }
        }
    }

    /*Getters*/
    public ArrayList<FlightPath> getFlightPaths() {
        return flightPaths;
    }

    public ArrayList<Point> getPl() {
        return pl;
    }

    public ArrayList<Order> getOrders() {
        return orders;
    }
}
