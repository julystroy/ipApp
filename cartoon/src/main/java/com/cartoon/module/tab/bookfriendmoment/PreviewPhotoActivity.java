package com.cartoon.module.tab.bookfriendmoment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;

import com.cartoon.module.BaseActivity;
import com.cartoon.view.CustomViewPager;
import com.jude.swipbackhelper.SwipeBackHelper;
import com.viewpagerindicator.CirclePageIndicator;

import butterknife.BindView;
import cn.com.xuanjiezhimen.R;

/**
 * Created by jinbangzhu on 7/23/16.
 */

public class PreviewPhotoActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.viewPager)
    CustomViewPager viewPager;
    @BindView(R.id.indicator)
    CirclePageIndicator indicator;

    public static final String PHOTO_URLS = "u";
    public static final String POSITION = "p";
    String[] list;

    @Override
    protected int getContentViewId() {
        return R.layout.preview_photo;
    }

    @Override
    protected int getFragmentContentId() {
        return 0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        list = getIntent().getStringArrayExtra(PHOTO_URLS);
        int position = getIntent().getIntExtra(POSITION, 0);

        PhotoAdapter adapter = new PhotoAdapter(getSupportFragmentManager());
        viewPager.setCanTouchScroll(true);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(position);


        SwipeBackHelper.getCurrentPage(PreviewPhotoActivity.this).setSwipeEdgePercent(0.2f);

        indicator.setViewPager(viewPager);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_left:
                onBackPressed();
                break;
        }
    }


    class PhotoAdapter extends FragmentPagerAdapter {

        public PhotoAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return PhotoFragment.getNewInstance(list[position]);
        }

        @Override
        public int getCount() {
            return list.length ==0?0:list.length;
        }
    }
}
