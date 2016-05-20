package com.jinguanguke.guwangjinlai.ui.viewholder;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.view.ViewGroup;

import com.jinguanguke.guwangjinlai.R;
import com.jinguanguke.guwangjinlai.ui.fragment.AccountFragment;
import com.jinguanguke.guwangjinlai.ui.fragment.PageFragment;
import com.jinguanguke.guwangjinlai.ui.fragment.SettingsFragment;


public class TabsPagerAdapter extends FragmentPagerAdapter {
    FragmentManager fm = null;
    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
        this.fm = fm;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        switch (position)
        {
            case 0:
            {
                return PageFragment.newInstance(position + 1);

            }
//            case 1:
//            {
//                return new SettingsFragment();
//
//            }
            default:
            {
                return new AccountFragment();
            }
        }

    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position)
        {
            case 0:
            {
                return "我的视频";
            }
//            case 1:
//            {
//                return "我的推荐";
//            }
            default:
            {
                return "设置";
            }
        }

    }


}
