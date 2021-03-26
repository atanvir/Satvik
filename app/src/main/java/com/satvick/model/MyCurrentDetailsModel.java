package com.satvick.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class MyCurrentDetailsModel {

    @Expose
    @SerializedName("as")
    private String as;

    @Expose
    @SerializedName("city")
    private String city;

    @Expose
    @SerializedName("country")
    private String country;

    @Expose
    @SerializedName("countryCode")
    private String countryCode;

    @Expose
    @SerializedName("currencies")
    private List<String> currencies;

    @Expose
    @SerializedName("isp")
    private String isp;

    @Expose
    @SerializedName("lat")
    private double lat;

    @Expose
    @SerializedName("lon")
    private double lon;

    @Expose
    @SerializedName("org")
    private String org;

    @Expose
    @SerializedName("query")
    private String query;

    @Expose
    @SerializedName("region")
    private String region;

    @Expose
    @SerializedName("regionName")
    private String regionName;

    @Expose
    @SerializedName("status")
    private String status;

    @Expose
    @SerializedName("timezone")
    private String timezone;

    @Expose
    @SerializedName("zip")
    private String zip;

    public String getAs() {
        return this.as;
    }

    public String getCity() {
        return this.city;
    }

    public String getCountry() {
        return this.country;
    }

    public String getCountryCode() {
        return this.countryCode;
    }

    public List<String> getCurrencies() {
        return this.currencies;
    }

    public String getIsp() {
        return this.isp;
    }

    public double getLat() {
        return this.lat;
    }

    public double getLon() {
        return this.lon;
    }

    public String getOrg() {
        return this.org;
    }

    public String getQuery() {
        return this.query;
    }

    public String getRegion() {
        return this.region;
    }

    public String getRegionName() {
        return this.regionName;
    }

    public String getStatus() {
        return this.status;
    }

    public String getTimezone() {
        return this.timezone;
    }

    public String getZip() {
        return this.zip;
    }

    public void setAs(String paramString) {
        this.as = paramString;
    }

    public void setCity(String paramString) {
        this.city = paramString;
    }

    public void setCountry(String paramString) {
        this.country = paramString;
    }

    public void setCountryCode(String paramString) {
        this.countryCode = paramString;
    }

    public void setCurrencies(List<String> paramList) {
        this.currencies = paramList;
    }

    public void setIsp(String paramString) {
        this.isp = paramString;
    }

    public void setLat(double paramDouble) {
        this.lat = paramDouble;
    }

    public void setLon(double paramDouble) {
        this.lon = paramDouble;
    }

    public void setOrg(String paramString) {
        this.org = paramString;
    }

    public void setQuery(String paramString) {
        this.query = paramString;
    }

    public void setRegion(String paramString) {
        this.region = paramString;
    }

    public void setRegionName(String paramString) {
        this.regionName = paramString;
    }

    public void setStatus(String paramString) {
        this.status = paramString;
    }

    public void setTimezone(String paramString) {
        this.timezone = paramString;
    }

    public void setZip(String paramString) {
        this.zip = paramString;
    }
}
