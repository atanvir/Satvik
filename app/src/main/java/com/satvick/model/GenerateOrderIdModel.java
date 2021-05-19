package com.satvick.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GenerateOrderIdModel {

    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("generateordercode")
    @Expose
    public Generateordercode generateordercode;
    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("requestKey")
    @Expose
    public String requestKey;





    public class Generateordercode {

        @SerializedName("order_number")
        @Expose
        public String orderNumber;

        @Override
        public String toString() {
            return "Generateordercode{" +
                    "orderNumber='" + orderNumber + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "GenerateOrderIdModel{" +
                "status='" + status + '\'' +
                ", generateordercode=" + generateordercode +
                ", message='" + message + '\'' +
                ", requestKey='" + requestKey + '\'' +
                '}';
    }
}
