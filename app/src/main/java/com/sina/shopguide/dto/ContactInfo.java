package com.sina.shopguide.dto;

import java.io.Serializable;

/**
 * Created by tiger on 18/5/10.
 */

public class ContactInfo implements Serializable {

    private static final long serialVersionUID = 4313185640041769093L;

    public String wechat;
    public String nick_name;

    public String avatar_url;
    public String weibo;
    public String qq;

    public String getWechat() {
        return wechat;
    }

    public void setWechat(String wechat) {
        this.wechat = wechat;
    }

    public String getNick_name() {
        return nick_name;
    }

    public void setNick_name(String nick_name) {
        this.nick_name = nick_name;
    }

    public String getAvatar_url() {
        return avatar_url;
    }

    public void setAvatar_url(String avatar_url) {
        this.avatar_url = avatar_url;
    }

    public String getWeibo() {
        return weibo;
    }

    public void setWeibo(String weibo) {
        this.weibo = weibo;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }
}
