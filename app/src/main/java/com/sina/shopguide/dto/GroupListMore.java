package com.sina.shopguide.dto;

import java.io.Serializable;
import java.util.List;

/**
 * Created by tiger on 18/5/10.
 */

public class GroupListMore implements Serializable {

    private static final long serialVersionUID = 4313185640041769093L;

    private List<Group> lists;

    private int nums1;
    private int nums2;
    private int page;
    private int perpage;

    public List<Group> getLists() {
        return lists;
    }

    public void setLists(List<Group> lists) {
        this.lists = lists;
    }

    public int getNums1() {
        return nums1;
    }

    public void setNums1(int nums1) {
        this.nums1 = nums1;
    }

    public int getNums2() {
        return nums2;
    }

    public void setNums2(int nums2) {
        this.nums2 = nums2;
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
