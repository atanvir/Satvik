package com.satvick.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PlaceOrderModel {

    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("placeorder")
    @Expose
    public Placeorder placeorder;
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

    public Placeorder getPlaceorder() {
        return placeorder;
    }

    public void setPlaceorder(Placeorder placeorder) {
        this.placeorder = placeorder;
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
