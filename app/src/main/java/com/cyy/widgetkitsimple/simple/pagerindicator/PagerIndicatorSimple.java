package com.cyy.widgetkitsimple.simple.pagerindicator;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.cyy.widgetkitsimple.R;
import com.xstudy.pagerindicator.PagerIndicator;

/**
 * Created by cyy on 17/12/11.
 * <p>
 * Pager Indicator
 */

public class PagerIndicatorSimple extends FragmentActivity {

    protected PagerIndicator pagerIndicator;
    protected ViewPager viewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        super.setContentView(R.layout.activity_pager_indicator);
        initView();

        MyPagerAdapter adapter = new MyPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        pagerIndicator.setViewPager(viewPager);
    }

    private void initView() {
        pagerIndicator = (PagerIndicator) findViewById(R.id.pagerIndicator);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
    }

    class MyPagerAdapter extends FragmentPagerAdapter{

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return PagerFragment.newInstance(position+"" , null);
        }

        @Override
        public int getCount() {
            return 10;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return position+"";
        }
    }


}
