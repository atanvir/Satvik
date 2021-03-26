package com.satvick.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CODAvailableModel {
    @Expose
    @SerializedName("codavailable")
    private Codavailable codavailable;

    @Expose
    @SerializedName("message")
    private String message;

    @Expose
    @SerializedName("requestKey")
    private String requestKey;

    @Expose
    @SerializedName("status")
    private String status;

    public Codavailable getCodavailable() {
        return this.codavailable;
    }

    public String getMessage() {
        return this.message;
    }

    public String getRequestKey() {
        return this.requestKey;
    }

    public String getStatus() {
        return this.status;
    }

    public void setCodavailable(Codavailable paramCodavailable) {
        this.codavailable = paramCodavailable;
    }

    public void setMessage(String paramString) {
        this.message = paramString;
    }

    public void setRequestKey(String paramString) {
        this.requestKey = paramString;
    }

    public void setStatus(String paramString) {
        this.status = paramString;
    }

    public class Codavailable {
        @Expose
        @SerializedName("cod_available")
        private String codAvailable;

        public String getCodAvailable() {
            return this.codAvailable;
        }

        public void setCodAvailable(String param1String) {
            this.codAvailable = param1String;
        }
    }
}
