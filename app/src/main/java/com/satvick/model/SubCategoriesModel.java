package com.satvick.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SubCategoriesModel {

    @SerializedName("requestKey")
    private String requestKey;
    @SerializedName("message")
    private String message;
    @SerializedName("getsubcategorylist")
    private List<Getsubcategorylist> getsubcategorylist;
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

    public List<Getsubcategorylist> getGetsubcategorylist() {
        return getsubcategorylist;
    }

    public void setGetsubcategorylist(List<Getsubcategorylist> getsubcategorylist) {
        this.getsubcategorylist = getsubcategorylist;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public static class Getsubcategorylist {
        @SerializedName("remark_list")
        private String remark_list;
        @SerializedName("Subcategory_name")
        private String Subcategory_name;
        @SerializedName("Subcategory_id")
        private String Subcategory_id;

        public String getRemark_list() {
            return remark_list;
        }

        public void setRemark_list(String remark_list) {
            this.remark_list = remark_list;
        }

        public String getSubcategory_name() {
            return Subcategory_name;
        }

        public void setSubcategory_name(String Subcategory_name) {
            this.Subcategory_name = Subcategory_name;
        }

        public String getSubcategory_id() {
            return Subcategory_id;
        }

        public void setSubcategory_id(String Subcategory_id) {
            this.Subcategory_id = Subcategory_id;
        }
    }

    public static class Remark_list {
        @SerializedName("remark_name")
        private String remark_name;
        @SerializedName("remark_id")
        private int remark_id;

        public String getRemark_name() {
            return remark_name;
        }

        public void setRemark_name(String remark_name) {
            this.remark_name = remark_name;
        }

        public int getRemark_id() {
            return remark_id;
        }

        public void setRemark_id(int remark_id) {
            this.remark_id = remark_id;
        }
    }
}
