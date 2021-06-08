package com.satvick.model;

public class PinCodeModel {
    private String status;
    private CheckPinCode checkpincodeseller;
    private String  message;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public CheckPinCode getCheckpincodeseller() {
        return checkpincodeseller;
    }

    public void setCheckpincodeseller(CheckPinCode checkpincodeseller) {
        this.checkpincodeseller = checkpincodeseller;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public class CheckPinCode{
        private String status;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }

}
