package com.satvick.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MyOrderHelp {

        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("response")
        @Expose
        private List<Response> response = null;
        @SerializedName("message")
        @Expose
        private String message;
        @SerializedName("requestKey")
        @Expose
        private String requestKey;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public List<Response> getResponse() {
            return response;
        }

        public void setResponse(List<Response> response) {
            this.response = response;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getRequestKey() {
            return requestKey;
        }

        public void setRequestKey(String requestKey) {
            this.requestKey = requestKey;
        }





    public class Response {

        @SerializedName("image")
        @Expose
        private String image;
        @SerializedName("sku")
        @Expose
        private String sku;
        @SerializedName("brand")
        @Expose
        private String brand;
        @SerializedName("product_id")
        @Expose
        private long productId;
        @SerializedName("id")
        @Expose
        private long id;
        @SerializedName("order_number")
        @Expose
        private String orderNumber;
        @SerializedName("Buyer")
        @Expose
        private String buyer;
        @SerializedName("location")
        @Expose
        private String location;
        @SerializedName("order_date")
        @Expose
        private String orderDate;
        @SerializedName("dispatch_by")
        @Expose
        private String dispatchBy;
        @SerializedName("payment_type")
        @Expose
        private String paymentType;
        @SerializedName("coupan_code")
        @Expose
        private String coupanCode;
        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("product_name")
        @Expose
        private String productName;
        @SerializedName("amount")
        @Expose
        private String amount;
        @SerializedName("percentage")
        @Expose
        private long percentage;
        @SerializedName("mrp")
        @Expose
        private String mrp;
        @SerializedName("quantity")
        @Expose
        private long quantity;
        @SerializedName("color")
        @Expose
        private String color;
        @SerializedName("size")
        @Expose
        private String size;
        @SerializedName("exchangeStatus")
        @Expose
        private long exchangeStatus;
        @SerializedName("notifyStatus")
        @Expose
        private List<Notifystatus> notifyStatus = null;

        public List<Notifystatus> getNotifyStatus() {
            return notifyStatus;
        }

        public void setNotifyStatus(List<Notifystatus> notifyStatus) {
            this.notifyStatus = notifyStatus;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getSku() {
            return sku;
        }

        public void setSku(String sku) {
            this.sku = sku;
        }

        public String getBrand() {
            return brand;
        }

        public void setBrand(String brand) {
            this.brand = brand;
        }

        public long getProductId() {
            return productId;
        }

        public void setProductId(long productId) {
            this.productId = productId;
        }

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getOrderNumber() {
            return orderNumber;
        }

        public void setOrderNumber(String orderNumber) {
            this.orderNumber = orderNumber;
        }

        public String getBuyer() {
            return buyer;
        }

        public void setBuyer(String buyer) {
            this.buyer = buyer;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getOrderDate() {
            return orderDate;
        }

        public void setOrderDate(String orderDate) {
            this.orderDate = orderDate;
        }

        public String getDispatchBy() {
            return dispatchBy;
        }

        public void setDispatchBy(String dispatchBy) {
            this.dispatchBy = dispatchBy;
        }

        public String getPaymentType() {
            return paymentType;
        }

        public void setPaymentType(String paymentType) {
            this.paymentType = paymentType;
        }

        public String getCoupanCode() {
            return coupanCode;
        }

        public void setCoupanCode(String coupanCode) {
            this.coupanCode = coupanCode;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public long getPercentage() {
            return percentage;
        }

        public void setPercentage(long percentage) {
            this.percentage = percentage;
        }

        public String getMrp() {
            return mrp;
        }

        public void setMrp(String mrp) {
            this.mrp = mrp;
        }

        public long getQuantity() {
            return quantity;
        }

        public void setQuantity(long quantity) {
            this.quantity = quantity;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }

        public String getSize() {
            return size;
        }

        public void setSize(String size) {
            this.size = size;
        }

        public long getExchangeStatus() {
            return exchangeStatus;
        }

        public void setExchangeStatus(long exchangeStatus) {
            this.exchangeStatus = exchangeStatus;
        }

    }

    public class Notifystatus {

        @SerializedName("date")
        @Expose
        private String date;
        @SerializedName("content")
        @Expose
        private String content;

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

    }

}
