package com.satvick.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReferalListModel {


    @SerializedName("status")
    @Expose
    public String status;

    @SerializedName("referal_list")
    @Expose
    public ReferalList referalList;

    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("requestKey")
    @Expose
    public String requestKey;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ReferalList getReferalList() {
        return referalList;
    }

    public void setReferalList(ReferalList referalList) {
        this.referalList = referalList;
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

    public class ReferalList {

        @SerializedName("referal_code")
        @Expose
        public String referalCode;
        @SerializedName("list")
        @Expose
        public java.util.List<List> list = null;
        @SerializedName("referal_price")
        @Expose
        public Integer referalPrice;


        public String getReferalCode() {
            return referalCode;
        }

        public void setReferalCode(String referalCode) {
            this.referalCode = referalCode;
        }

        public java.util.List<List> getList() {
            return list;
        }

        public void setList(java.util.List<List> list) {
            this.list = list;
        }

        public Integer getReferalPrice() {
            return referalPrice;
        }

        public void setReferalPrice(Integer referalPrice) {
            this.referalPrice = referalPrice;
        }
    }

    public class List {

        @SerializedName("name")
        @Expose
        public String name;
        @SerializedName("email")
        @Expose
        public String email;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }

}
