package uk.ac.ed.inf;

import java.util.ArrayList;

public class Order implements Comparable<Order> {
    String orderNo;
    String deliveryDate;
    String customer;
    String deliverTo;
    ArrayList<String> item;
    ArrayList<String> orderShopLocations = null;
    Integer price;
    ArrayList<LongLat> routeLongLat;
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
