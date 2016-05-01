package com.jinguanguke.guwangjinlai.api.service;

import com.jinguanguke.guwangjinlai.model.entity.Arctiny;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by jin on 16/5/1.
 */
public interface MemberWithThirdService {
    @POST("index.php?_interface=insert&_table=dede_member&c=api")
    Call<Arctiny> add(@Query("id") String id,
                      @Query("litpic") String litpic,
                      @Query("mid") String mid,
                      @Query("title") String title
    );
}
