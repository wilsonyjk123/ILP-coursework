package uk.ac.ed.inf;
import com.mapbox.geojson.Point;

import java.sql.SQLException;
import java.util.*;

public class Drone {
    // Fields
    DroneMap map;
    MenuParser menuParser;
    Database database;
    DroneMap droneMap;
    DroneUtils droneUtils;
    LongLat cp;
    LongLat tp;
    ArrayList<Order> orders;
    ArrayList<Point> pl;
    ArrayList<FlightPath> flightPaths;
    int cost = 0;
    int batteryLimit = 1450;
    int[] shift = new int[]{10,-10,20,-20,30,-30,40,-40,50,-50,60,-60,70,-70,80,-80,90,-90,100,-100,110,-110,120,-120,130,-130,140,-140,150,-150,160,-160,170,-170,180,-180,190,-190,200,-200,210,-210,220,-220,230,-230,240,-240,250,-250,260,-260,270,-270,280,-280,290,-290,300,-300,310,-310,320,-320,330,-330,340,-340,350,-350};

    // Class Constructor
    Drone(DroneMap map, MenuParser menuParser, Database database, DroneMap droneMap) throws SQLException {
        this.map = map;
        this.menuParser = menuParser; // get the location of stores
        this.database = database; // connect to database and get order details
        this.droneMap = droneMap;
        this.orders = database.readOrdersFromDatabase();
        this.droneUtils = new DroneUtils(droneMap);
    }

    public LongLat setAPT(){
        return new LongLat(droneMap.getATLong(),droneMap.getATLat());
    }

    public void findPath(){
        pl = new ArrayList<>();
        flightPaths = new ArrayList<>();
        cp = setAPT();
        pl.add(Point.fromLngLat(droneMap.getATLong(),droneMap.getATLat()));
        int orderCounter = 0;
        //对于每一个订单开始
        outerloop:
        for(Order order:orders){
            //对于订单中每一个target
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
                if(droneUtils.isNoFlyZone(cp.longitude , cp.latitude , tp.longitude , tp.latitude)&& droneUtils.isConfinementArea(cp.longitude , cp.latitude , tp.longitude , tp.latitude)){
                    LongLat closestLandmark  = droneUtils.getClosestLandmark(tp,cp);
                    //不接近终点就不停
                    while (!cp.closeTo(closestLandmark)){
                        //如果当前move没有进禁飞区
                        if(cost>= batteryLimit){
                            break outerloop;
                        }
                        if(!droneUtils.isNoFlyZone(cp.longitude , cp.latitude , tp.longitude , tp.latitude)){
                            break;
                        }
                        if(!droneUtils.isNoFlyZone(cp.nextPosition(droneUtils.getAngle(cp,closestLandmark)).longitude , cp.nextPosition(droneUtils.getAngle(cp,closestLandmark)).latitude , cp.longitude , cp.latitude)
                                && !droneUtils.isConfinementArea(cp.nextPosition(droneUtils.getAngle(cp,closestLandmark)).longitude , cp.nextPosition(droneUtils.getAngle(cp,closestLandmark)).latitude , cp.longitude , cp.latitude)){
                            FlightPath flightPath = new FlightPath(order.getOrderNo(),cp.longitude,cp.latitude,droneUtils.getAngle(cp,closestLandmark),cp.nextPosition(droneUtils.getAngle(cp,closestLandmark)).longitude,cp.nextPosition(droneUtils.getAngle(cp,closestLandmark)).latitude);
                            flightPaths.add(flightPath);
                            cp = cp.nextPosition(droneUtils.getAngle(cp,closestLandmark));
                            pl.add(Point.fromLngLat(cp.longitude,cp.latitude));
                            cost+=1;
                        }else{
                            //只要一直穿过禁飞区，就修正角度
                            FlightPath flightPath = new FlightPath(order.getOrderNo(),cp.longitude,cp.latitude,droneUtils.getShiftAngle(cp,closestLandmark,shift),cp.nextPosition(droneUtils.getAngle(cp,closestLandmark)).longitude,cp.nextPosition(droneUtils.getAngle(cp,closestLandmark)).latitude);
                            flightPaths.add(flightPath);
                            cp = cp.nextPosition(droneUtils.getShiftAngle(cp,closestLandmark,shift));
                            pl.add(Point.fromLngLat(cp.longitude,cp.latitude));
                            cost+=1;
                        }
                    }
                    while(!cp.closeTo(tp)){
                        //如果当前move没有进禁飞区
                        if(cost>= batteryLimit){
                            break outerloop;
                        }
                        if(!droneUtils.isNoFlyZone(cp.nextPosition(droneUtils.getAngle(cp, tp)).longitude , cp.nextPosition(droneUtils.getAngle(cp, tp)).latitude , cp.longitude , cp.latitude)
                                && !droneUtils.isConfinementArea(cp.nextPosition(droneUtils.getAngle(cp, tp)).longitude , cp.nextPosition(droneUtils.getAngle(cp, tp)).latitude , cp.longitude , cp.latitude)){
                            FlightPath flightPath = new FlightPath(order.getOrderNo(),cp.longitude,cp.latitude,droneUtils.getAngle(cp, tp),cp.nextPosition(droneUtils.getAngle(cp, tp)).longitude,cp.nextPosition(droneUtils.getAngle(cp, tp)).latitude);
                            flightPaths.add(flightPath);
                            cp = cp.nextPosition(droneUtils.getAngle(cp, tp));
                            pl.add(Point.fromLngLat(cp.longitude,cp.latitude));
                            cost+=1;
                        }else{
                            FlightPath flightPath = new FlightPath(order.getOrderNo(),cp.longitude,cp.latitude,droneUtils.getShiftAngle(cp, tp,shift),cp.nextPosition(droneUtils.getAngle(cp, tp)).longitude,cp.nextPosition(droneUtils.getAngle(cp, tp)).latitude);
                            flightPaths.add(flightPath);
                            cp = cp.nextPosition(droneUtils.getShiftAngle(cp, tp,shift));
                            pl.add(Point.fromLngLat(cp.longitude,cp.latitude));
                            cost += 1;
                        }
                    }
                    if(cp.closeTo(tp)) {
                        order.routeCounter += 1;
                        cost += 1;
                        FlightPath flightPath = new FlightPath(order.getOrderNo(),cp.longitude,cp.latitude,-999,cp.longitude,cp.latitude);
                        flightPaths.add(flightPath);
                    }
                }else{
                    while(!cp.closeTo(tp)){
                        if(cost>= batteryLimit){
                            break outerloop;
                        }
                        //如果当前move没有进禁飞区
                        if(!droneUtils.isNoFlyZone(cp.nextPosition(droneUtils.getAngle(cp, tp)).longitude , cp.nextPosition(droneUtils.getAngle(cp, tp)).latitude , cp.longitude , cp.latitude)
                                &&!droneUtils.isConfinementArea(cp.nextPosition(droneUtils.getAngle(cp, tp)).longitude , cp.nextPosition(droneUtils.getAngle(cp, tp)).latitude , cp.longitude , cp.latitude)){
                            FlightPath flightPath = new FlightPath(order.getOrderNo(),cp.longitude,cp.latitude,droneUtils.getAngle(cp, tp),cp.nextPosition(droneUtils.getAngle(cp, tp)).longitude,cp.nextPosition(droneUtils.getAngle(cp, tp)).latitude);
                            flightPaths.add(flightPath);
                            cp = cp.nextPosition(droneUtils.getAngle(cp, tp));
                            pl.add(Point.fromLngLat(cp.longitude,cp.latitude));
                            cost+=1;
                        }
                        else{
                            //只要一直穿过禁飞区，就修正角度
                            FlightPath flightPath = new FlightPath(order.getOrderNo(),cp.longitude,cp.latitude,droneUtils.getShiftAngle(cp, tp,shift),cp.nextPosition(droneUtils.getAngle(cp, tp)).longitude,cp.nextPosition(droneUtils.getAngle(cp, tp)).latitude);
                            flightPaths.add(flightPath);
                            cp = cp.nextPosition(droneUtils.getShiftAngle(cp, tp,shift));
                            pl.add(Point.fromLngLat(cp.longitude,cp.latitude));
                            cost += 1;
                        }
                    }
                    if(cp.closeTo(tp)) {
                        order.routeCounter += 1;
                        cost += 1;
                        FlightPath flightPath = new FlightPath(order.getOrderNo(),cp.longitude,cp.latitude,-999,cp.longitude,cp.latitude);
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
        tp = setAPT();
        backAPT();
    }

    public void preparation() throws SQLException {
        droneUtils.sortOrders(orders); //订单排序
        droneUtils.findOrderShopLocations(orders,menuParser); //找到pick up三字地址
        droneUtils.getRouteLongLat(orders,menuParser); //set每个订单的target
    }

    public void backAPT(){
        if(droneUtils.isNoFlyZone(cp.longitude , cp.latitude , tp.longitude , tp.latitude)&& droneUtils.isConfinementArea(cp.longitude , cp.latitude , tp.longitude , tp.latitude)){
            LongLat closestLandmark  = droneUtils.getClosestLandmark(tp,cp);
            //不接近终点就不停
            while (!cp.closeTo(closestLandmark)){
                //如果当前move没有进禁飞区
                if(!droneUtils.isNoFlyZone(cp.longitude , cp.latitude , tp.longitude , tp.latitude)){
                    break;
                }
                if(!droneUtils.isNoFlyZone(cp.nextPosition(droneUtils.getAngle(cp,closestLandmark)).longitude , cp.nextPosition(droneUtils.getAngle(cp,closestLandmark)).latitude , cp.longitude , cp.latitude)
                        && !droneUtils.isConfinementArea(cp.nextPosition(droneUtils.getAngle(cp,closestLandmark)).longitude , cp.nextPosition(droneUtils.getAngle(cp,closestLandmark)).latitude , cp.longitude , cp.latitude)){
                    FlightPath flightPath = new FlightPath(null,cp.longitude,cp.latitude,droneUtils.getAngle(cp,closestLandmark),cp.nextPosition(droneUtils.getAngle(cp,closestLandmark)).longitude,cp.nextPosition(droneUtils.getAngle(cp,closestLandmark)).latitude);
                    flightPaths.add(flightPath);
                    cp = cp.nextPosition(droneUtils.getAngle(cp,closestLandmark));
                    pl.add(Point.fromLngLat(cp.longitude,cp.latitude));
                    cost+=1;
                }else{
                    //只要一直穿过禁飞区，就修正角度
                    FlightPath flightPath = new FlightPath(null,cp.longitude,cp.latitude,droneUtils.getShiftAngle(cp,closestLandmark,shift),cp.nextPosition(droneUtils.getAngle(cp,closestLandmark)).longitude,cp.nextPosition(droneUtils.getAngle(cp,closestLandmark)).latitude);
                    flightPaths.add(flightPath);
                    cp = cp.nextPosition(droneUtils.getShiftAngle(cp,closestLandmark,shift));
                    pl.add(Point.fromLngLat(cp.longitude,cp.latitude));
                    cost+=1;
                }
            }
            while(!cp.closeTo(tp)){
                //如果当前move没有进禁飞区
                if(!droneUtils.isNoFlyZone(cp.nextPosition(droneUtils.getAngle(cp, tp)).longitude , cp.nextPosition(droneUtils.getAngle(cp, tp)).latitude , cp.longitude , cp.latitude)
                        && !droneUtils.isConfinementArea(cp.nextPosition(droneUtils.getAngle(cp, tp)).longitude , cp.nextPosition(droneUtils.getAngle(cp, tp)).latitude , cp.longitude , cp.latitude)){
                    FlightPath flightPath = new FlightPath(null,cp.longitude,cp.latitude,droneUtils.getAngle(cp, tp),cp.nextPosition(droneUtils.getAngle(cp, tp)).longitude,cp.nextPosition(droneUtils.getAngle(cp, tp)).latitude);
                    flightPaths.add(flightPath);
                    cp = cp.nextPosition(droneUtils.getAngle(cp, tp));
                    pl.add(Point.fromLngLat(cp.longitude,cp.latitude));
                    cost+=1;
                }else{
                    FlightPath flightPath = new FlightPath(null,cp.longitude,cp.latitude,droneUtils.getShiftAngle(cp, tp,shift),cp.nextPosition(droneUtils.getAngle(cp, tp)).longitude,cp.nextPosition(droneUtils.getAngle(cp, tp)).latitude);
                    flightPaths.add(flightPath);
                    cp = cp.nextPosition(droneUtils.getShiftAngle(cp, tp,shift));
                    pl.add(Point.fromLngLat(cp.longitude,cp.latitude));
                    cost += 1;
                }
            }
            if(cp.closeTo(tp)) {
                cost += 1;
                FlightPath flightPath = new FlightPath(null,cp.longitude,cp.latitude,-999,cp.longitude,cp.latitude);
                flightPaths.add(flightPath);
                System.out.println("The Drone has returned to APT");
            }
        }else{
            while(!cp.closeTo(tp)){
                //如果当前move没有进禁飞区
                if(!droneUtils.isNoFlyZone(cp.nextPosition(droneUtils.getAngle(cp, tp)).longitude , cp.nextPosition(droneUtils.getAngle(cp, tp)).latitude , cp.longitude , cp.latitude)
                        && !droneUtils.isConfinementArea(cp.nextPosition(droneUtils.getAngle(cp, tp)).longitude , cp.nextPosition(droneUtils.getAngle(cp, tp)).latitude , cp.longitude , cp.latitude)){
                    FlightPath flightPath = new FlightPath(null,cp.longitude,cp.latitude,droneUtils.getAngle(cp, tp),cp.nextPosition(droneUtils.getAngle(cp, tp)).longitude,cp.nextPosition(droneUtils.getAngle(cp, tp)).latitude);
                    flightPaths.add(flightPath);
                    cp = cp.nextPosition(droneUtils.getAngle(cp, tp));
                    pl.add(Point.fromLngLat(cp.longitude,cp.latitude));
                    cost+=1;
                }
                else{
                    //只要一直穿过禁飞区，就修正角度
                    FlightPath flightPath = new FlightPath(null,cp.longitude,cp.latitude,droneUtils.getShiftAngle(cp, tp,shift),cp.nextPosition(droneUtils.getAngle(cp, tp)).longitude,cp.nextPosition(droneUtils.getAngle(cp, tp)).latitude);
                    flightPaths.add(flightPath);
                    cp = cp.nextPosition(droneUtils.getShiftAngle(cp, tp,shift));
                    pl.add(Point.fromLngLat(cp.longitude,cp.latitude));
                    cost += 1;
                }
            }
            if(cp.closeTo(tp)) {
                cost += 1;
                FlightPath flightPath = new FlightPath(null,cp.longitude,cp.latitude,-999,cp.longitude,cp.latitude);
                flightPaths.add(flightPath);
                System.out.println("The Drone has returned to APT");
            }
        }
    }
}
