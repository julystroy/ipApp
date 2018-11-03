package com.cartoon.module;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.cartoon.CartoonApp;
import com.cartoon.data.EventNewMessage;
import com.cartoon.data.UserInfo;
import com.cartoon.data.response.BookFriendPost;
import com.cartoon.http.StaticField;
import com.cartoon.module.tab.ChangxianFragment;
import com.cartoon.module.tab.FanLiFragment;
import com.cartoon.module.tab.HomeFragment;
import com.cartoon.module.tab.XiuXingFragment;
import com.cartoon.module.tab.mine.MineFragment;
import com.cartoon.utils.EasySharePreference;
import com.cartoon.utils.StringUtils;
import com.cartoon.utils.UserDB;
import com.cartoon.utils.WebsocketUtil;
import com.cartton.library.utils.ToastUtils;
import com.jude.swipbackhelper.SwipeBackHelper;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.UMShareAPI;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.com.xuanjiezhimen.R;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 首页
 * <p/>
 * Created by David on 16/6/1.
 */
public class MainActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener {

    @BindView(R.id.viewpager)
     ViewPager            viewPager;
    @BindView(R.id.tab_bar)
     RadioGroup           mTabBar;
    @BindView(R.id.rb_mine)
    RadioButton          rbMine;
    @BindView(R.id.rb_home)
    RadioButton          rbHome;
    @BindView(R.id.rb_book)
    RadioButton          rbBook;
    @BindView(R.id.rb_activity)
    RadioButton          rbActivity;
    @BindView(R.id.iv_new_message)
    ImageView            ivNewMessage;

    @BindView(R.id.iv_book_message)
    ImageView            ivBookMessage;


    public List<Fragment> fragmentList;
    public List<String>   titleList;
    private int lastedActivityID = 0;
    public  int currtntPosition  = -1;

    private int positionNum;
    private long lastQuestionTime;
    private  String LastSystemTime;
    private int lastedBookID = 0;

    @Override
    protected int getContentViewId() {
        return R.layout.ac_main;

    }

    private void loadDown(){
        //模拟识别图资源下载
//        File datasetDat=new File(getExternalFilesDir("DataSets")+"/frxxz.dat");
//        if (!datasetDat.exists())Assets2Sd(this,"demo_dataset/frxxz.dat",datasetDat.getAbsolutePath());
//        File datasetXml=new File(getExternalFilesDir("DataSets")+"/frxxz.xml");
//        if (!datasetXml.exists())
//            Assets2Sd(this,"demo_dataset/frxxz.xml",datasetXml.getAbsolutePath());


        File datasetAss=new File(getExternalFilesDir("AssetsBundles")+"/frxxz/frxxz_yinyue.ass");
        if (!datasetAss.exists()) {
            datasetAss.getParentFile().mkdirs();
            Assets3Sd(this,"http://video.mopian.tv/frxxz_hanli.ass",getExternalFilesDir("AssetsBundles")+"/frxxz/frxxz_hanli.ass");
            Assets3Sd(this,"http://video.mopian.tv/frxxz_nangongwan.ass",getExternalFilesDir("AssetsBundles")+"/frxxz/frxxz_nangongwan.ass");
            Assets3Sd(this,"http://video.mopian.tv/frxxz_yinyue.ass",getExternalFilesDir("AssetsBundles")+"/frxxz/frxxz_yinyue.ass");
        }
    }
    public void Assets3Sd(final Context context, final String fileAssetPath, final String fileSdPath)
    {
        OkHttpClient mOkHttpClient = new OkHttpClient();
        final Request request = new Request.Builder().url(fileAssetPath).build();
        final Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream is = null;
                byte[] buf = new byte[2048];
                int len = 0;
                FileOutputStream fos = null;
                try {
                    long total = response.body().contentLength();
                    long current = 0;
                    is = response.body().byteStream();
                    fos = new FileOutputStream(fileSdPath);
                    while ((len = is.read(buf)) != -1) {
                        current += len;
                        fos.write(buf, 0, len);
                    }
                    fos.flush();
                    // System.out.println("@@下载成功");

                } catch (IOException e) {
                    // System.out.println("@@下载失败");
                } finally {
                    try {
                        if (is != null) {
                            is.close();
                        }
                        if (fos != null) {
                            fos.close();
                        }
                    } catch (IOException e) {

                    }
                }
            }

        });

    }
    @Override
    protected int getFragmentContentId() {
        return 0;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  SystemUtils.initSystemBar(this);
        EventBus.getDefault().register(this);
        SwipeBackHelper.getCurrentPage(this).setSwipeBackEnable(false);
        UserInfo userLastInfo = CartoonApp.getInstance().getUserLastInfo();
        if (userLastInfo!=null&&userLastInfo.getSectionId()!=null&&!userLastInfo.getSectionId().isEmpty()){
            UserDB.getInstance().buildSectDB();//创建宗门成员基础信息
        }

        fragmentList = new ArrayList<>();
        titleList = new ArrayList<>();
        titleList.add("主页");
        titleList.add("游戏尝鲜");
        titleList.add("书友圈");
        titleList.add("修行");
        titleList.add("我的");

        fragmentList.add(new HomeFragment());
        fragmentList.add(new ChangxianFragment());


        fragmentList.add(new FanLiFragment());
        //fragmentList.add(new NewBaseFragment());
        fragmentList.add(new XiuXingFragment());
        fragmentList.add(new MineFragment());

        viewPager.setOffscreenPageLimit(fragmentList.size());
        viewPager.setAdapter(new TabAdapter(getSupportFragmentManager()));
        mTabBar.setOnCheckedChangeListener(this);
        mTabBar.check(R.id.rb_home);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {


            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                MainActivity.this.currtntPosition = position;
                switch (position) {
                    case 0:
                        mTabBar.check(R.id.rb_home);
                        break;
                    case 1:
                        mTabBar.check(R.id.rb_changxian);
                        break;
                    case 3:
                        mTabBar.check(R.id.rb_activity);
                        break;
                    case 2:
                        mTabBar.check(R.id.rb_book);
                        if (lastedBookID > 0) {
                            CartoonApp.getInstance().getPreferences().edit().putInt(StaticField.LASTED_BOOKID, lastedBookID).commit();
                            EventBus.getDefault().post(new BookFriendPost(lastedBookID));
                        }
                        break;
                    case 4:
                        mTabBar.check(R.id.rb_mine);
                        break;
                }
                HashMap<String, String> map = new HashMap<>();
                map.put("type", "book");
                map.put("title", titleList.get(position));
                MobclickAgent.onEvent(MainActivity.this, "mainactivity", map);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        //initIntent();

        refreshSP();

        new Thread(new Runnable() {
            @Override
            public void run() {
                loadDown();
            }
        }).start();


    }



    //刷新时间
    private void refreshSP() {
        //第二天，信息处理
        lastQuestionTime = EasySharePreference.getLastQuestionTime(this);//每题开始答题时间
        LastSystemTime = StringUtils.getNowSystemTime(lastQuestionTime);
        positionNum = EasySharePreference.getPositionNum(this);//最新答题的位置
        String nowSystemTime = StringUtils.getNowSystemTime(System.currentTimeMillis());
        if (!TextUtils.equals(nowSystemTime, LastSystemTime)) {
            EasySharePreference.setLastQuestionTime(this,0);
            EasySharePreference.setPositionNum(this,0);
            EasySharePreference.setHaveAnswered(this,false);
            EasySharePreference.setAllQuestion(this,null);
            EasySharePreference.setQuestionPoint(this,null);

        }

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rb_home:
                viewPager.setCurrentItem(0);
                break;
            case R.id.rb_changxian:
                viewPager.setCurrentItem(1);
                break;
            case R.id.rl_xiuxing:
            case R.id.rb_activity:
                viewPager.setCurrentItem(3);
                break;
            case R.id.rb_book:
                viewPager.setCurrentItem(2);
                break;
            case R.id.rb_mine:
                viewPager.setCurrentItem(4);
                break;
        }
    }

    @OnClick({R.id.rb_changxian})
    public void onClickGame(){
        viewPager.setCurrentItem(1);
    }
    @OnClick({ R.id.rb_activity,R.id.rl_xiuxing})
    public void onClickActivity() {
        viewPager.setCurrentItem(3);
    }

    @OnClick({ R.id.rb_mine})
    public void onClickMine() {
        viewPager.setCurrentItem(4);
    }

    @OnClick({R.id.rb_book,R.id.rl_book})
    public void onClickBook(){viewPager.setCurrentItem(2);}





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
        UMShareAPI.get(this).release();
        EventBus.getDefault().unregister(this);

        ToastUtils.hideToast();
      //  UserDB.getInstance().deleteTable();//删除宗门用户基础信息
        MobclickAgent.onKillProcess(this);//保存umeng统计数据
        WebsocketUtil.getInstance().sendLeave();
       // WebSocketUtils.getInstance().stopHandler();
        super.onDestroy();
        System.exit(0);

    }



    @Subscribe
    public void onEvent(EventNewMessage eventNewMessage) {
        if (ivNewMessage == null)
            return;
        if (eventNewMessage.isShowNew) {
            ivNewMessage.setVisibility(View.VISIBLE);
        } else {
            ivNewMessage.setVisibility(View.GONE);
        }
    }





    //书有圈提示红点
    @Subscribe
    public void onEvent(BookFriendPost eventActionMessage) {
        if (ivBookMessage == null)
            return;
        if (eventActionMessage.id != CartoonApp.getInstance().getPreferences().getInt(StaticField.LASTED_BOOKID, -1)) {
            this.lastedBookID = eventActionMessage.id;
            if (currtntPosition != 2) {
                ivBookMessage.setVisibility(View.VISIBLE);
            }
        } else {
            ivBookMessage.setVisibility(View.GONE);
        }
    }



    // 双击退出功能实现
    private long exitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                WebsocketUtil.getInstance().sendLeave();
                UMShareAPI.get(this).release();

                onBackPressed();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
        //System.out.println("回调测试");分享回调
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        UMShareAPI.get(this).onSaveInstanceState(outState);
    }


}