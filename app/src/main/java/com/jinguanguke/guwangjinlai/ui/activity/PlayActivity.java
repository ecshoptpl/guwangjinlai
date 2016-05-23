package com.jinguanguke.guwangjinlai.ui.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jinguanguke.guwangjinlai.R;
import com.jinguanguke.guwangjinlai.api.service.SignupService;
import com.jinguanguke.guwangjinlai.model.entity.Register;
import com.jinguanguke.guwangjinlai.model.entity.User;
import com.jinguanguke.guwangjinlai.util.ServiceGenerator;
import com.smartydroid.android.starter.kit.account.AccountManager;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.com.video.venvy.param.JjVideoRelativeLayout;
import cn.com.video.venvy.param.JjVideoView;
import cn.com.video.venvy.param.OnJjBufferCompleteListener;
import cn.com.video.venvy.param.OnJjBufferStartListener;
import cn.com.video.venvy.param.OnJjBufferingUpdateListener;
import cn.com.video.venvy.param.OnJjOpenStartListener;
import cn.com.video.venvy.param.OnJjOpenSuccessListener;
import cn.com.video.venvy.param.OnJjOutsideLinkClickListener;
import cn.com.video.venvy.param.VideoJjMediaContoller;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.socialization.QuickCommentBar;
import cn.sharesdk.socialization.Socialization;
import retrofit2.Call;
import retrofit2.Response;

public class PlayActivity extends Activity {
    private String vurl;
    private String title;
    private String url;
    private String typedir;
    private String aid;
//    @Bind(R.id.iv_meizi)
//    ImageView image;

    @Bind(R.id.qcBar)
    QuickCommentBar qcBar;

    private JjVideoView mVideoView;//
    private View mLoadBufferView;// //
    private TextView mLoadBufferTextView;// //
    private View mLoadView;// /
    private TextView mLoadText;// /


    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //视频链接及分享设置
        this.vurl = "http://www.jinguanguke.com/" + getIntent().getExtras().getString("vurl");
        aid = getIntent().getExtras().getString("aid");
        title = getIntent().getExtras().getString("title");
        url = getIntent().getExtras().getString("url");
        typedir = getIntent().getExtras().getString("typedir");



        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);// 去掉信息栏
        setContentView(R.layout.activity_play);
        ButterKnife.bind(this);
        mVideoView = (JjVideoView) findViewById(R.id.video);
        mLoadView = findViewById(R.id.sdk_ijk_progress_bar_layout);
        mLoadText = (TextView) findViewById(R.id.sdk_ijk_progress_bar_text);
        mLoadBufferView = findViewById(R.id.sdk_load_layout);
        mLoadBufferTextView = (TextView) findViewById(R.id.sdk_sdk_ijk_load_buffer_text);
        mVideoView.setMediaController(new VideoJjMediaContoller(this, true));
        mLoadBufferTextView.setTextColor(Color.RED);
        /***
         * 用户自定义的外链 可 获取外链点击时间
         */
        mVideoView
                .setOnJjOutsideLinkClickListener(new OnJjOutsideLinkClickListener() {

                    @Override
                    public void onJjOutsideLinkClick(String arg0) {
                        // TODO Auto-generated method stub
                    }

                    @Override
                    public void onJjOutsideLinkClose() {
                        // TODO Auto-generated method stub

                    }
                });
        /***
         * 设置缓冲
         */
        mVideoView.setMediaBufferingView(mLoadBufferView);
        /***
         * 视频开始加载数据
         */
        mVideoView.setOnJjOpenStart(new OnJjOpenStartListener() {

            @Override
            public void onJjOpenStart(String arg0) {
                mLoadText.setText(arg0);
            }
        });
        /***
         * 视频开始播放
         */
        mVideoView.setOnJjOpenSuccess(new OnJjOpenSuccessListener() {

            @Override
            public void onJjOpenSuccess() {
                mLoadView.setVisibility(View.GONE);
            }
        });
        // 缓冲开始
        mVideoView.setOnJjBufferStart(new OnJjBufferStartListener() {

            @Override
            public void onJjBufferStartListener(int arg0) {
               // Log.e("Video++", "====================缓冲值=====" + arg0);
            }
        });
        mVideoView
                .setOnJjBufferingUpdateListener(new OnJjBufferingUpdateListener() {

                    @Override
                    public void onJjBufferingUpdate(int arg1) {
                        // TODO Auto-generated method stub
                        if (mLoadBufferView.getVisibility() == View.VISIBLE) {
                            mLoadBufferTextView.setText(String
                                    .valueOf(mVideoView.getBufferPercentage())
                                    + "%");
                            Log.e("Video++", "====================缓冲值2====="
                                    + arg1);
                        }
                    }
                });
        // 缓冲完成
        mVideoView.setOnJjBufferComplete(new OnJjBufferCompleteListener() {

            @Override
            public void onJjBufferCompleteListener(int arg0) {
                // TODO Auto-generated method stub

            }
        });
        /***
         * 注意VideoView 要调用下面方法 配置你用户信息
         */
        mVideoView.setVideoJjAppKey("N1VLhWJWW");
        mVideoView.setVideoJjPageName("com.jinguanguke.guwangjinlai");
        // mVideoView.setMediaCodecEnabled(true);// 是否开启 硬解 硬解对一些手机有限制
        // 判断是否源 0 代表 8大视频网站url 3代表自己服务器的视频源 2代表直播地址 1代表本地视频(手机上的视频源),4特殊需求
        mVideoView.setVideoJjType(3);
        /***
         * 视频标签显示的时间 默认显示5000毫秒 可设置 传入值 long类型 毫秒
         */
        // 参数代表是否记录视频播放位置 默认false不记录 true代表第二次或多次进入，直接跳转到上次退出的时间点开始播放
        // mVideoView.setVideoJjSaveExitTime(false);
        /***
         * 指定时间开始播放 毫秒
         */
        // mVideoView.setVideoJjSeekToTime(Long.valueOf(20000));
        //https://mvvideo5.meitudata.com/56d094a963d3a9973.mp4
        mVideoView.setResourceVideo(vurl);
        Button btn = (Button) findViewById(R.id.button1);
        btn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                mVideoView.setVideoJjResetState();
                mVideoView.setVideoJjType(4);
                mVideoView
                        .setResourceVideo(vurl);
            }
        });
        RelativeLayout mLayout = (RelativeLayout) findViewById(R.id.root);
        JjVideoRelativeLayout mJjVideoRelativeLayout = (JjVideoRelativeLayout) findViewById(R.id.jjlayout);
        mJjVideoRelativeLayout.setJjToFront(mLayout);// 设置此方法
        // 重新排序视图层级JjVideoRelativeLayout，避免横屏其它遮挡
        init_sharesdk();
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        //必须调用 要不直播有问题
        if (mVideoView != null)
            mVideoView.onDestroy();
    }

    private void init_sharesdk()
    {
        String web_url = "http://m.jinguanguke.com" + typedir + "/" + aid + ".html";
        ShareSDK.initSDK(PlayActivity.this);
        ShareSDK.registerService(Socialization.class);
        OnekeyShare oks = new OnekeyShare();
        oks.setTitle(title);
        oks.setTitleUrl(web_url); // 标题的超链接
        oks.setText(title);
        // String t_url = "http://m.jinguanguke.com/yishengjiangzuo/gusuiyan/319.html";
        oks.setUrl(web_url);
        oks.setImageUrl(url);
        oks.setCallback(new PlatformActionListener() {
            @Override
            public void onError(Platform arg0, int arg1, Throwable arg2) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
                plus_score();

            }

            @Override
            public void onCancel(Platform arg0, int arg1) {
                // TODO Auto-generated method stub

            }
        });
        qcBar.setTopic(vurl, title, null, null);
//        qcBar.getBackButton().setOnClickListener(PlayActivity);
        qcBar.setOnekeyShare(oks);
    }

    private void plus_score() {
        SignupService signupService = ServiceGenerator.createService(SignupService.class);
        User user = AccountManager.getCurrentAccount();
        String mid = user.getMid();
        Call<Register> feedServicecall = signupService.update_score(mid);
        feedServicecall.enqueue(new retrofit2.Callback<Register>() {
            @Override
            public void onResponse(Call<Register> call, Response<Register> response) {
                if(response.body().getErr_msg() == "success"){

                }
            }

            @Override
            public void onFailure(Call<Register> call, Throwable t) {

            }
        });
    }
}
