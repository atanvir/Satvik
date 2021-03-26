package com.satvick.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SocialLoginModel {

    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("sociallogin")
    @Expose
    public Sociallogin sociallogin;
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

    public Sociallogin getSociallogin() {
        return sociallogin;
    }

    public void setSociallogin(Sociallogin sociallogin) {
        this.sociallogin = sociallogin;
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

    public class Sociallogin {

        @SerializedName("id")
        @Expose
        public Integer id;
        @SerializedName("name")
        @Expose
        public String name;
        @SerializedName("image")
        @Expose
        public String image;
        @SerializedName("email")
        @Expose
        public Object email;
        @SerializedName("phone")
        @Expose
        public Object phone;
        @SerializedName("dob")
        @Expose
        public Object dob;
        @SerializedName("gender")
        @Expose
        public Object gender;
        @SerializedName("notify_status")
        @Expose
        public String notifyStatus;
        @SerializedName("social_id")
        @Expose
        public String socialId;
        @SerializedName("social_type")
        @Expose
        public String socialType;
        @SerializedName("status")
        @Expose
        public String status;
        @SerializedName("country")
        @Expose
        public Object country;
        @SerializedName("referal_code")
        @Expose
        public String referalCode;
        @SerializedName("created_at")
        @Expose
        public String createdAt;
        @SerializedName("updated_at")
        @Expose
        public String updatedAt;
        @SerializedName("token")
        @Expose
        public String token;


        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
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

        public Object getEmail() {
            return email;
        }

        public void setEmail(Object email) {
            this.email = email;
        }

        public Object getPhone() {
            return phone;
        }

        public void setPhone(Object phone) {
            this.phone = phone;
        }

        public Object getDob() {
            return dob;
        }

        public void setDob(Object dob) {
            this.dob = dob;
        }

        public Object getGender() {
            return gender;
        }

        public void setGender(Object gender) {
            this.gender = gender;
        }

        public String getNotifyStatus() {
            return notifyStatus;
        }

        public void setNotifyStatus(String notifyStatus) {
            this.notifyStatus = notifyStatus;
        }

        public String getSocialId() {
            return socialId;
        }

        public void setSocialId(String socialId) {
            this.socialId = socialId;
        }

        public String getSocialType() {
            return socialType;
        }

        public void setSocialType(String socialType) {
            this.socialType = socialType;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public Object getCountry() {
            return country;
        }

        public void setCountry(Object country) {
            this.country = country;
        }

        public String getReferalCode() {
            return referalCode;
        }

        public void setReferalCode(String referalCode) {
            this.referalCode = referalCode;
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

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }
}
