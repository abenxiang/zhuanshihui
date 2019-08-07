package com.sina.shopguide.net.request;

/**
 * Created by tiger on 18/5/10.
 */

public class MobileIsExistRequest extends BaseRequestParams {

    public String mobilenum;

    public String getMobilenum() {
        return mobilenum;
    }

    public void setMobilenum(String mobilenum) {
        this.mobilenum = mobilenum;
    }
}
