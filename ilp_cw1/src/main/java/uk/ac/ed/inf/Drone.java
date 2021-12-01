package uk.ac.ed.inf;
import com.mapbox.geojson.*;
import com.mapbox.geojson.Point;
import java.awt.geom.Line2D;
import java.sql.SQLException;
import java.util.*;
import java.lang.Math;

public class Drone {
    // Fields
    DroneMap map;
    MenuParser menuParser;
    Database database;
    DroneMap droneMap;
    LongLat currentPosition;
    LongLat targetPosition;
    ArrayList<Order> orders;
    ArrayList<Point> pl = new ArrayList<>();
    ArrayList<FlightPath> flightPaths = new ArrayList<>();
    int cost = 0;
    int[] shift = new int[]{10,-10,20,-20,30,-30,40,-40,50,-50,60,-60,70,-70,80,-80,90,-90,100,-100,110,-110,120,-120,130,-130,140,-140,150,-150,160,-160,170,-170,180,-180,190,-190,200,-200,210,-210,220,-220,230,-230,240,-240,250,-250,260,-260,270,-270,280,-280,290,-290,300,-300,310,-310,320,-320,330,-330,340,-340,350,-350};



    // Class Constructor
    Drone(DroneMap map, MenuParser menuParser, Database database, DroneMap droneMap) throws SQLException {
        this.map = map;
        this.menuParser = menuParser; // get the location of stores
        this.database = database; // connect to database and get order details
        this.droneMap = droneMap;
        this.orders = database.readOrdersFromDatabase();
    }

    public LongLat setStartPosition(){
        return new LongLat(droneMap.getATLong(),droneMap.getATLat());
    }

    public void findPath(){
        //设置起点
        currentPosition = setStartPosition();
        //添加起点的点
        pl.add(Point.fromLngLat(droneMap.getATLong(),droneMap.getATLat()));
        int outOfBattery = 0;
        int fullBattery = 0;
        //对于每一个订单开始
        for(Order order:orders){
            //对于订单中每一个target
            if(outOfBattery==1){
                break;
            }
            int routeCounter = 0;
            for(LongLat target: order.getRouteLongLat()){
                targetPosition = target;
                //每个target之间判断一次，查看是否需要landmark，假如过禁飞区则去landmark然后再去target
                if(isNoFlyZone(currentPosition.longitude , currentPosition.latitude , targetPosition.longitude , targetPosition.latitude)&& isConfinementArea(currentPosition.longitude , currentPosition.latitude , targetPosition.longitude , targetPosition.latitude)){
                    LongLat closestLandmark  = getClosestLandmark(targetPosition,currentPosition);
                    //不接近终点就不停
                    while (!currentPosition.closeTo(closestLandmark)){
                        //如果当前move没有进禁飞区
                        if(cost>=1450){
                            outOfBattery = 1;
                            break;
                        }
                        if(!isNoFlyZone(currentPosition.longitude , currentPosition.latitude , targetPosition.longitude , targetPosition.latitude)){
                            break;
                        }
                        if(!isNoFlyZone(currentPosition.nextPosition(getAngle(currentPosition,closestLandmark)).longitude , currentPosition.nextPosition(getAngle(currentPosition,closestLandmark)).latitude , currentPosition.longitude , currentPosition.latitude)){
                            FlightPath flightPath = new FlightPath(order.getOrderNo(),currentPosition.longitude,currentPosition.latitude,getAngle(currentPosition,closestLandmark),currentPosition.nextPosition(getAngle(currentPosition,closestLandmark)).longitude,currentPosition.nextPosition(getAngle(currentPosition,closestLandmark)).latitude);
                            flightPaths.add(flightPath);
                            System.out.println(flightPath.getOrderNo());
                            System.out.println(flightPath.getFromLongitude());
                            System.out.println(flightPath.getFromLatitude());
                            System.out.println(flightPath.getAngle());
                            System.out.println(flightPath.getToLongitude());
                            System.out.println(flightPath.getToLatitude());
                            currentPosition = currentPosition.nextPosition(getAngle(currentPosition,closestLandmark));
                            pl.add(Point.fromLngLat(currentPosition.longitude,currentPosition.latitude));
                            cost+=1;

                        }else{
                            //只要一直穿过禁飞区，就修正角度
                            int counter = 0;
                            int shiftAngle = getAngle(currentPosition,closestLandmark);
                            while(isNoFlyZone(currentPosition.nextPosition(shiftAngle).longitude , currentPosition.nextPosition(shiftAngle).latitude , currentPosition.longitude , currentPosition.latitude)){
                                shiftAngle = getAngle(currentPosition,closestLandmark) + shift[counter];
                                if(shiftAngle<0){
                                    shiftAngle = 360 - Math.abs(shift[counter]);
                                }
                                if(shiftAngle>350){
                                    shiftAngle = shiftAngle % 360;
                                }
                                counter +=1;
                            }
                            FlightPath flightPath = new FlightPath(order.getOrderNo(),currentPosition.longitude,currentPosition.latitude,shiftAngle,currentPosition.nextPosition(getAngle(currentPosition,closestLandmark)).longitude,currentPosition.nextPosition(getAngle(currentPosition,closestLandmark)).latitude);
                            flightPaths.add(flightPath);
                            System.out.println(flightPath.getOrderNo());
                            System.out.println(flightPath.getFromLongitude());
                            System.out.println(flightPath.getFromLatitude());
                            System.out.println(flightPath.getAngle());
                            System.out.println(flightPath.getToLongitude());
                            System.out.println(flightPath.getToLatitude());
                            currentPosition = currentPosition.nextPosition(shiftAngle);
                            pl.add(Point.fromLngLat(currentPosition.longitude,currentPosition.latitude));
                            cost+=1;
                        }

                    }
                    while(!currentPosition.closeTo(targetPosition)){
                        //如果当前move没有进禁飞区
                        if(cost>=1450){
                            outOfBattery = 1;
                            break;
                        }
                        if(!isNoFlyZone(currentPosition.nextPosition(getAngle(currentPosition,targetPosition)).longitude , currentPosition.nextPosition(getAngle(currentPosition,targetPosition)).latitude , currentPosition.longitude , currentPosition.latitude) ){
                            FlightPath flightPath = new FlightPath(order.getOrderNo(),currentPosition.longitude,currentPosition.latitude,getAngle(currentPosition,targetPosition),currentPosition.nextPosition(getAngle(currentPosition,targetPosition)).longitude,currentPosition.nextPosition(getAngle(currentPosition,targetPosition)).latitude);
                            flightPaths.add(flightPath);
                            System.out.println(flightPath.getOrderNo());
                            System.out.println(flightPath.getFromLongitude());
                            System.out.println(flightPath.getFromLatitude());
                            System.out.println(flightPath.getAngle());
                            System.out.println(flightPath.getToLongitude());
                            System.out.println(flightPath.getToLatitude());
                            currentPosition = currentPosition.nextPosition(getAngle(currentPosition,targetPosition));
                            pl.add(Point.fromLngLat(currentPosition.longitude,currentPosition.latitude));
                            cost+=1;
                        }else{
                            //只要一直穿过禁飞区，就修正角度
                            int counter = 0;
                            int shiftAngle = getAngle(currentPosition,targetPosition);
                            while(isNoFlyZone(currentPosition.nextPosition(shiftAngle).longitude , currentPosition.nextPosition(shiftAngle).latitude , currentPosition.longitude , currentPosition.latitude)){
                                shiftAngle = getAngle(currentPosition,targetPosition) + shift[counter];
                                if(shiftAngle<0){
                                    shiftAngle = 360 - Math.abs(shift[counter]);
                                }
                                if(shiftAngle>350){
                                    shiftAngle = shiftAngle % 360;
                                }
                                counter +=1;
                            }
                            FlightPath flightPath = new FlightPath(order.getOrderNo(),currentPosition.longitude,currentPosition.latitude,shiftAngle,currentPosition.nextPosition(getAngle(currentPosition,targetPosition)).longitude,currentPosition.nextPosition(getAngle(currentPosition,targetPosition)).latitude);
                            flightPaths.add(flightPath);
                            System.out.println(flightPath.getOrderNo());
                            System.out.println(flightPath.getFromLongitude());
                            System.out.println(flightPath.getFromLatitude());
                            System.out.println(flightPath.getAngle());
                            System.out.println(flightPath.getToLongitude());
                            System.out.println(flightPath.getToLatitude());
                            currentPosition = currentPosition.nextPosition(shiftAngle);
                            pl.add(Point.fromLngLat(currentPosition.longitude,currentPosition.latitude));
                            cost += 1;
                        }
                    }
                    if(currentPosition.closeTo(targetPosition)) {
                        routeCounter += 1;
                    }
                }else{
                    while(!currentPosition.closeTo(targetPosition)){
                        if(cost>=1450){
                            outOfBattery = 1;
                            break;
                        }
                        //如果当前move没有进禁飞区
                        if(!isNoFlyZone(currentPosition.nextPosition(getAngle(currentPosition,targetPosition)).longitude , currentPosition.nextPosition(getAngle(currentPosition,targetPosition)).latitude , currentPosition.longitude , currentPosition.latitude) ){
                            FlightPath flightPath = new FlightPath(order.getOrderNo(),currentPosition.longitude,currentPosition.latitude,getAngle(currentPosition,targetPosition),currentPosition.nextPosition(getAngle(currentPosition,targetPosition)).longitude,currentPosition.nextPosition(getAngle(currentPosition,targetPosition)).latitude);
                            flightPaths.add(flightPath);
                            System.out.println(flightPath.getOrderNo());
                            System.out.println(flightPath.getFromLongitude());
                            System.out.println(flightPath.getFromLatitude());
                            System.out.println(flightPath.getAngle());
                            System.out.println(flightPath.getToLongitude());
                            System.out.println(flightPath.getToLatitude());
                            currentPosition = currentPosition.nextPosition(getAngle(currentPosition,targetPosition));
                            pl.add(Point.fromLngLat(currentPosition.longitude,currentPosition.latitude));
                            cost+=1;
                        }
                        else{
                            //只要一直穿过禁飞区，就修正角度
                            int counter = 0;
                            int shiftAngle = getAngle(currentPosition,targetPosition);
                            while(isNoFlyZone(currentPosition.nextPosition(shiftAngle).longitude , currentPosition.nextPosition(shiftAngle).latitude , currentPosition.longitude , currentPosition.latitude)){
                                shiftAngle = getAngle(currentPosition,targetPosition) + shift[counter];
                                if(shiftAngle<0){
                                    shiftAngle = 360 - Math.abs(shift[counter]);
                                }
                                if(shiftAngle>350){
                                    shiftAngle = shiftAngle % 360;
                                }
                                counter +=1;
                            }
                            FlightPath flightPath = new FlightPath(order.getOrderNo(),currentPosition.longitude,currentPosition.latitude,shiftAngle,currentPosition.nextPosition(getAngle(currentPosition,targetPosition)).longitude,currentPosition.nextPosition(getAngle(currentPosition,targetPosition)).latitude);
                            flightPaths.add(flightPath);
                            System.out.println(flightPath.getOrderNo());
                            System.out.println(flightPath.getFromLongitude());
                            System.out.println(flightPath.getFromLatitude());
                            System.out.println(flightPath.getAngle());
                            System.out.println(flightPath.getToLongitude());
                            System.out.println(flightPath.getToLatitude());
                            currentPosition = currentPosition.nextPosition(shiftAngle);
                            pl.add(Point.fromLngLat(currentPosition.longitude,currentPosition.latitude));
                            cost += 1;
                        }
                    }
                    if(currentPosition.closeTo(targetPosition)) {
                        routeCounter += 1;
                    }
                }
                //如果订单未完成，直接break
                if(outOfBattery==1){
                    break;
                }
                cost += 1;
            }
            if(routeCounter == order.getRouteLongLat().size()){
                order.setIsDelivered(true);
                System.out.println("The order("+order.getOrderNo()+") has been finished");
            }else{
                System.out.println("We did not finish all the orders");
            }
        }
        if(outOfBattery == 0){
            fullBattery = 1;
        }

        //回到at，如果电量不足50
        if(outOfBattery==1 || fullBattery==1){
            if(isNoFlyZone(currentPosition.longitude , currentPosition.latitude , droneMap.getATLong() , droneMap.getATLat()) && isConfinementArea(currentPosition.longitude , currentPosition.latitude , droneMap.getATLong() , droneMap.getATLat())){
                LongLat closestLandmark  = getClosestLandmark(new LongLat(droneMap.getATLong() , droneMap.getATLat()),currentPosition);
                while(!currentPosition.closeTo(closestLandmark)){
                    if(!isNoFlyZone(currentPosition.nextPosition(getAngle(currentPosition,closestLandmark)).longitude , currentPosition.nextPosition(getAngle(currentPosition,closestLandmark)).latitude , currentPosition.longitude , currentPosition.latitude) ){
                        FlightPath flightPath = new FlightPath(null,currentPosition.longitude,currentPosition.latitude,getAngle(currentPosition,closestLandmark),currentPosition.nextPosition(getAngle(currentPosition,closestLandmark)).longitude,currentPosition.nextPosition(getAngle(currentPosition,closestLandmark)).latitude);
                        flightPaths.add(flightPath);
                        System.out.println(flightPath.getOrderNo());
                        System.out.println(flightPath.getFromLongitude());
                        System.out.println(flightPath.getFromLatitude());
                        System.out.println(flightPath.getAngle());
                        System.out.println(flightPath.getToLongitude());
                        System.out.println(flightPath.getToLatitude());
                        currentPosition = currentPosition.nextPosition(getAngle(currentPosition,closestLandmark));
                    }else{
                        //只要一直穿过禁飞区，就修正角度
                        int counter = 0;
                        int shiftAngle = getAngle(currentPosition,closestLandmark);
                        while(isNoFlyZone(currentPosition.nextPosition(shiftAngle).longitude , currentPosition.nextPosition(shiftAngle).latitude , currentPosition.longitude , currentPosition.latitude)){
                            //System.out.println("asdhjkasdkjh");

                            shiftAngle = getAngle(currentPosition,closestLandmark) + shift[counter];
                            if(shiftAngle<0){
                                shiftAngle = 360 - Math.abs(shift[counter]);
                            }
                            if(shiftAngle>350){
                                shiftAngle = shiftAngle % 360;
                            }
                            counter +=1;
                        }
                        FlightPath flightPath = new FlightPath(null,currentPosition.longitude,currentPosition.latitude,shiftAngle,currentPosition.nextPosition(getAngle(currentPosition,closestLandmark)).longitude,currentPosition.nextPosition(getAngle(currentPosition,closestLandmark)).latitude);
                        flightPaths.add(flightPath);
                        System.out.println(flightPath.getOrderNo());
                        System.out.println(flightPath.getFromLongitude());
                        System.out.println(flightPath.getFromLatitude());
                        System.out.println(flightPath.getAngle());
                        System.out.println(flightPath.getToLongitude());
                        System.out.println(flightPath.getToLatitude());
                        currentPosition = currentPosition.nextPosition(shiftAngle);
                    }
                    pl.add(Point.fromLngLat(currentPosition.longitude,currentPosition.latitude));
                    cost+=1;
                }
                while(!currentPosition.closeTo(new LongLat(droneMap.getATLong(), droneMap.getATLat()))){
                    //如果当前move没有进禁飞区
                    if(!isNoFlyZone(currentPosition.nextPosition(getAngle(currentPosition,new LongLat(droneMap.getATLong() , droneMap.getATLat()))).longitude , currentPosition.nextPosition(getAngle(currentPosition,new LongLat(droneMap.getATLong() , droneMap.getATLat()))).latitude , currentPosition.longitude , currentPosition.latitude) ){
                        FlightPath flightPath = new FlightPath(null,currentPosition.longitude,currentPosition.latitude,getAngle(currentPosition,targetPosition),currentPosition.nextPosition(getAngle(currentPosition,targetPosition)).longitude,currentPosition.nextPosition(getAngle(currentPosition,targetPosition)).latitude);
                        flightPaths.add(flightPath);
                        System.out.println(flightPath.getOrderNo());
                        System.out.println(flightPath.getFromLongitude());
                        System.out.println(flightPath.getFromLatitude());
                        System.out.println(flightPath.getAngle());
                        System.out.println(flightPath.getToLongitude());
                        System.out.println(flightPath.getToLatitude());
                        currentPosition = currentPosition.nextPosition(getAngle(currentPosition,new LongLat(droneMap.getATLong() , droneMap.getATLat())));
                        pl.add(Point.fromLngLat(currentPosition.longitude,currentPosition.latitude));
                        cost+=1;
                    }else{
                        //只要一直穿过禁飞区，就修正角度
                        int counter = 0;
                        int shiftAngle = getAngle(currentPosition,new LongLat(droneMap.getATLong() , droneMap.getATLat()));
                        while(isNoFlyZone(currentPosition.nextPosition(shiftAngle).longitude , currentPosition.nextPosition(shiftAngle).latitude , currentPosition.longitude , currentPosition.latitude)){
                            shiftAngle = getAngle(currentPosition,new LongLat(droneMap.getATLong() , droneMap.getATLat())) + shift[counter];
                            if(shiftAngle<0){
                                shiftAngle = 360 - Math.abs(shift[counter]);
                            }
                            if(shiftAngle>350){
                                shiftAngle = shiftAngle % 360;
                            }
                            counter +=1;
                        }
                        FlightPath flightPath = new FlightPath(null,currentPosition.longitude,currentPosition.latitude,shiftAngle,currentPosition.nextPosition(getAngle(currentPosition,targetPosition)).longitude,currentPosition.nextPosition(getAngle(currentPosition,targetPosition)).latitude);
                        flightPaths.add(flightPath);
                        System.out.println(flightPath.getOrderNo());
                        System.out.println(flightPath.getFromLongitude());
                        System.out.println(flightPath.getFromLatitude());
                        System.out.println(flightPath.getAngle());
                        System.out.println(flightPath.getToLongitude());
                        System.out.println(flightPath.getToLatitude());
                        currentPosition = currentPosition.nextPosition(shiftAngle);
                        pl.add(Point.fromLngLat(currentPosition.longitude,currentPosition.latitude));
                        cost += 1;
                    }
                }
            }else{
                while(!currentPosition.closeTo(new LongLat(droneMap.getATLong(), droneMap.getATLat()))){
                    //如果当前move没有进禁飞区
                    if(!isNoFlyZone(currentPosition.nextPosition(getAngle(currentPosition,new LongLat(droneMap.getATLong() , droneMap.getATLat()))).longitude , currentPosition.nextPosition(getAngle(currentPosition,new LongLat(droneMap.getATLong() , droneMap.getATLat()))).latitude , currentPosition.longitude , currentPosition.latitude) ){
                        FlightPath flightPath = new FlightPath(null,currentPosition.longitude,currentPosition.latitude,getAngle(currentPosition,targetPosition),currentPosition.nextPosition(getAngle(currentPosition,targetPosition)).longitude,currentPosition.nextPosition(getAngle(currentPosition,targetPosition)).latitude);
                        flightPaths.add(flightPath);
                        System.out.println(flightPath.getOrderNo());
                        System.out.println(flightPath.getFromLongitude());
                        System.out.println(flightPath.getFromLatitude());
                        System.out.println(flightPath.getAngle());
                        System.out.println(flightPath.getToLongitude());
                        System.out.println(flightPath.getToLatitude());
                        currentPosition = currentPosition.nextPosition(getAngle(currentPosition,new LongLat(droneMap.getATLong() , droneMap.getATLat())));
                        pl.add(Point.fromLngLat(currentPosition.longitude,currentPosition.latitude));
                        cost+=1;
                    }
                    else{
                        //只要一直穿过禁飞区，就修正角度
                        int counter = 0;
                        int shiftAngle = getAngle(currentPosition,new LongLat(droneMap.getATLong() , droneMap.getATLat()));
                        while(isNoFlyZone(currentPosition.nextPosition(shiftAngle).longitude , currentPosition.nextPosition(shiftAngle).latitude , currentPosition.longitude , currentPosition.latitude)){
                            shiftAngle = getAngle(currentPosition,new LongLat(droneMap.getATLong() , droneMap.getATLat())) + shift[counter];
                            if(shiftAngle<0){
                                shiftAngle = 360 - Math.abs(shift[counter]);
                            }
                            if(shiftAngle>350){
                                shiftAngle = shiftAngle % 360;
                            }
                            counter +=1;
                        }
                        FlightPath flightPath = new FlightPath(null,currentPosition.longitude,currentPosition.latitude,shiftAngle,currentPosition.nextPosition(getAngle(currentPosition,targetPosition)).longitude,currentPosition.nextPosition(getAngle(currentPosition,targetPosition)).latitude);
                        flightPaths.add(flightPath);
                        System.out.println(flightPath.getOrderNo());
                        System.out.println(flightPath.getFromLongitude());
                        System.out.println(flightPath.getFromLatitude());
                        System.out.println(flightPath.getAngle());
                        System.out.println(flightPath.getToLongitude());
                        System.out.println(flightPath.getToLatitude());
                        currentPosition = currentPosition.nextPosition(shiftAngle);
                        pl.add(Point.fromLngLat(currentPosition.longitude,currentPosition.latitude));
                        cost += 1;
                    }
                }
            }
        }
    }

    //找更小的landmark
    public LongLat getClosestLandmark(LongLat targetPosition,LongLat currentPosition){
        ArrayList<LongLat> landmarks = droneMap.getLandMarks();
        int flag0 = 0;
        int flag1 = 0;

        if(!isNoFlyZone(currentPosition.longitude,currentPosition.latitude,landmarks.get(0).longitude,landmarks.get(0).latitude)){
            flag0 = 1;
        }
        if(!isNoFlyZone(currentPosition.longitude,currentPosition.latitude,landmarks.get(1).longitude,landmarks.get(1).latitude)){
            flag1 = 1;
        }
        LongLat landmark = null;
        if(flag0 == 1 && flag1 == 1){
            if(targetPosition.distanceTo(landmarks.get(0))<targetPosition.distanceTo(landmarks.get(1))){
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


    //禁飞区判断
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


    //TODO sort orders based on the price
    public void sortOrders() throws SQLException {
        Comparator<Order> c = Collections.reverseOrder();
        orders.sort(c);
    }

    //TODO find the order pick up locations(1 or 2)
    public void findOrderShopLocations(){
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
                                    continue;
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
            //System.out.println(orders.get(i).orderShopLocations);
        }
    }

    //获取每个订单的pick up 经纬度和delivery to 经纬度，并顺序排列（先pick up 再delivery to）
    public void getRouteLongLat(){
        for (Order order : orders) {
            order.setRouteLongLat(new ArrayList<>());
            //System.out.println(order.orderShopLocations.size());
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
            //System.out.println(order.routeLongLat);
        }
    }


    //关于是否考虑剩余电量完成剩余订单并返回at
    //在每走一步之后，我们进行一次判断，如果剩余电量还有50的话就直接返回at




    public void preparation() throws SQLException {
        sortOrders(); //订单排序
        findOrderShopLocations(); //找到pick up三字地址
        getRouteLongLat(); //set每个订单的target
    }
}
