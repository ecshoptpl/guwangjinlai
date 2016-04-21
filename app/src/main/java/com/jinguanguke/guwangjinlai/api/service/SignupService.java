package com.jinguanguke.guwangjinlai.api.service;

import com.jinguanguke.guwangjinlai.model.entity.Signup;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by jin on 16/4/21.
 */
public interface SignupService {
    @GET("index.php?c=api&_interface=list&_table=dede_oauth")
    Call<Signup> check_oauth(
            @Query("oauth_name") String oauth_name,
            @Query("oauth_id") int oauth_id
    );
}
