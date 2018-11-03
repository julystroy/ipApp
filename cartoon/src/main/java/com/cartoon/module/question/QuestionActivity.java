package com.cartoon.module.question;

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
import com.cartoon.data.DayQuestionAnswer;
import com.cartoon.data.DayResponse;
import com.cartoon.data.RecordData;
import com.cartoon.data.UserInfo;
import com.cartoon.data.response.QuestionListResp;
import com.cartoon.http.BaseCallBack;
import com.cartoon.http.BuilderInstance;
import com.cartoon.http.StaticField;
import com.cartoon.module.BaseActivity;
import com.cartoon.module.listener.ResponseListener;
import com.cartoon.utils.EasySharePreference;
import com.cartoon.utils.NetworkUtils;
import com.cartoon.utils.SendToBG;
import com.cartoon.utils.StringUtils;
import com.cartoon.view.MyViewPage;
import com.cartoon.view.dialog.DayAskDialog;
import com.cartoon.view.dialog.QuestionDescDialog;
import com.cartton.library.utils.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.com.xuanjiezhimen.R;

/**
 * Created by cc on 17-1-17.
 */
public class QuestionActivity extends BaseActivity implements View.OnClickListener, CardPagerAdapter.nextQuestionListener {

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
    @BindView(R.id.start_question)
    ImageView      mStartQuestion;
    @BindView(R.id.fl_question_start)
    FrameLayout    mFlQuestionStart;
    @BindView(R.id.fl_question)
    FrameLayout    mFlQuestion;
    @BindView(R.id.tv_daily_stone)
    TextView       mTvDailyStone;
    @BindView(R.id.tv_daily_bonus)
    TextView       mTvDailyBonus;
    @BindView(R.id.tv_sect_right)
    TextView       mTvSectRight;
    @BindView(R.id.tv_sect_bonus)
    TextView       mTvSectBonus;
    @BindView(R.id.tv_sect_contribution)
    TextView       mTvSectContribution;
    @BindView(R.id.iv_end)
    ImageView      mIvEnd;
    @BindView(R.id.fl_question_end)
    FrameLayout    mFlQuestionEnd;
    private CardPagerAdapter  mCardAdapter;
    private ShadowTransformer mCardShadowTransformer;

    private int size = 0;
    private int next = 0;


    private int positionNum;


    private String allQuestion;


    private String nowSystemTime;
    public static final int  MIN_CLICK_DELAY_TIME = 1500;//这里设置不能超过多长时间

    private             long lastClickTime        = 0;
    public  final int  MAX_CLICK_DELAY_TIME = 2000;//这里设置不能超过多长时间
    private             long lastClickTime1       = 0;
    private boolean isAnswer =false;
    private boolean haveAnswered;
    @Override
    protected int getContentViewId() {
        return R.layout.activity_questioin;
    }

    @Override
    protected int getFragmentContentId() {
        return 0;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        positionNum = EasySharePreference.getPositionNum(this);//最新答题的位置
         haveAnswered = EasySharePreference.getHaveAnswered(QuestionActivity.this);
        //app未退出，隔天情况
        long lastQuestionTime = EasySharePreference.getLastQuestionTime(this);//每题开始答题时间
        nowSystemTime = StringUtils.getNowSystemTime(lastQuestionTime);

        long newTime = System.currentTimeMillis();
        String nowSystemTime1 = StringUtils.getNowSystemTime(newTime);
        //非同一天清除缓存
        if (!TextUtils.equals(nowSystemTime, nowSystemTime1)) {
            EasySharePreference.setLastQuestionTime(this, newTime);//网速比较差的话第一题会有时间间隔
            EasySharePreference.setHaveAnswered(this, false);
            EasySharePreference.setAllQuestion(this, null);
            EasySharePreference.setQuestionPoint(this,null);
            positionNum = 0;
        }else{
            if (positionNum != 0) {
                long time = newTime - lastQuestionTime;
                if (!haveAnswered && time >= 60 * 1000) {
                    EasySharePreference.setHaveAnswered(QuestionActivity.this, true);
                    haveAnswered = true;
                } else {
                    if (!haveAnswered && time < 60 * 1000)
                    isAnswer = true;
                }
            }
        }

        mCardAdapter = new CardPagerAdapter(QuestionActivity.this);
        mCardAdapter.setOnclickNext(this);

        setUpView();


    }


    private void LoadData() {
        if (!NetworkUtils.isNetworkOnline(QuestionActivity.this)) {
            ToastUtils.showShort(QuestionActivity.this, "请检查网络链接");
            return;
        }
        mProgressBar.setVisibility(View.VISIBLE);
        //从sp中读取
        allQuestion = EasySharePreference.getAllQuestion(this);
        if (allQuestion != null) {
            mProgressBar.setVisibility(View.GONE);
            QuestionListResp questionListResp = JSON.parseObject(allQuestion, QuestionListResp.class);
            if (questionListResp.getList() != null && questionListResp.getList().size() != 0 && positionNum != 0) {
                size = 11 - positionNum;//跟新加载数据的长度
                for (int i = 0; i < questionListResp.getList().size(); i++) {
                    //只加载没看过的
                    if (questionListResp.getList().get(i).getPosition() >= positionNum) {
                        mCardAdapter.addCardItem(questionListResp.getList().get(i));
                    }
                }
            }

            return;
        }

        //  String time = Utils.getTime();
        //从网络获取信息
        if (positionNum == 0) {
            BuilderInstance.getInstance().getGetBuilderInstance(StaticField.URL_DAY_QUESTION_list)
                    .addParams("uid", CartoonApp.getInstance().getUserId())
                    .addParams("token", CartoonApp.getInstance().getToken())
                    .build().execute(new BaseCallBack<QuestionListResp>() {

                @Override
                public void onLoadFail() {
                    mProgressBar.setVisibility(View.GONE);
                }

                @Override
                public void onContentNull() {
                }

                @Override
                public void onLoadSuccess(QuestionListResp response) {
                    mProgressBar.setVisibility(View.GONE);
                    if (response != null) {
                        if (response.getList().size() != 0) {
                            size = response.getList().size();
                            EasySharePreference.setPositionNum(QuestionActivity.this, 11-size);
                            for (int i = 0; i < size; i++) {
                                mCardAdapter.addCardItem(response.getList().get(i));
                            }
                        } else {
                            endUi();
                        }
                    }
                }

                @Override
                public QuestionListResp parseNetworkResponse(String response) throws Exception {
                    EasySharePreference.setAllQuestion(QuestionActivity.this, response);
                    return JSON.parseObject(response, QuestionListResp.class);
                }
            });
        }
    }



    private void setUpView() {
        mTvTitle.setText("每日答题");
        UserInfo userLastInfo = CartoonApp.getInstance().getUserLastInfo();
        if (userLastInfo != null) {
            mNowStone.setText("当前灵石 " + userLastInfo.getBonus_stone());
            String questionPoint = EasySharePreference.getQuestionPoint(QuestionActivity.this);
            if (questionPoint != null) {
                String[] split = questionPoint.split("#");
                if (split.length == 2) {
                    mGetStone.setText("已获得灵石 " + split[1]);
                    mGetBonus.setText("已获得经验 " + split[0]);
                }
            }
        } else {
            mNowStone.setText("当前灵石 00");
            mGetStone.setText("已获得灵石 00");
            mGetBonus.setText("已获得经验 00");
        }
        mBtRight.setVisibility(View.VISIBLE);
        mBtLeft.setOnClickListener(this);
        mBtRight.setOnClickListener(this);
        mStartQuestion.setOnClickListener(this);

        if (positionNum ==10 && isAnswer){
            uiChange();
        }else if (positionNum > 0&&positionNum<10) {
            uiChange();
        }else if(positionNum==10 && !isAnswer){//第10题并且已答
            endUi();
        }else{
            mFlQuestionEnd.setVisibility(View.GONE);
            mFlQuestionStart.setVisibility(View.VISIBLE);
            mFlQuestion.setVisibility(View.GONE);
        }
    }

    private void endUi(){
        mFlQuestionEnd.setVisibility(View.VISIBLE);
        mFlQuestionStart.setVisibility(View.GONE);
        mFlQuestion.setVisibility(View.GONE);
        mIvEnd.setOnClickListener(this);
        //end数据回显
        loadEnd();
        EasySharePreference.setPositionNum(QuestionActivity.this, 10);//记录最后一次记录的position
        EasySharePreference.setHaveAnswered(QuestionActivity.this, true);
        EasySharePreference.setQuestionPoint(QuestionActivity.this,null);
    }

    private void loadEnd() {
        BuilderInstance.getInstance().getPostBuilderInstance(StaticField.URL_QUESTION_RECORD)
                .build().execute(new BaseCallBack<RecordData>() {
            @Override
            public void onLoadFail() {

            }

            @Override
            public void onContentNull() {

            }

            @Override
            public void onLoadSuccess(RecordData response) {
                if (response != null) {
                    mTvDailyStone.setText("灵石：+"+response.getStone());
                    mTvDailyBonus.setText("经验：+"+response.getBonusPoint());
                    mTvSectBonus.setText("经验：+"+response.getSectBp());
                    mTvSectContribution.setText("      宗门贡献：+"+response.getSectCont());
                    mTvSectRight.setText("共答对"+response.getRightNum()+"题可以额外获得");
                }
            }

            @Override
            public RecordData parseNetworkResponse(String response) throws Exception {
                return JSON.parseObject(response.toString(),RecordData.class);
            }
        });
    }

    private void uiChange() {
        mFlQuestionStart.setVisibility(View.GONE);
        mFlQuestion.setVisibility(View.VISIBLE);
        mViewPager.setAdapter(mCardAdapter);
        mCardShadowTransformer = new ShadowTransformer(mViewPager, mCardAdapter);
        LoadData();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_left:
                onBackPressed();
                break;
            case R.id.bt_right:
                QuestionDescDialog descDialog = new QuestionDescDialog(QuestionActivity.this);
                descDialog.setCanceledOnTouchOutside(true);
                descDialog.show();
                break;
            case R.id.start_question:
                uiChange();
                break;
            case R.id.iv_end:
               onBackPressed();
                break;
        }
    }


    //判断是否是同一天
    private boolean isSameDay() {
        String nowTime = StringUtils.getNowSystemTime(System.currentTimeMillis());
        long lastQuestionTime = EasySharePreference.getLastQuestionTime(QuestionActivity.this);
        String nowSystemTime = StringUtils.getNowSystemTime(lastQuestionTime);
        boolean b = TextUtils.equals(nowSystemTime, "1970/1") || TextUtils.equals(nowSystemTime, nowTime);
        return b;
    }



    private void first5Question(final int position){

        if (isSameDay()) {//判断答题时间是不是同一天
            if (NetworkUtils.isNetworkOnline(QuestionActivity.this)) {
                long currentTime = System.currentTimeMillis();
                if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
                    lastClickTime = currentTime;

                    SendToBG.getNextQuestion( position, new ResponseListener() {
                        @Override
                        public void onLoadFail() {

                        }

                        @Override
                        public void onLoadSuccess(String response) {
                            isAnswer = true;
                            if (mViewPager != null && next < mCardAdapter.getCount()) {
                                mViewPager.setCurrentItem(++next);
                                EasySharePreference.setPositionNum(QuestionActivity.this, position + 1);//记录最后一次记录的position
                                EasySharePreference.setLastQuestionTime(QuestionActivity.this, System.currentTimeMillis());//记录答题时间
                                EasySharePreference.setHaveAnswered(QuestionActivity.this, false);
                            }
                        }
                    });

                }
            } else {
                ToastUtils.showShort(QuestionActivity.this, "请检查网络链接");
            }
        } else {
            ToastUtils.showShort(QuestionActivity.this, "题目已过期，请退出重新进入答题");
            refreshSP();
        }
    }



    //5题之后
    @Override
    public void clickNext(final int id , final int position) {
        if (position+1>5)
            last5Question(id,position);
       else if (isAnswer) {
            new MaterialDialog.Builder(this).title(R.string.notice)
                    .content("该题尚未回答，点击查看将会默认此题为不答且没任何奖励")
                    .positiveText("放弃")
                    .negativeText("取消")
                    .callback(new MaterialDialog.ButtonCallback() {
                        @Override
                        public void onPositive(MaterialDialog dialog) {
                            super.onPositive(dialog);
                            SendToBG.getAnswer(QuestionActivity.this, id,position, new ResponseListener() {
                                @Override
                                public void onLoadFail() {

                                }

                                @Override
                                public void onLoadSuccess(String response) {
                                        first5Question(position);
                                }
                            });


                        }

                        @Override
                        public void onNegative(MaterialDialog dialog) {
                            super.onNegative(dialog);

                        }
                    }).show();
        } else {
                first5Question(position);
        }


    }

    private void last5Question(final int id,final int position){
        if (isSameDay()) {//判断答题时间是不是同一天
        if (position == 10) {
            if (isAnswer) {
                new MaterialDialog.Builder(QuestionActivity.this).title(R.string.notice)
                        .content("该题尚未回答，点击查看将会默认此题为不答且没任何奖励")
                        .positiveText("放弃")
                        .negativeText("取消")
                        .callback(new MaterialDialog.ButtonCallback() {
                            @Override
                            public void onPositive(final MaterialDialog dialog) {
                                super.onPositive(dialog);
                                mProgressBar.setVisibility(View.VISIBLE);
                                SendToBG.getAnswer(QuestionActivity.this, id, position, new ResponseListener() {
                                    @Override
                                    public void onLoadFail() {
                                    }

                                    @Override
                                    public void onLoadSuccess(String response) {
                                        endUi();
                                    }
                                });


                            }

                            @Override
                            public void onNegative(MaterialDialog dialog) {
                                super.onNegative(dialog);

                            }
                        }).show();

            } else
                endUi();
        } else {
                final DayAskDialog dialog2 = new DayAskDialog(QuestionActivity.this);
                dialog2.setCanceledOnTouchOutside(true);
                dialog2.setOnClickListener(new DayAskDialog.OnDialogClickListener() {
                    @Override
                    public void onDilogClick(TextView view) {
                        long currentTime = System.currentTimeMillis();
                        if (currentTime - lastClickTime1 > MAX_CLICK_DELAY_TIME) {
                            lastClickTime1 = currentTime;
                            if (isAnswer) {

                                new MaterialDialog.Builder(QuestionActivity.this).title(R.string.notice)
                                        .content("该题尚未回答，点击查看将会默认此题为不答且没任何奖励")
                                        .positiveText("放弃")
                                        .negativeText("取消")
                                        .callback(new MaterialDialog.ButtonCallback() {
                                            @Override
                                            public void onPositive(final MaterialDialog dialog) {
                                                super.onPositive(dialog);
                                                mProgressBar.setVisibility(View.VISIBLE);
                                                SendToBG.getAnswer(QuestionActivity.this, id,position, new ResponseListener() {
                                                    @Override
                                                    public void onLoadFail() {
                                                    }
                                                    @Override
                                                    public void onLoadSuccess(String response) {

                                                        buyQuestion(position,dialog2);
                                                    }
                                                });


                                            }

                                            @Override
                                            public void onNegative(MaterialDialog dialog) {
                                                super.onNegative(dialog);

                                            }
                                        }).show();
                            }else{
                                buyQuestion(position,dialog2);
                            }
                        }
                    }
                });

                dialog2.show();

            }
        }else {
            ToastUtils.showShort(QuestionActivity.this, "题目已过期，请退出重新进入答题");
            refreshSP();
        }

    }

    private void buyQuestion(final int position, final DayAskDialog dialog){
                    BuilderInstance.getInstance().getGetBuilderInstance(StaticField.URL_NEXT_QUESTION)
                            .addParams("uid", CartoonApp.getInstance().getUserId())
                            .addParams("token", CartoonApp.getInstance().getToken())
                            .addParams("position", position + "")
                            .build().execute(new BaseCallBack<DayResponse>() {

                        @Override
                        public void onLoadFail() {
                            ToastUtils.showShort(QuestionActivity.this, "请检查网络链接");
                            try {
                                dialog.dismiss();
                            } catch (Exception e) {

                            }

                            mProgressBar.setVisibility(View.GONE);
                        }

                        @Override
                        public void onContentNull() {

                        }

                        @Override
                        public void onLoadSuccess(DayResponse response) {
                            isAnswer = true;
                            mProgressBar.setVisibility(View.GONE);
                            if (TextUtils.equals(response.getMsg(), "购买成功，请继续答题")) {
                                try {
                                    dialog.dismiss();
                                } catch (Exception e) {

                                }
                                int count = mCardAdapter.getCount();
                                if (next <= count) {
                                    mViewPager.setCurrentItem(++next);
                                    EasySharePreference.setPositionNum(QuestionActivity.this, (position+1));//记录最后一次记录的position
                                    EasySharePreference.setLastQuestionTime(QuestionActivity.this, System.currentTimeMillis());//记录答题时间
                                    EasySharePreference.setHaveAnswered(QuestionActivity.this, false);
                                }


                            }

                            ToastUtils.showShort(QuestionActivity.this, response.getMsg());

                        }

                        @Override
                        public DayResponse parseNetworkResponse(String response) throws Exception {
                            return JSON.parseObject(response, DayResponse.class);
                        }
                    });

    }
    //选择题目
    @Override
    public void clickItem(int id, String choice, final int position, final ImageView view, final RelativeLayout rlayout) {
        isAnswer = false;
        if (choice.equals("0"))
            return;
        BuilderInstance.getInstance().getGetBuilderInstance(StaticField.URL_DAY_QUESTION_GET_ANSWER)
                .addParams("uid", CartoonApp.getInstance().getUserId())
                .addParams("token", CartoonApp.getInstance().getToken())
                .addParams("id", id + "")
                .addParams("choice", choice)
                .addParams("position", position + "")
                .build().execute(new BaseCallBack<DayQuestionAnswer>() {

            @Override
            public void onLoadFail() {
                ToastUtils.showShort(QuestionActivity.this, "获取答案失败");
            }

            @Override
            public void onContentNull() {

            }

            @Override
            public void onLoadSuccess(DayQuestionAnswer response) {
                if (response != null) {
                    mGetStone.setText("已获得灵石 " + response.getWinBonusStone());
                    mGetBonus.setText("已获得经验 " + response.getWinBonusPoint());
                    EasySharePreference.setQuestionPoint(QuestionActivity.this,response.getWinBonusPoint()+"#"+response.getWinBonusStone());
                    mNowStone.setText("当前灵石 " + response.getNewStone());
                    EasySharePreference.setHaveAnswered(QuestionActivity.this, true);
                    view.setVisibility(View.VISIBLE);
                    if (TextUtils.equals(response.getAnswer(), "正确")) {
                        view.setBackgroundResource(R.mipmap.question_right);
                        rlayout.setBackgroundResource(R.drawable.question_background_green);
                    } else {
                        view.setBackgroundResource(R.mipmap.question_wrong);
                        rlayout.setBackgroundResource(R.drawable.question_background_yelow);
                    }
                }

                ToastUtils.showShort(QuestionActivity.this, response.getMsg());
                if (position == 10) {
                    EasySharePreference.setAllQuestion(QuestionActivity.this,null);
                }


            }

            @Override
            public DayQuestionAnswer parseNetworkResponse(String response) throws Exception {
                return JSON.parseObject(response, DayQuestionAnswer.class);
            }
        });
    }

    private void refreshSP(){
        EasySharePreference.setPositionNum(QuestionActivity.this,0);
        EasySharePreference.setLastQuestionTime(QuestionActivity.this,0);
        EasySharePreference.setHaveAnswered(QuestionActivity.this,false);
        EasySharePreference.setAllQuestion(QuestionActivity.this,null);
    }
}
