package com.satvick.model;

public class HomeSlidingProductResponse {

    String name;
    String image;

    public HomeSlidingProductResponse(String name, int image) {
        this.name = name;
        this.image = String.valueOf(image);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
