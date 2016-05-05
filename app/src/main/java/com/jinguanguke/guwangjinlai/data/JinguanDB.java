package com.jinguanguke.guwangjinlai.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jinguanguke.guwangjinlai.model.entity.ImageInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by jin on 16/4/13.
 */
public class JinguanDB {
    private String DB_NAME = "JinguanIO.db";
    private final int VERSION = 1;

    private static JinguanDB jinguanDB;
    private SQLiteDatabase db;

    private JinguanDB(Context context) {
        JinguanOpenHelper jinguanOpenHelper = new JinguanOpenHelper(context, DB_NAME, null, VERSION);
        db = jinguanOpenHelper.getWritableDatabase();
    }

    public synchronized static JinguanDB getInstance(Context context) {
        if (jinguanDB == null) {
            jinguanDB = new JinguanDB(context);
        }
        return jinguanDB;
    }

    public void saveImageInfo(ImageInfo info, String table_name) {
        if (info != null) {
            ContentValues values = new ContentValues();
            values.put("url", info.getUrl());
            values.put("who", info.getWho());
            values.put("time", info.getTime());
            values.put("width", info.getWidth());
            values.put("height", info.getHeight());
            values.put("title", info.getTitle());
            values.put("aid", info.getAid());
            values.put("typeid", info.getTypeid());
            values.put("vurl", info.getVurl());
            db.insert(table_name, null, values);
        }
    }

    public List<ImageInfo> findImageInfoAll(String table_name) {
        List<ImageInfo> imageInfos = new ArrayList<>();
        String[] typeid = {"14"};
        String selection = "typeid=?";
        selection = null;
        typeid = null;
        Cursor cursor = db.query(table_name, null, selection, typeid, null, null, "time desc");
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
                info.setTypeid(cursor.getString(cursor.getColumnIndex("typeid")));
                info.setVurl(cursor.getString(cursor.getColumnIndex("vurl")));

                imageInfos.add(info);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return imageInfos;
    }


    public boolean contain(ImageInfo info, String table_name) {
        boolean result = false;
        if (info != null) {
            Cursor cursor = db.query(table_name, null, "url=?", new String[]{info.getUrl()}, null, null, null);
            if (cursor.moveToFirst()) {
                result = true;
            }
            cursor.close();
        }
        return result;
    }
}
