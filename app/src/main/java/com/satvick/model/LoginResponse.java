package com.satvick.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mobulous55 on 21/11/18.
 */

public class LoginResponse {


   /* {
        "status": "SUCCESS",
            "login": {
                "name": "Dev",
                "image": "http://mobuloustech.com/yodapi/public/img/user_signup.png",
                "email": "dev.prakash@mobulous.com",
                "phone": "8750500425",
                "dob": null,
                "gender": null,
                "notify_status": "0",
                "country": "India",
                "created_at": "2019-08-07 08:17:40",
                "updated_at": "2019-08-07 08:17:40",
                "user_id": "3",
                "token": "aUVZ4redHe6SLMcnCDK5ZebjQ"
    },
            "message": "User login successfully.",
            "requestKey": "api/login"
    }*/

    @SerializedName("name")
    private String name;

    @SerializedName("email")
    private String email;

    @SerializedName("password")
    private String password;

    @SerializedName("phone")
    private String phone;

    @SerializedName("deviceType")
    private String deviceType;

    @SerializedName("deviceToken")
    private String deviceToken;

    @SerializedName("password_confirmation")
    private String password_confirmation;

    @SerializedName("id")
    private String id;

    @SerializedName("image")
    private String image;

    @SerializedName("token")
    private String token;

    @SerializedName("admin_status")
    private String admin_status;

    @SerializedName("type")
    private String type;

    @SerializedName("CUID")
    private String CUID;

    @SerializedName("created_at")
    private String created_at;

    @SerializedName("updated_at")
    private String updated_at;

    @SerializedName("social_status")
    private String social_status;

    public String getSocial_status() {
        return social_status;
    }

    public void setSocial_status(String social_status) {
        this.social_status = social_status;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public String getPassword_confirmation() {
        return password_confirmation;
    }

    public void setPassword_confirmation(String password_confirmation) {
        this.password_confirmation = password_confirmation;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getAdmin_status() {
        return admin_status;
    }

    public void setAdmin_status(String admin_status) {
        this.admin_status = admin_status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCUID() {
        return CUID;
    }

    public void setCUID(String CUID) {
        this.CUID = CUID;
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
}
