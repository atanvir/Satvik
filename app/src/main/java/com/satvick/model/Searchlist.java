package com.satvick.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Searchlist {


    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("subsubcatid")
    @Expose
    public Integer subsubcatid;
    @SerializedName("catid")
    @Expose
    public String catid;
    @SerializedName("brand")
    @Expose
    public String brand;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSubsubcatid() {
        return subsubcatid;
    }

    public void setSubsubcatid(Integer subsubcatid) {
        this.subsubcatid = subsubcatid;
    }

    public String getCatid() {
        return catid;
    }

    public void setCatid(String catid) {
        this.catid = catid;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }
}
