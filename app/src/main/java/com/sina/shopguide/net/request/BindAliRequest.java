package com.sina.shopguide.net.request;

import com.sina.shopguide.net.HttpParam;

/**
 * Created by tiger on 18/5/10.
 */

public class BindAliRequest extends BaseRequestParams {
    public String alipay;

    @HttpParam("real_name")
    public String realName;

    public String getAlipay() {
        return alipay;
    }

    public void setAlipay(String alipay) {
        this.alipay = alipay;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }
}
