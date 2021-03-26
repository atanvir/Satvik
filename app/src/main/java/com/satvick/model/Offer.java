package com.satvick.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;


public class Offer {

    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("cartoffer")
    @Expose
    public List<Cartoffer> cartoffer = null;
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

    public List<Cartoffer> getCartoffer() {
        return cartoffer;
    }

    public void setCartoffer(List<Cartoffer> cartoffer) {
        this.cartoffer = cartoffer;
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


    public class Cartoffer {

        @SerializedName("id")
        @Expose
        public Integer id;
        @SerializedName("buy")
        @Expose
        public Integer buy;
        @SerializedName("get")
        @Expose
        public Integer get;
        @SerializedName("catid")
        @Expose
        public Integer catid;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Integer getBuy() {
            return buy;
        }

        public void setBuy(Integer buy) {
            this.buy = buy;
        }

        public Integer getGet() {
            return get;
        }

        public void setGet(Integer get) {
            this.get = get;
        }

        public Integer getCatid() {
            return catid;
        }

        public void setCatid(Integer catid) {
            this.catid = catid;
        }
    }
}
