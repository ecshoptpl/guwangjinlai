package com.jinguanguke.guwangjinlai.api.service;

import com.jinguanguke.guwangjinlai.model.entity.Register;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by jin on 16/5/1.
 */
public interface OauthService {
    @POST("index.php?c=api&_interface=insert&_table=dede_oauth")
    Call<Register> register(
            @Query("user_id") String user_id,
            @Query("oauth_name") String oauth_name,
            @Query("oauth_id") String oauth_id
   );
}
