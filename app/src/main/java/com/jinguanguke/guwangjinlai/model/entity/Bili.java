package com.jinguanguke.guwangjinlai.model.entity;

/**
 * Created by jin on 16/4/28.
 */
public class Bili {

    /**
     * img : http://i0.hdslb.com/userup/1707/1262H2063-B55.jpg
     * cid : http://comment.bilibili.com/3790460.xml
     * src : http://ws.acgvideo.com/3/67/3790460-1.mp4?wsTime=1461848829&wsSecret2=55f0b7dfad49f454666f609a1c910126&oi=3683920404&internal=1
     */

    private String img;
    private String cid;
    private String src;

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }
}
