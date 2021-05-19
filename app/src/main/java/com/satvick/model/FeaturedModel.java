package com.satvick.model;

import java.util.List;

public class FeaturedModel {
    public String category;
    public String category_slug;
    public List<ProductBean> subcategory;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategory_slug() {
        return category_slug;
    }

    public void setCategory_slug(String category_slug) {
        this.category_slug = category_slug;
    }

    public List<ProductBean> getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(List<ProductBean> subcategory) {
        this.subcategory = subcategory;
    }
}
