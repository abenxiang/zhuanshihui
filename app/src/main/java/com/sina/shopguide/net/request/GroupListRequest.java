package com.sina.shopguide.net.request;

import com.sina.shopguide.net.HttpParam;

/**
 * Created by tiger on 18/5/10.
 */

public class GroupListRequest extends BaseRequestParams {
    public static final int TYPE_PARNER_A = 0;
    public static final int TYPE_PARNER_B = 1;
    //合伙人类型（A级 0，B级 1）
    public int type;
    @HttpParam("verify_type")
    public int perpage=15;
    public int page;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getPerpage() {
        return perpage;
    }

    public void setPerpage(int perpage) {
        this.perpage = perpage;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }
}
