package com.example.pc;

public class Product {
     public String product_type;
     public int quantity;
     // setter and getter method


    public Product(String product_type, int quantity) {
        this.product_type = product_type;
        this.quantity = quantity;
    }

    public void setProduct_type(String product_type) {
        this.product_type = product_type;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getProduct_type() {
        return product_type;
    }

    public int getQuantity() {
        return quantity;
    }
}
