package com.satvick.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mobulous55 on 21/11/18.
 */

public class LoginModel {
    @SerializedName("status")
    private String status;

    @SerializedName("message")
    private String message;

    @SerializedName("requestKey")
    private String requestKey;

    @SerializedName("login")
    private LoginResponse loginResponse;

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

    public LoginResponse getLoginResponse() {
        return loginResponse;
    }

    public void setLoginResponse(LoginResponse loginResponse) {
        this.loginResponse = loginResponse;
    }



}
