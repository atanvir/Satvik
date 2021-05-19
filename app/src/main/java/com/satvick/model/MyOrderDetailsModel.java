package com.satvick.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MyOrderDetailsModel implements Parcelable {


    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("response")
    @Expose
    public List<Response> response = null;
    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("requestKey")
    @Expose
    public String requestKey;


    protected MyOrderDetailsModel(Parcel in) {
        status = in.readString();
        response = in.createTypedArrayList(Response.CREATOR);
        message = in.readString();
        requestKey = in.readString();
    }

    public static final Creator<MyOrderDetailsModel> CREATOR = new Creator<MyOrderDetailsModel>() {
        @Override
        public MyOrderDetailsModel createFromParcel(Parcel in) {
            return new MyOrderDetailsModel(in);
        }

        @Override
        public MyOrderDetailsModel[] newArray(int size) {
            return new MyOrderDetailsModel[size];
        }
    };

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Response> getResponse() {
        return response;
    }

    public void setResponse(List<Response> response) {
        this.response = response;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(status);
        parcel.writeTypedList(response);
        parcel.writeString(message);
        parcel.writeString(requestKey);
    }


    public static class Response implements Parcelable {

        @SerializedName("image")
        @Expose
        public String image;
        @SerializedName("sku")
        @Expose
        public String sku;
        @SerializedName("brand")
        @Expose
        public String brand;
        @SerializedName("product_id")
        @Expose
        public Integer productId;
        @SerializedName("id")
        @Expose
        public Integer id;
        @SerializedName("order_number")
        @Expose
        public String orderNumber;
        @SerializedName("Buyer")
        @Expose
        public String buyer;
        @SerializedName("location")
        @Expose
        public String location;
        @SerializedName("order_date")
        @Expose
        public String orderDate;
        @SerializedName("dispatch_by")
        @Expose
        public String dispatchBy;
        @SerializedName("payment_type")
        @Expose
        public String paymentType;
        @SerializedName("coupan_code")
        @Expose
        public String coupanCode;
        @SerializedName("status")
        @Expose
        public String status;
        @SerializedName("product_name")
        @Expose
        public String productName;
        @SerializedName("amount")
        @Expose
        public String amount;
        @SerializedName("percentage")
        @Expose
        public Integer percentage;
        @SerializedName("mrp")
        @Expose
        public String mrp;
        @SerializedName("quantity")
        @Expose
        public Integer quantity;
        @SerializedName("color")
        @Expose
        public String color;
        @SerializedName("size")
        @Expose
        public String size;



        @SerializedName("notifyStatus")
        @Expose
        private List<Notifystatus> notifyStatus = null;
        @Expose
        @SerializedName("size_label")
        private String size_label;



        protected Response(Parcel in) {
            image = in.readString();
            sku = in.readString();
            brand = in.readString();
            if (in.readByte() == 0) {
                productId = null;
            } else {
                productId = in.readInt();
            }
            if (in.readByte() == 0) {
                id = null;
            } else {
                id = in.readInt();
            }
            orderNumber = in.readString();
            buyer = in.readString();
            location = in.readString();
            orderDate = in.readString();
            dispatchBy = in.readString();
            paymentType = in.readString();
            coupanCode = in.readString();
            status = in.readString();
            productName = in.readString();
            amount = in.readString();
            if (in.readByte() == 0) {
                percentage = null;
            } else {
                percentage = in.readInt();
            }
            mrp = in.readString();
            if (in.readByte() == 0) {
                quantity = null;
            } else {
                quantity = in.readInt();
            }
            color = in.readString();
            size = in.readString();
            notifyStatus = in.createTypedArrayList(Notifystatus.CREATOR);
            size_label=in.readString();
        }

        public static final Creator<Response> CREATOR = new Creator<Response>() {
            @Override
            public Response createFromParcel(Parcel in) {
                return new Response(in);
            }

            @Override
            public Response[] newArray(int size) {
                return new Response[size];
            }
        };

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getSku() {
            return sku;
        }

        public void setSku(String sku) {
            this.sku = sku;
        }

        public String getBrand() {
            return brand;
        }

        public void setBrand(String brand) {
            this.brand = brand;
        }

        public Integer getProductId() {
            return productId;
        }

        public void setProductId(Integer productId) {
            this.productId = productId;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getOrderNumber() {
            return orderNumber;
        }

        public void setOrderNumber(String orderNumber) {
            this.orderNumber = orderNumber;
        }

        public String getBuyer() {
            return buyer;
        }

        public void setBuyer(String buyer) {
            this.buyer = buyer;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getOrderDate() {
            return orderDate;
        }

        public void setOrderDate(String orderDate) {
            this.orderDate = orderDate;
        }

        public String getDispatchBy() {
            return dispatchBy;
        }

        public void setDispatchBy(String dispatchBy) {
            this.dispatchBy = dispatchBy;
        }

        public String getPaymentType() {
            return paymentType;
        }

        public void setPaymentType(String paymentType) {
            this.paymentType = paymentType;
        }

        public String getCoupanCode() {
            return coupanCode;
        }

        public void setCoupanCode(String coupanCode) {
            this.coupanCode = coupanCode;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public Integer getPercentage() {
            return percentage;
        }

        public void setPercentage(Integer percentage) {
            this.percentage = percentage;
        }

        public String getMrp() {
            return mrp;
        }

        public void setMrp(String mrp) {
            this.mrp = mrp;
        }

        public Integer getQuantity() {
            return quantity;
        }

        public void setQuantity(Integer quantity) {
            this.quantity = quantity;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }

        public String getSize() {
            return size;
        }

        public void setSize(String size) {
            this.size = size;
        }

        public List<Notifystatus> getNotifyStatus() {
            return notifyStatus;
        }

        public void setNotifyStatus(List<Notifystatus> notifyStatus) {
            this.notifyStatus = notifyStatus;
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
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeString(image);
            parcel.writeString(sku);
            parcel.writeString(brand);
            if (productId == null) {
                parcel.writeByte((byte) 0);
            } else {
                parcel.writeByte((byte) 1);
                parcel.writeInt(productId);
            }
            if (id == null) {
                parcel.writeByte((byte) 0);
            } else {
                parcel.writeByte((byte) 1);
                parcel.writeInt(id);
            }
            parcel.writeString(orderNumber);
            parcel.writeString(buyer);
            parcel.writeString(location);
            parcel.writeString(orderDate);
            parcel.writeString(dispatchBy);
            parcel.writeString(paymentType);
            parcel.writeString(coupanCode);
            parcel.writeString(status);
            parcel.writeString(productName);
            parcel.writeString(amount);
            if (percentage == null) {
                parcel.writeByte((byte) 0);
            } else {
                parcel.writeByte((byte) 1);
                parcel.writeInt(percentage);
            }
            parcel.writeString(mrp);
            if (quantity == null) {
                parcel.writeByte((byte) 0);
            } else {
                parcel.writeByte((byte) 1);
                parcel.writeInt(quantity);
            }
            parcel.writeString(color);
            parcel.writeString(size);
            parcel.writeTypedList(notifyStatus);
            parcel.writeString(size_label);
        }

        @Override
        public String toString() {
            return "Response{" +
                    "image='" + image + '\'' +
                    ", sku='" + sku + '\'' +
                    ", brand='" + brand + '\'' +
                    ", productId=" + productId +
                    ", id=" + id +
                    ", orderNumber='" + orderNumber + '\'' +
                    ", buyer='" + buyer + '\'' +
                    ", location='" + location + '\'' +
                    ", orderDate='" + orderDate + '\'' +
                    ", dispatchBy='" + dispatchBy + '\'' +
                    ", paymentType='" + paymentType + '\'' +
                    ", coupanCode='" + coupanCode + '\'' +
                    ", status='" + status + '\'' +
                    ", productName='" + productName + '\'' +
                    ", amount='" + amount + '\'' +
                    ", percentage=" + percentage +
                    ", mrp='" + mrp + '\'' +
                    ", quantity=" + quantity +
                    ", color='" + color + '\'' +
                    ", size='" + size + '\'' +
                    ", notifyStatus=" + notifyStatus +
                    ", size_label='" + size_label + '\'' +
                    '}';
        }
    }

    public static class Notifystatus implements Parcelable {

        @SerializedName("date")
        @Expose
        private String date;
        @SerializedName("content")
        @Expose
        private String content;

        protected Notifystatus(Parcel in) {
            date = in.readString();
            content = in.readString();
        }

        public static final Creator<Notifystatus> CREATOR = new Creator<Notifystatus>() {
            @Override
            public Notifystatus createFromParcel(Parcel in) {
                return new Notifystatus(in);
            }

            @Override
            public Notifystatus[] newArray(int size) {
                return new Notifystatus[size];
            }
        };

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeString(date);
            parcel.writeString(content);
        }
    }

    @Override
    public String toString() {
        return "MyOrderDetailsModel{" +
                "status='" + status + '\'' +
                ", response=" + response +
                ", message='" + message + '\'' +
                ", requestKey='" + requestKey + '\'' +
                '}';
    }
}
