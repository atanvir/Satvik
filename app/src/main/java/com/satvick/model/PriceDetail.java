package com.satvick.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PriceDetail {
    @Expose
    @SerializedName("mrp")
    private String mrp;

    @Expose
    @SerializedName("percentage")
    private String percentage;

    @Expose
    @SerializedName("sp")
    private String sp;

    public String getMrp() {
        return this.mrp;
    }

    public String getPercentage() {
        return this.percentage;
    }

    public String getSp() {
        return this.sp;
    }

    public void setMrp(String param1String) {
        this.mrp = param1String;
    }

    public void setPercentage(String param1String) {
        this.percentage = param1String;
    }

    public void setSp(String param1String) {
        this.sp = param1String;
    }
}
