package com.jinguanguke.guwangjinlai.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jinguanguke.guwangjinlai.R;
import com.jinguanguke.guwangjinlai.api.service.AuthService;
import com.jinguanguke.guwangjinlai.api.service.FeedService;
import com.jinguanguke.guwangjinlai.api.service.SignupService;
import com.jinguanguke.guwangjinlai.model.entity.DataInfo;
import com.jinguanguke.guwangjinlai.model.entity.Register;
import com.jinguanguke.guwangjinlai.model.entity.User;
import com.jinguanguke.guwangjinlai.ui.fragment.AccountFragment;
import com.jinguanguke.guwangjinlai.ui.view.RevealBackgroundView;
import com.jinguanguke.guwangjinlai.ui.viewholder.TabsPagerAdapter;
import com.jinguanguke.guwangjinlai.ui.viewholder.UserProfileAdapter;
import com.jinguanguke.guwangjinlai.util.CircleTransformation;
import com.jinguanguke.guwangjinlai.util.ServiceGenerator;
import com.smartydroid.android.starter.kit.account.AccountManager;
import com.squareup.picasso.Picasso;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.valuesfeng.picker.Picker;
import io.valuesfeng.picker.engine.glide.GlideEngine;
import io.valuesfeng.picker.engine.picasso.PicassoEngine;
import io.valuesfeng.picker.utils.PicturePickerUtils;
import retrofit2.Call;
import retrofit2.Response;


/**
 * Created by Miroslaw Stanek on 14.01.15.
 */
public class UserProfileActivity extends Fragment implements RevealBackgroundView.OnStateChangeListener {
    public static final String ARG_REVEAL_START_LOCATION = "reveal_start_location";

    private static final int USER_OPTIONS_ANIMATION_DELAY = 300;
    private static final Interpolator INTERPOLATOR = new DecelerateInterpolator();
    private String mid = null;
    List<String> potos = null;

    @Bind(R.id.vRevealBackground)
    RevealBackgroundView vRevealBackground;
//    @Bind(R.id.rvUserProfile)
//    RecyclerView rvUserProfile;

    @Bind(R.id.tlUserProfileTabs)
    TabLayout tlUserProfileTabs;

    @Bind(R.id.ivUserProfilePhoto)
    ImageView ivUserProfilePhoto;
    @Bind(R.id.vUserDetails)
    View vUserDetails;
    @Bind(R.id.btnFollow)
    Button btnFollow;
    @Bind(R.id.vUserStats)
    View vUserStats;
    @Bind(R.id.vUserProfileRoot)
    View vUserProfileRoot;
    @Bind(R.id.nickename)
    TextView nikename;

    @Bind(R.id.pager)
    ViewPager pager;

    private int avatarSize;
    private String profilePhoto;
    private UserProfileAdapter userPhotosAdapter;

    public static void startUserProfileFromLocation(int[] startingLocation, Activity startingActivity) {
        Intent intent = new Intent(startingActivity, UserProfileActivity.class);
        intent.putExtra(ARG_REVEAL_START_LOCATION, startingLocation);
        startingActivity.startActivity(intent);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view1 = inflater.inflate(R.layout.activity_user_profile,container,false);
//        setContentView(R.layout.activity_user_profile);
         ButterKnife.bind(this, view1);
        User user = AccountManager.getCurrentAccount();
        mid = user.getMid();
        get_me_video(mid);
        this.avatarSize = getResources().getDimensionPixelSize(R.dimen.user_profile_avatar_size);
        this.profilePhoto = "http://www.jinguanguke.com/uploads/app/ava/" + mid + ".png";

        //用户名
        if(user.getUname().length() == 0)
        {
            nikename.setText("点击修改昵称");
        }
        else
        {
            nikename.setText(user.getUname());
        }
        Picasso.with(getActivity())
                .load(profilePhoto)
                .placeholder(R.drawable.img_circle_placeholder)
                .resize(avatarSize, avatarSize)
                .centerCrop()
                .transform(new CircleTransformation())
                .into(ivUserProfilePhoto);


        TabsPagerAdapter adapter = new TabsPagerAdapter(getActivity().getSupportFragmentManager());
        pager.setAdapter(adapter);
        tlUserProfileTabs.setupWithViewPager(pager);

//        setupTabs();

//        setupUserProfileGrid();
        setupRevealBackground(savedInstanceState);
        return view1;
    }

    private void setupTabs() {
        tlUserProfileTabs.addTab(tlUserProfileTabs.newTab().setIcon(R.drawable.ic_grid_on_white));
        tlUserProfileTabs.addTab(tlUserProfileTabs.newTab().setIcon(R.drawable.ic_list_white));
        tlUserProfileTabs.addTab(tlUserProfileTabs.newTab().setIcon(R.drawable.ic_place_white));
        tlUserProfileTabs.addTab(tlUserProfileTabs.newTab().setIcon(R.drawable.ic_label_white));
    }

//    private void setupUserProfileGrid() {
//        final StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
//        rvUserProfile.setLayoutManager(layoutManager);
//        rvUserProfile.setOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                userPhotosAdapter.setLockedAnimations(true);
//            }
//        });
//    }

    private void setupRevealBackground(Bundle savedInstanceState) {
        vRevealBackground.setOnStateChangeListener(this);
        if (savedInstanceState == null) {
            final int[] startingLocation = {0,0};
//            final int[] startingLocation = getActivity().getIntent().getIntArrayExtra(ARG_REVEAL_START_LOCATION);

            vRevealBackground.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    vRevealBackground.getViewTreeObserver().removeOnPreDrawListener(this);
                    vRevealBackground.startFromLocation(startingLocation);
                    return true;
                }
            });
        } else {
            vRevealBackground.setToFinishedFrame();
            userPhotosAdapter.setLockedAnimations(true);
        }
    }

    @Override
    public void onStateChange(int state) {
        if (RevealBackgroundView.STATE_FINISHED == state) {
//            rvUserProfile.setVisibility(View.VISIBLE);
            tlUserProfileTabs.setVisibility(View.VISIBLE);
            vUserProfileRoot.setVisibility(View.VISIBLE);
//            userPhotosAdapter = new UserProfileAdapter(getActivity());
//            rvUserProfile.setAdapter(userPhotosAdapter);
            animateUserProfileOptions();
            animateUserProfileHeader();
        } else {
            tlUserProfileTabs.setVisibility(View.INVISIBLE);
//            rvUserProfile.setVisibility(View.INVISIBLE);
            vUserProfileRoot.setVisibility(View.INVISIBLE);
        }
    }

    private void animateUserProfileOptions() {
        tlUserProfileTabs.setTranslationY(-tlUserProfileTabs.getHeight());
        tlUserProfileTabs.animate().translationY(0).setDuration(300).setStartDelay(USER_OPTIONS_ANIMATION_DELAY).setInterpolator(INTERPOLATOR);
    }

    private void animateUserProfileHeader() {
           vUserProfileRoot.setTranslationY(-vUserProfileRoot.getHeight());
           ivUserProfilePhoto.setTranslationY(-ivUserProfilePhoto.getHeight());
           vUserDetails.setTranslationY(-vUserDetails.getHeight());
           vUserStats.setAlpha(0);

           vUserProfileRoot.animate().translationY(0).setDuration(300).setInterpolator(INTERPOLATOR);
           ivUserProfilePhoto.animate().translationY(0).setDuration(300).setStartDelay(100).setInterpolator(INTERPOLATOR);
           vUserDetails.animate().translationY(0).setDuration(300).setStartDelay(200).setInterpolator(INTERPOLATOR);
           vUserStats.animate().alpha(1).setDuration(200).setStartDelay(400).setInterpolator(INTERPOLATOR).start();
    }


    //处理头像点击事件
    @OnClick(R.id.ivUserProfilePhoto)
    public void pick(View view) {
        Picker.from(this)
                .singleChoice()
                .setEnableCamera(true)
                .setEngine(new GlideEngine())
                .forResult(1);
    }

    //修改昵称
    @OnClick(R.id.nickename)
    public void chang_nikename() {
        final EditText inputServer = new EditText(getActivity());
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("修改昵称").setIcon(android.R.drawable.ic_dialog_info).setView(inputServer)
                .setNegativeButton("取消", null);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

          public void onClick(DialogInterface dialog, int which) {
//            video_title = inputServer.getText().toString();
              nikename.setText(inputServer.getText().toString());
              update_uname(mid,inputServer.getText().toString());
              AccountManager.getCurrentAccount();

          }
        });
        builder.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        List<Uri> mSelected;
        User user = AccountManager.getCurrentAccount();
        String mid = user.getMid();
        final String ava_name = mid + ".png";
        String url = "http://www.jinguanguke.com/plus/io/index.php?" +
                "c=upload&a=file";
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            mSelected = PicturePickerUtils.obtainResult(data);
            for (Uri u : mSelected) {
                String img_path = getRealPathFromURI(getContext(),u);
                File file = new File(img_path);
//                File file = new File(u.getPath());
                OkHttpUtils.post()//
                        .addFile("ava", ava_name, file)//
                        .url(url)
                        .build()//
                        .execute(new StringCallback() {
                            @Override
                            public void onError(okhttp3.Call call, Exception e) {
                                Log.i("video_error","is had error");
                            }

                            @Override
                            public void onResponse(String response) {
                                String i_ava = "http://www.jinguanguke.com/uploads/app/ava/" + ava_name;
                                Picasso.with(getActivity())
                                        .load(i_ava)
                                        .placeholder(R.drawable.img_circle_placeholder)
                                        .resize(avatarSize, avatarSize)
                                        .centerCrop()
                                        .transform(new CircleTransformation())
                                        .into(ivUserProfilePhoto);
                            }
                        });
                Log.i("picture", u.getPath());
            }
        }
        super.onActivityResult(requestCode, resultCode, data);

    }

    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    private void get_me_video(String mid)
    {
        FeedService feedService = ServiceGenerator.createService(FeedService.class);
       final List<String> photos = null;
        Call<DataInfo> feedServicecall = feedService.get_me(mid);
        feedServicecall.enqueue(new retrofit2.Callback<DataInfo>() {
            @Override
            public void onResponse(Call<DataInfo> call, Response<DataInfo> response) {
                if(response.body().getData().getItems() != null){
                    for(DataInfo.DataBean.ItemsBean item : response.body().getData().getItems()){
                        photos.add(item.getLitpic());
                    }
                    userPhotosAdapter = new UserProfileAdapter(getActivity(),photos);


                }
            }

            @Override
            public void onFailure(Call<DataInfo> call, Throwable t) {

            }
        });
    }

    private void update_uname(String mid, String uname)
    {
        SignupService signupService = ServiceGenerator.createService(SignupService.class);
        Call<Register> feedServicecall = signupService.update_uname(mid,uname);
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
