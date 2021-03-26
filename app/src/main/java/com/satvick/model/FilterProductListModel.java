package com.satvick.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FilterProductListModel {


    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("filter_section")
    @Expose
    public FilterSection filterSection;
    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("requestKey")
    @Expose
    public String requestKey;

    @SerializedName("type")
    @Expose
    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

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

    public FilterSection getFilterSection() {
        return filterSection;
    }

    public void setFilterSection(FilterSection filterSection) {
        this.filterSection = filterSection;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public static class FilterSection {

        @SerializedName("size")
        @Expose
        public List<String> size = null;
        @SerializedName("color")
        @Expose
        public List<String> color = null;
        @SerializedName("hexcode")
        @Expose
        public List<String> hexcode = null;
        @SerializedName("brand")
        @Expose
        public List<String> brand = null;


        public List<String> getHexcode() {
            return hexcode;
        }

        public void setHexcode(List<String> hexcode) {
            this.hexcode = hexcode;
        }

        private boolean isChecked;

        public boolean isChecked() {
            return isChecked;
        }

        public void setChecked(boolean checked) {
            isChecked = checked;
        }

        public List<String> getBrand() {
            return brand;
        }

        public void setBrand(List<String> brand) {
            this.brand = brand;
        }

        public List<String> getColor() {
            return color;
        }

        public void setColor(List<String> color) {
            this.color = color;
        }

        public List<String> getSize() {
            return size;
        }

        public void setSize(List<String> size) {
            this.size = size;
        }
    }
}
