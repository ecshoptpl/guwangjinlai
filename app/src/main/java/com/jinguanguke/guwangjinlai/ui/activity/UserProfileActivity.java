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
import android.view.ViewParent;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.google.gson.Gson;
import com.jinguanguke.guwangjinlai.R;
import com.jinguanguke.guwangjinlai.api.service.AuthService;
import com.jinguanguke.guwangjinlai.api.service.FeedService;
import com.jinguanguke.guwangjinlai.api.service.SignupService;
import com.jinguanguke.guwangjinlai.model.entity.DataInfo;
import com.jinguanguke.guwangjinlai.model.entity.Register;
import com.jinguanguke.guwangjinlai.model.entity.User;
import com.jinguanguke.guwangjinlai.model.entity.VideoTotal;
import com.jinguanguke.guwangjinlai.ui.fragment.AccountFragment;
import com.jinguanguke.guwangjinlai.ui.view.RevealBackgroundView;
import com.jinguanguke.guwangjinlai.ui.viewholder.TabsPagerAdapter;
import com.jinguanguke.guwangjinlai.ui.viewholder.UserProfileAdapter;
import com.jinguanguke.guwangjinlai.util.CircleTransformation;
import com.jinguanguke.guwangjinlai.util.ServiceGenerator;
import com.jinguanguke.guwangjinlai.util.Utils;
import com.smartydroid.android.starter.kit.account.AccountManager;
import com.squareup.picasso.Picasso;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
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
public class UserProfileActivity extends Fragment implements RevealBackgroundView.OnStateChangeListener,BDLocationListener {
    public static final String ARG_REVEAL_START_LOCATION = "reveal_start_location";

    private static final int USER_OPTIONS_ANIMATION_DELAY = 300;
    private static final Interpolator INTERPOLATOR = new DecelerateInterpolator();
    private String mid = null;
    String photo = null;
    List<String> photos = new ArrayList<>();

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
//    @Bind(R.id.btnFollow)
//    Button btnFollow;
    @Bind(R.id.vUserStats)
    View vUserStats;

    @Bind(R.id.vUserProfileRoot)
    View vUserProfileRoot;

    @Bind(R.id.nickename)
    TextView nikename;

    @Bind(R.id.pager)
    ViewPager pager;

    @Bind(R.id.scores)
    TextView scores;

    @Bind(R.id.video_total)
    TextView video_total;

    @Bind(R.id.jointime)
    TextView jointime;

    @Bind(R.id.address)
    TextView address;

    @Bind(R.id.sub)
    TextView sub;

    private int avatarSize;
    private String profilePhoto;
    private UserProfileAdapter userPhotosAdapter;
    User user = AccountManager.getCurrentAccount();
    private LayoutInflater mInflater;
    private WeakReference<View> mRootView = null;

    public LocationClient mLocationClient = null;
    public BDLocationListener myListener = this;

    public static void startUserProfileFromLocation(int[] startingLocation, Activity startingActivity) {
        Intent intent = new Intent(startingActivity, UserProfileActivity.class);
        intent.putExtra(ARG_REVEAL_START_LOCATION, startingLocation);
        startingActivity.startActivity(intent);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view1 = inflater.inflate(R.layout.activity_user_profile,container,false);

         ButterKnife.bind(this, view1);


        mLocationClient = new LocationClient(getActivity().getApplicationContext());     //声明LocationClient类
        mLocationClient.registerLocationListener( myListener );    //注册监听函数
        initLocation();

        mLocationClient.start();    //开始定位

        mid = user.getMid();

        set_video_num(mid);
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

       // 头像
        Picasso.with(getActivity())
                .load(profilePhoto)
                .placeholder(R.drawable.img_circle_placeholder)
                .resize(avatarSize, avatarSize)
                .centerCrop()
                .transform(new CircleTransformation())
                .into(ivUserProfilePhoto);



        //积分
        scores.setText(user.getScores());

        //加入时间
        String jtime = user.getJointime();
        if(jtime != null)
        {
            jtime = Utils.TimeStamp2Date(jtime, "yyyy-MM-dd");
            jointime.setText("注册日期：" + jtime);
        }
        else
        {
            jointime.setText("注册日期未知");
        }


        //设置下级数
        set_sub_num(mid);

//        setupTabs();

//        setupUserProfileGrid();


        TabsPagerAdapter adapter = new TabsPagerAdapter(getChildFragmentManager());
//        TabsPagerAdapter adapter = new TabsPagerAdapter(getActivity().getSupportFragmentManager());
        pager.setAdapter(adapter);
        tlUserProfileTabs.setupWithViewPager(pager);
        setupRevealBackground(savedInstanceState);
        return view1;
    }

    private void setupTabs() {
        tlUserProfileTabs.addTab(tlUserProfileTabs.newTab().setIcon(R.drawable.ic_grid_on_white));
        tlUserProfileTabs.addTab(tlUserProfileTabs.newTab().setIcon(R.drawable.ic_list_white));
        tlUserProfileTabs.addTab(tlUserProfileTabs.newTab().setIcon(R.drawable.ic_place_white));
        tlUserProfileTabs.addTab(tlUserProfileTabs.newTab().setIcon(R.drawable.ic_label_white));
    }



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
            //userPhotosAdapter.setLockedAnimations(true);
        }
    }

    @Override
    public void onStateChange(int state) {
        if (RevealBackgroundView.STATE_FINISHED == state) {
//            rvUserProfile.setVisibility(View.VISIBLE);
            tlUserProfileTabs.setVisibility(View.VISIBLE);
            vUserProfileRoot.setVisibility(View.VISIBLE);
           // userPhotosAdapter = new UserProfileAdapter(getActivity());
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
              user.setUname(inputServer.getText().toString());
              nikename.setText(inputServer.getText().toString());
              update_uname(mid,inputServer.getText().toString());


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



    //设置视频数
    private void set_video_num(String mid)
    {
        FeedService feedService = ServiceGenerator.createService(FeedService.class);

        Call<VideoTotal> feedServicecall = feedService.get_video_total(mid);
        feedServicecall.enqueue(new retrofit2.Callback<VideoTotal>() {
            @Override
            public void onResponse(Call<VideoTotal> call, Response<VideoTotal> response) {
                if(response.body().getData() != null){
                    video_total.setText(response.body().getData());
                }
            }

            @Override
            public void onFailure(Call<VideoTotal> call, Throwable t) {

            }
        });
    }

    //设置推荐下级数量
    private void set_sub_num(String mid)
    {
        FeedService feedService = ServiceGenerator.createService(FeedService.class);

        Call<VideoTotal> feedServicecall = feedService.get_sub_total(mid);
        feedServicecall.enqueue(new retrofit2.Callback<VideoTotal>() {
            @Override
            public void onResponse(Call<VideoTotal> call, Response<VideoTotal> response) {
                if(response.body().getData() != null){
                    sub.setText(response.body().getData());
                }
            }

            @Override
            public void onFailure(Call<VideoTotal> call, Throwable t) {

            }
        });
    }

    //更新用户名
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

    //百度定位选项配置

    private void initLocation(){
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy
        );//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        int span=1000;
        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        mLocationClient.setLocOption(option);
    }



        @Override
        public void onReceiveLocation(BDLocation location) {
            //Receive Location
            StringBuffer sb = new StringBuffer(256);
            sb.append("time : ");
            sb.append(location.getTime());
            sb.append("\nerror code : ");
            sb.append(location.getLocType());
            sb.append("\nlatitude : ");
            sb.append(location.getLatitude());
            sb.append("\nlontitude : ");
            sb.append(location.getLongitude());
            sb.append("\nradius : ");
            sb.append(location.getRadius());
            if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
                sb.append("\nspeed : ");
                sb.append(location.getSpeed());// 单位：公里每小时
                sb.append("\nsatellite : ");
                sb.append(location.getSatelliteNumber());
                sb.append("\nheight : ");
                sb.append(location.getAltitude());// 单位：米
                sb.append("\ndirection : ");
                sb.append(location.getDirection());// 单位度
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
                sb.append("\ndescribe : ");
                sb.append("gps定位成功");

            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
                //运营商信息
                sb.append("\noperationers : ");
                sb.append(location.getOperators());
                sb.append("\ndescribe : ");
                sb.append("网络定位成功");
            } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
                sb.append("\ndescribe : ");
                sb.append("离线定位成功，离线定位结果也是有效的");
            } else if (location.getLocType() == BDLocation.TypeServerError) {
                sb.append("\ndescribe : ");
                sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
            } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                sb.append("\ndescribe : ");
                sb.append("网络不同导致定位失败，请检查网络是否通畅");
            } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                sb.append("\ndescribe : ");
                sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
            }
            sb.append("\nlocationdescribe : ");
            sb.append(location.getLocationDescribe());// 位置语义化信息
            List<Poi> list = location.getPoiList();// POI数据
            if (list != null) {
                sb.append("\npoilist size = : ");
                sb.append(list.size());
                for (Poi p : list) {
                    sb.append("\npoi= : ");
                    sb.append(p.getId() + " " + p.getName() + " " + p.getRank());
                }
            }

            address.setText(location.getLocationDescribe());
            mLocationClient.stop();
            Log.i("BaiduLocationApiDem", sb.toString());


        }



}
