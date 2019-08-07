package com.sina.shopguide.dto;

import java.io.Serializable;
import java.util.List;

/**
 * Created by tiger on 18/5/10.
 */

public class GroupProduct implements Serializable {

    private static final long serialVersionUID = 4313185640041769093L;

    private List<SimpleProduct> product;
    private String gid;
    private String group_name;

    public List<SimpleProduct> getProduct() {
        return product;
    }

    public void setProduct(List<SimpleProduct> product) {
        this.product = product;
    }

    public String getGid() {
        return gid;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }

    public String getGroup_name() {
        return group_name;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }
}
