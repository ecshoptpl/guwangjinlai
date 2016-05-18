package com.jinguanguke.guwangjinlai.ui.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Message;

import android.view.View;

import android.view.WindowManager;
import android.widget.ImageView;


import com.android.tedcoder.wkvideoplayer.dlna.engine.DLNAContainer;
import com.android.tedcoder.wkvideoplayer.dlna.service.DLNAService;
import com.android.tedcoder.wkvideoplayer.model.Video;
import com.android.tedcoder.wkvideoplayer.model.VideoUrl;
import com.android.tedcoder.wkvideoplayer.util.DensityUtil;
import com.android.tedcoder.wkvideoplayer.view.MediaController;
import com.android.tedcoder.wkvideoplayer.view.SuperVideoPlayer;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.jinguanguke.guwangjinlai.R;
import com.jinguanguke.guwangjinlai.api.service.BiliService;
import com.jinguanguke.guwangjinlai.api.service.FeedService;
import com.jinguanguke.guwangjinlai.api.service.SignupService;
import com.jinguanguke.guwangjinlai.model.entity.Bili;
import com.jinguanguke.guwangjinlai.model.entity.Register;
import com.jinguanguke.guwangjinlai.model.entity.User;
import com.jinguanguke.guwangjinlai.model.entity.VideoTotal;
import com.jinguanguke.guwangjinlai.network.MyNetwork;
import com.jinguanguke.guwangjinlai.ui.App;
import com.jinguanguke.guwangjinlai.util.ServiceGenerator;
import com.smartydroid.android.starter.kit.account.AccountManager;


import java.util.ArrayList;
import java.util.HashMap;
import android.os.Handler;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.socialization.QuickCommentBar;
import cn.sharesdk.socialization.Socialization;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by jin on 16/4/13.
 */
public class DetailActivity extends Activity implements View.OnClickListener {

    @Bind(R.id.iv_meizi)
    ImageView image;

    @Bind(R.id.qcBar)
    QuickCommentBar qcBar;


    private ProgressDialog progressDialog = null;


    private SuperVideoPlayer mSuperVideoPlayer;
    private View mPlayBtnView;
    private String vurl;
    private String title;
    private String url;
    private String typedir;
    private String aid;
    private SuperVideoPlayer.VideoPlayCallbackImpl mVideoPlayCallback = new SuperVideoPlayer.VideoPlayCallbackImpl() {
        @Override
        public void onCloseVideo() {
            mSuperVideoPlayer.close();
            mPlayBtnView.setVisibility(View.VISIBLE);
            mSuperVideoPlayer.setVisibility(View.GONE);
            resetPageToPortrait();
        }

        @Override
        public void onSwitchPageType() {
            if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                mSuperVideoPlayer.setPageType(MediaController.PageType.SHRINK);
            } else {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                mSuperVideoPlayer.setPageType(MediaController.PageType.EXPAND);
            }
        }

        @Override
        public void onPlayFinish() {

        }
    };

    @Override
    public void onClick(View view) {
        qcBar.setVisibility(View.INVISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,  WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        mPlayBtnView.setVisibility(View.GONE);
        mSuperVideoPlayer.setVisibility(View.VISIBLE);
        mSuperVideoPlayer.setAutoHideController(false);



        Video video = new Video();

        VideoUrl videoUrl1 = new VideoUrl();
//        videoUrl1.setFormatName("");
        videoUrl1.setFormatUrl(this.vurl);

        ArrayList<VideoUrl> arrayList1 = new ArrayList<>();
        arrayList1.add(videoUrl1);

        //video.setVideoName("");
        video.setVideoUrl(arrayList1);
        ArrayList<Video> videoArrayList = new ArrayList<>();
        videoArrayList.add(video);
        mSuperVideoPlayer.loadMultipleVideo(videoArrayList,0,0,0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopDLNAService();
    }

    /***
     * 旋转屏幕之后回调
     *
     * @param newConfig newConfig
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (null == mSuperVideoPlayer) return;
        /***
         * 根据屏幕方向重新设置播放器的大小
         */
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().getDecorView().invalidate();
            float height = DensityUtil.getWidthInPx(this);
            float width = DensityUtil.getHeightInPx(this);
            mSuperVideoPlayer.getLayoutParams().height = (int) width;
            mSuperVideoPlayer.getLayoutParams().width = (int) height;
        } else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            final WindowManager.LayoutParams attrs = getWindow().getAttributes();
            attrs.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().setAttributes(attrs);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            float width = DensityUtil.getWidthInPx(this);
            float height = DensityUtil.dip2px(this, 200.f);
            mSuperVideoPlayer.getLayoutParams().height = (int) height;
            mSuperVideoPlayer.getLayoutParams().width = (int) width;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.image_detail);
        App.getInstance().addActivity(DetailActivity.this);
        init();
        mSuperVideoPlayer = (SuperVideoPlayer) findViewById(R.id.video_player_item_1);
        mPlayBtnView = findViewById(R.id.play_btn);
        mPlayBtnView.setOnClickListener(this);
        mSuperVideoPlayer.setVideoPlayCallback(mVideoPlayCallback);
        startDLNAService();
    }

    private void init() {
        ButterKnife.bind(this);
        this.vurl = "http://www.jinguanguke.com/" + getIntent().getExtras().getString("vurl");
        aid = getIntent().getExtras().getString("aid");
        title = getIntent().getExtras().getString("title");
        url = getIntent().getExtras().getString("url");
        typedir = getIntent().getExtras().getString("typedir");

        Glide.with(this).load(url).diskCacheStrategy(DiskCacheStrategy.ALL).into(image);
        init_sharesdk();


    }

    /***
     * 恢复屏幕至竖屏
     */
    private void resetPageToPortrait() {
        if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            mSuperVideoPlayer.setPageType(MediaController.PageType.SHRINK);
        }
    }

    private void startDLNAService() {
        // Clear the device container.
        DLNAContainer.getInstance().clear();
        Intent intent = new Intent(getApplicationContext(), DLNAService.class);
        startService(intent);
    }

    private void stopDLNAService() {
        Intent intent = new Intent(getApplicationContext(), DLNAService.class);
        stopService(intent);
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

    private void init_sharesdk()
    {
        String web_url = "http://m.jinguanguke.com" + typedir + "/" + aid + ".html";
        ShareSDK.initSDK(DetailActivity.this);
        ShareSDK.registerService(Socialization.class);
        OnekeyShare oks = new OnekeyShare();
        oks.setTitle(title);
        oks.setTitleUrl("http://www.jinguanguke.com"); // 标题的超链接
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
        qcBar.getBackButton().setOnClickListener(DetailActivity.this);
        qcBar.setOnekeyShare(oks);
    }


}