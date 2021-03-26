package com.satvick.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ViewAddressModel {

   /* {
        "status": "SUCCESS",
            "addaddress": {
        "status": "success"
    },
        "message": "Address added successfully",
            "requestKey": "api/addaddress"
    }*/

    @SerializedName("requestKey")
    private String requestKey;
    @SerializedName("message")
    private String message;
    @SerializedName("viewaddress")
    private List<Viewaddress> viewaddress;
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

    public List<Viewaddress> getViewaddress() {
        return viewaddress;
    }

    public void setViewaddress(List<Viewaddress> viewaddress) {
        this.viewaddress = viewaddress;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public static class Viewaddress {
        @SerializedName("remark")
        private String remark;
        @SerializedName("updated_at")
        private String updated_at;
        @SerializedName("created_at")
        private String created_at;
        @SerializedName("type")
        private String type;
        @SerializedName("state")
        private String state;
        @SerializedName("city")
        private String city;
        @SerializedName("town")
        private String town;
        @SerializedName("address")
        private String address;
        @SerializedName("pincode")
        private String pincode;
        @SerializedName("phone")
        private String phone;
        @SerializedName("name")
        private String name;
        @SerializedName("user_id")
        private String user_id;
        @SerializedName("id")
        private int id;
        @SerializedName("country")
        @Expose
        private String country;
        @SerializedName("latitude")
        @Expose
        private String latitude;
        @SerializedName("longitude")
        @Expose
        private String longitude;


        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getUpdated_at() {
            return updated_at;
        }

        public void setUpdated_at(String updated_at) {
            this.updated_at = updated_at;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getTown() {
            return town;
        }

        public void setTown(String town) {
            this.town = town;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getPincode() {
            return pincode;
        }

        public void setPincode(String pincode) {
            this.pincode = pincode;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }
}
