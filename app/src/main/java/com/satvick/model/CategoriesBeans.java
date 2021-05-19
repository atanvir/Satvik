package com.satvick.model;

import java.util.List;

public class CategoriesBeans {
    public String category;
    public String category_slug;
    public boolean viewAll;
    public java.util.List<ProductBean> products;

    public CategoriesBeans(String category, String category_slug, Boolean viewAll, List<ProductBean> products) {
        this.category = category;
        this.category_slug = category_slug;
        this.viewAll = viewAll;
        this.products = products;
    }

    public Boolean getViewAll() {
        return viewAll;
    }

    public void setViewAll(Boolean viewAll) {
        this.viewAll = viewAll;
    }

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

    public java.util.List<ProductBean> getProducts() {
        return products;
    }

    public void setProducts(List<ProductBean> products) {
        this.products = products;
    }
}
