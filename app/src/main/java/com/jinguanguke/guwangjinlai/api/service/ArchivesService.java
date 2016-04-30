package com.jinguanguke.guwangjinlai.api.service;

import com.jinguanguke.guwangjinlai.model.entity.Arctiny;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by jin on 16/4/30.
 */
public interface ArchivesService {
    @POST("_interface=insert&_table=dede_archives&c=api&channel=18&typeid=14&voteid=0")
    Call<Arctiny> add(@Query("id") String id,
                      @Query("litpic") String litpic,
                      @Query("mid") String mid,
                      @Query("title") String title
        );
}
