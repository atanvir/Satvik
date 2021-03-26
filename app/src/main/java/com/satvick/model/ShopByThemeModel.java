package com.satvick.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ShopByThemeModel {
    @SerializedName("subcatId")
    @Expose
    private String subcatId;


    @SerializedName("image")
    @Expose
    private String image;


    @SerializedName("section_name")
    @Expose
    private String section_name;


    public String getSubcatId() {
        return subcatId;
    }

    public void setSubcatId(String subcatId) {
        this.subcatId = subcatId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSection_name() {
        return section_name;
    }

    public void setSection_name(String section_name) {
        this.section_name = section_name;
    }

    public ShopByThemeModel(String subcatId, String image, String section_name) {
        this.subcatId = subcatId;
        this.image = image;
        this.section_name = section_name;
    }
}
