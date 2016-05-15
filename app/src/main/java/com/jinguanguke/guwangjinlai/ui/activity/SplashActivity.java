package com.jinguanguke.guwangjinlai.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.jinguanguke.guwangjinlai.model.entity.User;
import com.smartydroid.android.starter.kit.account.AccountManager;
import com.smartydroid.android.starter.kit.utilities.ViewUtils;


/**
 * Created by jin on 16/4/25.
 */
public class SplashActivity extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (AccountManager.isLogin()) {
            Intent intent = new Intent(this, TabActivity.class);
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

    }
}
