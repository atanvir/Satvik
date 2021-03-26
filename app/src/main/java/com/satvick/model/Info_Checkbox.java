package com.satvick.model;

import com.google.gson.annotations.SerializedName;

public class Info_Checkbox {

    @SerializedName("id")
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
