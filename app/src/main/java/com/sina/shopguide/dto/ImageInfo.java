package com.sina.shopguide.dto;

import java.io.Serializable;

/**
 * Created by tiger on 18/5/10.
 */

public class ImageInfo implements Serializable {

    private static final long serialVersionUID = 4313185640041769093L;

    public String img;

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
