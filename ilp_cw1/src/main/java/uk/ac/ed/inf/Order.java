package uk.ac.ed.inf;

import java.util.ArrayList;

public class Order implements Comparable<Order> {
    private final String orderNo; //数据库读取时初始化
    private final String deliveryDate; //数据库读取时初始化
    private final String customer; //数据库读取时初始化
    private final String deliverTo; //数据库读取时初始化
    private final ArrayList<String> item; //数据库读取时初始化
    private ArrayList<String> orderShopLocations; //findOrderShopLocations中初始化，获取订单的三字地址
    private final Integer price;
    private ArrayList<LongLat> routeLongLat; //订单
    private boolean isDelivered = false;



    Order(String orderNo, String deliveryDate, String customer, String deliverTo, ArrayList<String> item, Integer price){
        this.orderNo = orderNo;
        this.deliveryDate = deliveryDate;
        this.customer = customer;
        this.deliverTo = deliverTo;;
        this.item = item;
        this.price = price;
    }

    public String getOrderNo() { return orderNo; }

    public String getDeliveryDate(){ return deliveryDate; }

    public String getCustomer(){ return customer; }

    public String getDeliverTo(){ return deliverTo;}

    public ArrayList<String> getItem(){ return item; }

    public ArrayList<String> getOrderShopLocations(){ return orderShopLocations; }

    public void setOrderShopLocations(ArrayList<String> strings){ orderShopLocations = strings;}

    public Integer getPrice(){ return price; }

    public void setIsDelivered(boolean bool){ isDelivered = bool; }

    public boolean getIsDelivered(){ return isDelivered; }

    public ArrayList<LongLat> getRouteLongLat(){ return routeLongLat; }

    public void setRouteLongLat(ArrayList<LongLat> longLat){ routeLongLat = longLat; }
    @Override
    public int compareTo(Order o) {
        return price.compareTo(o.price);
    }
}
