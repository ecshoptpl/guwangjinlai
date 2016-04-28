package com.jinguanguke.guwangjinlai.api.service;

import com.jinguanguke.guwangjinlai.model.entity.Bili;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by jin on 16/4/28.
 */
public interface BiliService {
    @GET("html5")
    Call<Bili> getBili(
            @Query("aid") int id);
}
