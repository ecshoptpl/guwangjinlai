/**
 * Created by YuGang Yang on September 25, 2015.
 * Copyright 2007-2015 Laputapp.com. All rights reserved.
 */
package com.jinguanguke.guwangjinlai.api.service;

import com.jinguanguke.guwangjinlai.model.entity.DataInfo;
import com.jinguanguke.guwangjinlai.model.entity.Feed;
import com.jinguanguke.guwangjinlai.model.entity.VideoTotal;

import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface FeedService {

  @GET("index.php?c=player&a=play_music") Call<ArrayList<Feed>> getFeedList(
      @Query("page") int page,
      @Query("page_size") int pageSize);

  @GET("index.php?c=player&a=play_youku") Call<ArrayList<Feed>> getFeedsWith(
      @Query("max-id") String maxId,
      @Query("since-id") String sinceId,
      @Query("page_size") int pageSize);

  @GET("index.php?c=api&_table=dede_archives&_interface=list")
  Call<DataInfo> Data(
          @Query("page") int page
  );

//  @GET("index.php?c=player&a=play_movie")
  @GET("index.php?c=api&_interface=list&_table=dede_archives&typeid=17")
  Call<DataInfo> get_lecture(
          @Query("page") int page
  );

  @GET("index.php?c=api&_interface=list&_table=dede_archives&typeid=14")
  Call<DataInfo> get_funny(
          @Query("page") int page
  );

  @GET("index.php?c=api&_interface=list&_table=dede_archives")
  Call<DataInfo> get_me(
          @Query("mid") String mid
  );

  @GET("index.php?c=api&_interface=list&_table=dede_archives")
  Call<DataInfo> get_my_videos(
          @Query("mid") String mid
  );

  @GET("index.php?c=api&_interface=get_video_total&_table=dede_archives")
  Call<VideoTotal> get_video_total(
          @Query("mid") String mid
  );

  @GET("index.php?c=api&_interface=get_sub_total&_table=dede_member")
  Call<VideoTotal> get_sub_total(
          @Query("mid") String mid
  );

}
