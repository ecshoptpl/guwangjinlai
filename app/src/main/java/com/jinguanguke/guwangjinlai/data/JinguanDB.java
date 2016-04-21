package com.jinguanguke.guwangjinlai.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jinguanguke.guwangjinlai.model.entity.ImageInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jin on 16/4/13.
 */
public class JinguanDB {
    private String DB_NAME = "JinguanIO";
    private final int VERSION = 1;

    private static JinguanDB gankDB;
    private SQLiteDatabase db;

    private JinguanDB(Context context) {
        JinguanOpenHelper gankOpenHelper = new JinguanOpenHelper(context, DB_NAME, null, VERSION);
        db = gankOpenHelper.getWritableDatabase();
    }

    public synchronized static JinguanDB getInstance(Context context) {
        if (gankDB == null) {
            gankDB = new JinguanDB(context);
        }
        return gankDB;
    }

    public void saveImageInfo(ImageInfo info) {
        if (info != null) {
            ContentValues values = new ContentValues();
            values.put("url", info.getUrl());
            values.put("who", info.getWho());
            values.put("time", info.getTime());
            values.put("width", info.getWidth());
            values.put("height", info.getHeight());
            values.put("title", info.getTitle());
            values.put("aid", info.getAid());
            values.put("vurl", info.getVurl());
            db.insert("ImageInfo", null, values);
        }
    }

    public List<ImageInfo> findImageInfoAll() {
        List<ImageInfo> imageInfos = new ArrayList<>();
        Cursor cursor = db.query("ImageInfo", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                ImageInfo info = new ImageInfo();
                info.setUrl(cursor.getString(cursor.getColumnIndex("url")));
                info.setWho(cursor.getString(cursor.getColumnIndex("who")));
                info.setTime(cursor.getString(cursor.getColumnIndex("time")));
                info.setWidth(cursor.getInt(cursor.getColumnIndex("width")));
                info.setHeight(cursor.getInt(cursor.getColumnIndex("height")));
                info.setTitle(cursor.getString(cursor.getColumnIndex("title")));
                info.setAid(cursor.getString(cursor.getColumnIndex("aid")));
                info.setVurl(cursor.getString(cursor.getColumnIndex("vurl")));

                imageInfos.add(info);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return imageInfos;
    }


    public boolean contain(ImageInfo info) {
        boolean result = false;
        if (info != null) {
            Cursor cursor = db.query("ImageInfo", null, "url=?", new String[]{info.getUrl()}, null, null, null);
            if (cursor.moveToFirst()) {
                result = true;
            }
            cursor.close();
        }
        return result;
    }
}
