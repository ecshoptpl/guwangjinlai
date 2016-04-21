package com.jinguanguke.guwangjinlai.update.bean;

/**
 * Created by jin on 16/4/15.
 */
public class Apkinfo {
    private int version;
    private String introduction;
    private String url;

    public Apkinfo() {

    }

    public Apkinfo(int ver, String intro, String u) {
        this.version = ver;
        this.introduction = intro;
        this.url = u;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
