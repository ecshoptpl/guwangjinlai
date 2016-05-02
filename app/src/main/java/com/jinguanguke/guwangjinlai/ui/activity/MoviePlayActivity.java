package com.jinguanguke.guwangjinlai.ui.activity;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jinguanguke.guwangjinlai.R;
import com.jinguanguke.guwangjinlai.api.service.BiliService;
import com.jinguanguke.guwangjinlai.model.entity.Bili;
import com.jinguanguke.guwangjinlai.network.MyNetwork;

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
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.socialization.QuickCommentBar;
import cn.sharesdk.socialization.Socialization;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MoviePlayActivity extends Activity {
    private JjVideoView mVideoView;//
    private View mLoadBufferView;// //
    private TextView mLoadBufferTextView;// //
    private View mLoadView;// /
    private TextView mLoadText;// /
    String mUrl = "/storage/emulated/0/Android/json.txt";
    String mPath = "/storage/emulated/0/Android/28932D2E0132471294162C90B502E32F12BF71FA.mp4";
    String mRtmp = "rtmp://live.hkstv.hk.lxdns.com/live/hks";
    private ProgressDialog progressDialog = null;
    private String video_url;

    @Bind(R.id.qcBar)
    QuickCommentBar qcBar;
    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);

        ShareSDK.initSDK(this);
        ShareSDK.registerService(Socialization.class);





        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);// 去掉信息栏
        setContentView(R.layout.activity_movie_play);
        ButterKnife.bind(this);

        String aid = getIntent().getExtras().getString("aid");
        String vurl = getIntent().getExtras().getString("vurl");
        String title = getIntent().getExtras().getString("title");
        qcBar.setTopic(vurl, title, null, null);
//        qcBar.getBackButton().setOnClickListener(MoviePlayActivity.this);
        OnekeyShare oks = new OnekeyShare();
        qcBar.setOnekeyShare(oks);
        mVideoView = (JjVideoView) findViewById(R.id.video);
        mLoadView = findViewById(R.id.sdk_ijk_progress_bar_layout);
        mLoadText = (TextView) findViewById(R.id.sdk_ijk_progress_bar_text);
        mLoadBufferView = findViewById(R.id.sdk_load_layout);
        mLoadBufferTextView = (TextView) findViewById(R.id.sdk_sdk_ijk_load_buffer_text);
//        mVideoView.setMediaController(new VideoJjMediaContoller(getBaseContext(), true));
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
                Log.e("Video++", "====================缓冲值=====" + arg0);
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



        /***
         * 视频标签显示的时间 默认显示5000毫秒 可设置 传入值 long类型 毫秒
         */
        // 参数代表是否记录视频播放位置 默认false不记录 true代表第二次或多次进入，直接跳转到上次退出的时间点开始播放
        // mVideoView.setVideoJjSaveExitTime(false);
        /***
         * 指定时间开始播放 毫秒
         */
        // mVideoView.setVideoJjSeekToTime(Long.valueOf(20000));
        if(getIntent().getAction() == "com.guwangjinlai.jiankang")
        {
            // 判断是否源 0 代表 8大视频网站url 3代表自己服务器的视频源 2代表直播地址 1代表本地视频(手机上的视频源),4特殊需求
            mVideoView.setVideoJjType(3);
            video_url = "http://www.jinguanguke.com/" + getIntent().getExtras().getString("vurl");
        }
        else
        {
            // 判断是否源 0 代表 8大视频网站url 3代表自己服务器的视频源 2代表直播地址 1代表本地视频(手机上的视频源),4特殊需求
            mVideoView.setVideoJjType(0);
            video_url =  getIntent().getExtras().getString("video_url");
//            int a_id = getIntent().getExtras().getInt("aid");
//            BiliService biliService = MyNetwork.createBiliService();
//            Call<Bili> bili_video = biliService.getBili(a_id);
//            progressDialog = ProgressDialog.show(MoviePlayActivity.this, "请稍等", "获取数据中.....", true);
//            bili_video.enqueue(new Callback<Bili>() {
//                @Override
//                public void onResponse(Call<Bili> call, Response<Bili> response) {
//
//                    // String url = feed.images.get(0).url;
//
//
//                    video_url = "http://v.youku.com/v_show/id_XMTU0OTE5MDg2NA==.html";
////                    video_url = response.body().getSrc();
//                    progressDialog.dismiss();
//
//                    //Toast.makeText(getContext(), response.body().getSrc(), Toast.LENGTH_SHORT).show();
//                }
//
//
//                @Override
//                public void onFailure(Call<Bili> call, Throwable t) {
//                    Toast.makeText(null, "请求失败", Toast.LENGTH_SHORT).show();
//                    progressDialog.dismiss();
//                }
//            });


        }
//        mVideoView.setVideoJjType(0);
//       // String video_url = "http://www.jinguanguke.com/" + getIntent().getExtras().getString("vurl");
//        String video_url = "http://v.youku.com/v_show/id_XMTU0OTE5MDg2NA==.html";
        mVideoView
                .setResourceVideo(video_url);
//                .setResourceVideo("http://v.youku.com/v_show/id_XMTUzNzM1MjUwMA==_ev_5.html?from=y1.3-idx-uhome-1519-20887.205805-205902.8-1");

        Button btn = (Button) findViewById(R.id.button1);
        btn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                mVideoView.setVideoJjResetState();
                mVideoView.setVideoJjType(0);
                mVideoView
                        .setResourceVideo("http://123.57.28.174:8080/cs_app_server/palyer/getVideoInfo?video_id=xxx&site=bve");
            }
        });
        RelativeLayout mLayout = (RelativeLayout) findViewById(R.id.root);
        JjVideoRelativeLayout mJjVideoRelativeLayout = (JjVideoRelativeLayout) findViewById(R.id.jjlayout);
        mJjVideoRelativeLayout.setJjToFront(mLayout);// 设置此方法
        // 重新排序视图层级JjVideoRelativeLayout，避免横屏其它遮挡
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        //必须调用 要不直播有问题
        if (mVideoView != null)
            mVideoView.onDestroy();
    }
}
