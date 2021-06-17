package com.satvick.model;

import java.util.List;

public class AddressValidModel {
    String Message;
    String Status;
    List<PostOffice> PostOffice;

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public List<AddressValidModel.PostOffice> getPostOffice() {
        return PostOffice;
    }

    public void setPostOffice(List<AddressValidModel.PostOffice> postOffice) {
        PostOffice = postOffice;
    }

    public class PostOffice{
        String Name;
        String Region;
        String State;
        String Country;

        public String getName() {
            return Name;
        }

        public void setName(String name) {
            Name = name;
        }

        public String getRegion() {
            return Region;
        }

        public void setRegion(String region) {
            Region = region;
        }

        public String getState() {
            return State;
        }

        public void setState(String state) {
            State = state;
        }

        public String getCountry() {
            return Country;
        }

        public void setCountry(String country) {
            Country = country;
        }
    }
}
