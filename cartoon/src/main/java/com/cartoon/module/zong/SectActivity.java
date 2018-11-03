package com.cartoon.module.zong;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.cartoon.CartoonApp;
import com.cartoon.data.BuildSectData;
import com.cartoon.data.EventMessageCount;
import com.cartoon.data.Keys;
import com.cartoon.data.PublicRes;
import com.cartoon.data.SectUi;
import com.cartoon.data.UserInfo;
import com.cartoon.data.chat.SectChat;
import com.cartoon.data.response.SectListRes;
import com.cartoon.http.BaseCallBack;
import com.cartoon.http.BuilderInstance;
import com.cartoon.http.StaticField;
import com.cartoon.module.BaseActivity;
import com.cartoon.module.login.LicenseActivity;
import com.cartoon.module.tab.adapter.SectListAdpter;
import com.cartoon.utils.CollectionUtils;
import com.cartoon.utils.UserDB;
import com.cartoon.view.CustomItemDecoration;
import com.cartoon.view.DialogToast;
import com.cartoon.view.dialog.SectDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.com.xuanjiezhimen.R;
import cndroid.com.smoothendlesslibrary.EndLessListener;
import cndroid.com.smoothendlesslibrary.EndLessRecyclerView;

/**
 * Created by cc on 17-9-25.
 * 宗门
 */
public class SectActivity extends BaseActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener, EndLessListener {

    public List<Fragment> fragmentList;
    public List<String>   titleList;
    @BindView(R.id.bt_left)
    ImageButton         mBtLeft;
    @BindView(R.id.tv_title)
    TextView            mTvTitle;
    @BindView(R.id.bt_right)
    ImageButton         mBtRight;
    @BindView(R.id.rl_title)
    RelativeLayout      mRlTitle;
    @BindView(R.id.viewpager)
    ViewPager           mViewpager;
    @BindView(R.id.sect_task)
    RadioButton         mSectTask;
    @BindView(R.id.sect_mine)
    RadioButton         mSectMine;
    @BindView(R.id.iv_new_message)
    ImageView           mIvNewMessage;
    @BindView(R.id.sect_event)
    RadioButton         mSectEvent;
    @BindView(R.id.sect_fight)
    RadioButton         mSectFight;
    @BindView(R.id.rl_event)
    RelativeLayout      mRlEvent;
    @BindView(R.id.tab_bar)
    RadioGroup          mTabBar;
    @BindView(R.id.recycleview)
    EndLessRecyclerView mRecycleview;
    @BindView(R.id.tv_build_sect)
    TextView            mTvBuildSect;
    @BindView(R.id.fl_sect_build)
    FrameLayout         mFlSectBuild;
    @BindView(R.id.fl_sect_show)
    FrameLayout         mFlSectShow;
    @BindView(R.id.chat)
    ImageView ivChat;
    @BindView(R.id.chat_num)
    TextView tvChatNum;

    private  boolean red =true;
    private SectListAdpter adapter;
    @Override
    protected int getContentViewId() {
        return R.layout.activity_sect;
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
        boolean join = getIntent().getBooleanExtra("join", false);// FIXME: 17-10-13 是否参加过宗门
        String sectionId = getIntent().getStringExtra("sectionId");

        initView();


        if (sectionId == null || sectionId.isEmpty()) {
            //创建宗门界面
            BuildSectUi();
        } else {

            //已参加过宗门
            SectUi(join);
            if (join)
            loadMsseage(sectionId);
        }
    }

    private void showRedPoint(boolean red) {
        if (red)
            mIvNewMessage.setVisibility(View.VISIBLE);
        else
            mIvNewMessage.setVisibility(View.GONE);
    }

    private void SectUi(boolean join) {
        mFlSectShow.setVisibility(View.VISIBLE);
        mFlSectBuild.setVisibility(View.GONE);
        SectChat sectChat = UserDB.getInstance().querySect(CartoonApp.getInstance().getUserId());
        if (sectChat==null){//刚加入宗门时创建表判断
            UserDB.getInstance().buildSectDB();

        }

        fragmentList = new ArrayList<>();
        titleList = new ArrayList<>();
        titleList.add("我的宗门");
        titleList.add("宗门任务");
        titleList.add("宗门战");


        fragmentList.add(new MineSectFragment());
        fragmentList.add(new SectTaskFragment());
        fragmentList.add(new SectFightFragment());
        if (join) {
            mRlEvent.setVisibility(View.VISIBLE);
            titleList.add("宗门事务");
            fragmentList.add(new EventSectFragment());
        } else {
            mRlEvent.setVisibility(View.GONE);
        }

        mViewpager.setOffscreenPageLimit(fragmentList.size());
        mViewpager.setAdapter(new TabAdapter(getSupportFragmentManager()));
        mTabBar.setOnCheckedChangeListener(this);
        mTabBar.check(R.id.sect_mine);
        mViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 1:
                        mTabBar.check(R.id.sect_task);
                        break;
                    case 0:
                        mTabBar.check(R.id.sect_mine);
                        break;
                    case 2:
                        mTabBar.check(R.id.sect_fight);
                        break;
                    case 3:
                        mTabBar.check(R.id.sect_event);
                        if (red)
                            showRedPoint(false);
                        break;

                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private void loadMsseage(String sectionId) {
        BuilderInstance.getInstance().getPostBuilderInstance(StaticField.URL_SECT_APPLYMESSAGE)
                .addParams("sectionId",sectionId)
                .build().execute(new BaseCallBack<PublicRes>() {
            @Override
            public void onLoadFail() {
            }
            @Override
            public void onContentNull() {
            }
            @Override
            public void onLoadSuccess(PublicRes response) {
                if (response != null) {
                    red = !response.getApplyMsg().equals("true");
                    showRedPoint(red);
                }
            }
            @Override
            public PublicRes parseNetworkResponse(String response) throws Exception {
                return JSON.parseObject(response,PublicRes.class);
            }
        });

    }

    public void BuildSectUi(){
        mFlSectShow.setVisibility(View.GONE);
        mFlSectBuild.setVisibility(View.VISIBLE);
        adapter = new SectListAdpter(SectActivity.this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecycleview.setLayoutManager(layoutManager);
        mRecycleview.addItemDecoration(new CustomItemDecoration((int) getResources().getDimension(R.dimen.dp3), 1));
        mRecycleview.setAdapter(adapter);
        mRecycleview.setEndLessListener(this);
        //宗门请求
        loadSect(START_PAGE);
    }

    private void loadSect(final int page) {
        BuilderInstance.getInstance().getPostBuilderInstance(StaticField.URL_SECT_SECTLIST)
                .addParams(Keys.PAGE_NUM, String.valueOf(page))
                .addParams(Keys.PAGE_SIZE, String.valueOf(PAGE_SIZE))
                .build().execute(new BaseCallBack<SectListRes>() {
            @Override
            public void onLoadFail() {
            }
            @Override
            public void onContentNull() {
            }
            @Override
            public void onLoadSuccess(SectListRes response) {


                if (response != null) {
                    setupLoadMoreList(response);
                    if (page==START_PAGE)
                    adapter.setData(response.getList());
                   else
                        adapter.addData(response.getList());
                }

                mRecycleview.completeLoadMore();
            }
            @Override
            public SectListRes parseNetworkResponse(String response) throws Exception {
                return JSON.parseObject(response,SectListRes.class);
            }
        });
    }
    private void setupLoadMoreList(SectListRes response) {

        if (CollectionUtils.isEmpty(response.getList())
                || response.getList().size() < PAGE_SIZE) {
            mRecycleview.setEndLessListener(null);
        } else {
            mRecycleview.setEndLessListener(this);
        }
    }
    private void initView() {
        mTvTitle.setText("宗门");
        mBtRight.setImageResource(R.mipmap.ic_zong_ruler);
        mBtLeft.setImageResource(R.mipmap.icon_back_black);
        mBtLeft.setOnClickListener(this);
        ivChat.setOnClickListener(this);
        int count = ChatClients.getCount(false);
        if (count>1){
            tvChatNum.setVisibility(View.VISIBLE);
            if (count>99)
            tvChatNum.setText("99+");
            else
                tvChatNum.setText(String.valueOf(count-1));
        }else{
            tvChatNum.setVisibility(View.GONE);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_left:
                onBackPressed();
                break;
            case R.id.chat:
                tvChatNum.setVisibility(View.GONE);
                startActivity(new Intent(SectActivity.this,SectChatActivity.class));
                break;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.sect_task:
                mViewpager.setCurrentItem(1);
                break;
            case R.id.sect_mine:
                mViewpager.setCurrentItem(0);
                break;
            case R.id.sect_fight:
                mViewpager.setCurrentItem(2);
                break;
            case R.id.rl_event:
            case R.id.sect_event:
                mViewpager.setCurrentItem(3);
                break;

        }
    }


    @OnClick({R.id.sect_task})
    public void onClickActivity() {
        mViewpager.setCurrentItem(1);
    }
    @OnClick({R.id.sect_fight})
    public void onClickActivity2() {
        mViewpager.setCurrentItem(2);
    }

    @OnClick(R.id.bt_right)
    public void onclicBUtton() {
        Intent intent = new Intent(this, LicenseActivity.class);
        intent.putExtra("type", 1);
        startActivity(intent);
    }
    @OnClick(R.id.tv_build_sect)
    public void buildSect(){
        showLoadingDialog();
        BuilderInstance.getInstance().getPostBuilderInstance(StaticField.URL_SECT_CANCREAT)
                .build().execute(new BaseCallBack<String>() {
            @Override
            public void onLoadFail() {
                hideLoadingDialog();
            }

            @Override
            public void onContentNull() {

            }

            @Override
            public void onLoadSuccess(String response) {
                hideLoadingDialog();
                SectDialog dialog = new SectDialog(SectActivity.this);
                dialog.setCanceledOnTouchOutside(true);
                dialog.setOnBuildSectClickListner(new SectDialog.BuildSectClickListener() {
                    @Override
                    public void buildSectLisener(String et) {
                        showLoadingDialog();
                        BuilderInstance.getInstance().getPostBuilderInstance(StaticField.URL_SECT_SECTBUILD)
                                .addParams("sectionName",et)
                                .build().execute(new BaseCallBack<BuildSectData>() {
                            @Override
                            public void onLoadFail() {
                                hideLoadingDialog();
                                //   DialogToast.createToastConfig().ToastShow(SectActivity.this,"灵石不足或者请刷新进入",4);
                                finish();
                            }

                            @Override
                            public void onContentNull() {

                            }

                            @Override
                            public void onLoadSuccess(BuildSectData response) {
                                hideLoadingDialog();
                                DialogToast.createToastConfig().ToastShow(SectActivity.this,response.getMsg().toString(),3);
                                UserInfo userLastInfo = CartoonApp.getInstance().getUserLastInfo();
                                userLastInfo.setSectionId(String.valueOf(response.getSect_id()));
                                userLastInfo.setManager(true);
                                CartoonApp.getInstance().setUserLastInfo(userLastInfo);
                               // UserDB.getInstance().buildSectDB();//创建宗门成员基础信息
                                SectUi(true);
                            }

                            @Override
                            public BuildSectData parseNetworkResponse(String response) throws Exception {
                                return JSON.parseObject(response,BuildSectData.class);
                            }
                        });
                    }
                });
                dialog.show();
            }

            @Override
            public String parseNetworkResponse(String response) throws Exception {
                return response;
            }
        });

    }


    @OnClick({R.id.rl_event, R.id.sect_event})
    public void onClickBook() {
        mViewpager.setCurrentItem(3);
        if (red)
            showRedPoint(false);
    }

    @Override
    public void onLoadMoreData(int i) {
        loadSect(i);
    }


    public class TabAdapter extends FragmentPagerAdapter {

        public TabAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
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

    //浮点
    @Subscribe
    public void onEvent(SectUi d) {
        BuildSectUi();
    }

    @Subscribe
    public void onEvent(final EventMessageCount count){
        if (count != null) {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    tvChatNum.setVisibility(View.VISIBLE);
                    if (count.count==99)
                        tvChatNum.setText("99+");
                    else
                        tvChatNum.setText(String.valueOf(count.count-1));
                }
            });

        }

    }

}
