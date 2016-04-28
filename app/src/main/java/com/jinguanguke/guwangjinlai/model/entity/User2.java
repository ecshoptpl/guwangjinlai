package com.jinguanguke.guwangjinlai.model.entity;

/**
 * Created by jin on 16/4/28.
 */
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartydroid.android.starter.kit.account.Account;
import com.smartydroid.android.starter.kit.model.entity.Entity;


@JsonIgnoreProperties(ignoreUnknown = true) public class User2 extends Entity implements Account {

    public Integer id;
    public String phone;
    public String nickname;
    public String avatar;
    public String token;

    public User2() {

    }

    public User2(Parcel source) {
        this.id = source.readInt();
        this.phone = source.readString();
        this.nickname = source.readString();
        this.avatar = source.readString();
        this.token = source.readString();
    }

    public static final Parcelable.Creator<User2> CREATOR = new Parcelable.Creator<User2>() {
        public User2 createFromParcel(Parcel source) {
            return new User2(source);
        }

        public User2[] newArray(int size) {
            return new User2[size];
        }
    };

    @Override public int describeContents() {
        return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(phone);
        dest.writeString(nickname);
        dest.writeString(avatar);
        dest.writeString(token);
    }

    public Uri uri() {
        if (TextUtils.isEmpty(avatar)) return null;

        if (avatar.startsWith("http://")) {
            return Uri.parse(avatar);
        }

        return null;
    }

//    @Override public String token() {
//        return token;
//    }

    @Override public Object key() {
        return id;
    }

    @Override public String toJson() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }
    @Override public String token() {
        return token;
    }
}