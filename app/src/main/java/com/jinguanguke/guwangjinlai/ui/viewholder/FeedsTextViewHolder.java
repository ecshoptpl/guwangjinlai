/**
 * Created by YuGang Yang on September 24, 2015.
 * Copyright 2007-2015 Laputapp.com. All rights reserved.
 */
package com.jinguanguke.guwangjinlai.ui.viewholder;


import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;

import support.ui.adapters.EasyViewHolder;

import com.facebook.drawee.view.SimpleDraweeView;
import com.jinguanguke.guwangjinlai.R;
import com.jinguanguke.guwangjinlai.model.entity.Feed;





public class FeedsTextViewHolder extends EasyViewHolder<Feed> {

  @Bind(R.id.image_feed_user_avatar) SimpleDraweeView mAvatarView;
  @Bind(R.id.text_feed_username) TextView mUsernameTextView;
  @Bind(R.id.text_feed_content) TextView mContentTextView;

  public FeedsTextViewHolder(Context context, ViewGroup parent) {
    super(context, parent, R.layout.list_item_feed);
    ButterKnife.bind(this, itemView);
  }

  @Override public void bindTo(int position, Feed feed) {
    mAvatarView.setImageURI(feed.user.uri());
    mUsernameTextView.setText(feed.user.nickname);
    mContentTextView.setText(feed.content);
  }
}
