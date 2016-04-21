package com.jinguanguke.guwangjinlai.util;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by jin on 16/4/13.
 */
public class httpUtils {
    OkHttpClient client = new OkHttpClient();
    public static Response get(String url) throws IOException {
        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS).readTimeout(10, TimeUnit.SECONDS).writeTimeout(10, TimeUnit.SECONDS).build();
        Request request = new Request.Builder()
                .url(url)
                .build();
        Response res = client.newCall(request).execute();
        return res;
    }

    public  String run(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = client.newCall(request).execute();
        return response.body().string();
    }
}
