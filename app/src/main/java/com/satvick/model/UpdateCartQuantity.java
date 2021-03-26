package com.satvick.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UpdateCartQuantity {

    @SerializedName("token")
    private String token;

    @SerializedName("user_id")
    private String user_id;

    @SerializedName("product_id")
    private String product_id;

    @SerializedName("quantity")
    private String quantity;

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("updatecart")
    @Expose
    private Updatecart updatecart;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("requestKey")
    @Expose
    private String requestKey;

    public String getRequestKey() {
        return requestKey;
    }

    public void setRequestKey(String requestKey) {
        this.requestKey = requestKey;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Updatecart getUpdatecart() {
        return updatecart;
    }

    public void setUpdatecart(Updatecart updatecart) {
        this.updatecart = updatecart;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
}
