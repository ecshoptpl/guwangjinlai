package com.jinguanguke.guwangjinlai.ui.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import com.jinguanguke.guwangjinlai.ui.App;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.socialization.QuickCommentBar;
import cn.sharesdk.socialization.Socialization;

/**
 * Created by jin on 16/4/13.
 */
public class DetailActivity extends AppCompatActivity implements View.OnClickListener {

    @Bind(R.id.iv_meizi)
    ImageView image;

    @Bind(R.id.qcBar)
    QuickCommentBar qcBar;
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.image_detail);
//        App.getInstance().addActivity(DetailActivity.this);
//        init();
//    }
//
//    private void init() {
//        ButterKnife.bind(this);
//        String url = getIntent().getExtras().getString("url");
//        Glide.with(this).load(url).diskCacheStrategy(DiskCacheStrategy.ALL).into(image);
//
//    }
//
//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        this.finish();
//        return super.onTouchEvent(event);
//    }

    private SuperVideoPlayer mSuperVideoPlayer;
    private View mPlayBtnView;
    private String vurl;
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
        mPlayBtnView.setVisibility(View.GONE);
        mSuperVideoPlayer.setVisibility(View.VISIBLE);
        mSuperVideoPlayer.setAutoHideController(false);

        Video video = new Video();
        VideoUrl videoUrl1 = new VideoUrl();
        videoUrl1.setFormatName("720P");
        videoUrl1.setFormatUrl(this.vurl);
        VideoUrl videoUrl2 = new VideoUrl();
        videoUrl2.setFormatName("480P");
        videoUrl2.setFormatUrl(this.vurl);
        ArrayList<VideoUrl> arrayList1 = new ArrayList<>();
        arrayList1.add(videoUrl1);
        arrayList1.add(videoUrl2);
        video.setVideoName("测试视频一");
        video.setVideoUrl(arrayList1);

        Video video2 = new Video();
        VideoUrl videoUrl3 = new VideoUrl();
        videoUrl3.setFormatName("720P");
        videoUrl3.setFormatUrl( this.vurl);
        VideoUrl videoUrl4 = new VideoUrl();
        videoUrl4.setFormatName("480P");
        videoUrl4.setFormatUrl(this.vurl);
        ArrayList<VideoUrl> arrayList2 = new ArrayList<>();
        arrayList2.add(videoUrl3);
        arrayList2.add(videoUrl4);
        video2.setVideoName("测试视频二");
        video2.setVideoUrl(arrayList2);

        ArrayList<Video> videoArrayList = new ArrayList<>();
        videoArrayList.add(video);
        videoArrayList.add(video2);

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


        qcBar.setTopic("1", "test", null, null);
        qcBar.getBackButton().setOnClickListener(this);
        OnekeyShare oks = new OnekeyShare();
        qcBar.setOnekeyShare(oks);
    }

    private void init() {
        ButterKnife.bind(this);
        String url = getIntent().getExtras().getString("url");
        this.vurl = getIntent().getExtras().getString("vurl");
        Glide.with(this).load(url).diskCacheStrategy(DiskCacheStrategy.ALL).into(image);
        ShareSDK.initSDK(this);
        ShareSDK.registerService(Socialization.class);

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



}