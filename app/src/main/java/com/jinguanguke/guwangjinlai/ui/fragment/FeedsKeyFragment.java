package com.jinguanguke.guwangjinlai.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Toast;
import com.carlosdelachica.easyrecycleradapters.adapter.EasyRecyclerAdapter;
import com.jinguanguke.guwangjinlai.api.ApiService;
import com.jinguanguke.guwangjinlai.api.service.FeedService;
import com.jinguanguke.guwangjinlai.model.entity.Feed;
import com.jinguanguke.guwangjinlai.ui.viewholder.FeedViewHolderFactory;
import com.jinguanguke.guwangjinlai.ui.viewholder.FeedsTextViewHolder;
import com.smartydroid.android.starter.kit.app.StarterKeysFragment;
import com.smartydroid.android.starter.kit.utilities.RecyclerViewUtils;
import java.util.ArrayList;

import retrofit2.Call;


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
    return mFeedService.getFeedsWith(maxItem == null ? null : maxItem.code + "", null, perPage);
  }

  @Override public Object getKeyForData(Feed item) {
    return item.code;
  }

  @Override public void bindViewHolders(EasyRecyclerAdapter adapter) {
    adapter.bind(Feed.class, FeedsTextViewHolder.class);
  }

  @Override public void onItemClick(int position, View view) {
    super.onItemClick(position, view);
    final Feed feed = getItem(position);
    Toast.makeText(getContext(), feed.code, Toast.LENGTH_SHORT).show();
  }
}
