package com.driver;
import java.util.*;

import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class OrderRepository {
    Map<String, Order> orderDB = new HashMap<>();
    Map<String , DeliveryPartner> deliveryPartnerDB = new HashMap<>();
    Map<String , String> orderPartnerDB = new HashMap<>();
    Map<String , List<String>> partnerOrderDB = new HashMap<>();

    public void addOrder(Order order){
        orderDB.put(order.getId(),order);
    }

    public void addPartner(String partnerId){
        deliveryPartnerDB.put(partnerId , new DeliveryPartner(partnerId));
    }

    public void addOrderPartnerPair(String orderID , String partnerID){
        if(orderDB.containsKey(orderID) && deliveryPartnerDB.containsKey(partnerID)){
            orderPartnerDB.put(orderID,partnerID);

            List<String> currentOrder = new ArrayList<>();

            if(partnerOrderDB.containsKey(partnerID)){
                currentOrder = partnerOrderDB.get(partnerID);
            }

            currentOrder.add(orderID);
            partnerOrderDB.put(partnerID, currentOrder);

            // increase number of order of partner
           DeliveryPartner deliveryPartner = deliveryPartnerDB.get(partnerID);
           deliveryPartner.setNumberOfOrders(currentOrder.size());
        }
    }

    public Order getOrderById(String orderID){
        return orderDB.get(orderID);
    }

    public DeliveryPartner getPartnerById(String partnerID){
        return deliveryPartnerDB.get(partnerID);
    }

    public int getOrderCountByPartnerId(String partnerID){
        return partnerOrderDB.get(partnerID).size();
    }

    public List<String> getOrdersByPartnerId(String partnerID) {
        return partnerOrderDB.get(partnerID);
    }

    public List<String> getAllOrders() {
        List<String> orders = new ArrayList<>();
        for(String order : orderDB.keySet()){
            orders.add(order);
        }
        return orders;
    }

    public int getCountOfUnassignedOrders() {
        return orderDB.size() - orderPartnerDB.size();
    }

    public int getOrdersLeftAfterGivenTimeByPartnerId(int Time, String partnerId) {
        int count = 0 ;
        List<String> orders = partnerOrderDB.get(partnerId);
        for(String orderId : orders){
            int deliveryTime = orderDB.get(orderId).getDeliveryTime();
            if(deliveryTime > Time) {
                count++;
            }
        }
        return count;
    }

    public int getLastDeliveryTimeByPartnerId(String partnerId) {
        int maxTime = 0 ;
        List<String> orders = partnerOrderDB.get(partnerId);
        for(String orderID : orders){
            int currentTime = orderDB.get(orderID).getDeliveryTime();
            maxTime = Math.max(maxTime, currentTime);
        }
        return maxTime;
    }

    public void deletePartnerById(String partnerId) {
        deliveryPartnerDB.remove(partnerId);
        List<String> listOfOrder = partnerOrderDB.get(partnerId);

        for(String order : listOfOrder){
            orderPartnerDB.remove(order);
        }
    }

    public void deleteOrderById(String orderId) {
        orderDB.remove(orderId);
        String partnerID = orderPartnerDB.get(orderId);
        orderPartnerDB.remove(partnerID);

        List<String> order = partnerOrderDB.get(partnerID);
        order.remove(orderId);
    }
}