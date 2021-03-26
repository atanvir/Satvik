package com.satvick.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CouponsListModel {


    @SerializedName("requestKey")
    private String requestKey;
    @SerializedName("message")
    private String message;
    @SerializedName("response")
    private List<Response> response;
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

    public List<Response> getResponse() {
        return response;
    }

    public void setResponse(List<Response> response) {
        this.response = response;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public static class Response {



        @SerializedName("min_price")
        private String min_price;
        @SerializedName("cupon_id")
        private int cupon_id;
        @SerializedName("expiry_date")
        private String expiry_date;
        @SerializedName("discount")
        private String discount;
        @SerializedName("cupon_code")
        private String cupon_code;
        @SerializedName("discount_type")
        public String discountType;


        public String getDiscountType() {
            return discountType;
        }

        public void setDiscountType(String discountType) {
            this.discountType = discountType;
        }

        public String getMin_price() {
            return min_price;
        }

        public void setMin_price(String min_price) {
            this.min_price = min_price;
        }

        public int getCupon_id() {
            return cupon_id;
        }

        public void setCupon_id(int cupon_id) {
            this.cupon_id = cupon_id;
        }

        public String getExpiry_date() {
            return expiry_date;
        }

        public void setExpiry_date(String expiry_date) {
            this.expiry_date = expiry_date;
        }

        public String getDiscount() {
            return discount;
        }

        public void setDiscount(String discount) {
            this.discount = discount;
        }

        public String getCupon_code() {
            return cupon_code;
        }

        public void setCupon_code(String cupon_code) {
            this.cupon_code = cupon_code;
        }
    }
}
