package com.sina.shopguide.net.request;

/**
 * Created by tiger on 18/5/10.
 */

public class GetUserInfoRequest extends BaseRequestParams {
    private String uid;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
