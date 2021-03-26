package com.satvick.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CartListModelResponse {


    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("addtocart")
    @Expose
    private Addtocart addtocart;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("requestKey")
    @Expose
    private String requestKey;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Addtocart getAddtocart() {
        return addtocart;
    }

    public void setAddtocart(Addtocart addtocart) {
        this.addtocart = addtocart;
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
