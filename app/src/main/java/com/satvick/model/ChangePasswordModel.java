package com.satvick.model;

import com.google.gson.annotations.SerializedName;

public class ChangePasswordModel {
    @SerializedName("status")
    private String status;

    @SerializedName("message")
    private String message;

    @SerializedName("requestKey")
    private String requestKey;

    @SerializedName("changepassword")
    private ChangePasswordResponse changePasswordResponse;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public ChangePasswordResponse getChangePasswordResponse() {
        return changePasswordResponse;
    }

    public void setChangePasswordResponse(ChangePasswordResponse changePasswordResponse) {
        this.changePasswordResponse = changePasswordResponse;
    }
}
