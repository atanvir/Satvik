package com.satvick.model;

public class BrandCheckedListModel {

    private String brandName;
    boolean isBrandChecked;

    public BrandCheckedListModel(String brandName) {
        this.brandName = brandName;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public boolean isBrandChecked() {
        return isBrandChecked;
    }

    public void setBrandChecked(boolean brandChecked) {
        isBrandChecked = brandChecked;
    }
}
