package com.jinguanguke.guwangjinlai.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jinguanguke.guwangjinlai.R;
import com.jinguanguke.guwangjinlai.api.service.FeedService;
import com.jinguanguke.guwangjinlai.data.JinguanDB;
import com.jinguanguke.guwangjinlai.model.entity.DataInfo;
import com.jinguanguke.guwangjinlai.model.entity.ImageInfo;
import com.jinguanguke.guwangjinlai.ui.activity.DetailActivity;
import com.jinguanguke.guwangjinlai.ui.activity.PlayActivity;
import com.jinguanguke.guwangjinlai.ui.viewholder.OnVideoClickListener;
import com.jinguanguke.guwangjinlai.ui.viewholder.VideosAdapter;
import com.jinguanguke.guwangjinlai.util.httpUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by jin on 16/4/21.
 */
public class LectureFragment extends Fragment implements OnVideoClickListener,SwipeRefreshLayout.OnRefreshListener {
    private static final int REQUEST_NUM = 5;
    private static final String REQUEST_URL = "http://www.jinguanguke.com/plus/io/";
    private static final int REQUEST_FAIL = 2;
    private static final int REQUEST_SUCCESS = 3;
    private static final int GET_URL_SUCCESS = 4;
    private static final int GET_SIZE_SUCCESS = 5;

    private SwipeRefreshLayout refresh;
    private RecyclerView recyclerView;
    private Context mContext;
    private List<ImageInfo> imageInfos;
    private List<ImageInfo> imageCache;
    private boolean isLoadMore = false;
    private int hasLoadPage = 0;


    private retrofit2.Call<DataInfo> call;

    private VideosAdapter mAdapter;



    public static Fragment create() {
        return new LectureFragment();
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.getData().getInt("state")) {
                case REQUEST_FAIL:
                    showRefreshing(false);
                    isLoadMore = false;

                    break;
                case GET_URL_SUCCESS:
                    showRefreshing(false);
                    List<ImageInfo> infos = new ArrayList<>();
                    for (ImageInfo info : imageCache) {
                        if (!db.contain(info,"ImageInfo_jz")) {
                            infos.add(info);
                        } else {
                        }
                    }
                    dosometing(infos);
                    hasLoadPage++;
                    break;
                case GET_SIZE_SUCCESS:
                    Bundle data = msg.getData();
                    ImageInfo info = new ImageInfo();
                    info.setHeight(data.getInt("height"));
                    info.setWho(data.getString("who"));
                    info.setAid(data.getString("id"));
                    info.setWidth(data.getInt("width"));
                    info.setUrl(data.getString("url"));
                    info.setVurl(data.getString("vurl"));
                    info.setTime(data.getString("time"));
                    info.setTitle(data.getString("title"));
                    info.setTypeid(data.getString("typeid"));
                    info.setTypedir(data.getString("typedir"));
//                    String table_name = getArguments().getString("table_name","ImageInfo");
                    db.saveImageInfo(info,"ImageInfo_jz");
                    imageInfos.add(info);
                    mAdapter.notifyItemInserted(imageInfos.size());
                    isLoadMore = false;
                    break;
            }
        }
    };
    private JinguanDB db;
    private StaggeredGridLayoutManager mStaggeredGridLayoutManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = getActivity();
        db = JinguanDB.getInstance(mContext);
//        String table_name = getArguments().getString("table_name","ImageInfo");
        imageInfos = db.findImageInfoAll("ImageInfo_jz");
        imageCache = new ArrayList<>();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.content_layout, container, false);
        refresh = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);

        refresh.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R
                .color.holo_orange_light, android.R.color.holo_green_light);
        refresh.setOnRefreshListener(this);
        showRefreshing(true);
        return view;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mStaggeredGridLayoutManager = new StaggeredGridLayoutManager(2,
                StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mStaggeredGridLayoutManager);
        mAdapter = new VideosAdapter(mContext, imageInfos);
        mAdapter.setOnVideoClickListener(this);
        recyclerView.setAdapter(mAdapter);


        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {

                    int[] positions = new int[mStaggeredGridLayoutManager.getSpanCount()];
                    positions = mStaggeredGridLayoutManager.findLastCompletelyVisibleItemPositions(positions);
                    for (int position : positions) {
                        if (position == mStaggeredGridLayoutManager.getItemCount() - 1) {
                            loadMore();
                            break;
                        }
                    }
                }


            }
        });

        requestData(1);
    }

    private void loadMore() {
        if (!isLoadMore) {
            isLoadMore = true;
            requestData(hasLoadPage + 1);
        }
    }


    private void showRefreshing(boolean isShow) {
        if (isShow) {
            refresh.setProgressViewOffset(false, 0, (int) (mContext.getResources().getDisplayMetrics().density * 24 +
                    0.5f));
            refresh.setRefreshing(true);
        } else {
            refresh.setRefreshing(false);
        }
    }

    public void requestData(int page) {
        showRefreshing(true);
        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS).writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(REQUEST_URL).client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        FeedService apiService = retrofit.create(FeedService.class);
        //retrofit2.Call<DataInfo> call = apiService.Data();

        call = apiService.get_lecture(page);
        call.enqueue(new retrofit2.Callback<DataInfo>() {
            @Override
            public void onResponse(retrofit2.Call<DataInfo> call, retrofit2.Response<DataInfo> response) {
                if (response.body().getData().getItems() == null) {

                    Message msg = Message.obtain();
                    Bundle bundle = new Bundle();
                    bundle.putInt("state", REQUEST_FAIL);
                    msg.setData(bundle);
                    handler.sendMessage(msg);
                } else {
                    imageCache.clear();
                    for (DataInfo.DataBean.ItemsBean entity : response.body().getData().getItems()) {
                        ImageInfo info = new ImageInfo();
                        if(entity.getLitpic().indexOf("http") != -1)
                        {
                            info.setUrl(entity.getLitpic());
                        }
                        else
                        {
                            info.setUrl("http://www.jinguanguke.com/" + entity.getLitpic());
                        }
                        //info.setUrl("http://www.jinguanguke.com/" + entity.getLitpic());
                        info.setTime(entity.getPubdate());
                        info.setWho(entity.getUname());
                        info.setTitle(entity.getTitle());
                        info.setTypedir(entity.getTypedir());
                        info.setTypeid(entity.getTypeid());
                        info.setAid(entity.getId());
                        info.setVurl(entity.getVurl());
                        imageCache.add(info);
                    }
                    Message msg = Message.obtain();
                    Bundle bundle = new Bundle();
                    bundle.putInt("state", GET_URL_SUCCESS);
                    msg.setData(bundle);
                    handler.sendMessage(msg);
                }
            }

            @Override
            public void onFailure(retrofit2.Call<DataInfo> call, Throwable t) {

            }
        });

    }

    @Override
    public void onRefresh() {
        showRefreshing(false);
    }

    public Point loadImageForSize(String url) {
        Point point = new Point();
        try {
            Response response = httpUtils.get(url);
            if (response.code() == 200) {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeStream(response.body().byteStream(), null, options);
                point.x = options.outWidth;
                point.y = options.outHeight;
                return point;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        point.x = 0;
        point.y = 0;
        return point;

    }

    public void dosometing(final List<ImageInfo> infos) {
        for (final ImageInfo info : infos) {
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    Point point = loadImageForSize(info.getUrl());
                    Message msg = Message.obtain();
                    Bundle bundle = new Bundle();
                    bundle.putInt("state", GET_SIZE_SUCCESS);
                    bundle.putString("url", info.getUrl());
                    bundle.putString("title", info.getTitle());
                    // bundle.putString("typedir", info.getTypedir());
                    bundle.putString("time", info.getTime());
                    bundle.putString("who", info.getWho());
                    bundle.putString("id", info.getAid());
                    bundle.putString("vurl", info.getVurl());
                    bundle.putString("typeid", info.getTypeid());
                    bundle.putString("typedir", info.getTypedir());
                    bundle.putInt("width", point.x);
                    bundle.putInt("height", point.y);
                    msg.setData(bundle);
                    handler.sendMessage(msg);
                }
            }.start();
        }
    }
    @Override
    public void onVideoClick(View itemView, int position) {
        String url = imageInfos.get(position).getUrl();
        String vurl = imageInfos.get(position).getVurl();
        String aid = imageInfos.get(position).getAid();
        String typedir = imageInfos.get(position).getTypedir();
        String title = imageInfos.get(position).getTitle();

        Intent intent = new Intent(getActivity(), PlayActivity.class);
        intent.putExtra("url", url);
        intent.putExtra("vurl", vurl);
        intent.putExtra("aid", aid);
        intent.putExtra("title", title);
        intent.putExtra("typedir", typedir);
        startActivity(intent);
    }
}
