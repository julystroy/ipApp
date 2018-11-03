package com.cartoon.module.tab;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.cartoon.CartoonApp;
import com.cartoon.module.BaseFragment;
import com.cartoon.module.tab.bookfriendmoment.BookFragment;
import com.cartoon.utils.DensityUtil;
import com.cartoon.view.SendPopWindow;
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
 * Created by cc on 17-7-25.
 */
public class FanLiFragment extends BaseFragment implements View.OnClickListener, ViewPager.OnPageChangeListener {
   /* @BindView(R.id.bt_left)
    ImageButton    mBtLeft;*/
    @BindView(R.id.indicator)
   MagicIndicator mIndicator;
    @BindView(R.id.bt_right)
    ImageButton   mBtRight;
    @BindView(R.id.viewpager)
    ViewPager     mViewpager;
    private List<Fragment>       fragmentList;
    private List<String>         titles;

  //  private AnimationDrawable    animationDrawable;

    private FanLiFragmentAdapter adapter;
    @Override
    protected int getLayoutId() {
        return R.layout.fg_fanli;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        this.titles = new ArrayList<>();
        titles.add("饭粒说");
        titles.add("书评");

        this.fragmentList = new ArrayList<>();
        fragmentList.add(new BookFragment());
        fragmentList.add(new BookCommentFragment());

        adapter = new FanLiFragmentAdapter(getChildFragmentManager());
        mViewpager.setOffscreenPageLimit(fragmentList.size());
        mViewpager.setAdapter(adapter);
       // mIndicator.setViewPager(mViewpager);
        final CommonNavigator commonNavigator2 = new CommonNavigator(getContext());
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

        mBtRight.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (!CartoonApp.getInstance().isLogin(mActivity))
            return;
        SendPopWindow pw = new SendPopWindow(getContext());
        pw.showPopupWindow(v);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        mIndicator.onPageScrolled(position, positionOffset, positionOffsetPixels);
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

    public class FanLiFragmentAdapter extends FragmentPagerAdapter {

        public FanLiFragmentAdapter(FragmentManager fragmentManager) {
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
