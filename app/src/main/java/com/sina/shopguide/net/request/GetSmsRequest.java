package com.sina.shopguide.net.request;

import com.sina.shopguide.net.HttpParam;

/**
 * Created by tiger on 18/5/10.
 */

public class GetSmsRequest extends BaseRequestParams {

    public static final String TYPE_REGISTER="0";
    public static final String TYPE_BIND_ALI="2";
    public static final String TYPE_RESET="1";

    public String mobilenum;
    @HttpParam("verify_type")
    public String verifyType;

    public String getMobilenum() {
        return mobilenum;
    }

    public void setMobilenum(String mobilenum) {
        this.mobilenum = mobilenum;
    }

    public String getVerifyType() {
        return verifyType;
    }

    public void setVerifyType(String verifyType) {
        this.verifyType = verifyType;
    }
}
