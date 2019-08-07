package com.sina.shopguide.test;

import com.sina.shopguide.net.request.BaseRequestParams;

import retrofit2.http.Field;

/**
 * Created by tiger on 18/4/28.
 */

public class TestRequest extends BaseRequestParams {
    public String i;

    public String other;

    public String getI() {
        return i;
    }

    public void setI(String i) {
        this.i = i;
    }

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }
}
