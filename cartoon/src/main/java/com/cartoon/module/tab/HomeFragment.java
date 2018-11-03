package com.cartoon.module.tab;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cartoon.data.AppVersion;
import com.cartoon.data.Disappear;
import com.cartoon.data.Keys;
import com.cartoon.module.BaseFragment;
import com.cartoon.module.action.ActionOneActivity;
import com.cartoon.module.home.CiYuanFragment;
import com.cartoon.module.home.JiNianFragment;
import com.cartoon.module.home.ReadNovelFragment;
import com.cartoon.module.home.RecommendFragment;
import com.cartoon.module.home.TrendsFragment;
import com.cartoon.module.newmodules.NewBaseActivity;
import com.cartoon.module.newmodules.NewBaseFragment;
import com.cartoon.module.tab.mine.MinePresenter;
import com.cartoon.module.tab.mine.MinePresenterImpl;
import com.cartoon.module.tab.mine.MineView;
import com.cartoon.utils.DensityUtil;
import com.cartoon.utils.EasySharePreference;
import com.cartoon.view.dialog.AppVersionDialog;
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
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import cn.com.xuanjiezhimen.BuildConfig;
import cn.com.xuanjiezhimen.R;
import cn.jpush.android.api.JPushInterface;

/**
 * 首页
 * <p/>
 * Created by David on 16/6/5.
 */
public class HomeFragment extends BaseFragment implements  ViewPager.OnPageChangeListener, MineView, View.OnClickListener/*,RecommendFragment.setFragmentPosition*/ {

    @BindView(R.id.indicator)
    MagicIndicator indicator;
   /* @BindView(R.id.iv_animal)
    public ImageView ivAnimal;*/

    @BindView(R.id.viewpager)
    ViewPager viewPager;

    @BindView(R.id.fab)
    ImageView mFAButton;

    private List<String>   titles;
    private List<Fragment> fragmentList;
   // private AnimationDrawable animationDrawable;
   private    MinePresenter  presenter;
    private boolean isTips=false;


    @Override
    protected int getLayoutId() {
        return R.layout.fg_home;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        //检查更新
        presenter = new MinePresenterImpl(this,isTips);
        presenter.checkUpdate();


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    //浮点
    @Subscribe
    public void onEvent(Disappear d) {

        mFAButton.setVisibility(View.VISIBLE);
    }

    private HomeFragmentAdapter adapter;

    @Override
    protected void initView(View view, Bundle savedInstanceState) {

        this.titles = new ArrayList<>();
        this.titles.add(getString(R.string.fg_trends));
        this.titles.add(getString(R.string.fg_read));
        this.titles.add(getString(R.string.fg_expound));
        this.titles.add(getString(R.string.fg_recommend));
        this.titles.add(getString(R.string.fg_qman));
        this.titles.add(getString(R.string.tab_activity));

        this.fragmentList = new ArrayList<>();


        fragmentList.add(new TrendsFragment());
        fragmentList.add(new ReadNovelFragment());
        fragmentList.add(new JiNianFragment());
        fragmentList.add(new RecommendFragment());
        fragmentList.add(new CiYuanFragment());
        fragmentList.add(new NewBaseFragment());

        adapter = new HomeFragmentAdapter(getChildFragmentManager());
        viewPager.setOffscreenPageLimit(fragmentList.size());
        viewPager.setAdapter(adapter);

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
//                    textView.setText("3");
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



//        ivEdit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (!CartoonApp.getInstance().isLogin(mActivity))
//                    return;
//
//                //startActivity(new Intent(mActivity, ComposeActivity.class));
//
//                SendPopWindow  p = new SendPopWindow(getContext());
//                p.showPopupWindow(ivEdit);
//            }
//        });

//        CartoonApp.getInstance().registerPlayListener(this);


        receiveJPushNotification();
        mFAButton.setOnClickListener(this);
    }

    private void receiveJPushNotification() {
        Bundle bundle = getActivity().getIntent().getExtras();
        if (null != bundle) {
            String content = bundle.getString(JPushInterface.EXTRA_EXTRA);
            if (TextUtils.isEmpty(content))
                return;

            Log.d("HomeFragment", "extra=" + content);

            int position = 0;

            try {
                JSONObject jsonObject = new JSONObject(content);
                position = jsonObject.getInt("type");
            } catch (JSONException e) {
                e.printStackTrace();
            }


            position -= 1;

            if (position < 0 || position > fragmentList.size())
                position = 0;
            final int finalPosition = position;
            viewPager.post(new Runnable() {
                @Override
                public void run() {
                    if (null != adapter) {
                        viewPager.setCurrentItem(finalPosition);
                    }
                }
            });
        }
    }



    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        indicator.onPageScrolled(position, positionOffset, positionOffsetPixels);
    }

    @Override
    public void onPageSelected(int position) {
        HashMap<String, String> map = new HashMap<>();
        map.put("type", "book");
        map.put("title", titles.get(position));
        MobclickAgent.onEvent(getContext(), "homefragemnt", map);

        indicator.onPageSelected(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        indicator.onPageScrollStateChanged(state);
    }

    @Override
    public void showLoadingView() {

    }

    @Override
    public void hideLoadingView() {

    }

    @Override
    public void updateNickName(String name) {

    }

    @Override
    public void updateGender(int gender) {

    }

    @Override
    public void showTips(String message) {

    }

    @Override
    public void updateAvatar(String avatarUrl) {

    }

    @Override
    public void logoutSuccess() {

    }

    @Override
    public void processUpdate(AppVersion appVersion) {
        float v_code = Float.parseFloat(appVersion.getVersion_code());
        if (v_code > BuildConfig.VERSION_CODE) {
            if (appVersion.getConpel() == 1) {
                forceUpdate(appVersion);
            } else {
                noticeUpdate(appVersion);
            }
        }
    }

    private void noticeUpdate(final AppVersion appVersion) {
        AppVersionDialog dialog = new AppVersionDialog(getContext(),appVersion.getVersion_code(),appVersion.getContent(),1);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setOnButtonClickListener(new AppVersionDialog.buttonClick() {
            @Override
            public void onButtonClickListener() {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(appVersion.getUrl()));
                startActivity(i);
            }
        });
        dialog.show();
    }

    //更新
    private void forceUpdate(final AppVersion appVersion) {
        AppVersionDialog dialog = new AppVersionDialog(getContext(),appVersion.getVersion_code(),appVersion.getContent(),2);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setOnButtonClickListener(new AppVersionDialog.buttonClick() {
            @Override
            public void onButtonClickListener() {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(appVersion.getUrl()));
                startActivity(i);
            }
        });
        dialog.show();


    }

    @Override
    public void onClick(View v) {
        String pushActionId = EasySharePreference.getPushActionId(getContext());
        if (pushActionId != null) {
            String[] split = pushActionId.split("#");
            if (split.length==2){
                if (split[1].equals("11")) {
                    Intent intent = new Intent(getContext(), ActionOneActivity.class);
                    intent.putExtra(Keys.TARGET_ID, split[0]);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(mActivity, NewBaseActivity.class);
                    intent.putExtra(Keys.TARGET_ID, split[0]);
                    startActivity(intent);
                }
            }
        }
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
