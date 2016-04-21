/**
 * Created by YuGang Yang on September 24, 2015.
 * Copyright 2007-2015 Laputapp.com. All rights reserved.
 */
package com.jinguanguke.guwangjinlai.ui.viewholder;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.carlosdelachica.easyrecycleradapters.adapter.EasyViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.jinguanguke.guwangjinlai.R;
import com.jinguanguke.guwangjinlai.model.entity.Feed;
import com.jinguanguke.guwangjinlai.ui.view.PhotoCollectionView;

public class FeedsSingleImageViewHolder extends EasyViewHolder<Feed> {

  @Bind(R.id.image_feed_user_avatar) SimpleDraweeView mAvatarView;
  @Bind(R.id.text_feed_username) TextView mUsernameTextView;
  @Bind(R.id.text_feed_content) TextView mContentTextView;
  @Bind(R.id.feed_photo_view) PhotoCollectionView mPhotoView;

  public FeedsSingleImageViewHolder(Context context, ViewGroup parent) {
    super(context, parent, R.layout.list_item_feed_image);
    ButterKnife.bind(this, itemView);
    mPhotoView.setLayoutManager(new LinearLayoutManager(context));
  }

  @Override public void bindTo(Feed feed) {
//    mAvatarView.setImageURI(feed.user.uri());
//    mUsernameTextView.setText(feed.user.uname);
    mContentTextView.setText(feed.content);
    mPhotoView.setData(feed.images);
  }
}
