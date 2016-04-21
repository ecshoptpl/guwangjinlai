package com.jinguanguke.guwangjinlai.ui.activity;

import android.os.Bundle;
import android.widget.TextView;
import butterknife.Bind;
import com.jinguanguke.guwangjinlai.R;
import com.jinguanguke.guwangjinlai.api.ApiService;
import com.jinguanguke.guwangjinlai.api.service.AuthService;
import com.jinguanguke.guwangjinlai.model.entity.User;
import com.jinguanguke.guwangjinlai.util.MessageCallback;
import com.jinguanguke.android.starter.kit.app.StarterActivity;
import com.jinguanguke.android.starter.kit.utilities.NetworkUtils;
import retrofit2.Call;

/**
 * Created by YuGang Yang on February 26, 2016.
 * Copyright 2015-2016 qiji.tech. All rights reserved.
 */
public class AccountProfileActivity extends StarterActivity {

  @Bind(R.id.text_account_username) TextView mUsernameTextView;

  private NetworkUtils<User> mNetworkUtils;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_profile);
    AuthService authService = ApiService.createAuthService();
    Call<User> userCall = authService.profile();

    mNetworkUtils = NetworkUtils.create(new MessageCallback<User>(this) {
      @Override public void respondSuccess(User data) {
        super.respondSuccess(data);
      //  mUsernameTextView.setText(data.getData().getItems());
      }
    });
    mNetworkUtils.enqueue(userCall);
  }


  @Override protected void onDestroy() {
    super.onDestroy();
    mNetworkUtils.onDestroy();
    mNetworkUtils = null;
  }
}
