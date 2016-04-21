package com.jinguanguke.guwangjinlai.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by jin on 16/4/16.
 */
public class AppGlobalSetting {
    private static final String GLOBALSETTING="AppGlobalSetting";
    private Context context;

    public AppGlobalSetting(Context context) {
        this.context = context;
    }

    public boolean getBooleanGlobalItem(String key, boolean defaultValue){
        return context.getSharedPreferences(GLOBALSETTING,0).getBoolean(key, defaultValue);
    }

    public void saveGlobalConfigItem(String key, boolean value){
        SharedPreferences.Editor editor = this.context.
                getSharedPreferences(GLOBALSETTING, 0).edit();
        editor.putBoolean(key, value);
        editor.commit();
    }
}
