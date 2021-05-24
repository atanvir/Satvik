package com.satvick.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class LifeResponseModel {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName(value = "appsatvicklife",alternate = "appsatvicklifeblog")
    @Expose
    private Appsatvicklife appsatvicklife;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("requestKey")
    @Expose
    private String requestKey;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Appsatvicklife getAppsatvicklife() {
        return appsatvicklife;
    }

    public void setAppsatvicklife(Appsatvicklife appsatvicklife) {
        this.appsatvicklife = appsatvicklife;
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


    public class Appsatvicklife {

        @SerializedName("category")
        @Expose
        private List<Category> category = null;
        @SerializedName("page_content")
        @Expose
        private String pageContent;
        @SerializedName("random_blog_one")
        @Expose
        private List<RandomBlog> RandomBlog = null;
        @SerializedName("random_blog_two")
        @Expose
        private List<RandomBlog> randomBlogTwo = null;

        @SerializedName("blogs_array")
        private List<RandomBlog> blogs_array=null;


        @SerializedName("vblogs_array")
        private List<RandomBlog> vblogs_array=null;

        @SerializedName("workshop_array")
        private List<RandomBlog> workshop_array=null;

        public List<LifeResponseModel.RandomBlog> getWorkshop_array() {
            return workshop_array;
        }

        public void setWorkshop_array(List<LifeResponseModel.RandomBlog> workshop_array) {
            this.workshop_array = workshop_array;
        }

        public List<LifeResponseModel.RandomBlog> getBlogs_array() {
            return blogs_array;
        }

        public void setBlogs_array(List<LifeResponseModel.RandomBlog> blogs_array) {
            this.blogs_array = blogs_array;
        }

        public List<LifeResponseModel.RandomBlog> getVblogs_array() {
            return vblogs_array;
        }

        public void setVblogs_array(List<LifeResponseModel.RandomBlog> vblogs_array) {
            this.vblogs_array = vblogs_array;
        }


        public List<Category> getCategory() {
            return category;
        }

        public void setCategory(List<Category> category) {
            this.category = category;
        }

        public String getPageContent() {
            return pageContent;
        }

        public void setPageContent(String pageContent) {
            this.pageContent = pageContent;
        }

        public List<RandomBlog> getRandomBlog() {
            return RandomBlog;
        }

        public void setRandomBlog(List<RandomBlog> RandomBlog) {
            this.RandomBlog = RandomBlog;
        }

        public List<RandomBlog> getRandomBlogTwo() {
            return randomBlogTwo;
        }

        public void setRandomBlogTwo(List<RandomBlog> randomBlogTwo) {
            this.randomBlogTwo = randomBlogTwo;
        }





    }

    public class Category {

        @SerializedName("id")
        @Expose
        private long id;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("slug")
        @Expose
        private String slug;
        @SerializedName("created_at")
        @Expose
        private String createdAt;
        @SerializedName("updated_at")
        @Expose
        private String updatedAt;

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSlug() {
            return slug;
        }

        public void setSlug(String slug) {
            this.slug = slug;
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


    public class RandomBlog {
        @SerializedName("id")
        @Expose
        private long id;
        @SerializedName("title")
        @Expose
        private String title;
        @SerializedName("slug")
        @Expose
        private String slug;
        @SerializedName("payment_mode")
        @Expose
        private String paymentMode;
        @SerializedName("price")
        @Expose
        private Double price;
        @SerializedName("image")
        @Expose
        private String image;
        @SerializedName("short_desc")
        @Expose
        private String shortDesc;

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getSlug() {
            return slug;
        }

        public void setSlug(String slug) {
            this.slug = slug;
        }

        public String getPaymentMode() {
            return paymentMode;
        }

        public void setPaymentMode(String paymentMode) {
            this.paymentMode = paymentMode;
        }

        public Double getPrice() {
            return price;
        }

        public void setPrice(Double price) {
            this.price = price;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getShortDesc() {
            return shortDesc;
        }

        public void setShortDesc(String shortDesc) {
            this.shortDesc = shortDesc;
        }
    }

}
