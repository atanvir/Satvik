package com.satvick.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class HomeResponseModel {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("homescreenapi")
    @Expose
    private HomeScreenApi homescreenapi;
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

    public HomeScreenApi getHomescreenapi() {
        return homescreenapi;
    }

    public void setHomescreenapi(HomeScreenApi homescreenapi) {
        this.homescreenapi = homescreenapi;
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

    public class HomeScreenApi {

        @SerializedName("banners")
        @Expose
        private List<BannerBean> banners = null;

        @SerializedName("featured")
        @Expose
        private FeaturedModel featured;

        @SerializedName("best_seller")
        @Expose
        private List<ProductBean> bestSeller = null;

        @SerializedName("flash_sale")
        @Expose
        private List<ProductBean> flashSale = null;

        @SerializedName("flash_sale_status")
        @Expose
        private String flashSaleStatus;
        @SerializedName("flash_sale_time")
        @Expose
        private Long flashSaleTime;
        @SerializedName("categories_with_products")
        @Expose
        private List<CategoriesBeans> categoriesWithProducts = null;

        @SerializedName("catgories")
        @Expose
        private List<ProductBean> catgories = null;

        @SerializedName("sub_catgories")
        @Expose
        private List<ProductBean> subCatgories = null;

        @SerializedName("num_of_addtocart")
        @Expose
        private Long numOfAddtocart;


        public List<BannerBean> getBanners() {
            return banners;
        }

        public void setBanners(List<BannerBean> banners) {
            this.banners = banners;
        }

        public FeaturedModel getFeatured() {
            return featured;
        }

        public void setFeatured(FeaturedModel featured) {
            this.featured = featured;
        }

        public List<ProductBean> getBestSeller() {
            return bestSeller;
        }

        public void setBestSeller(List<ProductBean> bestSeller) {
            this.bestSeller = bestSeller;
        }

        public List<ProductBean> getFlashSale() {
            return flashSale;
        }

        public void setFlashSale(List<ProductBean> flashSale) {
            this.flashSale = flashSale;
        }

        public String getFlashSaleStatus() {
            return flashSaleStatus;
        }

        public void setFlashSaleStatus(String flashSaleStatus) {
            this.flashSaleStatus = flashSaleStatus;
        }

        public Long getFlashSaleTime() {
            return flashSaleTime;
        }

        public void setFlashSaleTime(Long flashSaleTime) {
            this.flashSaleTime = flashSaleTime;
        }

        public List<CategoriesBeans> getCategoriesWithProducts() {
            return categoriesWithProducts;
        }

        public void setCategoriesWithProducts(List<CategoriesBeans> categoriesWithProducts) {
            this.categoriesWithProducts = categoriesWithProducts;
        }

        public List<ProductBean> getCatgories() {
            return catgories;
        }

        public void setCatgories(List<ProductBean> catgories) {
            this.catgories = catgories;
        }

        public List<ProductBean> getSubCatgories() {
            return subCatgories;
        }

        public void setSubCatgories(List<ProductBean> subCatgories) {
            this.subCatgories = subCatgories;
        }

        public Long getNumOfAddtocart() {
            return numOfAddtocart;
        }

        public void setNumOfAddtocart(Long numOfAddtocart) {
            this.numOfAddtocart = numOfAddtocart;
        }
    }
}
