package com.satvick.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProductListingResponse {


    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("moreproductlist")
    @Expose
    public List<Moreproductlist> moreproductlist = null;
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

    public List<Moreproductlist> getMoreproductlist() {
        return moreproductlist;
    }

    public void setMoreproductlist(List<Moreproductlist> moreproductlist) {
        this.moreproductlist = moreproductlist;
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
