package com.jinguanguke.guwangjinlai.api.service;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by jin on 16/4/29.
 */
public interface FileUploadService  {
    @Multipart
    @POST("index.php?c=upload&a=file")
    Call<ResponseBody> upload(@Part("description") Map<String, RequestBody> params);
}
