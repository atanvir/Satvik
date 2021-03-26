package com.satvick.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Addtocart {
    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("total_items_in_cart")
    @Expose
    private Integer totalItemsInCart;

    public Integer getTotalItemsInCart() {
        return totalItemsInCart;
    }

    public void setTotalItemsInCart(Integer totalItemsInCart) {
        this.totalItemsInCart = totalItemsInCart;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
