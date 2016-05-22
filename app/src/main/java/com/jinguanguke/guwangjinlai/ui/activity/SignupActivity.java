package com.jinguanguke.guwangjinlai.ui.activity;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.app.Activity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.jinguanguke.guwangjinlai.R;
import com.jinguanguke.guwangjinlai.api.service.SignupService;
import com.jinguanguke.guwangjinlai.data.Account;
import com.jinguanguke.guwangjinlai.data.Constant;
import com.jinguanguke.guwangjinlai.model.entity.Register;
import com.jinguanguke.guwangjinlai.model.entity.Signup;
import com.jinguanguke.guwangjinlai.model.entity.checkMobile;
import com.jinguanguke.guwangjinlai.util.Callback;
import com.jinguanguke.guwangjinlai.util.LocalAccountManager;
import com.jinguanguke.guwangjinlai.util.SMSManager;
import com.jinguanguke.guwangjinlai.util.ServiceGenerator;
import com.smartydroid.android.starter.kit.app.StarterKitApp;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;


import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnTextChanged;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
//import cn.smssdk.SMSSDK;


import static android.Manifest.permission.READ_CONTACTS;

/**
 * 用户注册
 */
public class SignupActivity extends Activity implements  TimeListener{


    private static final int REQUEST_READ_CONTACTS = 0;
    public static final int EXTERNAL_STORAGE_REQ_CODE = 10 ;
    private String puser = "0";



    @Bind(R.id.til_number)
    TextInputLayout tilNumber;

//    @Bind(R.id.til_password)
//    TextInputLayout passwordContainer;

    @Bind(R.id.get_code)
    Button btnCode;

    @Bind(R.id.til_code)
    EditText tilCode;

    @Bind(R.id.password)
    EditText tilPassword;

    @Bind(R.id.puser)
    EditText tilpuser;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);


        SMSManager.getInstance().registerTimeListener(this);
        SMSManager.getInstance().setDefaultDelay(60);


        btnCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendCode();
            }
        });
//        btnRegister.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                register();
//            }
//        });






        // Set up the login form.
      //  mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
       // populateAutoComplete();



        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
            }
        });

//        Button mGetcodeButton = (Button) findViewById(R.id.get_code);
//        mGetcodeButton.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                get_code();
//            }
//        });


    }

    public void sendCode()  {
        if (!requestPermission()){
            return;
        }
        if (tilNumber.getEditText().getText().toString().length() != 11) {
            Toast.makeText(this,"请输入正确手机号",Toast.LENGTH_SHORT).show();
            return;
        }

        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS).writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://www.jinguanguke.com/plus/io/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        SignupService service = retrofit.create(SignupService.class);
        retrofit2.Call<checkMobile> sign = service.check_mobile(tilNumber.getEditText().getText().toString());
        sign.enqueue(new retrofit2.Callback<checkMobile>() {
            @Override
            public void onResponse(retrofit2.Call<checkMobile> call, retrofit2.Response<checkMobile> response) {
                if(response.body().getErr_code() == 1)
                {
                    Toast.makeText(SignupActivity.this,"手机号已经注册",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    SMSManager.getInstance().sendMessage(SignupActivity.this, "86",tilNumber.getEditText().getText().toString());
                }
            }

            @Override
            public void onFailure(retrofit2.Call<checkMobile> call, Throwable t) {
                Toast.makeText(SignupActivity.this,"网络错误",Toast.LENGTH_SHORT).show();
            }
        });



    }

    public void register(){
//        if (TextUtils.isEmpty(tilName.getEditText().getText().toString())){
//            Toast.makeText(this,"请输入昵称",Toast.LENGTH_SHORT).show();
//            return;
//        }
        if (tilPassword.getText().toString().length() < 6 || tilPassword.getText().toString().length() > 12) {
            Toast.makeText(this,"请输入6-12位密码",Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(tilCode.getText().toString())){
            Toast.makeText(this,"请输入验证码",Toast.LENGTH_SHORT).show();
            return;
        }

        if(tilpuser.getText() == null || tilpuser.getText().length() <= 0)
        {
            verifycode();
        }
        else
        {
            verify_puser();

        }



    }

    //验证验证码是否正确并注册
    private void verifycode () {
        SMSManager.getInstance().verifyCode(this, "86", tilNumber.getEditText().getText().toString(), tilCode.getText().toString(), new Callback() {
            @Override
            public void success() {
                OkHttpClient client = new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS).writeTimeout(10, TimeUnit.SECONDS)
                        .readTimeout(30, TimeUnit.SECONDS)
                        .build();


                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://www.jinguanguke.com/plus/io/")
                        .client(client)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                SignupService service = retrofit.create(SignupService.class);
                retrofit2.Call<Register> rgst = service.register(tilNumber.getEditText().getText().toString(),tilPassword.getText().toString(),puser,"mid","app_android");
                rgst.enqueue(new retrofit2.Callback<Register>() {
                    @Override
                    public void onResponse(retrofit2.Call<Register> call, retrofit2.Response<Register> response) {
                        if(response.body().getErr_code() == "1")
                        {
                            Toast.makeText(SignupActivity.this,"注册失败",Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(SignupActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent();
                            intent.setClass(SignupActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(retrofit2.Call<Register> call, Throwable t) {
                        Toast.makeText(SignupActivity.this,"网络错误",Toast.LENGTH_SHORT).show();
                    }
                });



            }

            @Override
            public void error(Throwable error) {
                Toast.makeText(SignupActivity.this, "验证码错误", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //验证puser是否存在
    private void verify_puser() {
        SignupService signupService = ServiceGenerator.createService(SignupService.class);
        retrofit2.Call<checkMobile> sign = signupService.check_puser(tilpuser.getText().toString());
        sign.enqueue(new retrofit2.Callback<checkMobile>() {
            @Override
            public void onResponse(retrofit2.Call<checkMobile> call, retrofit2.Response<checkMobile> response) {
                if(response.body().getErr_code() == 1)
                {
                    puser = response.body().getErr_msg();
                    verifycode();
                }
                else
                {
                    Toast.makeText(SignupActivity.this,"您输入的推荐人不存在",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<checkMobile> call, Throwable t) {
                Toast.makeText(SignupActivity.this,"网络错误",Toast.LENGTH_SHORT).show();
            }
        });

    }
    public boolean requestPermission(){
        //判断当前Activity是否已经获得了该权限
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED
                ||ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {

            //如果App的权限申请曾经被用户拒绝过，就需要在这里跟用户做出解释
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                Toast.makeText(this, "please give me the permission", Toast.LENGTH_SHORT).show();
            } else {
                //进行权限请求
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_CONTACTS,Manifest.permission.READ_PHONE_STATE},
                        EXTERNAL_STORAGE_REQ_CODE);
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case EXTERNAL_STORAGE_REQ_CODE: {
                // 如果请求被拒绝，那么通常grantResults数组为空
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    //申请成功，进行相应操作
                    //sendCode();
                } else {
                    //申请失败，可以继续向用户解释。
                }
                return;
            }
        }
    }

    @Override
    public void onLastTimeNotify(int lastSecond) {
        if (lastSecond > 0)
            btnCode.setText(lastSecond + "秒后重新发送");
        else
            btnCode.setText("发送验证码");
    }

    @Override
    public void onAbleNotify(boolean valuable) {
        btnCode.setEnabled(valuable);
    }

    public static boolean isMobileNO(String mobiles) {
        Pattern p = Pattern
                .compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
        Matcher m = p.matcher(mobiles);
        System.out.println(m.matches() + "---");
        return m.matches();
    }


}

