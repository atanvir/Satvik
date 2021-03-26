package com.satvick.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Offers {
    @SerializedName("offer1")
    @Expose
    private String offer1;
    @SerializedName("offer2")
    @Expose
    private String offer2;

    public String getOffer1() {
        return offer1;
    }

    public void setOffer1(String offer1) {
        this.offer1 = offer1;
    }

    public String getOffer2() {
        return offer2;
    }

    public void setOffer2(String offer2) {
        this.offer2 = offer2;
    }
}
