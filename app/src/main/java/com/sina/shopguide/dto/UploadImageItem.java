package com.sina.shopguide.dto;

import java.io.Serializable;

public class UploadImageItem implements Serializable {

  private static final long serialVersionUID = -2273433197151656688L;

  private String imagePath;

  private String imageUrl;

  public String getImagePath() {
    return imagePath;
  }

  public void setImagePath(String imagePath) {
    this.imagePath = imagePath;
  }

  public String getImageUrl() {
    return imageUrl;
  }

  public void setImageUrl(String imageUrl) {
    this.imageUrl = imageUrl;
  }
}
