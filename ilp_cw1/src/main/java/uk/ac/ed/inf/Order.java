package uk.ac.ed.inf;

import java.util.ArrayList;

public class Order {
    String orderNo;
    String deliveryDate;
    String customer;
    String deliverTo;
    ArrayList<String> item;

    Order(String orderNo, String deliveryDate, String customer, String deliverTo, ArrayList<String> item){
        this.orderNo = orderNo;
        this.deliveryDate = deliveryDate;
        this.customer = customer;
        this.deliverTo = deliverTo;
        this.item = item;
    }
}
