package com.satvick.model;

public class CategoriesListResponse {

    String name;
    String productName;

    public CategoriesListResponse(String name, String productName) {
        this.name = name;
        this.productName = productName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }
}
