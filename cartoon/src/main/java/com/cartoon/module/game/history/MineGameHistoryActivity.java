package com.cartoon.module.game.history;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.cartoon.module.BaseActivity;
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
import cn.com.xuanjiezhimen.R;

/**
 * Created by wuchuchu on 2018/3/1.
 */

public class MineGameHistoryActivity extends BaseActivity implements ViewPager.OnPageChangeListener {
    @BindView(R.id.bt_left)
    ImageButton mBtLeft;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.indicator)
    MagicIndicator mIndicator;
    @BindView(R.id.viewpager)
    ViewPager mViewpager;
    private List<Fragment>       fragmentList;
    private List<String>         titles;

    private GameHistoryFragmentAdapter adapter;

    @Override
    protected int getContentViewId() {
        return R.layout.fg_mine_game_history;
    }

    @Override
    protected int getFragmentContentId() {
        return 0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupViews();
    }

    private void setupViews() {
        mTvTitle.setText(R.string.my_game_history);
        mBtLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        this.titles = new ArrayList<>();
        titles.add(getString(R.string.mgh_play));
        titles.add(getString(R.string.mgh_download_and_play));

        this.fragmentList = new ArrayList<>();
        Log.d("MineGame", "setupViews");
        GameHistoryPlayFragment play = new GameHistoryPlayFragment();
        play.setHistoryType(1);             //直接玩
        fragmentList.add(play);
        GameHistoryPlayFragment download = new GameHistoryPlayFragment();
        download.setHistoryType(2);         //边下边玩

        fragmentList.add(download);

        adapter = new GameHistoryFragmentAdapter(getSupportFragmentManager());
        mViewpager.setOffscreenPageLimit(fragmentList.size());
        mViewpager.setAdapter(adapter);
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
                        mViewpager.setCurrentItem(index);
                    }
                });
                badgePagerTitleView.setInnerPagerTitleView(colorTransitionPagerTitleView);

                return badgePagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                indicator.setMode(LinePagerIndicator.MODE_WRAP_CONTENT);
                indicator.setXOffset(15);
                List<String> colorList = new ArrayList<String>();
                colorList.add("#f85959");
                indicator.setColorList(colorList);
                return indicator;
            }
        });
        mIndicator.setNavigator(commonNavigator2);
        mViewpager.addOnPageChangeListener(this);
    }

    @Override
    public void onPageSelected(int position) {
        mViewpager.setCurrentItem(position);
        mIndicator.onPageSelected(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        mIndicator.onPageScrollStateChanged(state);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        mIndicator.onPageScrolled(position, positionOffset, positionOffsetPixels);
    }

    public class GameHistoryFragmentAdapter extends FragmentPagerAdapter {

        public GameHistoryFragmentAdapter(FragmentManager fragmentManager) {
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
