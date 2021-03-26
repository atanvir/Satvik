package com.satvick.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mobulous55 on 21/11/18.
 */

public class ViewProfileResponse {


  /*  {
        "status": "SUCCESS",
            "viewprofile": {
                "user_id": "3",
                "image": "http://mobuloustech.com/yodapi/public/img/user_signup.png",
                "name": "Dev",
                "email": "dev.prakash@mobulous.com",
                "phone": "8750500425",
                "dob": "",
                "gender": ""
    },
            "message": "User details retrieved successfully",
            "requestKey": "api/viewprofile"
    }*/


    @SerializedName("image")
    private String image;

    @SerializedName("user_id")
    private String user_id;

    @SerializedName("email")
    private String email;

    @SerializedName("phone")
    private String phone;

    @SerializedName("name")
    private String name;

    @SerializedName("dob")
    private String dob;

    @SerializedName("gender")
    private String gender;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}
