package com.sina.shopguide.net.request;

import com.sina.shopguide.net.HttpParam;

/**
 * 提出建议
 * Created by XiangWei on 18/6/1.
 */

public class SuggestBackParams extends BaseRequestParams {

    private String text;

    @HttpParam("other_question")
    private int otherQuestion = 1;

    @HttpParam("img_path")
    private String imgPath;

    @HttpParam("device_version")
    private String deviceVersion;

    @HttpParam("os_version")
    private String osVersion;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getOtherQuestion() {
        return otherQuestion;
    }

    public void setOtherQuestion(int otherQuestion) {
        this.otherQuestion = otherQuestion;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public String getDeviceVersion() {
        return deviceVersion;
    }

    public void setDeviceVersion(String deviceVersion) {
        this.deviceVersion = deviceVersion;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }
}
