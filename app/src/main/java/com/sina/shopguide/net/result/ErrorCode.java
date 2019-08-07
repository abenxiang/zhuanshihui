package com.sina.shopguide.net.result;

/**
 * Created by XiangWei on 18/5/11.
 */

public enum ErrorCode {

    SUCCESS(100000),
    USER_NOT_EXIST(10001),
    INVALID_TOKEN(100005),
    EXPIRE_TOKEN(100006),
    ;

    public int getCode() {
        return code;
    }

    final int code;

    ErrorCode(int code) {
        this.code = code;
    }
}
