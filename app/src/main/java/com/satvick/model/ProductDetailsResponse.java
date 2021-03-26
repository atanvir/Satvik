package com.satvick.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProductDetailsResponse {

    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("productdetails")
    @Expose
    public Productdetails productdetails;
    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("requestKey")
    @Expose
    public String requestKey;





    public String getRequestKey() {
        return requestKey;
    }

    public void setRequestKey(String requestKey) {
        this.requestKey = requestKey;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Productdetails getProductdetails() {
        return productdetails;
    }

    public void setProductdetails(Productdetails productdetails) {
        this.productdetails = productdetails;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public static class Productdetails implements Parcelable{


        @SerializedName("image_list")
        @Expose
        public List<String> imageList = null;
        @SerializedName("quantity")
        @Expose
        public String quantity;
        @SerializedName("brand")
        @Expose
        public String brand;
        @SerializedName("morebrand")
        @Expose
        public List<String> morebrand = null;
        @SerializedName("subsubcatid")
        @Expose
        public Integer subsubcatid;
        @SerializedName("name")
        @Expose
        public String name;
        @SerializedName("description")
        @Expose
        public String description;
        @SerializedName("fit")
        @Expose
        public String fit;
        @SerializedName("mrp")
        @Expose
        public String mrp;
        @SerializedName("sp")
        @Expose
        public String sp;
        @SerializedName("percentage")
        @Expose
        public Integer percentage;
        @SerializedName("size")
        @Expose
        public List<String> size = null;
        @SerializedName("color")
        @Expose
        public List<String> color = null;
        @SerializedName("defaultcolor")
        @Expose
        public String defaultcolor;
        @SerializedName("defaultcolorname")
        @Expose
        public String defaultcolorname;
        @SerializedName("num_of_addtocart")
        @Expose
        public Integer numOfAddtocart;
        @SerializedName("sold_by")
        @Expose
        public String soldBy;
        @SerializedName("hsn_code")
        @Expose
        public String hsnCode;
        @SerializedName("delivery_time")
        @Expose
        public String deliveryTime;
        @SerializedName("similar_product")
        @Expose
        public List<SimilarProduct> similarProduct = null;
        @SerializedName("customer_also_liked")
        @Expose
        public List<CustomerAlsoLiked> customerAlsoLiked = null;
        @SerializedName("offers")
        @Expose
        public List<String> offers = null;
        @Expose
        @SerializedName("details_offer")
        public List<String> detailsOffer;

        @SerializedName("wishHave")
        @Expose
        public String wishHave;

        @SerializedName("attributes")
        @Expose
        private List<Attribute> attributes = null;


        @SerializedName("sizechart")
        @Expose
        private List<Sizechart> sizechart = null;

        @SerializedName("size_label")
        @Expose
        private String size_label;


        protected Productdetails(Parcel in) {
            imageList = in.createStringArrayList();
            quantity = in.readString();
            brand = in.readString();
            morebrand = in.createStringArrayList();
            if (in.readByte() == 0) {
                subsubcatid = null;
            } else {
                subsubcatid = in.readInt();
            }
            name = in.readString();
            description = in.readString();
            fit = in.readString();
            mrp = in.readString();
            sp = in.readString();
            if (in.readByte() == 0) {
                percentage = null;
            } else {
                percentage = in.readInt();
            }
            size = in.createStringArrayList();
            color = in.createStringArrayList();
            defaultcolor = in.readString();
            defaultcolorname = in.readString();
            if (in.readByte() == 0) {
                numOfAddtocart = null;
            } else {
                numOfAddtocart = in.readInt();
            }
            soldBy = in.readString();
            hsnCode = in.readString();
            deliveryTime = in.readString();
            offers = in.createStringArrayList();
            detailsOffer = in.createStringArrayList();
            wishHave = in.readString();
            sizechart = in.createTypedArrayList(Sizechart.CREATOR);
            size_label=in.readString();
        }

        public static final Creator<Productdetails> CREATOR = new Creator<Productdetails>() {
            @Override
            public Productdetails createFromParcel(Parcel in) {
                return new Productdetails(in);
            }

            @Override
            public Productdetails[] newArray(int size) {
                return new Productdetails[size];
            }
        };

        public List<Sizechart> getSizechart() {
            return sizechart;
        }

        public void setSizechart(List<Sizechart> sizechart) {
            this.sizechart = sizechart;
        }

        public List<Attribute> getAttributes() {
            return attributes;
        }

        public void setAttributes(List<Attribute> attributes) {
            this.attributes = attributes;
        }

        public List<String> getMorebrand() {
            return morebrand;
        }

        public void setMorebrand(List<String> morebrand) {
            this.morebrand = morebrand;
        }

        public Integer getSubsubcatid() {
            return subsubcatid;
        }

        public void setSubsubcatid(Integer subsubcatid) {
            this.subsubcatid = subsubcatid;
        }

        public String getDefaultcolor() {
            return defaultcolor;
        }

        public void setDefaultcolor(String defaultcolor) {
            this.defaultcolor = defaultcolor;
        }

        public String getDefaultcolorname() {
            return defaultcolorname;
        }

        public void setDefaultcolorname(String defaultcolorname) {
            this.defaultcolorname = defaultcolorname;
        }



        public String getWishHave() {
            return wishHave;
        }

        public void setWishHave(String wishHave) {
            this.wishHave = wishHave;
        }

        public List<String> getDetailsOffer() {
            return detailsOffer;
        }

        public void setDetailsOffer(List<String> detailsOffer) {
            this.detailsOffer = detailsOffer;
        }

        public String getFit() {
            return fit;
        }

        public void setFit(String fit) {
            this.fit = fit;
        }


        public Integer getPercentage() {
            return percentage;
        }

        public void setPercentage(Integer percentage) {
            this.percentage = percentage;
        }

        public List<String> getOffers() {
            return offers;
        }

        public void setOffers(List<String> offers) {
            this.offers = offers;
        }

        public List<String> getImageList() {
            return imageList;
        }

        public void setImageList(List<String> imageList) {
            this.imageList = imageList;
        }

        public String getQuantity() {
            return quantity;
        }

        public void setQuantity(String quantity) {
            this.quantity = quantity;
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

        public String getHsnCode() {
            return hsnCode;
        }

        public void setHsnCode(String hsnCode) {
            this.hsnCode = hsnCode;
        }

        public String getDeliveryTime() {
            return deliveryTime;
        }

        public void setDeliveryTime(String deliveryTime) {
            this.deliveryTime = deliveryTime;
        }

        public List<SimilarProduct> getSimilarProduct() {
            return similarProduct;
        }

        public void setSimilarProduct(List<SimilarProduct> similarProduct) {
            this.similarProduct = similarProduct;
        }

        public List<CustomerAlsoLiked> getCustomerAlsoLiked() {
            return customerAlsoLiked;
        }

        public void setCustomerAlsoLiked(List<CustomerAlsoLiked> customerAlsoLiked) {
            this.customerAlsoLiked = customerAlsoLiked;
        }


        public List<String> getColor() {
            return color;
        }

        public void setColor(List<String> color) {
            this.color = color;
        }

        public List<String> getSize() {
            return size;
        }

        public void setSize(List<String> size) {
            this.size = size;
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

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getBrand() {
            return brand;
        }

        public void setBrand(String brand) {
            this.brand = brand;
        }

        public String getSize_label() {
            return size_label;
        }

        public void setSize_label(String size_label) {
            this.size_label = size_label;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeStringList(imageList);
            dest.writeString(quantity);
            dest.writeString(brand);
            dest.writeStringList(morebrand);
            if (subsubcatid == null) {
                dest.writeByte((byte) 0);
            } else {
                dest.writeByte((byte) 1);
                dest.writeInt(subsubcatid);
            }
            dest.writeString(name);
            dest.writeString(description);
            dest.writeString(fit);
            dest.writeString(mrp);
            dest.writeString(sp);
            if (percentage == null) {
                dest.writeByte((byte) 0);
            } else {
                dest.writeByte((byte) 1);
                dest.writeInt(percentage);
            }
            dest.writeStringList(size);
            dest.writeStringList(color);
            dest.writeString(defaultcolor);
            dest.writeString(defaultcolorname);
            if (numOfAddtocart == null) {
                dest.writeByte((byte) 0);
            } else {
                dest.writeByte((byte) 1);
                dest.writeInt(numOfAddtocart);
            }
            dest.writeString(soldBy);
            dest.writeString(hsnCode);
            dest.writeString(deliveryTime);
            dest.writeStringList(offers);
            dest.writeStringList(detailsOffer);
            dest.writeString(wishHave);
            dest.writeTypedList(sizechart);
            dest.writeString(size_label);
        }



    }


    public static class Attribute {

        @SerializedName("keyName")
        @Expose
        private String keyName;
        @SerializedName("keyValue")
        @Expose
        private String keyValue;

        public String getKeyName() {
            return keyName;
        }

        public void setKeyName(String keyName) {
            this.keyName = keyName;
        }

        public Attribute withKeyName(String keyName) {
            this.keyName = keyName;
            return this;
        }

        public String getKeyValue() {
            return keyValue;
        }

        public void setKeyValue(String keyValue) {
            this.keyValue = keyValue;
        }

        public Attribute withKeyValue(String keyValue) {
            this.keyValue = keyValue;
            return this;
        }

    }

}
