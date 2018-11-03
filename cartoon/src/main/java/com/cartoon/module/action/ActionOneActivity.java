package com.cartoon.module.action;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cartoon.CartoonApp;
import com.cartoon.data.EventPause;
import com.cartoon.data.Keys;
import com.cartoon.data.NewBase;
import com.cartoon.module.BaseActivity;
import com.cartoon.utils.ApiUtils;
import com.cartoon.utils.EasySharePreference;
import com.cartoon.utils.Utils;
import com.cartoon.view.indicator.BadgePagerTitleView;
import com.cartoon.view.indicator.ColorTransitionPagerTitleView;
import com.cartoon.view.indicator.CommonNavigator;
import com.cartoon.view.indicator.CommonNavigatorAdapter;
import com.cartoon.view.indicator.IPagerIndicator;
import com.cartoon.view.indicator.IPagerTitleView;
import com.cartoon.view.indicator.LinePagerIndicator;
import com.cartoon.view.indicator.MagicIndicator;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.com.xuanjiezhimen.R;

/**
 * Created by cc on 17-8-22.
 */
public class ActionOneActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener, View.OnClickListener {
    @BindView(R.id.bt_left)
    ImageButton    mBtLeft;
    @BindView(R.id.tv_title)
    TextView       mTvTitle;
    @BindView(R.id.bt_right)
    ImageButton    mBtRight;
    @BindView(R.id.rl_title)
    RelativeLayout mRlTitle;
    @BindView(R.id.viewpager)
    ViewPager      viewPager;
    @BindView(R.id.action_ruler)
    RadioButton    mActionRuler;
    @BindView(R.id.action_rank)
    RadioButton    mActionRank;
    @BindView(R.id.ll_comment)
    LinearLayout   mLlComment;
    @BindView(R.id.ll_nesbase)
    LinearLayout   mLlNesbase;
    @BindView(R.id.indicator)
    MagicIndicator mIndicator;
    @BindView(R.id.tab_bar)
    RadioGroup     mTabBar;
    @BindView(R.id.iv_show)
    ImageView      mIvShow;
    private List<Fragment> fragmentList;
    private List<String>         titles;
    private    NewBase        newbase;
    private String         target_id;
    @Override
    protected int getContentViewId() {
        return R.layout.ac_action_one;
    }

    @Override
    protected int getFragmentContentId() {
        return 0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);

        newbase = getIntent().getParcelableExtra(Keys.TARGET_OBJECT);
        target_id = getIntent().getStringExtra(Keys.TARGET_ID);
        EasySharePreference.setActionId(ActionOneActivity.this,target_id);//记录进入活动的id

        mBtLeft.setImageResource(R.mipmap.icon_back_black);
        mBtLeft.setOnClickListener(this);
        mBtRight.setImageResource(R.mipmap.icon_share);
        mBtRight.setOnClickListener(this);
        mTvTitle.setText("活动规则");

        this.titles = new ArrayList<>();
        titles.add("最热");
        titles.add("最新");

        fragmentList = new ArrayList<>();
        fragmentList.add(new WebviewFragment());
        fragmentList.add(new RankFragment());
        fragmentList.add(new NewRankFragment());

        viewPager.setOffscreenPageLimit(fragmentList.size());
        viewPager.setAdapter(new TabAdapter(getSupportFragmentManager()));
        mTabBar.setOnCheckedChangeListener(this);
        mTabBar.check(R.id.action_ruler);


        // mIndicator.setViewPager(mViewpager);
        final CommonNavigator commonNavigator2 = new CommonNavigator(ActionOneActivity.this);
        commonNavigator2.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return titles == null ? 0 : titles.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                BadgePagerTitleView badgePagerTitleView = new BadgePagerTitleView(context);
                ColorTransitionPagerTitleView colorTransitionPagerTitleView = new ColorTransitionPagerTitleView(context);
                colorTransitionPagerTitleView.setText(titles.get(index));
                colorTransitionPagerTitleView.setTextSize(16);
                colorTransitionPagerTitleView.setNormalColor(Color.parseColor("#ce939393"));
                colorTransitionPagerTitleView.setSelectedColor(Color.parseColor("#ce222222"));
                colorTransitionPagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewPager.setCurrentItem(index+1);
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

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {


            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (position>0)
                mIndicator.onPageScrolled(position-1, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        mTabBar.check(R.id.action_ruler);
                        mTvTitle.setVisibility(View.VISIBLE);
                        mIndicator.setVisibility(View.GONE);
                        break;
                    case 1:
                        mTabBar.check(R.id.action_rank);
                        mTvTitle.setVisibility(View.GONE);
                        mIndicator.setVisibility(View.VISIBLE);
                        mIndicator.onPageSelected(0);
                        break;
                    case 2:
                        mTabBar.check(R.id.action_rank);
                        mIndicator.onPageSelected(1);
                        mTvTitle.setVisibility(View.GONE);
                        mIndicator.setVisibility(View.VISIBLE);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                mIndicator.onPageScrollStateChanged(state);
            }
        });

        mIvShow.setOnClickListener(this);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.action_ruler:
                viewPager.setCurrentItem(0);
                mTvTitle.setVisibility(View.VISIBLE);
                mIndicator.setVisibility(View.GONE);
                break;
            case R.id.action_rank:
                viewPager.setCurrentItem(1);
                mIndicator.onPageSelected(0);
                mTvTitle.setVisibility(View.GONE);
                mIndicator.setVisibility(View.VISIBLE);
                break;
        }
    }

    @OnClick({R.id.action_rank})
    public void onClickActivity() {
        viewPager.setCurrentItem(1);
        mIndicator.onPageSelected(0);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_left:
                onBackPressed();
                break;
            case R.id.bt_right:
                if (!CartoonApp.getInstance().isLogin(ActionOneActivity.this)) {
                    return;
                }
                if (newbase!=null)
                ApiUtils.share(this, newbase.getTitle(), Utils.addBaseUrlShare(newbase.getCover_pic()), "活动规则", target_id);
                else
                    ApiUtils.share(this, "精彩活动", null, "活动规则", target_id);
                break;
            case R.id.iv_show:
                if (CartoonApp.getInstance().isLogin(ActionOneActivity.this))
                    startActivity(new Intent(this, ActionNoteActivity.class));
                break;
        }
    }


    public class TabAdapter extends FragmentPagerAdapter {

        public TabAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position % fragmentList.size());
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onEvent(EventPause b) {
        mIvShow.setVisibility(View.GONE);
    }
}
