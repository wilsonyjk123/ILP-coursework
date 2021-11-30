package uk.ac.ed.inf;

import java.util.ArrayList;

public class Order implements Comparable<Order> {
    String orderNo; //数据库读取时初始化
    String deliveryDate; //数据库读取时初始化
    String customer; //数据库读取时初始化
    String deliverTo; //数据库读取时初始化
    ArrayList<String> item; //数据库读取时初始化
    ArrayList<String> orderShopLocations = new ArrayList<>(); //findOrderShopLocations中初始化，获取订单的三字地址
    Integer price;
    ArrayList<LongLat> routeLongLat; //订单
    boolean isDelivered = false;

    Order(String orderNo, String deliveryDate, String customer, String deliverTo, ArrayList<String> item, Integer price){
        this.orderNo = orderNo;
        this.deliveryDate = deliveryDate;
        this.customer = customer;
        this.deliverTo = deliverTo;;
        this.item = item;
        this.price = price;
    }

    @Override
    public int compareTo(Order o) {
        return price.compareTo(o.price);
    }
}
