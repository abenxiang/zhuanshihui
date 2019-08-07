package com.sina.shopguide.util;

/**
 * Created by tiger on 18/4/27.
 */

//javac SecretParams.java
//javah -jni com.sina.shopguide.util.SecretParams

public class SecretParams {
    static {
        System.loadLibrary("SecretParams");
    }
    public static native String getHttpTransMd5Key(String sb);
    public static native String getHttpTransSource();

}
