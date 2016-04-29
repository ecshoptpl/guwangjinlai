package com.jinguanguke.guwangjinlai.ui.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
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
import com.jinguanguke.guwangjinlai.api.service.FileUploadService;
import com.jinguanguke.guwangjinlai.data.Constant;

import com.jinguanguke.guwangjinlai.data.RequestCode;
import com.jinguanguke.guwangjinlai.ui.fragment.AccountFragment;
import com.jinguanguke.guwangjinlai.ui.fragment.FeedsFragment;

import com.jinguanguke.guwangjinlai.util.AppConfig;


import com.jinguanguke.guwangjinlai.util.RecordResult;
import com.jinguanguke.guwangjinlai.util.ServiceGenerator;
import com.jmolsmobile.landscapevideocapture.VideoCaptureActivity;
import com.jmolsmobile.landscapevideocapture.configuration.CaptureConfiguration;
import com.jmolsmobile.landscapevideocapture.configuration.PredefinedCaptureConfigurations;
import com.smartydroid.android.starter.kit.app.StarterActivity;
//import com.google.common.io.Files;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import com.jmolsmobile.landscapevideocapture.VideoCaptureActivity;
import com.jmolsmobile.landscapevideocapture.configuration.CaptureConfiguration;
import com.jmolsmobile.landscapevideocapture.configuration.PredefinedCaptureConfigurations.CaptureQuality;
import com.jmolsmobile.landscapevideocapture.configuration.PredefinedCaptureConfigurations.CaptureResolution;

public class TabActivity extends StarterActivity {

  // Tab identifier
  public static final String TAB_FEEDS = "tab_feed_identifier";
  public static final String TAB_ACCOUNT = "tab_account_identifier";
  private String filename = null;
  private String statusMessage = null;


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

    FloatingActionButton myFab = (FloatingActionButton) mTabHost.findViewById(R.id.fab);

    myFab.setOnClickListener(new View.OnClickListener() {


      public void onClick(View v) {

        final EditText inputServer = new EditText(TabActivity.this);
        AlertDialog.Builder builder = new AlertDialog.Builder(TabActivity.this);
        builder.setTitle("视频标题").setIcon(android.R.drawable.ic_dialog_info).setView(inputServer)
                .setNegativeButton("取消", null);
        builder.setPositiveButton("完成", new DialogInterface.OnClickListener() {

          public void onClick(DialogInterface dialog, int which) {
            String title = inputServer.getText().toString();
            startVideoCaptureActivity();
          }
        });
        builder.show();
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
      uploadFile(filename);
      Log.i("filename", filename);
    } else if (resultCode == Activity.RESULT_CANCELED) {
      filename = null;
      statusMessage = getString(R.string.status_capturecancelled);
    } else if (resultCode == VideoCaptureActivity.RESULT_ERROR) {
      filename = null;
      statusMessage = getString(R.string.status_capturefailed);
    }
    updateStatusAndThumbnail();

    super.onActivityResult(requestCode, resultCode, data);
  }

  private void updateStatusAndThumbnail() {
    if (statusMessage == null) {
      statusMessage = getString(R.string.status_nocapture);
    }
//    statusTv.setText(statusMessage);

    final Bitmap thumbnail = getThumbnail();

    if (thumbnail != null) {
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
            R.drawable.ic_account_balance_black_24dp), FeedsFragment.class, null);

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
    // create upload service client
    FileUploadService service =
            ServiceGenerator.createService(FileUploadService.class);

    // use the FileUtils to get the actual file by uri
//    File file = FileUtils.getFile(this, fileUri);
    File file = new File(filename);

    // create RequestBody instance from file
    RequestBody requestFile =
            RequestBody.create(MediaType.parse("multipart/form-data"), file);

    // MultipartBody.Part is used to send also the actual file name
    MultipartBody.Part body =
            MultipartBody.Part.createFormData("picture", file.getName(), requestFile);
    MultipartBody.Part video =
            MultipartBody.Part.createFormData("name", file.getName(), requestFile);


    // add another part within the multipart request
    String descriptionString = "hello, this is description speaking";
    RequestBody description =
            RequestBody.create(
                    MediaType.parse("multipart/form-data"), descriptionString);

    // finally, execute the request
    Call<ResponseBody> call = service.upload(description, body);
    call.enqueue(new Callback<ResponseBody>() {
      @Override
      public void onResponse(Call<ResponseBody> call,
                             Response<ResponseBody> response) {
        Log.v("Upload", response.body().toString());
      }

      @Override
      public void onFailure(Call<ResponseBody> call, Throwable t) {
        Log.e("Upload error:", t.getMessage());
      }
    });
  }

}

