package com.satvick.model;

public class PaymentResponseModel {
    private String status;
    private IndiaPay indipay;
    private String message;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public IndiaPay getIndipay() {
        return indipay;
    }

    public void setIndipay(IndiaPay indipay) {
        this.indipay = indipay;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public class IndiaPay{
        private String order_id;
        private String tracking_id;
        private String order_status;
        private String status_message;

        public String getOrder_id() {
            return order_id;
        }

        public void setOrder_id(String order_id) {
            this.order_id = order_id;
        }

        public String getTracking_id() {
            return tracking_id;
        }

        public void setTracking_id(String tracking_id) {
            this.tracking_id = tracking_id;
        }

        public String getOrder_status() {
            return order_status;
        }

        public void setOrder_status(String order_status) {
            this.order_status = order_status;
        }

        public String getStatus_message() {
            return status_message;
        }

        public void setStatus_message(String status_message) {
            this.status_message = status_message;
        }
    }
}

