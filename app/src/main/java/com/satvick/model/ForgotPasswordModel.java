package com.satvick.model;

import com.google.gson.annotations.SerializedName;

public class ForgotPasswordModel {


    @SerializedName("requestKey")
    private String requestKey;
    @SerializedName("message")
    private String message;
    @SerializedName("changepasswordbyphone")
    private Changepasswordbyphone changepasswordbyphone;
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

    public Changepasswordbyphone getChangepasswordbyphone() {
        return changepasswordbyphone;
    }

    public void setChangepasswordbyphone(Changepasswordbyphone changepasswordbyphone) {
        this.changepasswordbyphone = changepasswordbyphone;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public static class Changepasswordbyphone {
        @SerializedName("status")
        private String status;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}
