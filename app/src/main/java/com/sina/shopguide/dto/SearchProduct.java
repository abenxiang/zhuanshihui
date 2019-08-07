package com.sina.shopguide.dto;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class SearchProduct implements Serializable {

    private static final long serialVersionUID = -6137664163310497568L;


    private List<Product> data;

    private int count;

    public List<Product> getData() {
        return data;
    }

    public void setData(List<Product> data) {
        this.data = data;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
