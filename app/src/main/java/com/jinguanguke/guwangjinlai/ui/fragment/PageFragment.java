package com.jinguanguke.guwangjinlai.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jinguanguke.guwangjinlai.R;
import com.jinguanguke.guwangjinlai.api.service.FeedService;
import com.jinguanguke.guwangjinlai.data.Account;
import com.jinguanguke.guwangjinlai.model.entity.DataInfo;
import com.jinguanguke.guwangjinlai.model.entity.User;
import com.jinguanguke.guwangjinlai.ui.viewholder.UserProfileAdapter;
import com.jinguanguke.guwangjinlai.util.ServiceGenerator;
import com.smartydroid.android.starter.kit.account.AccountManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Response;


public class PageFragment extends Fragment {
    private static final String ARG_PAGE_NUMBER = "page_number";
    private UserProfileAdapter userPhotosAdapter;

    @Bind(R.id.rvUserProfile)
    RecyclerView rvUserProfile;
    String photo = null;
    List<String> photos = new ArrayList<>();

    public PageFragment() {
    }

    public static PageFragment newInstance(int page) {
        PageFragment fragment = new PageFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE_NUMBER, page);
        fragment.setArguments(args);
        return fragment;
    }

    private void get_me_video(String mid)
    {
        FeedService feedService = ServiceGenerator.createService(FeedService.class);

        final String pic_link = null;
        Call<DataInfo> feedServicecall = feedService.get_me("1");
        feedServicecall.enqueue(new retrofit2.Callback<DataInfo>() {
            @Override
            public void onResponse(Call<DataInfo> call, Response<DataInfo> response) {
                if(response.body().getData().getItems() != null){
                    for(DataInfo.DataBean.ItemsBean item : response.body().getData().getItems()){
                        photo = "http://www.jinguanguke.com" + item.getLitpic();
                        photos.add(photo);
                    }
                    userPhotosAdapter = new UserProfileAdapter(getActivity(),photos);

                    rvUserProfile.setAdapter(userPhotosAdapter);

                    setupUserProfileGrid();
                }
            }

            @Override
            public void onFailure(Call<DataInfo> call, Throwable t) {

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_page_layout, container, false);
        ButterKnife.bind(this, rootView);
        User user = AccountManager.getCurrentAccount();
        get_me_video(user.getMid());
//        userPhotosAdapter = new UserProfileAdapter(getActivity());
//        rvUserProfile.setAdapter(userPhotosAdapter);
//        setupUserProfileGrid();
//

//        TextView txt = (TextView) rootView.findViewById(R.id.page_number_label);
//        int page = getArguments().getInt(ARG_PAGE_NUMBER, -1);
//        txt.setText(String.format("Page %d", page));

        return rootView;
    }

        private void setupUserProfileGrid() {
        final StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        rvUserProfile.setLayoutManager(layoutManager);
        rvUserProfile.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                userPhotosAdapter.setLockedAnimations(true);
            }
        });
    }
}
