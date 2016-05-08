package com.jinguanguke.guwangjinlai.ui.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.OvershootInterpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.jinguanguke.guwangjinlai.R;
import com.jinguanguke.guwangjinlai.api.service.AddonarticleService;
import com.jinguanguke.guwangjinlai.api.service.ArchivesService;
import com.jinguanguke.guwangjinlai.api.service.ArctinyService;
import com.jinguanguke.guwangjinlai.model.entity.Arctiny;
import com.jinguanguke.guwangjinlai.model.entity.User;
import com.jinguanguke.guwangjinlai.util.ServiceGenerator;
import com.smartydroid.android.starter.kit.account.AccountManager;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Callback;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;
import java.io.FileInputStream;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.tencent.qq.QQ;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by jin on 16/5/6.
 */
public class PublishActivity extends BaseActivity{
    public static final String ARG_TAKEN_PHOTO_URI = "arg_taken_photo_uri";
    private String url = "http://www.jinguanguke.com/plus/io/index.php?" +
            "c=upload&a=file";
    private String video_remote_path = null;
    private String img_remote_path = null;
    private String video_title = null;
    private String aid = null;

    @Bind(R.id.ivPhoto)
    ImageView ivPhoto;

    @Bind(R.id.etDescription)
    EditText etDescription;



    private boolean propagatingToggleState = false;
    private Uri photoUri;
    private int photoSize;

    public static void openWithPhotoUri(Activity openingActivity, Uri photoUri, String filename, String thumb) {
        Intent intent = new Intent(openingActivity, PublishActivity.class);
        intent.putExtra(ARG_TAKEN_PHOTO_URI, photoUri);
        intent.putExtra("filename", filename);
        intent.putExtra("thumb", thumb);
        openingActivity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_publish);
//        ButterKnife.bind(this);
        String path  = getIntent().getStringExtra("thumbnail");
        Bitmap bmp = BitmapFactory.decodeFile(path);
        ivPhoto.setImageBitmap(bmp);

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_grey600_24dp);

        photoSize = getResources().getDimensionPixelSize(R.dimen.publish_photo_thumbnail_size);

        if (savedInstanceState == null) {
            photoUri = getIntent().getParcelableExtra(ARG_TAKEN_PHOTO_URI);
        } else {
            photoUri = savedInstanceState.getParcelable(ARG_TAKEN_PHOTO_URI);
        }
        updateStatusBarColor();

        ivPhoto.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                ivPhoto.getViewTreeObserver().removeOnPreDrawListener(this);
                loadThumbnailPhoto();
                return true;
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void updateStatusBarColor() {
//        if (Utils.isAndroid5()) {
//            getWindow().setStatusBarColor(0xff888888);
//        }
    }

    private void loadThumbnailPhoto() {
        ivPhoto.setScaleX(0);
        ivPhoto.setScaleY(100);
        Picasso.with(this)
                .load(photoUri)
                .centerCrop()
                .resize(photoSize+(photoSize*4), photoSize*3)
                .into(ivPhoto, new Callback() {
                    @Override
                    public void onSuccess() {
                        ivPhoto.animate()
                                .scaleX(1.f).scaleY(1.f)
                                .setInterpolator(new OvershootInterpolator())
                                .setDuration(400)
                                .setStartDelay(200)
                                .start();
                    }

                    @Override
                    public void onError() {
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_publish, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_publish) {
            bringMainActivityToTop();
            return true;
        } else {
            finish();
            return true;
           // return super.onOptionsItemSelected(item);
        }

    }

    private void bringMainActivityToTop() {
//        Intent intent = new Intent(this, MainActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//        intent.setAction(MainActivity.ACTION_SHOW_LOADING_ITEM);
//        startActivity(intent);
        if(etDescription.getText().length() == 0)
        {
            new AlertDialog.Builder(this)

                    .setTitle("提示")
                    .setPositiveButton("确定",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {


                                }
                            }).setMessage("不写不让发!").setNegativeButton(null, null).create()
                    .show();
            return;
        }
        else
        {
            video_title = etDescription.getText().toString();
        }
        uploadFile(getIntent().getStringExtra("filename"));
        File thumb = new File(getIntent().getStringExtra("thumb"));
        uploadThumbnail(thumb);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(ARG_TAKEN_PHOTO_URI, photoUri);
    }

//    @OnCheckedChanged(R.id.tbFollowers)
//    public void onFollowersCheckedChange(boolean checked) {
//        if (!propagatingToggleState) {
//            propagatingToggleState = true;
//            tbDirect.setChecked(!checked);
//            propagatingToggleState = false;
//        }
//    }
//
//    @OnCheckedChanged(R.id.tbDirect)
//    public void onDirectCheckedChange(boolean checked) {
//        if (!propagatingToggleState) {
//            propagatingToggleState = true;
//            tbFollowers.setChecked(!checked);
//            propagatingToggleState = false;
//        }
//    }

    @OnClick({R.id.ivPhoto}) public void onPotoClick(View view) {
        playVideo(getIntent().getStringExtra("filename"));
    }

    public void playVideo(String filename) {
        if (filename == null) return;

        final Intent videoIntent = new Intent(Intent.ACTION_VIEW);
        videoIntent.setDataAndType(Uri.parse(filename), "video/*");
        try {
            startActivity(videoIntent);
        } catch (ActivityNotFoundException e) {
            // NOP
        }
    }

    //上传视频
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

    //上传缩略图
    private void uploadThumbnail(File thumbnail) {

        if(thumbnail != null) {
                OkHttpUtils.post()//
                        .addFile("img", thumbnail.getName(), thumbnail)//
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
        call.enqueue(new retrofit2.Callback<Arctiny>() {
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
        archivesServiceCall.enqueue(new retrofit2.Callback<Arctiny>() {
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
        addonarticleServiceCall.enqueue(new retrofit2.Callback<Arctiny>() {
            @Override
            public void onResponse(Call<Arctiny> call, Response<Arctiny> response) {
                Toast.makeText(PublishActivity.this, "发布成功，等待审核", Toast.LENGTH_LONG).show();
                Intent intent = new Intent();
                intent.setClass(PublishActivity.this, TabActivity.class);
                startActivity(intent);
            }

            @Override
            public void onFailure(Call<Arctiny> call, Throwable t) {

            }
        });
    }


}
