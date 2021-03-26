package com.satvick.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CategoryListModel {


    @SerializedName("requestKey")
    private String requestKey;
    @SerializedName("message")
    private String message;
    @SerializedName("Categorylist")
    private List<Categorylist> Categorylist;
    @SerializedName("status")
    private String status;

    public String getRequestKey() {
        return requestKey;
    }

    public void setRequestKey(String requestKey) {
        this.requestKey = requestKey;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Categorylist> getCategorylist() {
        return Categorylist;
    }

    public void setCategorylist(List<Categorylist> Categorylist) {
        this.Categorylist = Categorylist;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public static class Categorylist {
        @SerializedName("image")
        private String image;
        @SerializedName("name")
        private String name;
        @SerializedName("category_id")
        private int category_id;

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getCategory_id() {
            return category_id;
        }

        public void setCategory_id(int category_id) {
            this.category_id = category_id;
        }
    }
}
