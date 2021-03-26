package com.satvick.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mobulous55 on 20/11/18.
 */

public class SignUpModel {


    @SerializedName("requestKey")
    private String requestKey;
    @SerializedName("message")
    private String message;
    @SerializedName("signup")
    private Signup signup;
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

    public Signup getSignup() {
        return signup;
    }

    public void setSignup(Signup signup) {
        this.signup = signup;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public static class Signup {
        @SerializedName("token")
        private String token;
        @SerializedName("user_id")
        private String user_id;
        @SerializedName("image")
        private String image;
        @SerializedName("deviceToken")
        private String deviceToken;
        @SerializedName("deviceType")
        private String deviceType;
        @SerializedName("password_confirmation")
        private String password_confirmation;
        @SerializedName("password")
        private String password;
        @SerializedName("country")
        private String country;
        @SerializedName("phone")
        private String phone;
        @SerializedName("email")
        private String email;
        @SerializedName("name")
        private String name;
        @SerializedName("social_id")
        private String social_id;

        public String getSocial_id() {
            return social_id;
        }

        public void setSocial_id(String social_id) {
            this.social_id = social_id;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getDeviceToken() {
            return deviceToken;
        }

        public void setDeviceToken(String deviceToken) {
            this.deviceToken = deviceToken;
        }

        public String getDeviceType() {
            return deviceType;
        }

        public void setDeviceType(String deviceType) {
            this.deviceType = deviceType;
        }

        public String getPassword_confirmation() {
            return password_confirmation;
        }

        public void setPassword_confirmation(String password_confirmation) {
            this.password_confirmation = password_confirmation;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
