package com.sina.shopguide.net.request;

import com.sina.shopguide.net.HttpParam;

/**
 * Created by tiger on 18/5/10.
 */

public class ModifyMobileRequest extends BaseRequestParams {

    public String mobilenum;
    @HttpParam("verify_code")
    public String verifyCode;

    public String getMobilenum() {
        return mobilenum;
    }

    public void setMobilenum(String mobilenum) {
        this.mobilenum = mobilenum;
    }

    public String getVerifyCode() {
        return verifyCode;
    }

    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
    }
}
