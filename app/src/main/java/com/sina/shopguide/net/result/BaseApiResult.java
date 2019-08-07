package com.sina.shopguide.net.result;

import java.io.Serializable;

/**
 * Created by tiger on 18/4/25.
 */

public class BaseApiResult implements Serializable {
    private static final long serialVersionUID = 1058693987902787505L;

    private boolean result;

    private int code;

    private String msg;

    public boolean getResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public boolean isResult() {
        return result;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
