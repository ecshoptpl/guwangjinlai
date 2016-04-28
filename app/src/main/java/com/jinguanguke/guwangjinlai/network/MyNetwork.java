package com.jinguanguke.guwangjinlai.network;

import com.jinguanguke.guwangjinlai.api.service.BiliService;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by jin on 16/4/28.
 */
public class MyNetwork {
    private static final String sBaseUrl = "http://www.bilibili.tv/m/";
    private Retrofit mRetrofit;

    // Make this class a thread safe singleton
    private static class SingletonHolder {
        private static final MyNetwork INSTANCE = new MyNetwork();
    }

    public static synchronized MyNetwork get() {
        return SingletonHolder.INSTANCE;
    }


    protected Retrofit.Builder newRetrofitBuilder() {
        return new Retrofit.Builder();
    }

    private Retrofit retrofit() {
        if (mRetrofit == null) {
            Retrofit.Builder builder = newRetrofitBuilder();
            mRetrofit = builder.baseUrl(sBaseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return mRetrofit;
    }

    public static BiliService createBiliService() {
        return get().retrofit().create(BiliService.class);
    }
}
