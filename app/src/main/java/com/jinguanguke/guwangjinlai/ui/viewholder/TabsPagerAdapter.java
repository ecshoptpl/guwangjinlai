package com.jinguanguke.guwangjinlai.ui.viewholder;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.jinguanguke.guwangjinlai.R;
import com.jinguanguke.guwangjinlai.ui.fragment.AccountFragment;
import com.jinguanguke.guwangjinlai.ui.fragment.PageFragment;


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
            case 1:
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
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position)
        {
            case 0:
            {
                return "看过的";

            }
            case 1:
            {
                return "发布的";
            }
            default:
            {
                return "设置";
            }
        }

    }
}
