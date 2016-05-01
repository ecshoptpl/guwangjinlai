package com.jinguanguke.guwangjinlai.api.service;

import com.jinguanguke.guwangjinlai.model.entity.Arctiny;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by jin on 16/5/1.
 */
public interface AddonarticleService {
    @POST("index.php?_interface=insert&_table=dede_addonarticle18&c=api&typeid=14")
    Call<Arctiny> add(@Query("aid") String aid,
                      @Query("mph") String mph,
                      @Query("mid2") String mid

    );

}
