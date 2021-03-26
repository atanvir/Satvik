package com.satvick.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SearchListModel {

    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("searchlist")
    @Expose
    public List<Searchlist> searchlist = null;
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

    public List<Searchlist> getSearchlist() {
        return searchlist;
    }

    public void setSearchlist(List<Searchlist> searchlist) {
        this.searchlist = searchlist;
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
}
