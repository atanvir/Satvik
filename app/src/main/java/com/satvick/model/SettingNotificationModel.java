package com.satvick.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by mobulous55 on 30/11/18.
 */

public class SettingNotificationModel {

    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("notifystatus")
    @Expose
    public Notifystatus notifystatus;
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

    public Notifystatus getNotifystatus() {
        return notifystatus;
    }

    public void setNotifystatus(Notifystatus notifystatus) {
        this.notifystatus = notifystatus;
    }

    public class Notifystatus {

        @SerializedName("status")
        @Expose
        public String status;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}
