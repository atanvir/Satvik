package com.satvick.ccavenue;

public class RSAResponseModel {

    private String status;
    private Response response;

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

    public class Response{
        private String rsa;
        private String order_id;

        public String getRsa() {
            return rsa;
        }

        public void setRsa(String rsa) {
            this.rsa = rsa;
        }

        public String getOrder_id() {
            return order_id;
        }

        public void setOrder_id(String order_id) {
            this.order_id = order_id;
        }
    }
}
