package com.sina.shopguide.dto;

import java.io.Serializable;

/**
 * Created by tiger on 18/5/10.
 */

public class Group implements Serializable {

    private static final long serialVersionUID = 4313185640041769093L;

    public String uid;
    public String nick_name;

    public String avatar_url;
    public String thismonth_contribute;
    public String lastmonth_contribute;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
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

    public String getThismonth_contribute() {
        return thismonth_contribute;
    }

    public void setThismonth_contribute(String thismonth_contribute) {
        this.thismonth_contribute = thismonth_contribute;
    }

    public String getLastmonth_contribute() {
        return lastmonth_contribute;
    }

    public void setLastmonth_contribute(String lastmonth_contribute) {
        this.lastmonth_contribute = lastmonth_contribute;
    }
}
