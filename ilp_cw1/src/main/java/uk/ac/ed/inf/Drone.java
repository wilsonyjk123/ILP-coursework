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
    LongLat startPosition;
    LongLat currentPosition;
    LongLat nextPosition;
    LongLat targetPosition;
    ArrayList<Order> orders;
    ArrayList<LongLat> path;
    ArrayList<Point> pl = new ArrayList<>();
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
        for(int i =0;i<path.size()-1;i++){
            startPosition = path.get(i);
            targetPosition = path.get(i+1);
            if(isNoFlyZone(startPosition.longitude,startPosition.latitude,targetPosition.longitude,targetPosition.latitude)){
                if(isNoFlyZone(startPosition.longitude,startPosition.longitude,droneMap.landmarks.get(0).longitude,droneMap.landmarks.get(0).latitude)){
                    path.add(i+1,droneMap.landmarks.get(1));//if the first landmark is not working then add the another one
                }else{
                    path.add(i+1,droneMap.landmarks.get(0));
                }
                targetPosition = path.get(i+1);
            }
            currentPosition = startPosition;
            int offset = (int)(getAngle(startPosition,targetPosition))/10*10;
            int pointer = 0;
            while(!currentPosition.closeTo(targetPosition)){
                if(getAngle(currentPosition,targetPosition)%10!=0){
                    if(pointer%2==0){
                        pointer = pointer+1;
                        nextPosition = currentPosition.nextPosition(offset);
                        pl.add(Point.fromLngLat(currentPosition.longitude,currentPosition.latitude));
                        pl.add(Point.fromLngLat(nextPosition.longitude,nextPosition.latitude));
                        currentPosition = nextPosition;
                        cost +=1;

                    }else{
                        pointer = pointer+1;
                        nextPosition = currentPosition.nextPosition(offset+10);
                        pl.add(Point.fromLngLat(currentPosition.longitude,currentPosition.latitude));
                        pl.add(Point.fromLngLat(nextPosition.longitude,nextPosition.latitude));
                        currentPosition = nextPosition;
                        cost +=1;
                    }
                }else{
                    nextPosition = currentPosition.nextPosition( (int)(getAngle(startPosition,targetPosition)));
                    pl.add(Point.fromLngLat(currentPosition.longitude,currentPosition.latitude));
                    pl.add(Point.fromLngLat(nextPosition.longitude,nextPosition.latitude));
                    currentPosition = nextPosition;
                    cost +=1;
                }
            }
            cost +=1;
        }
    }

    public double getAngle(LongLat start, LongLat target){
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
            tan = 90; //第一象限和第而象限分界线
        }else if(target.longitude<start.longitude && target.latitude==start.latitude){
            tan = 180;
        }else if(target.longitude==start.longitude && target.latitude<start.latitude){
            tan = 270; //第三象限和第四象限分界线
        }
        return tan;
    }

    //TODO 角度取区间值

    public String printRoute(){
        LineString lineString = LineString.fromLngLats(pl);
        Feature feature = Feature.fromGeometry(lineString);
        FeatureCollection featureCollection = FeatureCollection.fromFeature(feature);
        String fc = featureCollection.toJson();
        return fc;
    }


    public boolean isNoFlyZone(double lng1, double lat1, double lng2, double lat2){
        Line2D line2D = new Line2D.Double();
        boolean isNOFlyZone = false; //True if it does not pass the NFZ, False if it passes the NFZ
        for(Line line:droneMap.lines){
            isNOFlyZone = Line2D.linesIntersect(line.point1.longitude,line.point1.latitude,line.point2.longitude,line.point2.longitude,lng1,lat1,lng2,lat2);
        }
        return isNOFlyZone;
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
        droneMap.getNoFlyZone();
    }
}
