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

    public int getAngle(LongLat start, LongLat target){
        double tan = 0;
        if(target.longitude>start.longitude && target.latitude>start.latitude){
            tan = Math.atan(Math.abs((target.latitude-start.latitude)/(target.longitude-start.longitude))) * 180 / Math.PI; //第一象限
        }else if(target.longitude<start.longitude && target.latitude>start.latitude){
            tan = (double)180-Math.atan(Math.abs((target.latitude-start.latitude)/(target.longitude-start.longitude))) * 180 / Math.PI; //第二象限
        }else if(target.longitude<start.longitude && target.latitude<start.latitude){
            tan = (double)180+Math.atan(Math.abs((target.latitude-start.latitude)/(target.longitude-start.longitude))) * 180 / Math.PI; //第三象限
        }else if(target.longitude>start.longitude && target.latitude<start.latitude){
            tan = (double)360-Math.atan(Math.abs((target.latitude-start.latitude)/(target.longitude-start.longitude))) * 180 / Math.PI; //第四象限
        }else if(target.longitude>start.longitude && target.latitude==start.latitude){
            tan = Math.atan(Math.abs((target.latitude-start.latitude)/(target.longitude-start.longitude))) * 180 / Math.PI; //第一象限和第四象限分界线
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

    //confinementArea 判断
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

    //TODO sort orders based on the price
    public void sortOrders(ArrayList<Order> orders) throws SQLException {
        Comparator<Order> c = Collections.reverseOrder();
        orders.sort(c);
    }

    //TODO find the order pick up locations(1 or 2)
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
                                if (order.getOrderShopLocations().contains(mi.location)) {
                                } else {
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

    //获取每个订单的pick up 经纬度和delivery to 经纬度，并顺序排列（先pick up 再delivery to）
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
