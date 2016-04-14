package com.xfzj.getbook.common;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by zj on 2016/4/12.
 * "mode":modeType
 * "image": imageURL,
 * "width":宽度，模式 0, 4, 5必填
 * "height"：高度，模式 1, 4, 5必填
 * "longEdge"：长边，模式 2必填
 * "shortEdge"：短边，模式 3必填
 * "quality"：质量，选填, 范围 1-100(只对jpg文件有效)
 * "outType"：输出类型，0:默认，输出url；1:输出base64编码的字符串流
 */
public class BmobThumbnail {

    private int modeType;
    
    private String imageURL;
    
    private int width,height;
    
    private int quality;

    private int outType;

    public BmobThumbnail(String imageURL, int width,int height) {
        this.imageURL = imageURL;
        this.width = width;
        this.height = height;
        modeType =4;
        quality=100;
        outType = 0;
    }


    public int getModeType() {
        return modeType;
    }

    public void setModeType(int modeType) {
        this.modeType = modeType;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getQuality() {
        return quality;
    }

    public void setQuality(int quality) {
        this.quality = quality;
    }

    public int getOutType() {
        return outType;
    }

    public void setOutType(int outType) {
        this.outType = outType;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    @Override
    public String toString() {
        return "BmobThumbnail{" +
                "modeType=" + modeType +
                ", imageURL='" + imageURL + '\'' +
                ", width=" + width +
                ", height=" + height +
                ", quality=" + quality +
                ", outType=" + outType +
                '}';
    }

    public String toJson() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("mode",modeType);
            jsonObject.put("image",imageURL);
            jsonObject.put("width",width);
            jsonObject.put("height",height);
            jsonObject.put("quality",quality);
            jsonObject.put("outType",outType);
            return jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
