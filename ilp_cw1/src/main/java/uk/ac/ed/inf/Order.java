package uk.ac.ed.inf;

import java.util.ArrayList;

public class Order implements Comparable<Order> {
    // Private fields
    private final String orderNo;
    private final String deliveryDate;
    private final String customer;
    private final String deliverTo;
    private final ArrayList<String> item;
    private ArrayList<String> orderShopLocations;
    private final Integer price;
    private ArrayList<LongLat> routeLongLat;
    private boolean isDelivered = false;
    public Integer routeCounter = 0;

    // Constructor
    Order(String orderNo, String deliveryDate, String customer, String deliverTo, ArrayList<String> item, Integer price){
        this.orderNo = orderNo;
        this.deliveryDate = deliveryDate;
        this.customer = customer;
        this.deliverTo = deliverTo;;
        this.item = item;
        this.price = price;
    }

    /**
     * Sort the order
     *
     * @param o - the input order that needs to be sorted
     * @return the sorted order by using the price in descending order
     * */
    @Override
    public int compareTo(Order o) {
        return price.compareTo(o.price);
    }

    /*Getters and Setters*/
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
}
