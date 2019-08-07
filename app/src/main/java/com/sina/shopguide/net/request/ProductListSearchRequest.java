package com.sina.shopguide.net.request;

/**
 * Created by XiangWei on 18/5/10.
 */

public class ProductListSearchRequest extends BaseRequestParams {

    private int page;
    private int page_size=10;
    private String keyword;

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public int getPage_size() {
        return page_size;
    }

    public void setPage_size(int page_size) {
        this.page_size = page_size;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }
}
