package com.sina.shopguide.dto;

import java.io.Serializable;
import java.util.List;

/**
 * Created by tiger on 18/5/10.
 */

public class ZhuantiListMore implements Serializable {

    private static final long serialVersionUID = 4313185640041769093L;

    private List<zhuanti> lists;

    private int total;
    private int page;
    private int perpage;

    public List<zhuanti> getLists() {
        return lists;
    }

    public void setLists(List<zhuanti> lists) {
        this.lists = lists;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPerpage() {
        return perpage;
    }

    public void setPerpage(int perpage) {
        this.perpage = perpage;
    }
}
