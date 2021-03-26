package com.satvick.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SimilarProduct {

    @SerializedName("image")
    @Expose
    public String image;
    @SerializedName("product_id")
    @Expose
    public Integer productId;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("mrp")
    @Expose
    public String mrp;
    @SerializedName("discount")
    @Expose
    public Integer discount;
    @SerializedName("brand")
    @Expose
    public String brand;
    @SerializedName("percentage")
    @Expose
    public Integer percentage;
    @SerializedName("sp")
    @Expose
    public String sp;




    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMrp() {
        return mrp;
    }

    public void setMrp(String mrp) {
        this.mrp = mrp;
    }

    public Integer getDiscount() {
        return discount;
    }

    public void setDiscount(Integer discount) {
        this.discount = discount;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public Integer getPercentage() {
        return percentage;
    }

    public void setPercentage(Integer percentage) {
        this.percentage = percentage;
    }

    public String getSp() {
        return sp;
    }

    public void setSp(String sp) {
        this.sp = sp;
    }
}
