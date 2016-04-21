/**
 * Created by YuGang Yang on September 25, 2015.
 * Copyright 2007-2015 Laputapp.com. All rights reserved.
 */
package com.jinguanguke.guwangjinlai.api;

import com.jinguanguke.guwangjinlai.api.service.AuthService;
import com.jinguanguke.guwangjinlai.api.service.FeedService;
import com.jinguanguke.guwangjinlai.api.service.SignupService;
import com.smartydroid.android.starter.kit.retrofit.RetrofitBuilder;
import retrofit2.Retrofit;

public class ApiService {

  public static FeedService createFeedService() {
    return retrofit().create(FeedService.class);
  }

  public static AuthService createAuthService() {
    return retrofit().create(AuthService.class);
  }

  public static SignupService createSignupService() {
    return retrofit().create(SignupService.class);
  }


  private static Retrofit retrofit() {
    return RetrofitBuilder.get().retrofit();
  }
}
