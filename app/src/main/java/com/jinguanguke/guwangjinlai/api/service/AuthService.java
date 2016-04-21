package com.jinguanguke.guwangjinlai.api.service;

import com.jinguanguke.guwangjinlai.model.entity.User;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by YuGang Yang on February 20, 2016.
 * Copyright 2015-2016 qiji.tech. All rights reserved.
 */
public interface AuthService {

  /**
   * 登录接口
   *
   * @param phone 手机号码
   * @param password 密码
   * @return Call
   */
  @FormUrlEncoded @POST("index.php?c=api&_table=dede_member&_interface=android_login") Call<User> login(
      @Field("userid") String phone,
      @Field("pwd") String password);

  @GET("/user/profile") Call<User> profile();
}
