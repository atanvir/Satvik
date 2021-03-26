package com.satvick.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CheckPhoneDataModel {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("checkphonedata")
    @Expose
    private Checkphonedata checkphonedata;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("requestKey")
    @Expose
    private String requestKey;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Checkphonedata getCheckphonedata() {
        return checkphonedata;
    }

    public void setCheckphonedata(Checkphonedata checkphonedata) {
        this.checkphonedata = checkphonedata;
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


    public class Checkphonedata {

        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("image")
        @Expose
        private String image;
        @SerializedName("email")
        @Expose
        private String email;
        @SerializedName("phone")
        @Expose
        private String phone;
        @SerializedName("dob")
        @Expose
        private String dob;
        @SerializedName("gender")
        @Expose
        private String gender;
        @SerializedName("notify_status")
        @Expose
        private String notifyStatus;
        @SerializedName("social_id")
        @Expose
        private Object socialId;
        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("country")
        @Expose
        private String country;
        @SerializedName("created_at")
        @Expose
        private String createdAt;
        @SerializedName("updated_at")
        @Expose
        private String updatedAt;
        @SerializedName("user_id")
        @Expose
        private String userId;
        @SerializedName("token")
        @Expose
        private String token;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getDob() {
            return dob;
        }

        public void setDob(String dob) {
            this.dob = dob;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getNotifyStatus() {
            return notifyStatus;
        }

        public void setNotifyStatus(String notifyStatus) {
            this.notifyStatus = notifyStatus;
        }

        public Object getSocialId() {
            return socialId;
        }

        public void setSocialId(Object socialId) {
            this.socialId = socialId;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

    }
   /* {
        "status": "SUCCESS",
             "checkphonedata": {
                "name": "Satya",
                "image": "http://mobuloustech.com/yodapi/public/img/user_signup.png",
                "email": "satya@gmail.com",
                "phone": "8887863950",
                "dob": null,
                "gender": null,
                "notify_status": "0",
                "status": "0",
                "country": "India",
                "created_at": "2019-08-22 14:45:56",
                "updated_at": "2019-08-22 15:20:31",
                "user_id": "10",
                "token": "TeBTcV0NvCG939pw5YF8EKBzz"
    },
        "message": "User login successfully.",
            "requestKey": "api/checkphonedata"
    }*/

   /* @SerializedName("requestKey")
    private String requestKey;
    @SerializedName("message")
    private String message;
    @SerializedName("status")
    private String status;

    @SerializedName("name")
    private String name;

    @SerializedName("image")
    private String image;

    @SerializedName("email")
    private String email;

    @SerializedName("phone")
    private String phone;

    @SerializedName("dob")
    private String dob;

    @SerializedName("gender")
    private String gender;

    @SerializedName("notify_status")
    private String notify_status;

    @SerializedName("country")
    private String country;

    @SerializedName("created_at")
    private String created_at;

    @SerializedName("updated_at")
    private String updated_at;

    @SerializedName("user_id")
    private String user_id;

    @SerializedName("token")
    private String token;


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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getNotify_status() {
        return notify_status;
    }

    public void setNotify_status(String notify_status) {
        this.notify_status = notify_status;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }*/


}
