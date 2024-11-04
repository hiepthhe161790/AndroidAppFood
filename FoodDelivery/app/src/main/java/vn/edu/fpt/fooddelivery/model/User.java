package vn.edu.fpt.fooddelivery.model;

import java.util.List;

public class User {
    private String name;
    private String email;
    private String phone;
    private List<Address> addresses;

    // Constructor
    public User(String name, String email, String phone, List<Address> addresses) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.addresses = addresses;
    }

    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public List<Address> getAddresses() { return addresses; }
    public void setAddresses(List<Address> addresses) { this.addresses = addresses; }
}