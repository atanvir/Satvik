package com.satvick.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BannerBean {

    @SerializedName("filter_data")
    @Expose
    private String filterData;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("desc")
    @Expose
    private String desc;
    @SerializedName("writter")
    private String writter;

    public BannerBean(String filterData, String type, String image, String desc, String writter) {
        this.filterData = filterData;
        this.type = type;
        this.image = image;
        this.desc = desc;
        this.writter = writter;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getWritter() {
        return writter;
    }

    public void setWritter(String writter) {
        this.writter = writter;
    }

    public String getFilterData() {
        return filterData;
    }

    public void setFilterData(String filterData) {
        this.filterData = filterData;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
