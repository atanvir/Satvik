package com.satvick.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CancelCouponModel {
    @Expose
    @SerializedName("message")
    private String message;

    @Expose
    @SerializedName("status")
    private String status;

    public String getMessage() {
        return this.message;
    }

    public String getStatus() {
        return this.status;
    }

    public void setMessage(String paramString) {
        this.message = paramString;
    }

    public void setStatus(String paramString) {
        this.status = paramString;
    }

}
