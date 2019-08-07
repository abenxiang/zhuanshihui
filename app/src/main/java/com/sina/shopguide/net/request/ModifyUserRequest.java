package com.sina.shopguide.net.request;

import com.sina.shopguide.net.HttpParam;

/**
 * Created by tiger on 18/5/16.
 */

public class ModifyUserRequest extends BaseRequestParams {

    public String wechat;
    public String weibo;
    public String qq;

    @HttpParam("nick_name")
    public String nickName;

    @HttpParam("invitation_code")
    public String invitationCode;

    @HttpParam("avatar_url")
    public String avatarUrl;

    @HttpParam("alimama_pid")
    public String alimamaPid;


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

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getInvitationCode() {
        return invitationCode;
    }

    public void setInvitationCode(String invitationCode) {
        this.invitationCode = invitationCode;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getAlimamaPid() {
        return alimamaPid;
    }

    public void setAlimamaPid(String alimamaPid) {
        this.alimamaPid = alimamaPid;
    }
}
