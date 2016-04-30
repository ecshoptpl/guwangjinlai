/**
 * Created by YuGang Yang on September 24, 2015.
 * Copyright 2007-2015 Laputapp.com. All rights reserved.
 */
package com.jinguanguke.guwangjinlai;

import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.os.StrictMode;
import android.support.multidex.MultiDex;
import android.widget.Toast;


import com.facebook.drawee.backends.pipeline.Fresco;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.jinguanguke.guwangjinlai.data.Constant;
import com.jinguanguke.guwangjinlai.model.entity.User;
import com.smartydroid.android.starter.kit.StarterKit;
import com.smartydroid.android.starter.kit.account.Account;
import com.smartydroid.android.starter.kit.app.StarterKitApp;
import com.smartydroid.android.starter.kit.retrofit.RetrofitBuilder;


import java.io.IOException;

import static android.os.Build.VERSION.SDK_INT;
import static android.os.Build.VERSION_CODES.GINGERBREAD;

public class DemoApp extends StarterKitApp {

  @Override public void onCreate() {

    // common config
    new StarterKit.Builder()
        .setDebug(BuildConfig.DEBUG)
        .build();

    super.onCreate();
    //enabledStrictMode();
    //LeakCanary.install(this);

    // init api service
    new RetrofitBuilder.Builder()

        .accept(Profile.API_ACCEPT)
        .baseUrl(Profile.API_ENDPOINT)
        .build();

    Fresco.initialize(appContext());


  }

  private void enabledStrictMode() {
    if (SDK_INT >= GINGERBREAD) {
      StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder() //
          .detectAll() //
          .penaltyLog() //
          .penaltyDeath() //
          .build());
    }
  }

  @Override public Account accountFromJson(String json) {
    ObjectMapper mapper = new ObjectMapper();
    try {
      return mapper.readValue(json, User.class);
    } catch (IOException e) {
      // Nothing to do
    }
    return null;
  }

  @Override
  protected void attachBaseContext(Context base) {
    super.attachBaseContext(base);
    MultiDex.install(this);
  }

}
