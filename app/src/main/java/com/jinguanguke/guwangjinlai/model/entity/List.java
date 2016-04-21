package com.jinguanguke.guwangjinlai.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.smartydroid.android.starter.kit.model.entity.Entity;

/**
 * Created by jin on 16/4/19.
 */


@JsonIgnoreProperties(ignoreUnknown = true) public class List extends Entity {

    public Integer aid;
    public String pic;
    public String title;
    public String author;

    public List()
    {

    }
    public List(Parcel source)
    {
        this.aid = source.readInt();
        this.pic = source.readString();
        this.title = source.readString();
        this.author = source.readString();
    }
    public static final Parcelable.Creator<List> CREATOR = new Parcelable.Creator<List>() {
        public List createFromParcel(Parcel source) {
            return new List(source);
        }

        public List[] newArray(int size) {
            return new List[size];
        }
    };

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(aid);
        dest.writeString(pic);
        dest.writeString(title);
        dest.writeString(author);
    }

    @Override public int describeContents() {
        return 0;
    }
}

