package com.jinguanguke.guwangjinlai.api.service;

import com.jinguanguke.guwangjinlai.model.entity.Arctiny;

import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * Created by jin on 16/4/30.
 */
public interface ArctinyService {
    @POST("index.php?_interface=insert&_table=dede_arctiny&c=api&channel=18&typeid=14")
    Call<Arctiny> add(@Query("mid") String mid);
}
