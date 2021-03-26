package com.satvick.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mobulous55 on 21/11/18.
 */

public class EditProfileModel {

    @SerializedName("status")
    private String status;

    @SerializedName("message")
    private String message;

    @SerializedName("requestKey")
    private String requestKey;

    @SerializedName("editprofile")
    private EditProfileResponse editProfileResponse;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public EditProfileResponse getEditProfileResponse() {
        return editProfileResponse;
    }

    public void setEditProfileResponse(EditProfileResponse editProfileResponse) {
        this.editProfileResponse = editProfileResponse;
    }
}
