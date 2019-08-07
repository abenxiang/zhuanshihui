package com.sina.shopguide.dto;

import java.io.Serializable;
import java.util.List;

/**
 * Created by tiger on 18/5/10.
 */

public class ZhuantiDetailMore implements Serializable {

    private static final long serialVersionUID = 4313185640041769093L;

    private List<zhuanti> relat;
    private zhuanti info;
    private List<GroupProduct> gproduct;

    public List<zhuanti> getRelat() {
        return relat;
    }

    public void setRelat(List<zhuanti> relat) {
        this.relat = relat;
    }

    public zhuanti getInfo() {
        return info;
    }

    public void setInfo(zhuanti info) {
        this.info = info;
    }

    public List<GroupProduct> getGproduct() {
        return gproduct;
    }

    public void setGproduct(List<GroupProduct> gproduct) {
        this.gproduct = gproduct;
    }
}
