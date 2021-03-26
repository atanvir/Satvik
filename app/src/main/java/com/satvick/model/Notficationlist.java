package com.satvick.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Notficationlist {
    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("order_id")
    @Expose
    public String orderId;
    @SerializedName("order_number")
    @Expose
    public String orderNumber;
    @SerializedName("title")
    @Expose
    public String title;
    @SerializedName("content")
    @Expose
    public String content;
    @SerializedName("created_at")
    @Expose
    public String createdAt;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
