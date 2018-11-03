package com.cartoon.module.tab.mine;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cartoon.module.BaseActivity;
import com.cartoon.module.tab.rank.SectRankFragment;
import com.cartoon.module.tab.rank.SelfRankFragment;
import com.cartoon.utils.DensityUtil;
import com.cartoon.view.indicator.BadgeAnchor;
import com.cartoon.view.indicator.BadgePagerTitleView;
import com.cartoon.view.indicator.BadgeRule;
import com.cartoon.view.indicator.ColorTransitionPagerTitleView;
import com.cartoon.view.indicator.CommonNavigator;
import com.cartoon.view.indicator.CommonNavigatorAdapter;
import com.cartoon.view.indicator.IPagerIndicator;
import com.cartoon.view.indicator.IPagerTitleView;
import com.cartoon.view.indicator.LinePagerIndicator;
import com.cartoon.view.indicator.MagicIndicator;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.com.xuanjiezhimen.R;

/**
 * Created by cc on 17-1-3.
 */
public class ChartsActivity extends BaseActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {

    @BindView(R.id.bt_left)
    ImageView      mBtLeft;
    @BindView(R.id.indicator)
    MagicIndicator indicator;
    @BindView(R.id.rl_title)
    RelativeLayout mRlTitle;
    @BindView(R.id.viewpager)
    ViewPager      viewPager;


    private List<Fragment> fragmentList;
    private List<String>   titles;
    private HomeFragmentAdapter adapter;
    @Override
    protected int getContentViewId() {
        return R.layout.activity_rank;
    }

    @Override
    protected int getFragmentContentId() {
        return 0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        setupView();

    }



    private void setupView() {
        this.titles = new ArrayList<>();
        this.titles.add("宗门排行");
        this.titles.add("个人排行");
        this.fragmentList = new ArrayList<>();
        fragmentList.add(new SectRankFragment());
        fragmentList.add(new SelfRankFragment());



        adapter = new HomeFragmentAdapter(getSupportFragmentManager());
        viewPager.setOffscreenPageLimit(fragmentList.size());
        viewPager.setAdapter(adapter);

        final CommonNavigator commonNavigator2 = new CommonNavigator(this);
        commonNavigator2.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return titles == null ? 0 : titles.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                BadgePagerTitleView badgePagerTitleView = new BadgePagerTitleView(context);
                if (index == 3) {
                    TextView textView = (TextView) LayoutInflater.from(context).inflate(R.layout.simple_count_badge_layout, null);
                    textView.setText("3");
                    badgePagerTitleView.setBadgeView(textView);
                    badgePagerTitleView.setXBadgeRule(new BadgeRule(BadgeAnchor.CONTENT_RIGHT, -DensityUtil.dip2px(context, 6)));
                    badgePagerTitleView.setYBadgeRule(new BadgeRule(BadgeAnchor.CONTENT_TOP, 0));
                }

                ColorTransitionPagerTitleView colorTransitionPagerTitleView = new ColorTransitionPagerTitleView(context);
                colorTransitionPagerTitleView.setText(titles.get(index));
                colorTransitionPagerTitleView.setTextSize(16);

                colorTransitionPagerTitleView.setNormalColor(Color.parseColor("#ce939393"));
                colorTransitionPagerTitleView.setSelectedColor(Color.parseColor("#ce222222"));
                colorTransitionPagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewPager.setCurrentItem(index);
                    }
                });
                badgePagerTitleView.setInnerPagerTitleView(colorTransitionPagerTitleView);

                return badgePagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                indicator.setMode(LinePagerIndicator.MODE_WRAP_CONTENT);
                indicator.setXOffset(25);
                List<String> colorList = new ArrayList<String>();
                colorList.add("#f85959");
                indicator.setColorList(colorList);
                return indicator;
            }
        });
        indicator.setNavigator(commonNavigator2);
        // indicator.setViewPager(viewPager);
        viewPager.addOnPageChangeListener(this);

        mBtLeft.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_left:
                onBackPressed();
                break;

        }
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        indicator.onPageScrolled(position, positionOffset, positionOffsetPixels);
    }

    @Override
    public void onPageSelected(int position) {
        indicator.onPageSelected(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        indicator.onPageScrollStateChanged(state);
    }

    public class HomeFragmentAdapter extends FragmentPagerAdapter {

        public HomeFragmentAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public int getCount() {
            return fragmentList == null ? 0 : fragmentList.size();
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position % titles.size());
        }

    }

}
