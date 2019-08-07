package com.sina.shopguide.dto;

import java.io.Serializable;
import java.util.List;

/**
 * Created by XiangWei on 18/7/25.
 */

public class HomeTopInfo implements Serializable {

    private static final long serialVersionUID = 5534362519497788416L;

    private List<zhuanti> banner;

    private List<zhuanti> select;

    public List<zhuanti> getBanner() {
        return banner;
    }

    public void setBanner(List<zhuanti> banner) {
        this.banner = banner;
    }

    public List<zhuanti> getSelect() {
        return select;
    }

    public void setSelect(List<zhuanti> select) {
        this.select = select;
    }

    public List<zhuanti> getTopic() {
        return topic;
    }

    public void setTopic(List<zhuanti> topic) {
        this.topic = topic;
    }

    private List<zhuanti> topic;


}
