package com.satvick.model;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class HomeModel {

    @SerializedName("requestKey")
    private String requestKey;
    @SerializedName("message")
    private String message;
    @SerializedName("homescreenapi")
    private Homeapi homeapi;
    @SerializedName("status")
    private String status;


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

    public Homeapi getHomeapi() {
        return homeapi;
    }

    public void setHomeapi(Homeapi homeapi) {
        this.homeapi = homeapi;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }





        

        
        

public class AnnouncementBar {

    @SerializedName("filter_data")
    @Expose
    public Integer filterData;
    @SerializedName("type")
    @Expose
    public String type;
    @SerializedName("content")
    @Expose
    public String content;


    public Integer getFilterData() {
        return filterData;
    }

    public void setFilterData(Integer filterData) {
        this.filterData = filterData;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

        

        
        

public class Banner {

    @SerializedName("filter_data")
    @Expose
    public String filterData;
    @SerializedName("type")
    @Expose
    public String type;
    @SerializedName("image")
    @Expose
    public String image;

    public String getFilterData() {
        return filterData;
    }

    public void setFilterData(String filterData) {
        this.filterData = filterData;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}

        

        
        

public class Banner1 {

    @SerializedName("filter_data")
    @Expose
    public String filterData;
    @SerializedName("type")
    @Expose
    public String type;
    @SerializedName("image")
    @Expose
    public String image;

    public String getFilterData() {
        return filterData;
    }

    public void setFilterData(String filterData) {
        this.filterData = filterData;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}

        

        
        

public class Banner2 {

    @SerializedName("filter_data")
    @Expose
    public String filterData;
    @SerializedName("type")
    @Expose
    public String type;
    @SerializedName("image")
    @Expose
    public String image;

    public String getFilterData() {
        return filterData;
    }

    public void setFilterData(String filterData) {
        this.filterData = filterData;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}

        

        
        

public class Brandinfocu {

    @SerializedName("filter_data")
    @Expose
    public String filterData;
    @SerializedName("type")
    @Expose
    public String type;
    @SerializedName("image")
    @Expose
    public String image;

    public String getFilterData() {
        return filterData;
    }

    public void setFilterData(String filterData) {
        this.filterData = filterData;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}

        

        
        

public class Example {

    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("homeapi")
    @Expose
    public Homeapi homeapi;
    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("requestKey")
    @Expose
    public String requestKey;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Homeapi getHomeapi() {
        return homeapi;
    }

    public void setHomeapi(Homeapi homeapi) {
        this.homeapi = homeapi;
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
}

        

        
        

public class Handpicked {

    @SerializedName("filter_data")
    @Expose
    public String filterData;
    @SerializedName("type")
    @Expose
    public String type;
    @SerializedName("image")
    @Expose
    public String image;

    public String getFilterData() {
        return filterData;
    }

    public void setFilterData(String filterData) {
        this.filterData = filterData;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}

        

        

public class Homeapi {

    @SerializedName("banners")
    @Expose
    public List<Banner> banners = null;
    @SerializedName("handpicked")
    @Expose
    public List<Handpicked> handpicked = null;
    @SerializedName("banner1")
    @Expose
    public List<Banner1> banner1 = null;
    @SerializedName("banner2")
    @Expose
    public List<Banner2> banner2 = null;
    @SerializedName("hotdeal")
    @Expose
    public List<Hotdeal> hotdeal = null;
    @SerializedName("slider2")
    @Expose
    public List<Slider2> slider2 = null;
    @SerializedName("slider1")
    @Expose
    public List<Slider1> slider1 = null;
    @SerializedName("stylefeed")
    @Expose
    public List<Stylefeed> stylefeed = null;
    @SerializedName("brandinfocus")
    @Expose
    public List<Brandinfocu> brandinfocus = null;
    @SerializedName("themedetails")
    @Expose
    public List<Themedetail> themedetails = null;
    @SerializedName("featured")
    @Expose
    public List<FlashSale> featured = null;

    @SerializedName("flash_sale")
    @Expose
    public List<FlashSale> flashSale = null;
    @SerializedName("flash_sale_status")
    @Expose
    public String flashSaleStatus;
    @SerializedName("flash_sale_time")
    @Expose
    public String flashSaleTime;
    @SerializedName("num_of_addtocart")
    @Expose
    public Integer numOfAddtocart;
    @SerializedName("announcement_bar")
    @Expose
    public List<AnnouncementBar> announcementBar = null;
    @SerializedName(value = "men",alternate = "catgories")
    @Expose
    public List<Man> men = null;
    @SerializedName(value = "women",alternate = "sub_catgories")
    @Expose
    public List<Woman> women = null;
    @SerializedName(value = "kids")
    @Expose
    public List<Kid> kids = null;

    public List<FlashSale> getFeatured() {
        return featured;
    }

    public void setFeatured(List<FlashSale> featured) {
        this.featured = featured;
    }

    public List<Banner> getBanners() {
        return banners;
    }

    public void setBanners(List<Banner> banners) {
        this.banners = banners;
    }

    public List<Handpicked> getHandpicked() {
        return handpicked;
    }

    public void setHandpicked(List<Handpicked> handpicked) {
        this.handpicked = handpicked;
    }

    public List<Banner1> getBanner1() {
        return banner1;
    }

    public void setBanner1(List<Banner1> banner1) {
        this.banner1 = banner1;
    }

    public List<Banner2> getBanner2() {
        return banner2;
    }

    public void setBanner2(List<Banner2> banner2) {
        this.banner2 = banner2;
    }

    public List<Hotdeal> getHotdeal() {
        return hotdeal;
    }

    public void setHotdeal(List<Hotdeal> hotdeal) {
        this.hotdeal = hotdeal;
    }

    public List<Slider2> getSlider2() {
        return slider2;
    }

    public void setSlider2(List<Slider2> slider2) {
        this.slider2 = slider2;
    }

    public List<Slider1> getSlider1() {
        return slider1;
    }

    public void setSlider1(List<Slider1> slider1) {
        this.slider1 = slider1;
    }

    public List<Stylefeed> getStylefeed() {
        return stylefeed;
    }

    public void setStylefeed(List<Stylefeed> stylefeed) {
        this.stylefeed = stylefeed;
    }

    public List<Brandinfocu> getBrandinfocus() {
        return brandinfocus;
    }

    public void setBrandinfocus(List<Brandinfocu> brandinfocus) {
        this.brandinfocus = brandinfocus;
    }

    public List<Themedetail> getThemedetails() {
        return themedetails;
    }

    public void setThemedetails(List<Themedetail> themedetails) {
        this.themedetails = themedetails;
    }

    public List<FlashSale> getFlashSale() {
        return flashSale;
    }

    public void setFlashSale(List<FlashSale> flashSale) {
        this.flashSale = flashSale;
    }

    public String getFlashSaleStatus() {
        return flashSaleStatus;
    }

    public void setFlashSaleStatus(String flashSaleStatus) {
        this.flashSaleStatus = flashSaleStatus;
    }

    public String getFlashSaleTime() {
        return flashSaleTime;
    }

    public void setFlashSaleTime(String flashSaleTime) {
        this.flashSaleTime = flashSaleTime;
    }

    public Integer getNumOfAddtocart() {
        return numOfAddtocart;
    }

    public void setNumOfAddtocart(Integer numOfAddtocart) {
        this.numOfAddtocart = numOfAddtocart;
    }

    public List<AnnouncementBar> getAnnouncementBar() {
        return announcementBar;
    }

    public void setAnnouncementBar(List<AnnouncementBar> announcementBar) {
        this.announcementBar = announcementBar;
    }

    public List<Man> getMen() {
        return men;
    }

    public void setMen(List<Man> men) {
        this.men = men;
    }

    public List<Woman> getWomen() {
        return women;
    }

    public void setWomen(List<Woman> women) {
        this.women = women;
    }

    public List<Kid> getKids() {
        return kids;
    }

    public void setKids(List<Kid> kids) {
        this.kids = kids;
    }
}

    public class FlashSale {

        @SerializedName("image")
        @Expose
        public String image;
        @SerializedName("brand")
        @Expose
        public String brand;
        @SerializedName("name")
        @Expose
        public String name;
        @SerializedName("sp")
        @Expose
        public Integer sp;
        @SerializedName("mrp")
        @Expose
        public String mrp;
        @SerializedName("percentage")
        @Expose
        public String percentage;
        @SerializedName("product_id")
        @Expose
        public String productId;
        @SerializedName("like_Status")
        @Expose
        public String likeStatus;

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

        public Integer getSp() {
            return sp;
        }

        public void setSp(Integer sp) {
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

        public String getLikeStatus() {
            return likeStatus;
        }

        public void setLikeStatus(String likeStatus) {
            this.likeStatus = likeStatus;
        }
    }

        

        
        

public class Hotdeal {

    @SerializedName("filter_data")
    @Expose
    public String filterData;
    @SerializedName("type")
    @Expose
    public String type;
    @SerializedName("image")
    @Expose
    public String image;

    public String getFilterData() {
        return filterData;
    }

    public void setFilterData(String filterData) {
        this.filterData = filterData;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}

        
        

public class Kid {

    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("category_id")
    @Expose
    public String categoryId;
    @SerializedName("status")
    @Expose
    public Object status;
    @SerializedName("image")
    @Expose
    public Object image;
    @SerializedName("occasion_status")
    @Expose
    public Object occasionStatus;
    @SerializedName("created_at")
    @Expose
    public String createdAt;
    @SerializedName("updated_at")
    @Expose
    public String updatedAt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public Object getStatus() {
        return status;
    }

    public void setStatus(Object status) {
        this.status = status;
    }

    public Object getImage() {
        return image;
    }

    public void setImage(Object image) {
        this.image = image;
    }

    public Object getOccasionStatus() {
        return occasionStatus;
    }

    public void setOccasionStatus(Object occasionStatus) {
        this.occasionStatus = occasionStatus;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}

public class Man {

    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("name")
    @Expose
    public String name;

    @SerializedName("slug")
    @Expose
    private String slug;
    @SerializedName("category_id")
    @Expose
    public String categoryId;
    @SerializedName("status")
    @Expose
    public Object status;
    @SerializedName("image")
    @Expose
    public String image;
    @SerializedName("occasion_status")
    @Expose
    public Object occasionStatus;
    @SerializedName("created_at")
    @Expose
    public String createdAt;
    @SerializedName("updated_at")
    @Expose
    public String updatedAt;

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public Object getStatus() {
        return status;
    }

    public void setStatus(Object status) {
        this.status = status;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Object getOccasionStatus() {
        return occasionStatus;
    }

    public void setOccasionStatus(Object occasionStatus) {
        this.occasionStatus = occasionStatus;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}



public class Slider1 {

    @SerializedName("filter_data")
    @Expose
    public String filterData;
    @SerializedName("type")
    @Expose
    public String type;
    @SerializedName("image")
    @Expose
    public String image;

    public String getFilterData() {
        return filterData;
    }

    public void setFilterData(String filterData) {
        this.filterData = filterData;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}

        

public class Slider2 {

    @SerializedName("filter_data")
    @Expose
    public String filterData;
    @SerializedName("type")
    @Expose
    public String type;
    @SerializedName("image")
    @Expose
    public String image;

    public String getFilterData() {
        return filterData;
    }

    public void setFilterData(String filterData) {
        this.filterData = filterData;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}



public class Stylefeed {

    @SerializedName("filter_data")
    @Expose
    public String filterData;
    @SerializedName("type")
    @Expose
    public String type;
    @SerializedName("image")
    @Expose
    public String image;

    public String getFilterData() {
        return filterData;
    }

    public void setFilterData(String filterData) {
        this.filterData = filterData;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}


        

        
        

public class Themedetail {

    @SerializedName("image")
    @Expose
    public String image;
    @SerializedName("himimage")
    @Expose
    public String himimage;
    @SerializedName("herimage")
    @Expose
    public String herimage;
    @SerializedName("title")
    @Expose
    public String title;
    @SerializedName("description")
    @Expose
    public String description;
    @SerializedName("filter_data")
    @Expose
    public String filterData;
    @SerializedName("type")
    @Expose
    public String type;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getHimimage() {
        return himimage;
    }

    public void setHimimage(String himimage) {
        this.himimage = himimage;
    }

    public String getHerimage() {
        return herimage;
    }

    public void setHerimage(String herimage) {
        this.herimage = herimage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFilterData() {
        return filterData;
    }

    public void setFilterData(String filterData) {
        this.filterData = filterData;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}


        

        
        

public class Woman {

    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("category_id")
    @Expose
    public String categoryId;
    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("image")
    @Expose
    public String image;

    @SerializedName("slug")
    @Expose
    private String slug;
    @SerializedName("occasion_status")
    @Expose
    public Object occasionStatus;
    @SerializedName("created_at")
    @Expose
    public String createdAt;
    @SerializedName("updated_at")
    @Expose
    public String updatedAt;

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Object getOccasionStatus() {
        return occasionStatus;
    }

    public void setOccasionStatus(Object occasionStatus) {
        this.occasionStatus = occasionStatus;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}
}


