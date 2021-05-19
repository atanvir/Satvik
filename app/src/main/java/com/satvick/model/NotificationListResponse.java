package com.satvick.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NotificationListResponse {
    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("requestKey")
    @Expose
    public String requestKey;

    @SerializedName("notficationlist")
    @Expose
    public List<Notficationlist> notficationlist = null;

    public List<Notficationlist> getNotficationlist() {
        return notficationlist;
    }

    public void setNotficationlist(List<Notficationlist> notficationlist) {
        this.notficationlist = notficationlist;
    }

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

    @Override
    public String toString() {
        return "NotificationListResponse{" +
                "status='" + status + '\'' +
                ", message='" + message + '\'' +
                ", requestKey='" + requestKey + '\'' +
                ", notficationlist=" + notficationlist +
                '}';
    }
}
