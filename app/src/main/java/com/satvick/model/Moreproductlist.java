package com.satvick.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

 public class Moreproductlist {

    @SerializedName("image")
    @Expose
    public String image;
    @SerializedName("brand")
    @Expose
    public String brand;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("sp")
    @Expose
    public String sp;
    @SerializedName("mrp")
    @Expose
    public String mrp;
    @SerializedName("percentage")
    @Expose
    public String percentage;
    @SerializedName("product_id")
    @Expose
    public String productId;
    @SerializedName("like_Status")
    @Expose
    public String likeStatus;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSp() {
        return sp;
    }

    public void setSp(String sp) {
        this.sp = sp;
    }

    public String getMrp() {
        return mrp;
    }

    public void setMrp(String mrp) {
        this.mrp = mrp;
    }

    public String getPercentage() {
        return percentage;
    }

    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getLikeStatus() {
        return likeStatus;
    }

    public void setLikeStatus(String likeStatus) {
        this.likeStatus = likeStatus;
    }
}
