package vn.edu.fpt.fooddelivery.model;

public class OrderItem {
    private String title;
    private double fee;
    private int quantity;

    public OrderItem(String title, double fee, int quantity) {
        this.title = title;
        this.fee = fee;
        this.quantity = quantity;
    }

    public String getTitle() { return title; }
    public double getFee() { return fee; }
    public int getQuantity() { return quantity; }
    @Override
    public String toString() {
        return title + " x" + quantity;
    }
}
