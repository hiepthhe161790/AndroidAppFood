package vn.edu.fpt.fooddelivery.model;

import java.util.List;

public class Order {
    private String orderId;
    private String date;
    private String userName; // Added for user name field
    private List<OrderItem> items;
    private double total;
    private String userAddress;
    private String userPhone;
    private String paymentMethod;
    private String note;

    public Order(String orderId, String userName, String date, List<OrderItem> items, double total) {
        this.orderId = orderId;
        this.userName = userName;
        this.date = date;
        this.items = items;
        this.total = total;
    }
    public Order(String orderId, String userName, String userAddress, String userPhone,
                 String paymentMethod, String note, double total, String date) {
        this.orderId = orderId;
        this.userName = userName;
        this.userAddress = userAddress;
        this.userPhone = userPhone;
        this.paymentMethod = paymentMethod;
        this.note = note;
        this.total = total;
        this.date = date;
    }

    public String getOrderId() { return orderId; }
    public String getUserName() { return userName; } // Getter for user name
    public String getDate() { return date; }

    public String getUserAddress() {
        return userAddress;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public String getNote() {
        return note;
    }

    public List<OrderItem> getItems() { return items; }
    public double getTotal() { return total; }
    @Override
    public String toString() {
        return "Order ID: " + orderId + "\nItems: " + items ;
    }

}
