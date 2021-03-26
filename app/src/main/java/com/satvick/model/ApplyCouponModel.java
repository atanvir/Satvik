package com.satvick.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ApplyCouponModel {

    @Expose
    @SerializedName("apply_cupon")
    private ApplyCupon applyCupon;

    @Expose
    @SerializedName("message")
    private String message;

    @Expose
    @SerializedName("requestKey")
    private String requestKey;

    @Expose
    @SerializedName("status")
    private String status;

    public ApplyCupon getApplyCupon() {
        return this.applyCupon;
    }

    public String getMessage() {
        return this.message;
    }

    public String getRequestKey() {
        return this.requestKey;
    }

    public String getStatus() {
        return this.status;
    }

    public void setApplyCupon(ApplyCupon paramApplyCupon) {
        this.applyCupon = paramApplyCupon;
    }

    public void setMessage(String paramString) {
        this.message = paramString;
    }

    public void setRequestKey(String paramString) {
        this.requestKey = paramString;
    }

    public void setStatus(String paramString) {
        this.status = paramString;
    }
}


