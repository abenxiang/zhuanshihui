package com.sina.shopguide.dto;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 商品
 * Created by XiangWei on 18/5/10.
 */

public class Product implements Serializable {

    private static final long serialVersionUID = -6137664163310497568L;


    private long id;

    private long iid;

    private String ctime;

    private String mtime;

    private String title;

    private List<String> pic = new ArrayList<>();

    @SerializedName("origin_price")
    private String orginPrice;

    @SerializedName("current_price")
    private String currentPrice;

    @SerializedName("product_desc")
    private List<String> productDesc;

    private int sales;

    @SerializedName("coupon_click_url")
    private String couponClickUrl;

    @SerializedName("coupon_end_time")
    private String couponEndTime;

    @SerializedName("coupon_info")
    private String couponInfo;

    @SerializedName("coupon_price")
    private String couponPrice;

    @SerializedName("coupon_start_time")
    private String couponStartTime;

    @SerializedName("coupon_total_count")
    private int couponTotalCount;

    @SerializedName("coupon_remain_count")
    private int couponRemainCount;

    private int source;

    private String commision;

    private String link;

    @SerializedName("releatedProduct")
    private List<SimpleProduct> relatedProduct;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getIid() {
        return iid;
    }

    public void setIid(long iid) {
        this.iid = iid;
    }

    public String getCtime() {
        return ctime;
    }

    public void setCtime(String ctime) {
        this.ctime = ctime;
    }

    public String getMtime() {
        return mtime;
    }

    public void setMtime(String mtime) {
        this.mtime = mtime;
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

    public List<String> getProductDesc() {
        return productDesc;
    }

    public void setProductDesc(List<String> productDesc) {
        this.productDesc = productDesc;
    }

    public int getSales() {
        return sales;
    }

    public void setSales(int sales) {
        this.sales = sales;
    }

    public String getCouponClickUrl() {
        return couponClickUrl;
    }

    public void setCouponClickUrl(String couponClickUrl) {
        this.couponClickUrl = couponClickUrl;
    }

    public String getCouponEndTime() {
        return couponEndTime;
    }

    public void setCouponEndTime(String couponEndTime) {
        this.couponEndTime = couponEndTime;
    }

    public String getCouponInfo() {
        return couponInfo;
    }

    public void setCouponInfo(String couponInfo) {
        this.couponInfo = couponInfo;
    }

    public String getCouponPrice() {
        return couponPrice;
    }

    public void setCouponPrice(String couponPrice) {
        this.couponPrice = couponPrice;
    }

    public String getCouponStartTime() {
        return couponStartTime;
    }

    public void setCouponStartTime(String couponStartTime) {
        this.couponStartTime = couponStartTime;
    }

    public int getCouponTotalCount() {
        return couponTotalCount;
    }

    public void setCouponTotalCount(int couponTotalCount) {
        this.couponTotalCount = couponTotalCount;
    }

    public int getCouponRemainCount() {
        return couponRemainCount;
    }

    public void setCouponRemainCount(int couponRemainCount) {
        this.couponRemainCount = couponRemainCount;
    }

    public int getSource() {
        return source;
    }

    public void setSource(int source) {
        this.source = source;
    }

    public String getCommision() {
        return commision;
    }

    public void setCommision(String commision) {
        this.commision = commision;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public List<SimpleProduct> getRelatedProduct() {
        return relatedProduct;
    }

    public void setRelatedProduct(List<SimpleProduct> relatedProduct) {
        this.relatedProduct = relatedProduct;
    }
}
