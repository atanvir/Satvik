package com.satvick.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MyWishListResponse {

    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("wishlistproduct")
    @Expose
    public Wishlistproduct wishlistproduct;
    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("requestKey")
    @Expose
    public String requestKey;


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Wishlistproduct getWishlistproduct() {
        return wishlistproduct;
    }

    public void setWishlistproduct(Wishlistproduct wishlistproduct) {
        this.wishlistproduct = wishlistproduct;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRequestKey() {
        return requestKey;
    }

    public void setRequestKey(String requestKey) {
        this.requestKey = requestKey;
    }



}
