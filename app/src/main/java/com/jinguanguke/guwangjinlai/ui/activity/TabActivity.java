package com.jinguanguke.guwangjinlai.ui.activity;

import android.content.Intent;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;

//import com.alibaba.sdk.android.AlibabaSDK;
//import com.alibaba.sdk.android.callback.FailureCallback;
//
//import com.duanqu.qupai.engine.session.FeedExportOptions;
//import com.duanqu.qupai.engine.session.VideoSessionCreateInfo;
//import com.duanqu.qupai.sdk.android.QupaiService;
//import com.duanqu.qupai.utils.AppGlobalSetting;
import com.github.clans.fab.FloatingActionButton;
//import com.google.common.io.Files;
import com.jinguanguke.guwangjinlai.R;
import com.jinguanguke.guwangjinlai.data.Constant;

import com.jinguanguke.guwangjinlai.data.RequestCode;
import com.jinguanguke.guwangjinlai.ui.fragment.AccountFragment;
import com.jinguanguke.guwangjinlai.ui.fragment.FeedsFragment;

import com.jinguanguke.guwangjinlai.util.AppConfig;

import com.jinguanguke.guwangjinlai.util.RecordResult;
import com.smartydroid.android.starter.kit.app.StarterActivity;
//import com.google.common.io.Files;

import java.io.File;
import java.io.IOException;

public class TabActivity extends StarterActivity {

  // Tab identifier
  public static final String TAB_FEEDS = "tab_feed_identifier";
  public static final String TAB_ACCOUNT = "tab_account_identifier";

  private double mDurationLimit;
  private int mVideoBitrate;
  private int mWaterMark = -1;
  private int beautySkinProgress;

  private String waterMarkPath ;
  private Switch st_more_music;
  private Switch st_lead_in;
  private Switch has_edit_page;
  private Switch camera_font_on;
  private Switch beauty_skin_on;


  @Bind(android.R.id.tabhost) FragmentTabHost mTabHost;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_tab);
   // FloatingActionButton myFab = (FloatingActionButton)  mTabHost.findViewById(R.id.fab);




//    myFab.setOnClickListener(new View.OnClickListener() {
//      public void onClick(View v) {
//        QupaiService qupaiService = AlibabaSDK
//                .getService(QupaiService.class);
//
//        if (qupaiService == null) {
//          Toast.makeText(TabActivity.this, "插件没有初始化，无法获取 QupaiService",
//                  Toast.LENGTH_LONG).show();
//          return;
//        }
//
//        //视频时长
//
//          mDurationLimit = Constant.DEFAULT_DURATION_LIMIT;
//
//
//        //视频码率
//
//          mVideoBitrate = Constant.DEFAULT_BITRATE;
//
//
//        //是否需要水印
//        mWaterMark = 0;
//        //水印存储的目录
//        //waterMarkPath = Constant.WATER_MARK_PATH ;
//
//        //美颜参数:1-100.这里不设指定为80,这个值只在第一次设置，之后在录制界面滑动美颜参数之后系统会记住上一次滑动的状态
//        beautySkinProgress= 80;
//
//        FeedExportOptions Feed_options = new FeedExportOptions.Builder()
//                .configureMuxer("movflags", "+faststart")
//                .configureMuxer("An invalid option", "generates a warning and is ignored.")
//                .build();
//        VideoSessionCreateInfo info =new VideoSessionCreateInfo.Builder()
//                .setOutputDurationLimit((float) mDurationLimit)
//                .setOutputVideoBitrate(mVideoBitrate)
//                .setHasImporter( false)
////                .setWaterMarkPath(waterMarkPath)
////                .setWaterMarkPosition(mWaterMark)
//                .setHasEditorPage(true)
//                .setCameraFacing(Camera.CameraInfo.CAMERA_FACING_FRONT)
//                .setBeautyProgress(beautySkinProgress)
//                .setBeautySkinOn(false)
//                .setFeedExportOptions(Feed_options)
//                .build();
//
//        //初始化，建议在application里面做初始化，这里做是为了方便开发者认识参数的意义
//        qupaiService.initRecord(info);
//
//        //是否需要更多音乐页面--如果不需要更多音乐可以干掉
//        Intent moreMusic = new Intent();
//
//        moreMusic = null;
//        qupaiService.hasMroeMusic(moreMusic);
//
//        //引导，只显示一次，这里用SharedPreferences记录
//        final AppGlobalSetting sp = new AppGlobalSetting(getApplicationContext());
//        Boolean isGuideShow = sp.getBooleanGlobalItem(
//                AppConfig.PREF_VIDEO_EXIST_USER, true);
//
//        /**
//         * 建议上面的initRecord只在application里面调用一次。这里为了能够开发者直观看到改变所以可以调用多次
//         */
//        qupaiService.showRecordPage(TabActivity.this, RequestCode.RECORDE_SHOW, isGuideShow,
//                new FailureCallback() {
//                  @Override
//                  public void onFailure(int i, String s) {
//                    Toast.makeText(TabActivity.this, "onFailure:"+ s + "CODE"+ i, Toast.LENGTH_LONG).show();
//                  }
//                });
//
//        sp.saveGlobalConfigItem(
//                AppConfig.PREF_VIDEO_EXIST_USER, false);
//      }
//    });
    setupTab();
  }

  private void setupTab() {
    mTabHost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);

    mTabHost.addTab(createTabSpec(TAB_FEEDS, R.string.tab_home,
        R.drawable.selector_tab_feeds), FeedsFragment.class, null);

    mTabHost.addTab(createTabSpec(TAB_ACCOUNT, R.string.tab_account,
        R.drawable.selector_tab_account), AccountFragment.class, null);
  }

  private TabHost.TabSpec createTabSpec(String tag, int stringRes, int drawableResId) {
    TabHost.TabSpec spec = mTabHost.newTabSpec(tag);
    spec.setIndicator(createTabIndicator(stringRes, drawableResId));
    spec.setContent(new TabHost.TabContentFactory() {
      public View createTabContent(String tag) {
       return findViewById(android.R.id.tabcontent);
      }
    });
    return spec;
  }

  private View createTabIndicator(int res, int drawableResId) {
    LinearLayout tabIndicator = (LinearLayout) LayoutInflater.from(this)
        .inflate(R.layout.tab_indicator, mTabHost.getTabWidget(), false);

    ImageView icon = (ImageView) tabIndicator.findViewById(android.R.id.icon1);
    icon.setImageResource(drawableResId);

    TextView text = ButterKnife.findById(tabIndicator, android.R.id.text1);
    text.setText(res);

    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) tabIndicator.getLayoutParams();

    tabIndicator.setEnabled(true);
    params.weight = 1.0F;
    tabIndicator.setGravity(Gravity.CENTER);

    return tabIndicator;
  }

//  @Override
//  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//    if (resultCode == RESULT_OK) {
//      RecordResult result =new RecordResult(data);
//      //得到视频地址，和缩略图地址的数组，返回十张缩略图
//      String videoPath = result.getPath();
//      String [] thum = result.getThumbnail();
//
////      try{
////        String newVideoPath = Constant.VIDEOPATH;
////        String newThumPath = Constant.THUMBPATH;
////       // Files.move(new File(videoPath), new File( newVideoPath ));
////        //Files.move(new File(thum[0]), new File( newThumPath ));
////
////       // tv_result.setText("视频路径:" + newVideoPath + "图片路径:" + newThumPath);
////      }catch (IOException e){
////        Toast.makeText(this,"拷贝失败",Toast.LENGTH_LONG).show();
////        e.printStackTrace();
////      }
//
//      /**
//       * 清除草稿,草稿文件将会删除。所以在这之前我们执行拷贝move操作。
//       * 上面的拷贝操作请自行实现，第一版本的copyVideoFile接口不再使用
//       */
//      QupaiService qupaiService = AlibabaSDK
//              .getService(QupaiService.class);
//      qupaiService.deleteDraft(getApplicationContext(),data);
//
//    } else {
//      if (resultCode == RESULT_CANCELED) {
//        Toast.makeText(TabActivity.this, "RESULT_CANCELED", Toast.LENGTH_LONG).show();
//      }
//    }

//  }

}

