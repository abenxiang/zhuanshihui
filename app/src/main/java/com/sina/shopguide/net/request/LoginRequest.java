package com.sina.shopguide.net.request;

/**
 * Created by tiger on 18/5/10.
 */

public class LoginRequest extends BaseRequestParams {

    public String mobilenum;

    public String password;

    public String getMobilenum() {
        return mobilenum;
    }

    public void setMobilenum(String mobilenum) {
        this.mobilenum = mobilenum;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
