package com.example.pc;

public class Product {
    private String product_type;
    private int quantity;
    private String inOutStatus; // IN/OUT status
    private String date; // New field for storing date

    // Constructor
    public Product(String product_type, int quantity, String inOutStatus, String date) {
        this.product_type = product_type;
        this.quantity = quantity;
        this.inOutStatus = inOutStatus;
        this.date = date; // Assigning the date value
    }

    // Setter and Getter methods
    public void setProduct_type(String product_type) {
        this.product_type = product_type;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setInOutStatus(String inOutStatus) {
        this.inOutStatus = inOutStatus;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getProduct_type() {
        return product_type;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getInOutStatus() {
        return inOutStatus;
    }

    public String getDate() {
        return date; // Getter for date
    }
}
