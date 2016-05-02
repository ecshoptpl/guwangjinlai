package com.jinguanguke.guwangjinlai.ui.activity;

import android.os.Bundle;
import android.webkit.WebView;

import com.jinguanguke.guwangjinlai.R;
import com.jinguanguke.guwangjinlai.model.entity.User;
import com.smartydroid.android.starter.kit.account.AccountManager;
import com.smartydroid.android.starter.kit.app.StarterActivity;

/**
 * Created by jin on 16/5/2.
 */
public class JifenActivity extends StarterActivity {
    private WebView webView;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jifen);
        User user = AccountManager.getCurrentAccount();
        String mid = user.getMid();
        webView = (WebView) findViewById(R.id.webView1);
        webView.getSettings().setJavaScriptEnabled(true);

        webView.loadUrl("http://www.jinguanguke.com/plus/io/index.php?c=personal&mid=" + mid);

    }
}
