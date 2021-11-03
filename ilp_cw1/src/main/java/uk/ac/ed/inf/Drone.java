package uk.ac.ed.inf;
import com.mapbox.geojson.*;
import com.mapbox.geojson.Point;

import java.awt.*;
import java.awt.geom.Line2D;
import java.sql.SQLException;
import java.util.*;

public class Drone {
    // Fields
    DroneMap map;
    MenuParser menuParser;
    Database database;
    DroneMap droneMap;
    WordParser wordParser;
    LongLat currentPosition;
    LongLat targetPosition;
    ArrayList<Order> orders;
    ArrayList<LongLat> path;
    ArrayList<Point> pl = new ArrayList<>();
    ArrayList<LongLat> pathChecker = new ArrayList<>();
    private int droneBattery = 1500;
    int cost = 0;


    // Class Constructor
    Drone(DroneMap map, MenuParser menuParser, Database database, DroneMap droneMap) throws SQLException {
        this.map = map;
        this.menuParser = menuParser; // get the location of stores
        this.database = database; // connect to database and get order details
        this.droneMap = droneMap;
        this.orders = database.readOrdersFromDatabase();
    }
    public void Move(){
        int pathChecker = path.size();
        int counter = 0;
        System.out.println(path.size());
        while(pathChecker != 1){
            if(isNoFlyZone(path.get(counter).longitude, path.get(counter).latitude, path.get(counter+1).longitude, path.get(counter+1).latitude)){
                if(isNoFlyZone(droneMap.landmarks.get(0).longitude,droneMap.landmarks.get(0).latitude,path.get(counter).longitude, path.get(counter).latitude)){
                    path.add(counter+1, new LongLat(droneMap.landmarks.get(1).longitude,droneMap.landmarks.get(1).latitude));
                }else{
                    path.add(counter+1, new LongLat(droneMap.landmarks.get(0).longitude,droneMap.landmarks.get(0).latitude));
                }
                counter = counter+2;
            }else{
                counter = counter+1;
            }
            pathChecker-=1;
        }
        System.out.println(path.size());
        pl.add(Point.fromLngLat(droneMap.getATLong(),droneMap.getATLat()));
        for(int i =0;i<path.size()-1;i++){
            currentPosition = path.get(i);
            targetPosition = path.get(i+1);
            while(!currentPosition.closeTo(targetPosition)){
                currentPosition = currentPosition.nextPosition(getAngle(currentPosition,targetPosition));
                pl.add(Point.fromLngLat(currentPosition.longitude,currentPosition.latitude));
                cost = cost +1;
            }
            cost = cost + 1;
        }
        System.out.println(cost);
    }

    public int getAngle(LongLat start, LongLat target){
        double tan = 0;
        if(target.longitude>start.longitude && target.latitude>start.latitude){
            tan = Math.atan(Math.abs((target.latitude-start.latitude)/(target.longitude-start.longitude))) * 180 / Math.PI;; //第一象限
        }else if(target.longitude<start.longitude && target.latitude>start.latitude){
            tan = (double)180-Math.atan(Math.abs((target.latitude-start.latitude)/(target.longitude-start.longitude))) * 180 / Math.PI;; //第二象限
        }else if(target.longitude<start.longitude && target.latitude<start.latitude){
            tan = (double)180+Math.atan(Math.abs((target.latitude-start.latitude)/(target.longitude-start.longitude))) * 180 / Math.PI;; //第三象限
        }else if(target.longitude>start.longitude && target.latitude<start.latitude){
            tan = (double)360-Math.atan(Math.abs((target.latitude-start.latitude)/(target.longitude-start.longitude))) * 180 / Math.PI;; //第四象限
        }else if(target.longitude>start.longitude && target.latitude==start.latitude){
            tan = Math.atan(Math.abs((target.latitude-start.latitude)/(target.longitude-start.longitude))) * 180 / Math.PI;; //第一象限和第四象限分界线
        }else if(target.longitude==start.longitude && target.latitude>start.latitude){
            tan = 90;
        }else if(target.longitude<start.longitude && target.latitude==start.latitude){
            tan = 180;
        }else if(target.longitude==start.longitude && target.latitude<start.latitude){
            tan = 270;
        }
        int angle = (int)tan;
        if(angle%10>=5){
            angle = angle/10*10+10;
        }else{
            angle = angle/10*10;
        }
        if(angle ==360){
            angle = 0;
        }
        return angle;
    }
    public String printRoute(){
        LineString lineString = LineString.fromLngLats(pl);
        Feature feature = Feature.fromGeometry(lineString);
        FeatureCollection featureCollection = FeatureCollection.fromFeature(feature);
        return featureCollection.toJson();
    }

    public boolean isNoFlyZone(double lng1, double lat1, double lng2, double lat2){
        boolean isCrossed = false;
        ArrayList<Line2D> noFlyZone2D = droneMap.getNoFlyZone();
        Line2D possiblePath = new Line2D.Double();
        possiblePath.setLine(lng1, lat1, lng2, lat2);
        for(Line2D line2D:noFlyZone2D){
            isCrossed = line2D.intersectsLine(possiblePath);
            if(isCrossed){
                break;
            }
        }
        return isCrossed;
    }

    public void sortOrders() throws SQLException {
        Comparator<Order> c = Collections.reverseOrder();
        orders.sort(c);
    }
    public void findOrderShopLocations(){
        for(int i = 0;i<orders.size();i++){
            orders.get(i).orderShopLocations = new ArrayList<>();
            for(int j = 0;j<orders.get(i).item.size();j++){
                String name = orders.get(i).item.get(j);
                ArrayList<MenuParser.Menu> menusList = menuParser.parseMenus();
                try{
                    for (MenuParser.Menu mi: menusList){
                        for(MenuParser.Menu.Item k: mi.menu){
                            if(k.item.equals(name)){
                                if(orders.get(i).orderShopLocations.contains(mi.location)){
                                }else {
                                    orders.get(i).orderShopLocations.add(mi.location);
                                }
                            }
                        }
                    }
                }catch (IllegalArgumentException | NullPointerException e){
                    e.printStackTrace();
                    System.exit(1); // Unsuccessful termination
                }
            }
        }
    }

    public void getShopLongLat(){
        for (Order order : orders) {
            order.routeLongLat = new ArrayList<>();
            for (int j = 0; j < order.orderShopLocations.size(); j++) {
                String threeWord = order.orderShopLocations.get(j);
                WordParser wordParser = new WordParser(menuParser.webPort);
                WordParser.Word word = wordParser.parseWord(threeWord);
                double lng = word.coordinates.lng;
                double lat = word.coordinates.lat;
                LongLat longLat = new LongLat(lng, lat);
                order.routeLongLat.add(longLat);
            }
        }
    }

    public void getDeliverToLongLat(){
        for (Order order : orders) {
            String deliverTo = order.deliverTo;
            WordParser wordParser = new WordParser(menuParser.webPort);
            WordParser.Word word = wordParser.parseWord(deliverTo);
            double lng = word.coordinates.lng;
            double lat = word.coordinates.lat;
            LongLat longLat = new LongLat(lng, lat);
            order.routeLongLat.add(longLat);
        }
    }
    public void createCoordinate(){
        path = new ArrayList<>();
        path.add(new LongLat(droneMap.getATLong(),droneMap.getATLat()));
        for(Order order:orders){
            path.addAll(order.routeLongLat);
        }
        path.add(new LongLat(droneMap.getATLong(),droneMap.getATLat()));
    }
    public void preparation() throws SQLException {
        sortOrders();
        findOrderShopLocations();
        getShopLongLat();
        getDeliverToLongLat();
        createCoordinate();
        droneMap.getLandMarks();
    }
}
