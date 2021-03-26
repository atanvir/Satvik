package com.satvick.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CommonModel {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("response")
    @Expose
    private Response response;
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

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
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

    public class Response {

        @SerializedName("image_list")
        @Expose
        private List<String> imageList = null;

        @Expose
        @SerializedName("priceDetail")
        private PriceDetail priceDetail;

        public PriceDetail getPriceDetail() {
            return priceDetail;
        }

        public void setPriceDetail(PriceDetail priceDetail) {
            this.priceDetail = priceDetail;
        }

        public List<String> getImageList() {
            return imageList;
        }

        public void setImageList(List<String> imageList) {
            this.imageList = imageList;
        }
    }
}
