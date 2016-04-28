/**
 * Created by YuGang Yang on September 25, 2015.
 * Copyright 2007-2015 Laputapp.com. All rights reserved.
 */
package com.jinguanguke.guwangjinlai.api.service;

import com.jinguanguke.guwangjinlai.model.entity.DataInfo;
import com.jinguanguke.guwangjinlai.model.entity.Feed;

import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface FeedService {

  @GET("index.php?c=api&_interface=list&_table=dede_archives") Call<ArrayList<Feed>> getFeedList(
      @Query("page") int page,
      @Query("page_size") int pageSize);

  @GET("index.php?c=player&a=play_movie") Call<ArrayList<Feed>> getFeedsWith(
      @Query("max-id") String maxId,
      @Query("since-id") String sinceId,
      @Query("page_size") int pageSize);

  @GET("index.php?c=api&_table=dede_archives&_interface=list")
  Call<DataInfo> Data();

//  @GET("index.php?c=player&a=play_movie")
  @GET("index.php?c=api&_interface=list&_table=dede_archives&typeid=13")
  Call<DataInfo> Dat();
}
