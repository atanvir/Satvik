package com.satvick.model;

public class LifeTabBean {
    public Long id;
    public String image;
    public String title;
    public String slug;
    public String payment_mode;
    public Double price;
    public String short_desc;



    public LifeTabBean(long id, String image, String title, String slug, String payment_mode, Double price, String short_desc) {
        this.id = id;
        this.image = image;
        this.title = title;
        this.slug = slug;
        this.payment_mode = payment_mode;
        this.price = price;
        this.short_desc = short_desc;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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

    public String getPayment_mode() {
        return payment_mode;
    }

    public void setPayment_mode(String payment_mode) {
        this.payment_mode = payment_mode;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getShort_desc() {
        return short_desc;
    }

    public void setShort_desc(String short_desc) {
        this.short_desc = short_desc;
    }
}
