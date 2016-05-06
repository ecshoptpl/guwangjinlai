package com.jinguanguke.guwangjinlai.ui.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.FragmentTabHost;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.alexrs.prefs.lib.Prefs;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//import com.alibaba.sdk.android.AlibabaSDK;
//import com.alibaba.sdk.android.callback.FailureCallback;
//
//import com.duanqu.qupai.engine.session.FeedExportOptions;
//import com.duanqu.qupai.engine.session.VideoSessionCreateInfo;
//import com.duanqu.qupai.sdk.android.QupaiService;
//import com.duanqu.qupai.utils.AppGlobalSetting;

import com.facebook.common.file.FileUtils;
import com.github.clans.fab.FloatingActionButton;
//import com.google.common.io.Files;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.common.io.Files;
import com.jinguanguke.guwangjinlai.DemoApp;
import com.jinguanguke.guwangjinlai.R;
import com.jinguanguke.guwangjinlai.api.service.AddonarticleService;
import com.jinguanguke.guwangjinlai.api.service.ArchivesService;
import com.jinguanguke.guwangjinlai.api.service.ArctinyService;
import com.jinguanguke.guwangjinlai.api.service.FileUploadService;
import com.jinguanguke.guwangjinlai.data.Constant;

import com.jinguanguke.guwangjinlai.data.RequestCode;
import com.jinguanguke.guwangjinlai.model.entity.Arctiny;
import com.jinguanguke.guwangjinlai.model.entity.User;
import com.jinguanguke.guwangjinlai.ui.fragment.AccountFragment;
import com.jinguanguke.guwangjinlai.ui.fragment.FeedsFragment;

import com.jinguanguke.guwangjinlai.update.CheckUpdate;
import com.jinguanguke.guwangjinlai.util.AppConfig;


import com.jinguanguke.guwangjinlai.util.ImageUtils;
import com.jinguanguke.guwangjinlai.util.RecordResult;
import com.jinguanguke.guwangjinlai.util.ServiceGenerator;
import com.jmolsmobile.landscapevideocapture.VideoCaptureActivity;
import com.jmolsmobile.landscapevideocapture.configuration.CaptureConfiguration;
import com.jmolsmobile.landscapevideocapture.configuration.PredefinedCaptureConfigurations;
import com.smartydroid.android.starter.kit.account.Account;
import com.smartydroid.android.starter.kit.account.AccountManager;
import com.smartydroid.android.starter.kit.app.StarterActivity;
//import com.google.common.io.Files;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import com.jmolsmobile.landscapevideocapture.VideoCaptureActivity;
import com.jmolsmobile.landscapevideocapture.configuration.CaptureConfiguration;
import com.jmolsmobile.landscapevideocapture.configuration.PredefinedCaptureConfigurations.CaptureQuality;
import com.jmolsmobile.landscapevideocapture.configuration.PredefinedCaptureConfigurations.CaptureResolution;
import com.smartydroid.android.starter.kit.app.StarterKitApp;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

public class TabActivity extends StarterActivity {

  // Tab identifier
  public static final String TAB_FEEDS = "tab_feed_identifier";
  public static final String TAB_ACCOUNT = "tab_account_identifier";
  private String filename = null;
  private String statusMessage = null;
  private String url = "http://www.jinguanguke.com/plus/io/index.php?" +
          "c=upload&a=file";
  private String video_remote_path = null;
  private String img_remote_path = null;
  private String video_title = null;
  private String aid = null;


  @Bind(android.R.id.tabhost)
  FragmentTabHost mTabHost;
  /**
   * ATTENTION: This was auto-generated to implement the App Indexing API.
   * See https://g.co/AppIndexing/AndroidStudio for more information.
   */
  private GoogleApiClient client;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_tab);
    CheckUpdate.getInstance().startCheck(this);
    FloatingActionButton myFab = (FloatingActionButton) mTabHost.findViewById(R.id.fab);

    myFab.setOnClickListener(new View.OnClickListener() {


      public void onClick(View v) {
        startVideoCaptureActivity();
//        final EditText inputServer = new EditText(TabActivity.this);
//        AlertDialog.Builder builder = new AlertDialog.Builder(TabActivity.this);
//        builder.setTitle("视频标题").setIcon(android.R.drawable.ic_dialog_info).setView(inputServer)
//                .setNegativeButton("取消", null);
//        builder.setPositiveButton("完成", new DialogInterface.OnClickListener() {
//
//          public void onClick(DialogInterface dialog, int which) {
//            video_title = inputServer.getText().toString();
//            startVideoCaptureActivity();
//          }
//        });
//        builder.show();
      }
    });
    setupTab();
    // ATTENTION: This was auto-generated to implement the App Indexing API.
    // See https://g.co/AppIndexing/AndroidStudio for more information.
    client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {

    if (resultCode == Activity.RESULT_OK) {
      filename = data.getStringExtra(VideoCaptureActivity.EXTRA_OUTPUT_FILENAME);
      statusMessage = String.format(getString(R.string.status_capturesuccess), filename);
      final Intent intent = new Intent(this, PublishActivity.class);
      startActivity(intent);
//      uploadFile(filename);
//      Log.i("filename", filename);
    } else if (resultCode == Activity.RESULT_CANCELED) {
      filename = null;
      statusMessage = getString(R.string.status_capturecancelled);
    } else if (resultCode == VideoCaptureActivity.RESULT_ERROR) {
      filename = null;
      statusMessage = getString(R.string.status_capturefailed);
    }
//    Log.i("statusMessage", statusMessage);
//    updateStatusAndThumbnail();
//
//    super.onActivityResult(requestCode, resultCode, data);
  }

  private void updateStatusAndThumbnail() {

    final Bitmap thumbnail = getThumbnail();

    if (thumbnail != null) {
      File file = storeImage(thumbnail);
      Log.i("thumbanail","is there");

      if(file != null) {
        OkHttpUtils.post()//
                .addFile("img", file.getName(), file)//
                .url(url)
                .build()//
                .execute(new StringCallback() {
                  @Override
                  public void onError(okhttp3.Call call, Exception e) {
                    Log.i("img_err", "is had error");
                  }

                  @Override
                  public void onResponse(String response) {
                    set_img_remote_path(response);

                  }
                });
      }

      //thumbnailIv.setImageBitmap(thumbnail);
    } else {
      //thumbnailIv.setImageResource(R.drawable.thumbnail_placeholder);
    }
  }

  private Bitmap getThumbnail() {
    if (filename == null) return null;
    return ThumbnailUtils.createVideoThumbnail(filename, MediaStore.Video.Thumbnails.FULL_SCREEN_KIND);
  }


  private void setupTab() {
    mTabHost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);

    mTabHost.addTab(createTabSpec(TAB_FEEDS, R.string.tab_home,
            R.drawable.selector_tab_account), FeedsFragment.class, null);

    mTabHost.addTab(createTabSpec(TAB_ACCOUNT, R.string.tab_account,
            R.drawable.selector_tab_feeds), AccountFragment.class, null);
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


  private void startVideoCaptureActivity() {
    final CaptureConfiguration config = createCaptureConfiguration();
    final String filename = UUID.randomUUID() + ".mp4";

    final Intent intent = new Intent(TabActivity.this, VideoCaptureActivity.class);
    intent.putExtra(VideoCaptureActivity.EXTRA_CAPTURE_CONFIGURATION, config);
    intent.putExtra(VideoCaptureActivity.EXTRA_OUTPUT_FILENAME, filename);
    startActivityForResult(intent, 101);
  }

  private CaptureConfiguration createCaptureConfiguration() {
    final CaptureResolution resolution = CaptureResolution.RES_480P;
    final CaptureQuality quality = CaptureQuality.MEDIUM;
    int fileDuration = CaptureConfiguration.NO_DURATION_LIMIT;
    fileDuration = 10;
    int filesize = CaptureConfiguration.NO_FILESIZE_LIMIT;
    filesize = 10;
    final CaptureConfiguration config = new CaptureConfiguration(resolution, quality, fileDuration, filesize, true);
    return config;
  }

  private void uploadFile(String fileUri) {
    File file = new File(fileUri);

    OkHttpUtils.post()//
            .addFile("video", file.getName(), file)//
            .url(url)
            .build()//
            .execute(new StringCallback() {
              @Override
              public void onError(okhttp3.Call call, Exception e) {
                Log.i("video_error","is had error");
              }

              @Override
              public void onResponse(String response) {
                set_video_remote_path(response.toString());

              }
            });
  }


  private  File getOutputMediaFile(){
    // To be safe, you should check that the SDCard is mounted
    // using Environment.getExternalStorageState() before doing this.
    boolean sdCardExist = Environment.getExternalStorageState()
            .equals(android.os.Environment.MEDIA_MOUNTED);
    String save_path = null;
    if(sdCardExist) {
       save_path = Environment.getExternalStorageDirectory()
              + "/Android/data/"
              + getApplicationContext().getPackageName()
              + "/Files";
    }
    else
    {
       save_path = Environment.getDataDirectory()
              //+ "/Android/data/"
              + getApplicationContext().getPackageName()
              + "/Files";
    }
    File mediaStorageDir = new File(save_path);
    // This location works best if you want the created images to be shared
    // between applications and persist after your app has been uninstalled.

    // Create the storage directory if it does not exist
    if (! mediaStorageDir.exists()){
      if (! mediaStorageDir.mkdirs()){
        return null;
      }
    }
    // Create a media file name
    String timeStamp = UUID.randomUUID().toString();
    File mediaFile;
    String mImageName="MI_"+ timeStamp +".jpg";
    mediaFile = new File(mediaStorageDir.getPath() + File.separator + mImageName);
    return mediaFile;
  }

  //存储视频缩略图到本地
  private File storeImage(Bitmap image) {
    File pictureFile = getOutputMediaFile();
    if (pictureFile == null) {
      Log.d("storeImage",
              "Error creating media file, check storage permissions: ");// e.getMessage());
      return null;
    }
    try {
      FileOutputStream fos = new FileOutputStream(pictureFile);
      image.compress(Bitmap.CompressFormat.JPEG, 50, fos);
      fos.close();
   } catch (FileNotFoundException e) {
      Log.d("storeImage", "File not found: " + e.getMessage());
      return null;
    } catch (IOException e) {
      Log.d("storeImage", "Error accessing file: " + e.getMessage());
      return null;
    }
    return pictureFile;

  }

  //设置视频远程路径
  private void set_video_remote_path(String path){
    video_remote_path = path;
    if (img_remote_path != null)
    {
      update_website();
    }
  }

  //设置视频缩略图的远程路径
  private void set_img_remote_path(String path)
  {
    img_remote_path = path;
    if (video_remote_path != null)
    {
      update_website();
    }
  }

  //更新网站
  private void update_website()
  {
    User account = AccountManager.getCurrentAccount();

    String mid = account.getMid();


    ArctinyService arctinyService = ServiceGenerator.createService(ArctinyService.class);
    Call<Arctiny> call = arctinyService.add(mid);
    call.enqueue(new Callback<Arctiny>() {
      @Override
      public void onResponse(Call<Arctiny> call, Response<Arctiny> response) {
        update_archives(response.body().getData().get(0).getId());
    }

      @Override
      public void onFailure(Call<Arctiny> call, Throwable t) {
        Log.i("arctiny_fail","chucuole");

      }
    });

  }

  //更新主表dede_archives
  private void update_archives(String id)
  {
    User account = AccountManager.getCurrentAccount();

    String mid = account.getMid();

    aid = id;
    ArchivesService archivesService = ServiceGenerator.createService(ArchivesService.class);
    Call<Arctiny> archivesServiceCall = archivesService.add(id,img_remote_path,mid,video_title);
    archivesServiceCall.enqueue(new Callback<Arctiny>() {
      @Override
      public void onResponse(Call<Arctiny> call, Response<Arctiny> response) {
        update_addonarticle();
      }

      @Override
      public void onFailure(Call<Arctiny> call, Throwable t) {

      }
    });

  }

  //更新附加表
  private void update_addonarticle()
  {
    User account = AccountManager.getCurrentAccount();

    String mid = account.getMid();

    AddonarticleService addonarticleService = ServiceGenerator.createService(AddonarticleService.class);
    Call<Arctiny> addonarticleServiceCall = addonarticleService.add(aid,video_remote_path,mid);
    addonarticleServiceCall.enqueue(new Callback<Arctiny>() {
      @Override
      public void onResponse(Call<Arctiny> call, Response<Arctiny> response) {
        Toast.makeText(TabActivity.this, "发布成功，等待审核", Toast.LENGTH_LONG).show();
      }

      @Override
      public void onFailure(Call<Arctiny> call, Throwable t) {

      }
    });
  }

}

