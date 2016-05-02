package com.jinguanguke.guwangjinlai.ui.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.OnClick;
import com.facebook.drawee.view.SimpleDraweeView;
import com.jinguanguke.guwangjinlai.R;
import com.jinguanguke.guwangjinlai.model.entity.User;
import com.jinguanguke.guwangjinlai.ui.activity.AgreementActivity;
import com.jinguanguke.guwangjinlai.ui.activity.JifenActivity;
import com.jinguanguke.guwangjinlai.update.CheckUpdate;
import com.smartydroid.android.starter.kit.account.AccountManager;
import com.smartydroid.android.starter.kit.utilities.ViewUtils;

/**
 * Created by YuGang Yang on February 21, 2016.
 * Copyright 2015-2016 qiji.tech. All rights reserved.
 */
public class AccountFragment extends BaseFragment {

  @Bind(R.id.toolbar) Toolbar mToolbar;
  @Bind(R.id.container_login) View mLoginContainer;
  @Bind(R.id.container_account) View mUserInfoContainer;
  @Bind(R.id.image_avatar) SimpleDraweeView mAvatarView;
  @Bind(R.id.text_account_username) TextView mUsernameTextView;

  @Override protected int getFragmentLayout() {
    return R.layout.fragment_account;
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    mToolbar.setTitle(R.string.toolbar_title_account);
  }

  @Override public void onResume() {
    super.onResume();
    if (AccountManager.isLogin()) {
      ViewUtils.setGone(mLoginContainer, true);
      ViewUtils.setGone(mUserInfoContainer, false);
      User user = AccountManager.getCurrentAccount();
      mUsernameTextView.setText(user.getUserid());
      String ava = "http://www.jinguanguke.com/uploads/app/ava" + user.getMid() + ".png";
      Uri face = Uri.parse(ava);
      mAvatarView.setImageURI(face);
    } else {
      ViewUtils.setGone(mLoginContainer, false);
      ViewUtils.setGone(mUserInfoContainer, true);
    }
  }

  @OnClick({
      R.id.container_account_settings,
  }) public void onClick(View view) {
    Intent intent = new Intent(getActivity(),AgreementActivity.class);
    startActivity(intent);
  }

  @OnClick({
          R.id.container_account_update,
  }) public void onUpdaateClick(View view) {
    CheckUpdate.getInstance().startCheck(view.getContext());
  }

  @OnClick({
          R.id.container_account_logout,
  }) public void onLogoutClick(View view) {
    AccountManager.registerObserver(new Logout());
    AccountManager.logout();
  }

  @OnClick({
          R.id.container_account_jifen,
  }) public void onJifenClick(View view) {
    Intent intent = new Intent(getActivity(),JifenActivity.class);
    startActivity(intent);
  }

  class Logout implements AccountManager.CurrentAccountObserver {
    @Override
    public void notifyChange() {
      getActivity().finish();
      System.exit(0);
    }
  }
}
