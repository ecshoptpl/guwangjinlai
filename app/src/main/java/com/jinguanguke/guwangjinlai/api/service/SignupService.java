package com.jinguanguke.guwangjinlai.api.service;

import com.jinguanguke.guwangjinlai.model.entity.Register;
import com.jinguanguke.guwangjinlai.model.entity.Signup;
import com.jinguanguke.guwangjinlai.model.entity.checkMobile;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by jin on 16/4/21.
 */
public interface SignupService {
    @GET("index.php?c=api&_interface=list&_table=dede_oauth")
    Call<Signup> check_oauth(
            @Query("oauth_name") String oauth_name,
            @Query("oauth_id") String oauth_id
    );

    @GET("index.php?c=api&_interface=check_mobile&_table=dede_member")
    Call<checkMobile> check_mobile(
            @Query("mobile") String mobile

    );

    @POST("index.php?c=api&_interface=insert&_table=dede_member")
    Call<Register> register(
            @Query("userid") String mobile,
            @Query("pwd") String pwd,
            @Query("puserid") String puserid,
            @Query("primary_id") String primary_id
    );

    @POST("index.php?c=api&_interface=update&_table=dede_member")
    Call<Register> update(
            @Query("mid") String mid,
            @Query("pwd") String pwd,
            @Query("puserid") String puserid,
            @Query("primary_id") String primary_id
    );

    @POST("index.php?c=api&_interface=update&_table=dede_member")
    Call<Register> update_uname(
            @Query("mid") String mid,
            @Query("uname") String uname

    );

    @POST("index.php?c=api&_interface=update_score&_table=dede_member")
    Call<Register> update_score(
            @Query("mid") String mid
    );

}
