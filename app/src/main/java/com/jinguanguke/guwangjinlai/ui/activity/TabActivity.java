package com.jinguanguke.guwangjinlai.ui.activity;

import android.app.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.FragmentTabHost;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;


import butterknife.Bind;
import butterknife.ButterKnife;


import com.github.clans.fab.FloatingActionButton;


import com.jinguanguke.guwangjinlai.ui.fragment.FeedsFragment;

import com.jinguanguke.guwangjinlai.update.CheckUpdate;

import com.jmolsmobile.landscapevideocapture.VideoCaptureActivity;
import com.jmolsmobile.landscapevideocapture.configuration.CaptureConfiguration;

import com.smartydroid.android.starter.kit.app.StarterActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import java.io.IOException;

import java.util.UUID;
import com.jinguanguke.guwangjinlai.R;

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
   // client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (resultCode == Activity.RESULT_OK && requestCode == 101) {
      filename = data.getStringExtra(VideoCaptureActivity.EXTRA_OUTPUT_FILENAME);
      statusMessage = String.format(getString(R.string.status_capturesuccess), filename);
      final Bitmap thumbnail = getThumbnail(filename);
      File thumbnail_file = storeImage(thumbnail);
      PublishActivity.openWithPhotoUri(this, Uri.fromFile(thumbnail_file),filename,thumbnail_file.toString());
//      final Intent intent = new Intent(this, PublishActivity.class);
//      intent.putExtra("thumbnail",thumbnail_file.toString());
//      startActivity(intent);
//      uploadFile(filename);
//      Log.i("filename", filename);
    } else if (resultCode == Activity.RESULT_CANCELED) {
      filename = null;
      statusMessage = getString(R.string.status_capturecancelled);
    } else if (resultCode == VideoCaptureActivity.RESULT_ERROR) {
      filename = null;
      statusMessage = getString(R.string.status_capturefailed);
    }
    super.onActivityResult(requestCode, resultCode, data);

//    Log.i("statusMessage", statusMessage);
//    updateStatusAndThumbnail();
//
//
  }



  private Bitmap getThumbnail(String filename) {
    if (filename == null) return null;
    return ThumbnailUtils.createVideoThumbnail(filename, MediaStore.Video.Thumbnails.FULL_SCREEN_KIND);
  }


  private void setupTab() {
    mTabHost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);

    mTabHost.addTab(createTabSpec(TAB_FEEDS, R.string.tab_home,
            R.drawable.selector_tab_account), FeedsFragment.class, null);

    mTabHost.addTab(createTabSpec(TAB_ACCOUNT, R.string.tab_account,
            R.drawable.selector_tab_feeds), UserProfileActivity.class, null);
//            R.drawable.selector_tab_feeds), AccountFragment.class, null);
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
      Log.d("storeImage", "File  " + e.getMessage());
      return null;
    } catch (IOException e) {
      Log.d("storeImage", "Error  file: " + e.getMessage());
      return null;
    }
    return pictureFile;

  }


}

