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

//import com.alibaba.sdk.android.AlibabaSDK;
//import com.alibaba.sdk.android.callback.InitResultCallback;
//import com.duanqu.qupai.engine.session.VideoSessionCreateInfo;
//import com.duanqu.qupai.sdk.android.QupaiService;
import com.alibaba.sdk.android.AlibabaSDK;
import com.alibaba.sdk.android.callback.InitResultCallback;
import com.duanqu.qupai.engine.session.VideoSessionCreateInfo;
import com.duanqu.qupai.sdk.android.QupaiService;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.Files;
import com.jinguanguke.guwangjinlai.data.Constant;
import com.jinguanguke.guwangjinlai.model.entity.User;
import com.smartydroid.android.starter.kit.StarterKit;
import com.smartydroid.android.starter.kit.account.Account;
import com.smartydroid.android.starter.kit.app.StarterKitApp;
import com.smartydroid.android.starter.kit.retrofit.RetrofitBuilder;

import java.io.File;
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
//    /**
//     * 集成必须要做的初始化
//     */
    AlibabaSDK.turnOnDebug();
    AlibabaSDK.asyncInit(this, new InitResultCallback() {
      @Override
      public void onSuccess() {
        Toast.makeText(DemoApp.this, "初始化成功 ", Toast.LENGTH_SHORT)
                .show();
//        QupaiService qupaiService = AlibabaSDK
//                .getService(QupaiService.class);
//
//        Intent moreMusic = new Intent();
//        //是否需要更多音乐页面--如果不需要填空
//       // moreMusic.setClass(DemoApp.this, MoreMusicActivity.class);
//
//        VideoSessionCreateInfo info =new VideoSessionCreateInfo.Builder()
//                .setOutputDurationLimit(Constant.DEFAULT_DURATION_LIMIT)
//                .setOutputVideoBitrate(Constant.DEFAULT_DURATION_LIMIT)
//                .setHasImporter( true)
//                .setWaterMarkPath(Constant.WATER_MARK_PATH)
//                .setWaterMarkPosition(1)
//                .setHasEditorPage(true)
//                .setCameraFacing(Camera.CameraInfo.CAMERA_FACING_FRONT)
//                .setBeautyProgress(80)
//                .setBeautySkinOn(true)
//                .build();
//
//        qupaiService.initRecord(info);
//        qupaiService.hasMroeMusic(moreMusic);
//
//        if(qupaiService != null){
//          qupaiService.addMusic(0, "Athena", "assets://Qupai/music/Athena");
//          qupaiService.addMusic(1, "Box Clever", "assets://Qupai/music/Box Clever");
//          qupaiService.addMusic(2, "Byebye love", "assets://Qupai/music/Byebye love");
//          qupaiService.addMusic(3, "chuangfeng", "assets://Qupai/music/chuangfeng");
//          qupaiService.addMusic(4, "Early days", "assets://Qupai/music/Early days");
//          qupaiService.addMusic(5, "Faraway", "assets://Qupai/music/Faraway");
//        }
      }

      @Override
      public void onFailure(int i, String s) {

        Toast.makeText(DemoApp.this, "初始化失败 " + s, Toast.LENGTH_SHORT)
                .show();
      }
    });
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
