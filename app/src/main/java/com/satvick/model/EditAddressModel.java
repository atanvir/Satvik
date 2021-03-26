package com.satvick.model;

import com.google.gson.annotations.SerializedName;

public  class EditAddressModel {

//    {
//        "status": "SUCCESS",
//            "editaddress": {
//        "status": "success"
//    },
//        "message": "Address updated successfully",
//            "requestKey": "api/editaddress"
//    }

    @SerializedName("requestKey")
    private String requestKey;
    @SerializedName("message")
    private String message;
    @SerializedName("status")
    private String status;
    @SerializedName("editaddress")
    private Editaddress editaddress;

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

    public Editaddress getEditaddress() {
        return editaddress;
    }

    public void setEditaddress(Editaddress editaddress) {
        this.editaddress = editaddress;
    }

    public static class Editaddress {
        @SerializedName("status")
        private String status;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}
