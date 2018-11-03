package com.cartoon.module.zong;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.alibaba.fastjson.JSON;
import com.cartoon.CartoonApp;
import com.cartoon.data.CardItem;
import com.cartoon.data.DayQuestionAnswer;
import com.cartoon.data.ResultGet;
import com.cartoon.data.UserInfo;
import com.cartoon.http.BaseCallBack;
import com.cartoon.http.BuilderInstance;
import com.cartoon.http.StaticField;
import com.cartoon.module.BaseActivity;
import com.cartoon.module.listener.ResponseListener;
import com.cartoon.module.login.LicenseActivity;
import com.cartoon.module.question.ShadowTransformer;
import com.cartoon.module.tab.adapter.SectFightAdapter;
import com.cartoon.utils.EasySharePreference;
import com.cartoon.utils.NetworkUtils;
import com.cartoon.utils.SendToBG;
import com.cartoon.utils.StringUtils;
import com.cartoon.view.MyViewPage;
import com.cartton.library.utils.ToastUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.com.xuanjiezhimen.R;

/**
 * Created by cc on 18-1-11.
 */
public class FightActivity extends BaseActivity implements SectFightAdapter.nextQuestionListener, View.OnClickListener {
    @BindView(R.id.bt_left)
    ImageButton    mBtLeft;
    @BindView(R.id.tv_title)
    TextView       mTvTitle;
    @BindView(R.id.bt_right)
    ImageButton    mBtRight;
    @BindView(R.id.rl_title)
    RelativeLayout mRlTitle;
    @BindView(R.id.progressBar)
    ProgressBar    mProgressBar;
    @BindView(R.id.now_stone)
    TextView       mNowStone;
    @BindView(R.id.get_stone)
    TextView       mGetStone;
    @BindView(R.id.get_bonus)
    TextView       mGetBonus;
    @BindView(R.id.ll_stone)
    LinearLayout   mLlStone;
    @BindView(R.id.viewPager)
    MyViewPage     mViewPager;
    @BindView(R.id.fl_question)
    FrameLayout    mFlQuestion;
    @BindView(R.id.tv_right_num)
    TextView       mTvRightNum;
    @BindView(R.id.tv_sect_bonus)
    TextView       mTvSectBonus;
    @BindView(R.id.tv_sect_contribution)
    TextView       mTvSectContribution;
    @BindView(R.id.iv_end)
    ImageView         mIvEnd;
    @BindView(R.id.fl_question_end)
    FrameLayout    mFlQuestionEnd;
    @BindView(R.id.ll_end)
    LinearLayout llEnd;

    private SectFightAdapter  mCardAdapter;
    private ShadowTransformer mCardShadowTransformer;
    private int size = 0;
    private int next = 0;


    private int positionNum;
    private int sectWarId;

    private String nowSystemTime;

    private String allQuestion;
    public static final int  MIN_CLICK_DELAY_TIME = 1000;//这里设置不能超过多长时间
    private             long lastClickTime        = 0;

    private int total;

    private boolean haveAnswered;
    private boolean isAnswered = false;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_fight;
    }

    @Override
    protected int getFragmentContentId() {
        return 0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        long lastQuestionTime = EasySharePreference.getSectQuestionTime(this);//每题开始答题时间
        positionNum = EasySharePreference.getSectPositionNum(FightActivity.this);
        nowSystemTime = StringUtils.getNowSystemTime(lastQuestionTime);
         haveAnswered = EasySharePreference.getSectHaveAnswered(FightActivity.this);
        long newTime = System.currentTimeMillis();
        String nowSystemTime1 = StringUtils.getNowSystemTime(newTime);
        //非同一天清除缓存
        if (!TextUtils.equals(nowSystemTime, nowSystemTime1)) {
            EasySharePreference.setSectQuestionTime(this, newTime);//网速比较差的话第一题会有时间间隔
            EasySharePreference.setSectHaveAnswered(this, false);
            EasySharePreference.setFightQuestion(this, null);
            positionNum = 0;
        }else{
            if (positionNum != 0) {
                long time = newTime - lastQuestionTime;
                if (!haveAnswered && time >= 60 * 1000) {
                    EasySharePreference.setHaveAnswered(FightActivity.this, true);
                    haveAnswered = true;
                    isAnswered = true;
                } else {
                    if (haveAnswered) {
                        isAnswered = true;
                    }
                }
            }
        }

         sectWarId = getIntent().getIntExtra("sectWarId", -1);
        mCardAdapter = new SectFightAdapter(FightActivity.this);
        mViewPager.setAdapter(mCardAdapter);
        mCardShadowTransformer = new ShadowTransformer(mViewPager, mCardAdapter);
        mCardAdapter.setOnclickNext(this);

        LoadData();

        setUpView();
    }

    private void LoadData() {
        if (!NetworkUtils.isNetworkOnline(FightActivity.this)) {
            ToastUtils.showShort(FightActivity.this, "请检查网络链接");
            return;
        }
        mProgressBar.setVisibility(View.VISIBLE);
        //从sp中读取
        allQuestion = EasySharePreference.getFightQuestion(this);
        if (allQuestion != null) {
            mProgressBar.setVisibility(View.GONE);
            List<CardItem> cardItems = JSON.parseArray(allQuestion, CardItem.class);
            if (cardItems != null && cardItems.size() != 0 && positionNum != 0) {
                size = cardItems.size();//跟新加载数据的长度
                total = size +cardItems.get(0).getPosition()-1;
                for (int i = 0; i < cardItems.size(); i++) {
                    //只加载没看过的
                    if (cardItems.get(i).getPosition() >= positionNum) {
                        mCardAdapter.addCardItem(cardItems.get(i),total);
                    }
                }
                return;
            }
        }
        if (positionNum == 0) {
            String sectionId ="";
            UserInfo userLastInfo = CartoonApp.getInstance().getUserLastInfo();
            if (userLastInfo != null) {
                sectionId = userLastInfo.getSectionId();
            }
            //从网络获取信息
            BuilderInstance.getInstance().getGetBuilderInstance(StaticField.URL_SECT_FIGHT_QUESTION)
                    .addParams("sectId", sectionId)
                    .addParams("sectWarId", sectWarId + "")
                    .build().execute(new BaseCallBack<List<CardItem>>() {

                @Override
                public void onLoadFail() {
                    mProgressBar.setVisibility(View.GONE);
                    changeUi();
                }

                @Override
                public void onContentNull() {
                }

                @Override
                public void onLoadSuccess(List<CardItem> response) {
                    mProgressBar.setVisibility(View.GONE);
                    if (response != null&&!response.isEmpty()) {
                        if (response.size() != 0) {
                            size = response.size();
                            for (int i = 0; i < size; i++) {
                                if (i == 0) {
                                    total = size +response.get(0).getPosition()-1;
                                    EasySharePreference.setSectPositionNum(FightActivity.this, response.get(0).getPosition());
                                }
                                mCardAdapter.addCardItem(response.get(i),total);
                            }
                        }
                    }else
                        changeUi();
                }

                @Override
                public List<CardItem> parseNetworkResponse(String response) throws Exception {
                    EasySharePreference.setFightQuestion(FightActivity.this, response);
                    return JSON.parseArray(response, CardItem.class);
                }
            });
        }
    }

    private String sectionId ="";
    private void setUpView() {
        mTvTitle.setText("宗门战答题");
        UserInfo userLastInfo = CartoonApp.getInstance().getUserLastInfo();
        if (userLastInfo != null) {
             sectionId = userLastInfo.getSectionId();

            String questionPoint = EasySharePreference.getSectQuestionPoint(FightActivity.this);
            if (questionPoint != null) {
                String[] split = questionPoint.split("#");
                if (split.length == 3) {
                    mGetStone.setText("已获得灵石 " + split[1]);
                    mGetBonus.setText("已获得经验 " + split[0]);
                    mNowStone.setText("答对题数 " +split[2] );
                }
            }
        } else {
            mNowStone.setText("答对题数 00");
            mGetStone.setText("已获得灵石 00");
            mGetBonus.setText("已获得经验 00");
        }
        mBtRight.setVisibility(View.VISIBLE);
        mBtLeft.setOnClickListener(this);
        mBtRight.setOnClickListener(this);
        mIvEnd.setOnClickListener(this);

        if (positionNum!=0&&allQuestion ==null)
            changeUi();
    }

    private void changeUi() {
        mFlQuestion.setVisibility(View.GONE);
        mFlQuestionEnd.setVisibility(View.VISIBLE);
        fightResult();
    }

    private void fightResult() {
        BuilderInstance.getInstance().getGetBuilderInstance(StaticField.URL_SECT_FIGHT_R)
                .build().execute(new BaseCallBack<ResultGet>() {
            @Override
            public void onLoadFail() {
            }

            @Override
            public void onContentNull() {
            }

            @Override
            public void onLoadSuccess(ResultGet response) {
                if (response != null) {
                     mTvRightNum.setText("答对题数:"+response.getTotalAnswerRight());
                     mTvSectBonus.setText("    经验:+"+response.getExp());
                    mTvSectContribution.setText("    灵石:+"+response.getStone());
                }
            }
            @Override
            public ResultGet parseNetworkResponse(String response) throws Exception {
                return JSON.parseObject(response,ResultGet.class);
            }
        });
    }


    //判断是否是同一天
    private boolean isSameDay() {
        String nowTime = StringUtils.getNowSystemTime(System.currentTimeMillis());
        long lastQuestionTime = EasySharePreference.getSectQuestionTime(FightActivity.this);
        String nowSystemTime = StringUtils.getNowSystemTime(lastQuestionTime);
        boolean b = TextUtils.equals(nowSystemTime, "1970/1") || TextUtils.equals(nowSystemTime, nowTime);
        return b;
    }

    @Override
    public void clickNext(final int id , final int position) {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
            lastClickTime = currentTime;
        if (!isAnswered) {
            new MaterialDialog.Builder(this).title(R.string.notice)
                    .content("道友还没有选择答案，点击下一题将默认此题为不答且没任何奖励")
                    .positiveText("放弃")
                    .negativeText("再答")
                    .callback(new MaterialDialog.ButtonCallback() {
                        @Override
                        public void onPositive(MaterialDialog dialog) {
                            super.onPositive(dialog);
                            nextQuestion(id,position);
                        }

                        @Override
                        public void onNegative(MaterialDialog dialog) {
                            super.onNegative(dialog);
                        }
                    }).show();
        } else {
            nextQuestion(id,position);
        }
        }
    }

    private int lastPosition =-1;
    private void nextQuestion(int id,final int position){
        if (total != position) {
            if (isSameDay()) {//判断答题时间是不是同一天
                if (NetworkUtils.isNetworkOnline(FightActivity.this)) {
                    if (lastPosition == position)return;
                    lastPosition = position;
                        SendToBG.getSectNextQuestion(isAnswered,position, sectWarId,id, sectionId, new ResponseListener() {
                            @Override
                            public void onLoadFail() {
                            }
                            @Override
                            public void onLoadSuccess(String response) {
                                isAnswered = false;
                                if (response.equals("501")) {
                                    changeUi();
                                    return;
                                }
                                if (mViewPager != null && next < mCardAdapter.getCount()) {
                                    mViewPager.setCurrentItem(++next);
                                    EasySharePreference.setSectPositionNum(FightActivity.this, position + 1);//记录最后一次记录的position
                                    EasySharePreference.setSectQuestionTime(FightActivity.this, System.currentTimeMillis());//记录答题时间
                                    EasySharePreference.setSectHaveAnswered(FightActivity.this, false);

                                }
                            }
                        });


                } else {
                    ToastUtils.showShort(FightActivity.this, "请检查网络链接");
                }
            } else {
                ToastUtils.showShort(FightActivity.this, "题目已过期，请退出重新进入答题");
                EasySharePreference.setFightQuestion(FightActivity.this, null);
            }
        } else {
            changeUi();
            EasySharePreference.setFightQuestion(FightActivity.this, null);
        }

    }

    @Override
    public void clickItem(int id, String choice, int position, final ImageView imag1, final RelativeLayout rlayout) {
        isAnswered = true;
        if (choice.equals("0")) {
            return;
        }
        BuilderInstance.getInstance().getGetBuilderInstance(StaticField.URL_SECT_FIGHT_ANSWER)
                .addParams("questionId", id + "")
                .addParams("sectWarId", sectWarId + "")
                .addParams("sectId", sectionId)
                .addParams("choice", choice)
                .addParams("position", position + "")
                .build().execute(new BaseCallBack<DayQuestionAnswer>() {

            @Override
            public void onLoadFail() {
                ToastUtils.showShort(FightActivity.this, "获取答案失败");
            }

            @Override
            public void onContentNull() {

            }

            @Override
            public void onLoadSuccess(DayQuestionAnswer response) {
                if (response != null) {
                    if (response.getCode() == 501) {
                        ToastUtils.showShort(FightActivity.this,"对方防御为0，此次宗门战结束");
                        changeUi();
                        return;
                    }
                    if (response.getCode() == 502) {
                        ToastUtils.showShort(FightActivity.this,"灵石不足");
                        return;
                    }
                    mGetStone.setText("已获得灵石 " + response.getWinBonusStone());
                    mGetBonus.setText("已获得经验 " + response.getWinBonusPoint());
                    mNowStone.setText("答对题数 " + response.getRightNumbers());
                    EasySharePreference.setSectQuestionPoint(FightActivity.this, response.getWinBonusPoint() + "#" + response.getWinBonusStone()+ "#" + response.getRightNumbers());
                    EasySharePreference.setSectHaveAnswered(FightActivity.this, true);
                    imag1.setVisibility(View.VISIBLE);
                    if (response.getIsRight()==1) {
                        imag1.setBackgroundResource(R.mipmap.question_right);
                        rlayout.setBackgroundResource(R.drawable.question_background_green);
                    } else {
                        imag1.setBackgroundResource(R.mipmap.question_wrong);
                        rlayout.setBackgroundResource(R.drawable.question_background_yelow);
                    }
                }
               // ToastUtils.showShort(FightActivity.this, response.getMsg());

            }

            @Override
            public DayQuestionAnswer parseNetworkResponse(String response) throws Exception {
                return JSON.parseObject(response, DayQuestionAnswer.class);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_left:
            case R.id.iv_end:
                onBackPressed();
                break;
            case R.id.bt_right:
                Intent intent = new Intent(this, LicenseActivity.class);
                intent.putExtra("type", 0);
                startActivity(intent);
                break;
        }
    }
}

