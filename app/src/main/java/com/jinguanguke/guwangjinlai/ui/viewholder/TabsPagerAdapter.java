package com.jinguanguke.guwangjinlai.ui.viewholder;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.jinguanguke.guwangjinlai.R;
import com.jinguanguke.guwangjinlai.ui.fragment.AccountFragment;
import com.jinguanguke.guwangjinlai.ui.fragment.PageFragment;
import com.jinguanguke.guwangjinlai.ui.fragment.SettingsFragment;


public class TabsPagerAdapter extends FragmentPagerAdapter {

    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
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
            default:
            {
                return "设置";
            }
        }

    }
}
