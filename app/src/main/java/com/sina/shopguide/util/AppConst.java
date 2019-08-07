package com.sina.shopguide.util;

/**
 * Created by tiger on 18/4/24.
 */

public class AppConst {
//    public static final String APP_URL = "http://test.daogouapi.weimai.com";
    public static final String APP_URL = "http://daogouapi.weimai.com";
	public static final String APP_URL_H5 = "http://daogouh5.weimai.com";
    public static final String APP_DIR = "shopguide";
    public static final String APP_PATCH_URL = "";
    public static final String APP_PATCH_DIR = "patch";
    public static final String ENCRYPT_MD5 = "md5";
    public static final String APP_PLATFORM = "android";
    public static final String PHOTO_DIR = "photo";
    public static final String PHOTO_SUFFIX = ".jpg";

    public static final String APP_DOWNLOAD_URL = APP_URL_H5+"/invitation?code=";//APP_URL_H5+"/download/dougou.apk";
    public static final String APP_FRESHGUIDE_URL = APP_URL_H5+"/guide";
    public static final String APP_PROTOCAL_URL = APP_URL_H5+"/agreement";
    public static final String APP_PROBLEM_URL = APP_URL_H5+"/question";

    public static final int SMS_LEN = 6;

    public class Preferences {
        public final static String USER_FIRST_OPENED = "user_first_open";
        public final static String USER_PREF = "user_pref";
        public final static String USER_ID = "user_id";
        public final static String USER_AVATAR = "user_avatar";
        public final static String USER_TOKEN = "user_token";
        public final static String IS_LOGIN = "is_login";
        public final static String USER_NICK = "user_nick";
    }
}
