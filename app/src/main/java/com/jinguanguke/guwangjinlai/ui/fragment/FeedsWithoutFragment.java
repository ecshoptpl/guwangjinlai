package com.jinguanguke.guwangjinlai.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Toast;
import com.carlosdelachica.easyrecycleradapters.adapter.EasyRecyclerAdapter;
import com.jinguanguke.guwangjinlai.api.ApiService;
import com.jinguanguke.guwangjinlai.api.service.FeedService;
import com.jinguanguke.guwangjinlai.model.entity.Feed;
import com.jinguanguke.guwangjinlai.ui.view.AppEmptyView;
import com.jinguanguke.guwangjinlai.ui.viewholder.FeedViewHolderFactory;
import com.jinguanguke.guwangjinlai.ui.viewholder.FeedsTextViewHolder;
import com.jinguanguke.android.starter.kit.app.StarterRecyclerFragment;
import java.util.ArrayList;
import retrofit2.Call;
import support.ui.content.RequiresContent;

/**
 * Created by YuGang Yang on February 13, 2016.
 * Copyright 2015-2016 qiji.tech. All rights reserved.
 */
@RequiresContent(emptyView = AppEmptyView.class)
public class FeedsWithoutFragment extends StarterRecyclerFragment<Feed> {
  private FeedService mFeedService;

  public static Fragment create() {
    return new FeedsWithoutFragment();
  }

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mFeedService = ApiService.createFeedService();
  }

  @Override public void viewHolderFactory(EasyRecyclerAdapter adapter) {
    adapter.viewHolderFactory(new FeedViewHolderFactory(getContext()));
  }

  @Override public Call<ArrayList<Feed>> paginate(int page, int perPage) {
    return mFeedService.getFeedList(1, 2);
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
