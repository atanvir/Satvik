package com.satvick.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CartListModel {

    @SerializedName("addtocart")
    @Expose
    private Addtocart addtocart;


    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("Cartlistproduct")
    @Expose
    public List<ProductListRetrievedSuccessfully> productListRetrievedSuccessfully = null;
    @SerializedName("message")
    @Expose
    public String message = null;
    @SerializedName("requestKey")
    @Expose
    public String requestKey;


    public Addtocart getAddtocart() {
        return addtocart;
    }

    public void setAddtocart(Addtocart addtocart) {
        this.addtocart = addtocart;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<ProductListRetrievedSuccessfully> getProductListRetrievedSuccessfully() {
        return productListRetrievedSuccessfully;
    }

    public void setProductListRetrievedSuccessfully(List<ProductListRetrievedSuccessfully> productListRetrievedSuccessfully) {
        this.productListRetrievedSuccessfully = productListRetrievedSuccessfully;
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

    public static class ProductListRetrievedSuccessfully {
        @SerializedName("image")
        @Expose
        public String image;
        @SerializedName("brand")
        @Expose
        public String brand;
        @SerializedName("name")
        @Expose
        public String name;
        @SerializedName("actual_price")
        @Expose
        public String actualPrice;
        @SerializedName("sp")
        @Expose
        public String sp;
        @SerializedName("mrp")
        @Expose
        public String mrp;
        @SerializedName("percentage")
        @Expose
        public String percentage;
        @SerializedName("product_id")
        @Expose
        public String productId;
        @SerializedName("num_of_addtocart")
        @Expose
        public Integer numOfAddtocart;
        @SerializedName("sold_by")
        @Expose
        public String soldBy;
        @SerializedName("size")
        @Expose
        public String size;
        @SerializedName("color")
        @Expose
        public String color;
        @SerializedName("quantity")
        @Expose
        public String quantity;
        @SerializedName("total_items")
        @Expose
        public String totalItems;

        @SerializedName("giftwrap_price")
        @Expose
        public String giftwrap_price;

        @SerializedName("shipping_charges")
        @Expose
        private String shipping_charges;

        @Expose
        @SerializedName("coupon_code")
        private String coupon_code;

        @Expose
        @SerializedName("discount_coupon")
        private String discount_coupon;

        @Expose
        @SerializedName("size_label")
        private String size_label;



        public String getShipping_charges() {
            return shipping_charges;
        }

        public void setShipping_charges(String shipping_charges) {
            this.shipping_charges = shipping_charges;
        }

        public String getGiftwrap_price() {
            return giftwrap_price;
        }

        public void setGiftwrap_price(String giftwrap_price) {
            this.giftwrap_price = giftwrap_price;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getBrand() {
            return brand;
        }

        public void setBrand(String brand) {
            this.brand = brand;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getActualPrice() {
            return actualPrice;
        }

        public void setActualPrice(String actualPrice) {
            this.actualPrice = actualPrice;
        }

        public String getSp() {
            return sp;
        }

        public void setSp(String sp) {
            this.sp = sp;
        }

        public String getMrp() {
            return mrp;
        }

        public void setMrp(String mrp) {
            this.mrp = mrp;
        }

        public String getPercentage() {
            return percentage;
        }

        public void setPercentage(String percentage) {
            this.percentage = percentage;
        }

        public String getProductId() {
            return productId;
        }

        public void setProductId(String productId) {
            this.productId = productId;
        }

        public Integer getNumOfAddtocart() {
            return numOfAddtocart;
        }

        public void setNumOfAddtocart(Integer numOfAddtocart) {
            this.numOfAddtocart = numOfAddtocart;
        }

        public String getSoldBy() {
            return soldBy;
        }

        public void setSoldBy(String soldBy) {
            this.soldBy = soldBy;
        }

        public String getSize() {
            return size;
        }

        public void setSize(String size) {
            this.size = size;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }

        public String getQuantity() {
            return quantity;
        }

        public void setQuantity(String quantity) {
            this.quantity = quantity;
        }

        public String getTotalItems() {
            return totalItems;
        }

        public void setTotalItems(String totalItems) {
            this.totalItems = totalItems;
        }

        public String getCoupon_code() {
            return coupon_code;
        }

        public void setCoupon_code(String coupon_code) {
            this.coupon_code = coupon_code;
        }

        public String getDiscount_coupon() {
            return discount_coupon;
        }

        public void setDiscount_coupon(String discount_coupon) {
            this.discount_coupon = discount_coupon;
        }

        public String getSize_label() {
            return size_label;
        }

        public void setSize_label(String size_label) {
            this.size_label = size_label;
        }
    }
}
