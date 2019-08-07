package com.sina.shopguide.util;

import java.util.HashSet;
import java.util.Set;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

import org.apache.commons.lang3.StringUtils;

public class UserPreferences {

    private static SharedPreferences sharedPreferences = AppUtils
            .getAppContext().getSharedPreferences(
                    AppConst.Preferences.USER_PREF, Context.MODE_PRIVATE);


    public static boolean firstOpen() {
        return sharedPreferences.getBoolean(
                AppConst.Preferences.USER_FIRST_OPENED
                        + AppUtils.getVersionCode(AppUtils.getAppContext()),
                true);
    }

    public static void setFirstOpen() {
        sharedPreferences
                .edit()
                .putBoolean(
                        AppConst.Preferences.USER_FIRST_OPENED
                                + AppUtils.getVersionCode(AppUtils
                                .getAppContext()), false).commit();
    }

    public static String getUserAvatar() {
        // return "4";
        return sharedPreferences
                .getString(AppConst.Preferences.USER_AVATAR, "");
    }

    public static void setUserAvatar(String avatar) {
        sharedPreferences.edit()
                .putString(AppConst.Preferences.USER_AVATAR, avatar).commit();
    }

    public static String getUserId() {
        // return "4";
        return sharedPreferences.getString(AppConst.Preferences.USER_ID, "0");
    }

    public static void setUserId(String userId) {
        sharedPreferences.edit()
                .putString(AppConst.Preferences.USER_ID, userId).commit();
    }

    public static String getUserToken() {
        return sharedPreferences.getString(AppConst.Preferences.USER_TOKEN,
                "123");
    }

    public static void setUserToken(String token) {
        sharedPreferences.edit()
                .putString(AppConst.Preferences.USER_TOKEN, token).commit();
    }

    public static void setLoginUser(String userId, String userToken, String nick,String avatar) {
        Editor editor = sharedPreferences.edit();
        editor.putString(AppConst.Preferences.USER_ID, userId);
        editor.putString(AppConst.Preferences.USER_TOKEN, userToken);
        if(StringUtils.isNotEmpty(nick)) {
            editor.putString(AppConst.Preferences.USER_NICK, nick);
        }

        if(StringUtils.isNotEmpty(avatar)) {
            editor.putString(AppConst.Preferences.USER_AVATAR, avatar);
        }
        editor.commit();
    }

    public static boolean isLogin() {
        return sharedPreferences.getBoolean(AppConst.Preferences.IS_LOGIN,
                false);
    }

    public static void setLogin(boolean login) {
        sharedPreferences.edit()
                .putBoolean(AppConst.Preferences.IS_LOGIN, login).commit();
    }

    public static void removeUserInfo() {
        Editor editor = sharedPreferences.edit();
        editor.remove(AppConst.Preferences.USER_ID);
        editor.remove(AppConst.Preferences.USER_TOKEN);
        editor.remove(AppConst.Preferences.IS_LOGIN);
        editor.remove(AppConst.Preferences.USER_AVATAR);
        editor.remove(AppConst.Preferences.USER_NICK);
        editor.commit();
    }

    public static Set<String> getShareSet() {
        return sharedPreferences.getStringSet("share_set",
                new HashSet<String>());
    }

    public static void putShareSet(Set<String> set) {
        sharedPreferences.edit().putStringSet("share_set", set).commit();
    }

    public static void setPrefInt(Context context, final String key,
                                  final int value) {
        final SharedPreferences settings = PreferenceManager
                .getDefaultSharedPreferences(context);
        settings.edit().putInt(key, value).commit();
    }


    public static String getUserNick() {
        // return "4";
        return sharedPreferences
                .getString(AppConst.Preferences.USER_NICK, "");
    }

    public static void setUserNick(String nick) {
        sharedPreferences.edit()
                .putString(AppConst.Preferences.USER_NICK, nick).commit();
    }


}
