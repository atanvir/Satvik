package com.satvick.model;

import com.google.gson.annotations.SerializedName;

public class AddAddressModel {

    /*{
        "status": "SUCCESS",
            "viewaddress": [
        {
            "id": 13,
                "user_id": "3",
                "name": "Dev",
                "phone": "01204096514",
                "pincode": "201307",
                "address": "H-146/147 Sec-63 Noida",
                "town": "NCR",
                "city": "Noida",
                "state": "UP",
                "type": "Home",
                "created_at": "2019-08-20 12:56:33",
                "updated_at": "2019-08-20 12:56:33",
                "remark": "0"
        },
        {
            "id": 14,
                "user_id": "3",
                "name": "Dev",
                "phone": "01204096514",
                "pincode": "201307",
                "address": "H-146/147 Sec-63 Noida",
                "town": "NCR",
                "city": "Noida",
                "state": "UP",
                "type": "Home",
                "created_at": "2019-08-20 13:01:14",
                "updated_at": "2019-08-20 13:01:14",
                "remark": "1"
        }
  ],
        "message": "Address list retrieved successfully",
            "requestKey": "api/viewaddress/3"
    }*/

    @SerializedName("requestKey")
    private String requestKey;
    @SerializedName("message")
    private String message;
    @SerializedName("addaddress")
    private Addaddress addaddress;
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

    public Addaddress getAddaddress() {
        return addaddress;
    }

    public void setAddaddress(Addaddress addaddress) {
        this.addaddress = addaddress;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public static class Addaddress {
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
