package com.jinguanguke.guwangjinlai.ui.fragment;

import android.content.pm.ApplicationInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.jinguanguke.guwangjinlai.R;
import com.jinguanguke.guwangjinlai.ui.viewholder.SettingsAdapter;
import com.jinguanguke.guwangjinlai.ui.viewholder.UserProfileAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by jin on 16/5/11.
 */
public class SettingsFragment extends Fragment {
    private List<ApplicationInfo> mAppList;
    private AppAdapter mAdapter;

    @Bind(R.id.recycler_view)
    RecyclerView fr_settings;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mListView = inflater.inflate(R.layout.fragment_settings_layout, container, false);
        ButterKnife.bind(this, mListView);
        fr_settings.setLayoutManager(new LinearLayoutManager(getActivity()));
       // mAdapter = new SettingsRecylerAdapter(getActivity());
       // fr_settings.setAdapter(mAdapter);

        mAppList = getActivity().getPackageManager().getInstalledApplications(0);














//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
//        fr_settings.setLayoutManager(linearLayoutManager);
//        fr_settings.setHasFixedSize(true);
//
//        String[] listItems = {"用户协议","用户积分","清除缓存","退出"};
////        String[] listItems = mItemData.split(" ");
//        List<String> list = new ArrayList<String>();
//        Collections.addAll(list, listItems);
//        SettingsAdapter settingsAdapter = new SettingsAdapter(list);
//        fr_settings.setAdapter(settingsAdapter);
       // setupUserProfileGrid();
//

//        TextView txt = (TextView) rootView.findViewById(R.id.page_number_label);
//        int page = getArguments().getInt(ARG_PAGE_NUMBER, -1);
//        txt.setText(String.format("Page %d", page));

        return mListView;
    }

    class AppAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mAppList.size();
        }

        @Override
        public ApplicationInfo getItem(int position) {
            return mAppList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(getActivity().getApplicationContext(),
                        R.layout.item_list_app, null);
                new ViewHolder(convertView);
            }
            ViewHolder holder = (ViewHolder) convertView.getTag();
            ApplicationInfo item = getItem(position);
            holder.iv_icon.setImageDrawable(item.loadIcon(getActivity().getPackageManager()));
            holder.tv_name.setText(item.loadLabel(getActivity().getPackageManager()));
            holder.iv_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getActivity(), "iv_icon_click", Toast.LENGTH_SHORT).show();
                }
            });
            holder.tv_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getActivity(),"iv_icon_click", Toast.LENGTH_SHORT).show();
                }
            });
            return convertView;
        }

        class ViewHolder {
            ImageView iv_icon;
            TextView tv_name;

            public ViewHolder(View view) {
                iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
                tv_name = (TextView) view.findViewById(R.id.tv_name);
                view.setTag(this);
            }
        }

       // @Override
        public boolean getSwipEnableByPosition(int position) {
            if(position % 2 == 0){
                return false;
            }
            return true;
        }
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }
}
