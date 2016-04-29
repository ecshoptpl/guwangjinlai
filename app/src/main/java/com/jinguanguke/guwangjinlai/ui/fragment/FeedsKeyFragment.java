package com.jinguanguke.guwangjinlai.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Toast;


import com.jinguanguke.guwangjinlai.api.ApiService;
import com.jinguanguke.guwangjinlai.api.service.BiliService;
import com.jinguanguke.guwangjinlai.api.service.FeedService;
import com.jinguanguke.guwangjinlai.model.entity.Bili;
import com.jinguanguke.guwangjinlai.model.entity.Feed;
import com.jinguanguke.guwangjinlai.network.MyNetwork;
import com.jinguanguke.guwangjinlai.ui.activity.DetailActivity;
import com.jinguanguke.guwangjinlai.ui.viewholder.FeedViewHolderFactory;
import com.jinguanguke.guwangjinlai.ui.viewholder.FeedsTextViewHolder;
import com.smartydroid.android.starter.kit.app.StarterKeysFragment;
import com.smartydroid.android.starter.kit.utilities.RecyclerViewUtils;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import support.ui.adapters.EasyRecyclerAdapter;


/**
 * Created by YuGang Yang on February 13, 2016.
 * Copyright 2015-2016 qiji.tech. All rights reserved.
 */
public class FeedsKeyFragment extends StarterKeysFragment<Feed> {
  private FeedService mFeedService;

  public static Fragment create() {
    return new FeedsKeyFragment();
  }

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mFeedService = ApiService.createFeedService();
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    getRecyclerView().addItemDecoration(RecyclerViewUtils.buildItemDecoration(getContext()));
  }

  @Override public void viewHolderFactory(EasyRecyclerAdapter adapter) {

    adapter.viewHolderFactory(new FeedViewHolderFactory(getContext()));
  }

  @Override public Call<ArrayList<Feed>> paginate(Feed sinceItem, Feed maxItem, int perPage) {
    //Call<ArrayList<Feed>> test = mFeedService.getFeedsWith(maxItem == null ? null : maxItem.id + "", null, perPage);
    return mFeedService.getFeedsWith(maxItem == null ? null : maxItem.id + "", null, perPage);
  }

  @Override public Object getKeyForData(Feed item) {
    return item.id;
  }

  @Override public void bindViewHolders(EasyRecyclerAdapter adapter) {
    adapter.bind(Feed.class, FeedsTextViewHolder.class);
  }

  @Override public void onItemClick(int position, View view) {
    super.onItemClick(position, view);
    final Feed feed = getItem(position);
    String url = feed.images.get(0).url;
    Intent intent = new Intent(getActivity(), DetailActivity.class);
    intent.putExtra("url", url);
    intent.putExtra("aid", feed.id);
    startActivity(intent);
  }
}