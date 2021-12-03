package uk.ac.ed.inf;

import java.awt.geom.Line2D;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class DroneUtils {
    DroneMap droneMap;

    DroneUtils(DroneMap droneMap){
        this.droneMap = droneMap;
    }

    /**
     * Get the angle of moving from one point to another, if the angle is not a multiple of 10, then the result will be rounded.
     * The value range of the angle is a multiple of 10 between 0 and 350.
     *
     * @param current - A LongLat object that represents the current position
     * @param target  - A LongLat object that represents the target position
     * @return an angle of moving from one point to another point
     * */
    public int getAngle(LongLat current, LongLat target){
        double tan = 0;
        if(target.longitude>current.longitude && target.latitude>current.latitude){
            tan = Math.atan(Math.abs((target.latitude-current.latitude)/(target.longitude-current.longitude))) * 180 / Math.PI; //第一象限
        }else if(target.longitude<current.longitude && target.latitude>current.latitude){
            tan = (double)180-Math.atan(Math.abs((target.latitude-current.latitude)/(target.longitude-current.longitude))) * 180 / Math.PI; //第二象限
        }else if(target.longitude<current.longitude && target.latitude<current.latitude){
            tan = (double)180+Math.atan(Math.abs((target.latitude-current.latitude)/(target.longitude-current.longitude))) * 180 / Math.PI; //第三象限
        }else if(target.longitude>current.longitude && target.latitude<current.latitude){
            tan = (double)360-Math.atan(Math.abs((target.latitude-current.latitude)/(target.longitude-current.longitude))) * 180 / Math.PI; //第四象限
        }else if(target.longitude>current.longitude && target.latitude==current.latitude){
            tan = Math.atan(Math.abs((target.latitude-current.latitude)/(target.longitude-current.longitude))) * 180 / Math.PI; //第一象限和第四象限分界线
        }else if(target.longitude==current.longitude && target.latitude>current.latitude){
            tan = 90;
        }else if(target.longitude<current.longitude && target.latitude==current.latitude){
            tan = 180;
        }else if(target.longitude==current.longitude && target.latitude<current.latitude){
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

    /**
     * Find the valid landmarks that will not cross the no-fly zone from the current position
     * Choose the closest landmark from the current position
     *
     * @param targetPosition - target position to go
     * @param currentPosition - current position right now
     * @return the valid landmark. If two landmarks are both valid, then return the one with the smaller sum of (distance between current position and landmark) and (distance between target position and landmark)
     * @throws NullPointerException If there is no valid landmark
     * */
    public LongLat getClosestLandmark(LongLat targetPosition,LongLat currentPosition){
        ArrayList<LongLat> landmarks = droneMap.getLandMarks();
        int flag0 = 0;
        int flag1 = 0;
        if(!isNoFlyZone(currentPosition.longitude,currentPosition.latitude,landmarks.get(0).longitude,landmarks.get(0).latitude)
        && !isNoFlyZone(targetPosition.longitude,targetPosition.latitude,landmarks.get(0).longitude,landmarks.get(0).latitude)){
            flag0 = 1;
        }
        if(!isNoFlyZone(currentPosition.longitude,currentPosition.latitude,landmarks.get(1).longitude,landmarks.get(1).latitude)&&
                !isNoFlyZone(targetPosition.longitude,targetPosition.latitude,landmarks.get(1).longitude,landmarks.get(1).latitude)){
            flag1 = 1;
        }
        LongLat landmark = null;
        if(flag0 == 1 && flag1 == 1){
            if(targetPosition.distanceTo(landmarks.get(0))+currentPosition.distanceTo(landmarks.get(0))<targetPosition.distanceTo(landmarks.get(1))+currentPosition.distanceTo(landmarks.get(1))){
                landmark =  landmarks.get(0);
            }else{
                landmark =  landmarks.get(1);
            }
        }else if(flag0 == 1){
            landmark = landmarks.get(0);
        }else if(flag1 == 1){
            landmark = landmarks.get(1);
        }
        return landmark;
    }

    /**
     * Given the longitude and latitude of two points, check if the line between these two points will cross the confinement area
     *
     * @param lng1 - longitude of the first point
     * @param lat1 - latitude of the first point
     * @param lng2 - longitude of the second point
     * @param lat2 - latitude of the second point
     * @return a boolean value true if the line between these two points will cross the confinement area and vice versa
     * */
    public boolean isConfinementArea(double lng1, double lat1, double lng2, double lat2){
        boolean isCrossed = false;
        ArrayList<Line2D> confinementArea2D = droneMap.getConfinementArea();
        Line2D possiblePath = new Line2D.Double();
        possiblePath.setLine(lng1, lat1, lng2, lat2);
        for(Line2D line2D:confinementArea2D){
            isCrossed = line2D.intersectsLine(possiblePath);
            if(isCrossed){
                break;
            }
        }
        return isCrossed;
    }

    /**
     * Given the longitude and latitude of two points, check if the line between these two points will cross the no-Fly Zone
     *
     * @param lng1 - longitude of the first point
     * @param lat1 - latitude of the first point
     * @param lng2 - longitude of the second point
     * @param lat2 - latitude of the second point
     * @return a boolean value true if the line between these two points will cross the confinement area and vice versa
     * */
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

    /**
     * When the calculated angle in each move is invalid, use this method to correct angle by shift 10 degree left and right (increment by 10)
     *
     * @param currentPosition - A LongLat object that represents the current position
     * @param targetPosition  - A LongLat object that represents the target position
     * @param angle  - A list of type int that contains all the possible correction angle values
     * @return an int-type angle that is valid so that the next move will not cross the no-fly zone and confinement area
     * @throws IndexOutOfBoundsException if all the correction angle is not valid
     * */
    public int getShiftAngle(LongLat currentPosition, LongLat targetPosition, int[] angle){
        int counter = 0;
        int shiftAngle = getAngle(currentPosition,targetPosition);
        while(isNoFlyZone(currentPosition.nextPosition(shiftAngle).longitude , currentPosition.nextPosition(shiftAngle).latitude , currentPosition.longitude , currentPosition.latitude)){
            shiftAngle = getAngle(currentPosition,targetPosition) + angle[counter];
            if(shiftAngle<0){
                shiftAngle = 360 - Math.abs(angle[counter]);
            }
            if(shiftAngle>350){
                shiftAngle = shiftAngle % 360;
            }
            counter +=1;
        }
        return shiftAngle;
    }

    /**
     * Sort orders based on the price of each order as descending trend
     *
     * @param orders - All orders
     * */
    public void sortOrders(ArrayList<Order> orders) {
        Comparator<Order> c = Collections.reverseOrder();
        orders.sort(c);
    }

    /**
     * Find the three-word location for each order in the menu on the website
     *
     * @param orders - All orders
     * @param menuParser - A MenuParser object that helps parse the json data
     * @throws IllegalArgumentException if arguments are not correct
     * @throws NullPointerException if reference of the shop location is null
     * */
    public void findOrderShopLocations(ArrayList<Order> orders, MenuParser menuParser){
        for (Order order : orders) {
            order.setOrderShopLocations(new ArrayList<>());
            for (int j = 0; j < order.getItem().size(); j++) { //有可能是需要去两个商店取餐
                String name = order.getItem().get(j);
                ArrayList<MenuParser.Menu> menusList = menuParser.parseMenus();
                try {
                    for (MenuParser.Menu mi : menusList) {
                        for (MenuParser.Menu.Item k : mi.menu) {
                            if (k.item.equals(name)) {
                                if (!order.getOrderShopLocations().contains(mi.location)) {
                                    order.getOrderShopLocations().add(mi.location);
                                }
                            }
                        }
                    }
                } catch (IllegalArgumentException | NullPointerException e) {
                    e.printStackTrace();
                    System.exit(1); // Unsuccessful termination
                }
            }
        }
    }


    /**
     * Add each order's pick-up location(s) and deliver-to location into a list of LongLat attribute in class Order
     *
     * @param orders - All orders
     * @param menuParser - A MenuParser object that helps parse the json data
     * */
    public void getRouteLongLat(ArrayList<Order> orders, MenuParser menuParser){
        for (Order order : orders) {
            order.setRouteLongLat(new ArrayList<>());
            for (int j = 0; j < order.getOrderShopLocations().size(); j++) {
                String threeWord = order.getOrderShopLocations().get(j);
                WordParser wordParser = new WordParser(menuParser.webPort);
                WordParser.Word word = wordParser.parseWord(threeWord);
                double lng = word.coordinates.lng;
                double lat = word.coordinates.lat;
                LongLat longLat = new LongLat(lng, lat);
                order.getRouteLongLat().add(longLat);
            }
            String deliverTo = order.getDeliverTo();
            WordParser wordParser = new WordParser(menuParser.webPort);
            WordParser.Word word = wordParser.parseWord(deliverTo);
            double lng = word.coordinates.lng;
            double lat = word.coordinates.lat;
            LongLat longLat = new LongLat(lng, lat);
            order.getRouteLongLat().add(longLat);
        }
    }
}
