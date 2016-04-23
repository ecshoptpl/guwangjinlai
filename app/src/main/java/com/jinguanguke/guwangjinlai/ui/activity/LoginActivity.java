package com.jinguanguke.guwangjinlai.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import butterknife.Bind;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import com.jinguanguke.guwangjinlai.R;
import com.jinguanguke.guwangjinlai.api.ApiService;
import com.jinguanguke.guwangjinlai.api.service.AuthService;
import com.jinguanguke.guwangjinlai.api.service.SignupService;
import com.jinguanguke.guwangjinlai.model.entity.DataInfo;
import com.jinguanguke.guwangjinlai.model.entity.Signup;
import com.jinguanguke.guwangjinlai.model.entity.User;
import com.smartydroid.android.starter.kit.app.StarterKitApp;
import com.smartydroid.android.starter.kit.app.StarterNetworkActivity;
import android.os.Handler.Callback;
import android.widget.Toast;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.tencent.qzone.QZone;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by YuGang Yang on February 20, 2016.
 * Copyright 2015-2016 qiji.tech. All rights reserved.
 */
public class LoginActivity extends StarterNetworkActivity<User> implements View.OnClickListener,Callback, PlatformActionListener {

  private static final int MSG_SMSSDK_CALLBACK = 1;
  private static final int MSG_AUTH_CANCEL = 2;
  private static final int MSG_AUTH_ERROR= 3;
  private static final int MSG_AUTH_COMPLETE = 4;

  @Bind(R.id.container_login_username) TextInputLayout mUsernameContainer;
  @Bind(R.id.container_login_password) TextInputLayout mPasswordContainer;
  EditText mUsernameEdit;
  EditText mPasswordEdit;
  private Handler handler;

  private AuthService mAuthService;
  private SignupService mSignuService;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
    SharedPreferences.Editor editor = pref.edit();
    if(pref.getBoolean("isUserLogin", false)){
      startActivity(new Intent(LoginActivity.this, TabActivity.class));
      finish();
    } else {
     // ShareSDK.initSDK(this);
//      handler = new Handler(this);
      setContentView(R.layout.activity_login);

      mAuthService = ApiService.createAuthService();
      mSignuService = ApiService.createSignupService();

      Resources resources = StarterKitApp.appResources();
      mUsernameContainer.setHint(resources.getString(R.string.login_username_hint));
      mPasswordContainer.setHint(resources.getString(R.string.login_passowrd_hint));
      setupViews();
    }
  }

  private void setupViews() {
    ActionBar actionBar = getSupportActionBar();
    if (actionBar != null) {
      actionBar.setElevation(0);
      actionBar.setDisplayShowTitleEnabled(false);
    }
    mUsernameEdit = mUsernameContainer.getEditText();
    mPasswordEdit = mPasswordContainer.getEditText();
  }

  @OnTextChanged(
      R.id.edit_login_username)
  public void onUsernameTextChanged(CharSequence s, int start, int before, int count) {
    if (s.length() < 6) {
      mUsernameContainer.setErrorEnabled(true);
      mUsernameContainer.setError(
          StarterKitApp.appResources().getString(R.string.login_username_error));
    } else {
      mUsernameContainer.setErrorEnabled(false);
    }
  }

  @OnTextChanged(
      R.id.edit_login_password)
  public void onPasswordTextChanged(CharSequence s, int start, int before, int count) {
    if (s.length() < 6) {
      mPasswordContainer.setErrorEnabled(true);
      mPasswordContainer.setError(
          StarterKitApp.appResources().getString(R.string.login_passowrd_error));
    } else {
      mPasswordContainer.setErrorEnabled(false);
    }
  }

  @OnClick({ R.id.btn_login, R.id.container_register }) public void onClick(View view) {
    switch (view.getId()) {
      case R.id.btn_login: {
        doLogin();
        break;
      }
      case R.id.container_register:{
        Intent intent = new Intent();
        intent.setClass(LoginActivity.this, SignupActivity.class);
        startActivity(intent);
        finish();
        break;
      }
    }
  }

  @OnClick({ R.id.image_login_qq}) public void onQqClick(View view) {
    Platform qzone = ShareSDK.getPlatform(QZone.NAME);
    authorize(qzone);
    Log.i("qq_long", "qqstart");
  }
  //执行授权,获取用户信息
  //文档：http://wiki.mob.com/Android_%E8%8E%B7%E5%8F%96%E7%94%A8%E6%88%B7%E8%B5%84%E6%96%99
  private void authorize(Platform plat) {
    if (plat == null) {
      //popupOthers();
      return;
    }

    plat.setPlatformActionListener(LoginActivity.this);
    //关闭SSO授权
    plat.SSOSetting(true);
    plat.showUser(null);
  }


  private void doLogin() {
    hideSoftInputMethod();
    final String username = mUsernameEdit.getText().toString();
    final String password = mPasswordEdit.getText().toString();

    Call<User> userCall = mAuthService.login(username, password);
    networkQueue().enqueue(userCall);
  }

  @Override public void startRequest() {
    showHud("正在登录....");
  }

  @Override public void respondSuccess(User data) {
//    ArrayList users = (ArrayList) data.getData().getItems();
//    User user = (User) users.get(0);

    if(data.getUserid() != null) {
      //AccountManager.store(data.getData().getItems().get(0));
      Snackbar.make(getWindow().getDecorView(), "登录成功", Snackbar.LENGTH_SHORT).show();
      Intent intent = new Intent();
      intent.setClass(LoginActivity.this, TabActivity.class);
      startActivity(intent);
      finish();//停止当前的Activity,如果不写,则按返回键会跳转回原来的Activit
    }
    else
    {
      Snackbar.make(getWindow().getDecorView(), "用户名或密码错误", Snackbar.LENGTH_SHORT).show();
    }
  }


  @Override public void endRequest() {
    dismissHud();
  }

  @Nullable @Override public Intent getParentActivityIntent() {
    // 可以根据action来手动指定你想返回的Parent Activity
    String action = getIntent().getAction();
    if (com.jinguanguke.guwangjinlai.NavUtils.ACCOUNT_PROFILE_ACTION.equals(action)) {
      return new Intent(this, AccountProfileActivity.class);
    }
    return super.getParentActivityIntent();
  }



  public void onComplete(Platform platform, int action, HashMap<String, Object> res) {
    if (action == Platform.ACTION_USER_INFOR) {
      Message msg = new Message();
      msg.what = MSG_AUTH_COMPLETE;
      msg.obj = new Object[] {platform.getName(), res};
      handler.sendMessage(msg);
    }
  }

  public void onError(Platform platform, int action, Throwable t) {
    if (action == Platform.ACTION_USER_INFOR) {
      handler.sendEmptyMessage(MSG_AUTH_ERROR);
    }
    t.printStackTrace();
  }

  public void onCancel(Platform platform, int action) {
    if (action == Platform.ACTION_USER_INFOR) {
      handler.sendEmptyMessage(MSG_AUTH_CANCEL);
    }
  }
  public boolean handleMessage(Message msg) {



    switch(msg.what) {
      case MSG_AUTH_CANCEL: {
        //取消授权
//        Toast.makeText(activity, R.string.auth_cancel, Toast.LENGTH_SHORT).show();
      }
      break;
      case MSG_AUTH_ERROR: {
        //授权失败
//        Toast.makeText(activity, R.string.auth_error, Toast.LENGTH_SHORT).show();
      }
      break;
      case MSG_AUTH_COMPLETE: {
        //授权成功
        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS).writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://www.jinguanguke.com/plus/io/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        SignupService service = retrofit.create(SignupService.class);
        Call<Signup> sign = service.check_oauth("aa", 1);
        sign.enqueue(new retrofit2.Callback<Signup>() {
          @Override
          public void onResponse(retrofit2.Call<Signup> call, retrofit2.Response<Signup> response) {
            if(response.body().isData() == false)
            {
              Intent intent = new Intent();
              intent.setClass(LoginActivity.this, SignupActivity.class);
              startActivity(intent);
              //finish();
            }
            else
            {
            }
          }

          @Override
          public void onFailure(retrofit2.Call<Signup> call, Throwable t) {
            Log.i("shibai", t.toString());
          }
        });
        Object[] objs = (Object[]) msg.obj;
        String platform = (String) objs[0];
        Toast.makeText(getBaseContext(), platform, Toast.LENGTH_SHORT).show();
        HashMap<String, Object> res = (HashMap<String, Object>) objs[1];
        switch (platform)
        {
          case "QZone":

            Log.i("res",res.toString());
          break;

          default:
          {
            Log.i("res",platform);
          }
        }
//        if (signupListener != null && signupListener.onSignin(platform, res)) {
//          SignupPage signupPage = new SignupPage();
//          signupPage.setOnLoginListener(signupListener);
//          signupPage.setPlatform(platform);
//          signupPage.show(activity, null);
        } break;
      }




    return false;
  }

}
