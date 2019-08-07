package com.sina.shopguide.dto;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SimpleProduct implements Serializable {

    private static final long serialVersionUID = -6137664163310497568L;


    private String id;

    private String title;

    private List<String> pic;

    @SerializedName("origin_price")
    private String orginPrice;

    @SerializedName("current_price")
    private String currentPrice;

    @SerializedName("coupon_click_url")
    private String couponClickUrl;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getPic() {
        return pic;
    }

    public void setPic(List<String> pic) {
        this.pic = pic;
    }

    public String getOrginPrice() {
        return orginPrice;
    }

    public void setOrginPrice(String orginPrice) {
        this.orginPrice = orginPrice;
    }

    public String getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(String currentPrice) {
        this.currentPrice = currentPrice;
    }

    public String getCouponClickUrl() {
        return couponClickUrl;
    }

    public void setCouponClickUrl(String couponClickUrl) {
        this.couponClickUrl = couponClickUrl;
    }
}
