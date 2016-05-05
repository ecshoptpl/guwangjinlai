package com.jinguanguke.guwangjinlai.ui.fragment;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import butterknife.Bind;
import com.jinguanguke.guwangjinlai.R;
import com.smartydroid.android.starter.kit.app.StarterKitApp;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;


public class FeedsFragment extends BaseFragment {

  @Bind(R.id.view_pager) ViewPager mViewPager;
  @Bind(R.id.tabs) TabLayout mTabLayout;
  @Bind(R.id.toolbar) Toolbar mToolbar;

  @Bind(R.id.toolbar_title)
  TextView mToolbar_title;

  private Adapter mAdapter;

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mAdapter = new Adapter(getChildFragmentManager());

    Resources r = StarterKitApp.appResources();

    Bundle args = new Bundle();
    VideoFragment videoFragment = new VideoFragment();
    videoFragment.setArguments(args);
    mAdapter.addFragment(videoFragment, "全部");


    LectureFragment lectureFragment = new LectureFragment();
    mAdapter.addFragment(lectureFragment, "医生讲座");


    MovieFragment movieFragment = new MovieFragment();
    mAdapter.addFragment(movieFragment, "微视频");



   // mAdapter.addFragment(new VideoFragment(), r.getString(R.string.feed_without));
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    mToolbar.setTitle("");
    mToolbar_title.setText(R.string.app_name);
    mViewPager.setAdapter(mAdapter);

    final int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4,
        StarterKitApp.appResources().getDisplayMetrics());
    mViewPager.setPageMargin(pageMargin);
    mTabLayout.setupWithViewPager(mViewPager);
  }

  // http://stackoverflow.com/questions/15207305/getting-the-error-java-lang-illegalstateexception-activity-has-been-destroyed
  @Override public void onDetach() {
    super.onDetach();
    try {
      Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
      childFragmentManager.setAccessible(true);
      childFragmentManager.set(this, null);
    } catch (NoSuchFieldException e) {
      throw new RuntimeException(e);
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    }
  }

  @Override protected int getFragmentLayout() {
    return R.layout.fragment_home;
  }

  static class Adapter extends FragmentPagerAdapter {
    private final List<Fragment> mFragments = new ArrayList<>();
    private final List<String> mFragmentTitles = new ArrayList<>();

    public Adapter(FragmentManager fm) {
      super(fm);
    }

    public void addFragment(Fragment fragment, String title) {
      mFragments.add(fragment);
      mFragmentTitles.add(title);
    }

    @Override public Fragment getItem(int position) {
      return mFragments.get(position);
    }

    @Override public int getCount() {
      return mFragments.size();
    }

    @Override public CharSequence getPageTitle(int position) {
      return mFragmentTitles.get(position);
    }
  }

}
