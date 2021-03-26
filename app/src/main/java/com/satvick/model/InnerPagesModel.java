package com.satvick.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class InnerPagesModel {
    @SerializedName("requestKey")
    private String requestKey;
    @SerializedName("message")
    private String message;
    @SerializedName("status")
    private String status;
    @SerializedName("response")
    private Response response;

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

    public static class Response {
        @SerializedName("other_section")
        private List<Other_section> other_section;
        @SerializedName("bannerlist")
        private List<Bannerlist> bannerlist;
        @SerializedName("occasion_list")
        private List<Occasion_list> occasion_list;
        @SerializedName("flash_sale")
        private List<Flash_sale> flash_sale;
        @SerializedName("subcategory_slider")
        private List<Subcategory_slider> subcategory_slider;

        public List<Other_section> getOther_section() {
            return other_section;
        }

        public void setOther_section(List<Other_section> other_section) {
            this.other_section = other_section;
        }

        public List<Bannerlist> getBannerlist() {
            return bannerlist;
        }

        public void setBannerlist(List<Bannerlist> bannerlist) {
            this.bannerlist = bannerlist;
        }

        public List<Occasion_list> getOccasion_list() {
            return occasion_list;
        }

        public void setOccasion_list(List<Occasion_list> occasion_list) {
            this.occasion_list = occasion_list;
        }

        public List<Flash_sale> getFlash_sale() {
            return flash_sale;
        }

        public void setFlash_sale(List<Flash_sale> flash_sale) {
            this.flash_sale = flash_sale;
        }

        public List<Subcategory_slider> getSubcategory_slider() {
            return subcategory_slider;
        }

        public void setSubcategory_slider(List<Subcategory_slider> subcategory_slider) {
            this.subcategory_slider = subcategory_slider;
        }
    }

    public static class Other_section {
        @SerializedName("section_name")
        @Expose
        public String sectionName;
        @SerializedName("subcat_id")
        @Expose
        public Integer subcatId;
        @SerializedName("data")
        @Expose
        public List<Data> data = null;

        public List<Data> getData() {
            return data;
        }

        public String getSectionName() {
            return sectionName;
        }

        public void setSectionName(String sectionName) {
            this.sectionName = sectionName;
        }

        public Integer getSubcatId() {
            return subcatId;
        }

        public void setSubcatId(Integer subcatId) {
            this.subcatId = subcatId;
        }

        public void setData(List<Data> data) {
            this.data = data;
        }
    }

    public static class Data {
        @SerializedName("like_Status")
        private String like_Status;
        @SerializedName("product_id")
        private String product_id;
        @SerializedName("percentage")
        private String percentage;
        @SerializedName("mrp")
        private String mrp;
        @SerializedName("sp")
        private String sp;
        @SerializedName("name")
        private String name;
        @SerializedName("brand")
        private String brand;
        @SerializedName("image")
        private String image;

        public Data(String like_Status, String product_id, String percentage, String mrp, String sp, String name, String brand, String image) {
            this.like_Status = like_Status;
            this.product_id = product_id;
            this.percentage = percentage;
            this.mrp = mrp;
            this.sp = sp;
            this.name = name;
            this.brand = brand;
            this.image = image;
        }

        public Data() {
        }

        public String getLike_Status() {
            return like_Status;
        }

        public void setLike_Status(String like_Status) {
            this.like_Status = like_Status;
        }

        public String getProduct_id() {
            return product_id;
        }

        public void setProduct_id(String product_id) {
            this.product_id = product_id;
        }

        public String getPercentage() {
            return percentage;
        }

        public void setPercentage(String percentage) {
            this.percentage = percentage;
        }

        public String getMrp() {
            return mrp;
        }

        public void setMrp(String mrp) {
            this.mrp = mrp;
        }

        public String getSp() {
            return sp;
        }

        public void setSp(String sp) {
            this.sp = sp;
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

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }
    }

    public static class Bannerlist {
        @SerializedName("name")
        private String name;
        @SerializedName("image")
        private String image;
        @SerializedName("filter_data")
        private int filter_data;
        @SerializedName("filter_type")
        private String filter_type;

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

        public int getFilter_data() {
            return filter_data;
        }

        public void setFilter_data(int filter_data) {
            this.filter_data = filter_data;
        }

        public String getFilter_type() {
            return filter_type;
        }

        public void setFilter_type(String filter_type) {
            this.filter_type = filter_type;
        }
    }

    public static class Occasion_list {
        @SerializedName("name")
        private String name;
        @SerializedName("image")
        private String image;
        @SerializedName("filter_data")
        private int filter_data;
        @SerializedName("filter_type")
        private String filter_type;

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

        public int getFilter_data() {
            return filter_data;
        }

        public void setFilter_data(int filter_data) {
            this.filter_data = filter_data;
        }

        public String getFilter_type() {
            return filter_type;
        }

        public void setFilter_type(String filter_type) {
            this.filter_type = filter_type;
        }
    }

    public static class Flash_sale {
        @SerializedName("like_Status")
        private String like_Status;
        @SerializedName("product_id")
        private String product_id;
        @SerializedName("percentage")
        private String percentage;
        @SerializedName("mrp")
        private String mrp;
        @SerializedName("sp")
        private String sp;
        @SerializedName("name")
        private String name;
        @SerializedName("brand")
        private String brand;
        @SerializedName("image")
        private String image;

        public String getLike_Status() {
            return like_Status;
        }

        public void setLike_Status(String like_Status) {
            this.like_Status = like_Status;
        }

        public String getProduct_id() {
            return product_id;
        }

        public void setProduct_id(String product_id) {
            this.product_id = product_id;
        }

        public String getPercentage() {
            return percentage;
        }

        public void setPercentage(String percentage) {
            this.percentage = percentage;
        }

        public String getMrp() {
            return mrp;
        }

        public void setMrp(String mrp) {
            this.mrp = mrp;
        }

        public String getSp() {
            return sp;
        }

        public void setSp(String sp) {
            this.sp = sp;
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

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }
    }

    public static class Subcategory_slider {
        @SerializedName("name")
        private String name;
        @SerializedName("image")
        private String image;
        @SerializedName("filter_data")
        private int filter_data;
        @SerializedName("filter_type")
        private String filter_type;

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

        public int getFilter_data() {
            return filter_data;
        }

        public void setFilter_data(int filter_data) {
            this.filter_data = filter_data;
        }

        public String getFilter_type() {
            return filter_type;
        }

        public void setFilter_type(String filter_type) {
            this.filter_type = filter_type;
        }
    }

//    @SerializedName("requestKey")
//    private String requestKey;
//    @SerializedName("message")
//    private String message;
//    @SerializedName("response")
//    private Response response;
//    @SerializedName("status")
//    private String status;
//
//    public String getRequestKey() {
//        return requestKey;
//    }
//
//    public void setRequestKey(String requestKey) {
//        this.requestKey = requestKey;
//    }
//
//    public String getMessage() {
//        return message;
//    }
//
//    public void setMessage(String message) {
//        this.message = message;
//    }
//
//    public Response getResponse() {
//        return response;
//    }
//
//    public void setResponse(Response response) {
//        this.response = response;
//    }
//
//    public String getStatus() {
//        return status;
//    }
//
//    public void setStatus(String status) {
//        this.status = status;
//    }
//
//    public static class Response {
//        @SerializedName("bannerlist")
//        private List<Bannerlist> bannerlist;
//        @SerializedName("occasion_list")
//        private List<Occasion_list> occasion_list;
//        @SerializedName("flash_sale")
//        private List<Flash_sale> flash_sale;
//        @SerializedName("subcategory_slider")
//        private List<Subcategory_slider> subcategory_slider;
//
//        public List<Bannerlist> getBannerlist() {
//            return bannerlist;
//        }
//
//        public void setBannerlist(List<Bannerlist> bannerlist) {
//            this.bannerlist = bannerlist;
//        }
//
//        public List<Occasion_list> getOccasion_list() {
//            return occasion_list;
//        }
//
//        public void setOccasion_list(List<Occasion_list> occasion_list) {
//            this.occasion_list = occasion_list;
//        }
//
//        public List<Flash_sale> getFlash_sale() {
//            return flash_sale;
//        }
//
//        public void setFlash_sale(List<Flash_sale> flash_sale) {
//            this.flash_sale = flash_sale;
//        }
//
//        public List<Subcategory_slider> getSubcategory_slider() {
//            return subcategory_slider;
//        }
//
//        public void setSubcategory_slider(List<Subcategory_slider> subcategory_slider) {
//            this.subcategory_slider = subcategory_slider;
//        }
//    }
//
//    public static class Bannerlist {
//        @SerializedName("name")
//        private String name;
//        @SerializedName("image")
//        private String image;
//        @SerializedName("filter_data")
//        private int filter_data;
//        @SerializedName("filter_type")
//        private String filter_type;
//
//        public String getName() {
//            return name;
//        }
//
//        public void setName(String name) {
//            this.name = name;
//        }
//
//        public String getImage() {
//            return image;
//        }
//
//        public void setImage(String image) {
//            this.image = image;
//        }
//
//        public int getFilter_data() {
//            return filter_data;
//        }
//
//        public void setFilter_data(int filter_data) {
//            this.filter_data = filter_data;
//        }
//
//        public String getFilter_type() {
//            return filter_type;
//        }
//
//        public void setFilter_type(String filter_type) {
//            this.filter_type = filter_type;
//        }
//    }
//
//    public static class Occasion_list {
//        @SerializedName("name")
//        private String name;
//        @SerializedName("image")
//        private String image;
//        @SerializedName("filter_data")
//        private int filter_data;
//        @SerializedName("filter_type")
//        private String filter_type;
//
//        public String getName() {
//            return name;
//        }
//
//        public void setName(String name) {
//            this.name = name;
//        }
//
//        public String getImage() {
//            return image;
//        }
//
//        public void setImage(String image) {
//            this.image = image;
//        }
//
//        public int getFilter_data() {
//            return filter_data;
//        }
//
//        public void setFilter_data(int filter_data) {
//            this.filter_data = filter_data;
//        }
//
//        public String getFilter_type() {
//            return filter_type;
//        }
//
//        public void setFilter_type(String filter_type) {
//            this.filter_type = filter_type;
//        }
//    }
//
//    public static class Flash_sale {
//        @SerializedName("like_Status")
//        private String like_Status;
//        @SerializedName("product_id")
//        private String product_id;
//        @SerializedName("percentage")
//        private String percentage;
//        @SerializedName("mrp")
//        private String mrp;
//        @SerializedName("sp")
//        private String sp;
//        @SerializedName("name")
//        private String name;
//        @SerializedName("brand")
//        private String brand;
//        @SerializedName("image")
//        private String image;
//
//        public String getLike_Status() {
//            return like_Status;
//        }
//
//        public void setLike_Status(String like_Status) {
//            this.like_Status = like_Status;
//        }
//
//        public String getProduct_id() {
//            return product_id;
//        }
//
//        public void setProduct_id(String product_id) {
//            this.product_id = product_id;
//        }
//
//        public String getPercentage() {
//            return percentage;
//        }
//
//        public void setPercentage(String percentage) {
//            this.percentage = percentage;
//        }
//
//        public String getMrp() {
//            return mrp;
//        }
//
//        public void setMrp(String mrp) {
//            this.mrp = mrp;
//        }
//
//        public String getSp() {
//            return sp;
//        }
//
//        public void setSp(String sp) {
//            this.sp = sp;
//        }
//
//        public String getName() {
//            return name;
//        }
//
//        public void setName(String name) {
//            this.name = name;
//        }
//
//        public String getBrand() {
//            return brand;
//        }
//
//        public void setBrand(String brand) {
//            this.brand = brand;
//        }
//
//        public String getImage() {
//            return image;
//        }
//
//        public void setImage(String image) {
//            this.image = image;
//        }
//    }
//
//    public static class Subcategory_slider {
//        @SerializedName("name")
//        private String name;
//        @SerializedName("image")
//        private String image;
//        @SerializedName("filter_data")
//        private int filter_data;
//        @SerializedName("filter_type")
//        private String filter_type;
//
//        public String getName() {
//            return name;
//        }
//
//        public void setName(String name) {
//            this.name = name;
//        }
//
//        public String getImage() {
//            return image;
//        }
//
//        public void setImage(String image) {
//            this.image = image;
//        }
//
//        public int getFilter_data() {
//            return filter_data;
//        }
//
//        public void setFilter_data(int filter_data) {
//            this.filter_data = filter_data;
//        }
//
//        public String getFilter_type() {
//            return filter_type;
//        }
//
//        public void setFilter_type(String filter_type) {
//            this.filter_type = filter_type;
//        }
//    }


}
