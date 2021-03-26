package com.satvick.model;

public class ProductDetails {

    private String product_id;

    private String color_name;

    private String size;

    private String quantity;

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getColor_name() {
        return color_name;
    }

    public void setColor_name(String color_name) {
        this.color_name = color_name;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public ProductDetails(String product_id, String color_name, String size,String quantity) {
        this.product_id = product_id;
        this.color_name = color_name;
        this.size = size;
        this.quantity=quantity;
    }
}
