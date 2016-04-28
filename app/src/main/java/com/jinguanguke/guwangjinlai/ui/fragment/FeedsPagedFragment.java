/**
 * Created by YuGang Yang on October 28, 2015.
 * Copyright 2007-2015 Laputapp.com. All rights reserved.
 */
package com.jinguanguke.guwangjinlai.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Toast;
import support.ui.adapters.EasyRecyclerAdapter;
import com.jinguanguke.guwangjinlai.api.ApiService;
import com.jinguanguke.guwangjinlai.api.service.FeedService;
import com.jinguanguke.guwangjinlai.model.entity.Feed;
import com.jinguanguke.guwangjinlai.ui.viewholder.FeedViewHolderFactory;
import com.jinguanguke.guwangjinlai.ui.viewholder.FeedsTextViewHolder;
import com.smartydroid.android.starter.kit.app.StarterPagedFragment;
import com.smartydroid.android.starter.kit.utilities.RecyclerViewUtils;
import java.util.ArrayList;
import retrofit2.Call;

public class FeedsPagedFragment extends StarterPagedFragment<Feed> {

  private FeedService mFeedService;

  public static Fragment create() {
    return new FeedsPagedFragment();
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

  @Override public Call<ArrayList<Feed>> paginate(int page, int perPage) {
    return mFeedService.getFeedList(1000, perPage);
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
    Toast.makeText(getContext(), feed.content, Toast.LENGTH_SHORT).show();
  }

}