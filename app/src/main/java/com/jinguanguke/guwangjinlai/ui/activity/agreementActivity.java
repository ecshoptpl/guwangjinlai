package com.jinguanguke.guwangjinlai.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

import com.jinguanguke.guwangjinlai.R;
import com.smartydroid.android.starter.kit.app.StarterActivity;

/**
 * Created by jin on 16/5/2.
 */
public class AgreementActivity extends StarterActivity {
    private WebView webView;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.agreement);

        webView = (WebView) findViewById(R.id.webView1);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("http://www.jinguanguke.com/app/p.html");

    }
}
