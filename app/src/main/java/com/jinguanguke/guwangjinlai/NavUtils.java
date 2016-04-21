package com.jinguanguke.guwangjinlai;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import com.jinguanguke.guwangjinlai.ui.activity.AccountProfileActivity;
import com.jinguanguke.guwangjinlai.ui.activity.LoginActivity;
import com.jinguanguke.guwangjinlai.ui.activity.TabActivity;

/**
 * Created by YuGang Yang on February 20, 2016.
 * Copyright 2015-2016 qiji.tech. All rights reserved.
 */
public final class NavUtils {

  public static final String ACCOUNT_PROFILE_ACTION = "com.jinguanguke.guwangjinlai.AccountProfileActivity";

  public static void startTab(Activity activity) {
    Intent intent = new Intent(activity, TabActivity.class);
    ActivityCompat.startActivity(activity, intent, null);
  }

  public static void startLogin(Activity activity) {
    Intent intent = new Intent(activity, LoginActivity.class);
    ActivityCompat.startActivity(activity, intent, null);
  }

  public static void startLogin(Activity activity, String action) {
    Intent intent = new Intent(activity, LoginActivity.class);
    intent.setAction(action);
    ActivityCompat.startActivity(activity, intent, null);
  }

  public static void startAccountProfile(Activity activity) {
    Intent intent = new Intent(activity, AccountProfileActivity.class);
    ActivityCompat.startActivity(activity, intent, null);
  }

  private NavUtils() {

  }
}
