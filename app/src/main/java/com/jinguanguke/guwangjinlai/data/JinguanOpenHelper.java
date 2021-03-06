package com.jinguanguke.guwangjinlai.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by jin on 16/4/13.
 */
public class JinguanOpenHelper extends SQLiteOpenHelper {
    private final String CREATE_DB = "create table ImageInfo ("
            + "id integer primary key autoincrement, "
            + "url text, "
            + "vurl text, "
            + "title text, "
            + "who char(20), "
            + "time char(50), "
            + "width integer, "
            + "aid char(50), "
            + "height integer)";


    public JinguanOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_DB);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
