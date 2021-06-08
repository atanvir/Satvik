package com.satvick.model;

public class BillingModel {
    public String PRODUCT_ID;
    public String PRODUCT_QUANTITY;
    public String COUPON_CODE;
    public String DISCOUNT;
    public String GIFT_WRAP;
    public String SHIPPING_CHARGES;
    public String TOTAL;
    public String SUB_TOTAL;

    public BillingModel(String PRODUCT_ID, String PRODUCT_QUANTITY, String COUPON_CODE, String DISCOUNT, String GIFT_WRAP, String SHIPPING_CHARGES, String TOTAL, String SUB_TOTAL) {
        this.PRODUCT_ID = PRODUCT_ID;
        this.PRODUCT_QUANTITY = PRODUCT_QUANTITY;
        this.COUPON_CODE = COUPON_CODE;
        this.DISCOUNT = DISCOUNT;
        this.GIFT_WRAP = GIFT_WRAP;
        this.SHIPPING_CHARGES = SHIPPING_CHARGES;
        this.TOTAL = TOTAL;
        this.SUB_TOTAL = SUB_TOTAL;
    }

    public String getPRODUCT_ID() {
        return PRODUCT_ID;
    }

    public void setPRODUCT_ID(String PRODUCT_ID) {
        this.PRODUCT_ID = PRODUCT_ID;
    }

    public String getPRODUCT_QUANTITY() {
        return PRODUCT_QUANTITY;
    }

    public void setPRODUCT_QUANTITY(String PRODUCT_QUANTITY) {
        this.PRODUCT_QUANTITY = PRODUCT_QUANTITY;
    }

    public String getCOUPON_CODE() {
        return COUPON_CODE;
    }

    public void setCOUPON_CODE(String COUPON_CODE) {
        this.COUPON_CODE = COUPON_CODE;
    }

    public String getDISCOUNT() {
        return DISCOUNT;
    }

    public void setDISCOUNT(String DISCOUNT) {
        this.DISCOUNT = DISCOUNT;
    }

    public String getGIFT_WRAP() {
        return GIFT_WRAP;
    }

    public void setGIFT_WRAP(String GIFT_WRAP) {
        this.GIFT_WRAP = GIFT_WRAP;
    }

    public String getSHIPPING_CHARGES() {
        return SHIPPING_CHARGES;
    }

    public void setSHIPPING_CHARGES(String SHIPPING_CHARGES) {
        this.SHIPPING_CHARGES = SHIPPING_CHARGES;
    }

    public String getTOTAL() {
        return TOTAL;
    }

    public void setTOTAL(String TOTAL) {
        this.TOTAL = TOTAL;
    }

    public String getSUB_TOTAL() {
        return SUB_TOTAL;
    }

    public void setSUB_TOTAL(String SUB_TOTAL) {
        this.SUB_TOTAL = SUB_TOTAL;
    }
}
