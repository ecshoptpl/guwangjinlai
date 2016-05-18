package com.jinguanguke.guwangjinlai.ui.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.jinguanguke.guwangjinlai.R;
import com.jinguanguke.guwangjinlai.api.service.SignupService;
import com.jinguanguke.guwangjinlai.model.entity.Register;
import com.jinguanguke.guwangjinlai.model.entity.checkMobile;
import com.jinguanguke.guwangjinlai.util.Callback;
import com.jinguanguke.guwangjinlai.util.SMSManager;

import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by jin on 16/5/18.
 */
public class FindPasswordActivity extends Activity implements  TimeListener {

    private  String mid = null;
    retrofit2.Call<Register> rgst = null;
    public static final int EXTERNAL_STORAGE_REQ_CODE = 10 ;

    //手机号
    @Bind(R.id.phone)
    TextInputLayout tilNumber;

    //验证码
    @Bind(R.id.code)
    EditText tilCode;

    @Bind(R.id.password)
    EditText tilPassword;

    @Bind(R.id.get_code)
    Button btnCode;

    @Bind(R.id.submit_button)
    Button btnSubmit;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_findpassword);
        ButterKnife.bind(this);


        SMSManager.getInstance().registerTimeListener(this);
        SMSManager.getInstance().setDefaultDelay(60);


        btnCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendCode();
            }
        });


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
            }
        });
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
                    mid = response.body().getErr_msg();
                    SMSManager.getInstance().sendMessage(FindPasswordActivity.this, "86",tilNumber.getEditText().getText().toString());
                    Toast.makeText(FindPasswordActivity.this,"您已注册，信息将更新",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(FindPasswordActivity.this,"您输入的手机号不存在",Toast.LENGTH_SHORT).show();
//                    SMSManager.getInstance().sendMessage(FindPasswordActivity.this, "86",tilNumber.getEditText().getText().toString());
                }
            }

            @Override
            public void onFailure(retrofit2.Call<checkMobile> call, Throwable t) {
                Toast.makeText(FindPasswordActivity.this,"网络错误",Toast.LENGTH_SHORT).show();
            }
        });



    }

    public void register(){

        if (tilPassword.getText().toString().length() < 6 || tilPassword.getText().toString().length() > 12) {
            Toast.makeText(this,"请输入6-12位密码",Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(tilCode.getText().toString())){
            Toast.makeText(this,"请输入验证码",Toast.LENGTH_SHORT).show();
            return;
        }

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

                rgst = service.update_password(mid,tilPassword.getText().toString(),"mid");
                rgst.enqueue(new retrofit2.Callback<Register>() {
                    @Override
                    public void onResponse(retrofit2.Call<Register> call, retrofit2.Response<Register> response) {
                        if(response.body().getErr_code() == "1")
                        {
                            Toast.makeText(FindPasswordActivity.this,"密码修改失败",Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(FindPasswordActivity.this, "密码修改成功", Toast.LENGTH_SHORT).show();
                            login();
                        }
                    }

                    @Override
                    public void onFailure(retrofit2.Call<Register> call, Throwable t) {
                        Toast.makeText(FindPasswordActivity.this,"网络错误",Toast.LENGTH_SHORT).show();
                    }
                });



            }

            @Override
            public void error(Throwable error) {
                Toast.makeText(FindPasswordActivity.this, "验证码错误", Toast.LENGTH_SHORT).show();
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

    private void login() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
