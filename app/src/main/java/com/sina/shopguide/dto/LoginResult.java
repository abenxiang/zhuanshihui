package com.sina.shopguide.dto;

import java.io.Serializable;

/**
 * Created by tiger on 18/5/10.
 */

public class LoginResult implements Serializable {

    private static final long serialVersionUID = 4313185640041769093L;

    public String id;
    public String token;

    public String ctime;
    public String mtime;
    public String mobilenum;
    public String nick_name;
    public String avatar_url;

    public String invitation_code;
    public String superior;
    public String bind_invitation_time;

    public String token_time;
    public String ltime;
    public String real_name;

    public String alipay;
    public String alipay_mod_num;
    public String alimama_pid;

    public String wechat;
    public String weibo;
    public String qq;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }


    public String getCtime() {
        return ctime;
    }

    public void setCtime(String ctime) {
        this.ctime = ctime;
    }

    public String getMtime() {
        return mtime;
    }

    public void setMtime(String mtime) {
        this.mtime = mtime;
    }

    public String getMobilenum() {
        return mobilenum;
    }

    public void setMobilenum(String mobilenum) {
        this.mobilenum = mobilenum;
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

    public String getInvitation_code() {
        return invitation_code;
    }

    public void setInvitation_code(String invitation_code) {
        this.invitation_code = invitation_code;
    }

    public String getSuperior() {
        return superior;
    }

    public void setSuperior(String superior) {
        this.superior = superior;
    }

    public String getBind_invitation_time() {
        return bind_invitation_time;
    }

    public void setBind_invitation_time(String bind_invitation_time) {
        this.bind_invitation_time = bind_invitation_time;
    }

    public String getToken_time() {
        return token_time;
    }

    public void setToken_time(String token_time) {
        this.token_time = token_time;
    }

    public String getLtime() {
        return ltime;
    }

    public void setLtime(String ltime) {
        this.ltime = ltime;
    }

    public String getReal_name() {
        return real_name;
    }

    public void setReal_name(String real_name) {
        this.real_name = real_name;
    }

    public String getAlipay() {
        return alipay;
    }

    public void setAlipay(String alipay) {
        this.alipay = alipay;
    }

    public String getAlipay_mod_num() {
        return alipay_mod_num;
    }

    public void setAlipay_mod_num(String alipay_mod_num) {
        this.alipay_mod_num = alipay_mod_num;
    }

    public String getAlimama_pid() {
        return alimama_pid;
    }

    public void setAlimama_pid(String alimama_pid) {
        this.alimama_pid = alimama_pid;
    }

    public String getWechat() {
        return wechat;
    }

    public void setWechat(String wechat) {
        this.wechat = wechat;
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
