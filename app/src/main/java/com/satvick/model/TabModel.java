package com.satvick.model;

import java.util.List;

public class TabModel {
    public Long id;
    public List<BannerBean> banner;
    public String name;
    public List<LifeTabBean> lifeTab;

    public TabModel(Long id,List<BannerBean> banner, String name, List<LifeTabBean> lifeTab) {
        this.id=id;
        this.banner = banner;
        this.name = name;
        this.lifeTab = lifeTab;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<BannerBean> getBanner() {
        return banner;
    }

    public void setBanner(List<BannerBean> banner) {
        this.banner = banner;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<LifeTabBean> getLifeTab() {
        return lifeTab;
    }

    public void setLifeTab(List<LifeTabBean> lifeTab) {
        this.lifeTab = lifeTab;
    }
}
