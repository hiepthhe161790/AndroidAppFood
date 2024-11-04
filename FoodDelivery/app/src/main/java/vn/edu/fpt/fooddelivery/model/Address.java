package vn.edu.fpt.fooddelivery.model;

public class Address {
    private String addressLine;
    private String addressType; // home, work, other

    // Constructor
    public Address(String addressLine, String addressType) {
        this.addressLine = addressLine;
        this.addressType = addressType;
    }

    // Getters and Setters
    public String getAddressLine() { return addressLine; }
    public void setAddressLine(String addressLine) { this.addressLine = addressLine; }

    public String getAddressType() { return addressType; }
    public void setAddressType(String addressType) { this.addressType = addressType; }
}