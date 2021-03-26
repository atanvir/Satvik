package com.satvick.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class Wishlistproduct {

    @SerializedName("list")
    @Expose
    public List<com.satvick.model.List> list = null;

    @SerializedName("num_of_addtocart")
    @Expose
    public Integer numOfAddtocart;

    public List<com.satvick.model.List> getList() {
        return list;
    }

    public void setList(List<com.satvick.model.List> list) {
        this.list = list;
    }

    public Integer getNumOfAddtocart() {
        return numOfAddtocart;
    }

    public void setNumOfAddtocart(Integer numOfAddtocart) {
        this.numOfAddtocart = numOfAddtocart;
    }
}
