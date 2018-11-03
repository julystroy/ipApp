package com.cartoon.module.tab;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.cartoon.CartoonApp;
import com.cartoon.data.EventMessageCount;
import com.cartoon.data.EventNewMessage;
import com.cartoon.data.UserInfo;
import com.cartoon.data.chat.SectChat;
import com.cartoon.http.BaseCallBack;
import com.cartoon.http.BuilderInstance;
import com.cartoon.http.StaticField;
import com.cartoon.module.BaseFragment;
import com.cartoon.module.login.LoginActivity;
import com.cartoon.module.markets.MarketsActivity;
import com.cartoon.module.mymessage.MyMessageActivity;
import com.cartoon.module.mytask.MyTaskActivity;
import com.cartoon.module.question.QuestionActivity;
import com.cartoon.module.tab.adapter.MineGradViewAdpter;
import com.cartoon.module.tab.ar.ARActivity;
import com.cartoon.module.tab.mine.ChartsActivity;
import com.cartoon.module.tab.mine.MyPackageActivity;
import com.cartoon.module.tab.mine.SettingActivity;
import com.cartoon.module.zong.ChatClients;
import com.cartoon.module.zong.SectActivity;
import com.cartoon.module.zong.SectChatActivity;
import com.cartoon.utils.UserDB;
import com.cartoon.view.dialog.DescDialog;
import com.cartoon.view.MyGridView;
import com.cartton.library.utils.ToastUtils;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.com.xuanjiezhimen.R;

/**
 * Created by cc on 17-12-22.
 */
public class XiuXingFragment extends BaseFragment implements View.OnClickListener, AdapterView.OnItemClickListener {
    @BindView(R.id.bt_left)
    ImageButton    mBtLeft;
    @BindView(R.id.tv_title)
    TextView       tvTitle;
    @BindView(R.id.bt_right)
    ImageButton    btRight;
    @BindView(R.id.textPoint)
    TextView       mTextPoint;
    @BindView(R.id.rl_title)
    RelativeLayout mRlTitle;
    @BindView(R.id.xx_unload)
    ImageView      mXxUnload;
    @BindView(R.id.tv_name)
    TextView       mTvName;
    @BindView(R.id.tv_level)
    TextView       mTvLevel;
    @BindView(R.id.tv_day)
    TextView       mTvDay;
    @BindView(R.id.tv_sign)
    TextView       mTvSign;
    @BindView(R.id.xx_load)
    LinearLayout   mXxLoad;
    @BindView(R.id.iv_icon)
    ImageView      mIvIcon;
    @BindView(R.id.tv_item_name)
    TextView       mTvItemName;
    @BindView(R.id.message_new)
    ImageView      mMessageNew;
    @BindView(R.id.tv_item_desc)
    TextView       mTvItemDesc;
    @BindView(R.id.ic_ar_desc)
    ImageView      mIcArDesc;
    @BindView(R.id.ll_aritem)
    LinearLayout   llArItem;
    @BindView(R.id.gridView)
    MyGridView     mGridView;

    @BindView(R.id.chat)
    ImageView ivChat;
    @BindView(R.id.chat_num)
    TextView tvChatNum;
    @BindView(R.id.rl_chat)
    RelativeLayout rlChat;
    private MineGradViewAdpter adpter;
   private boolean manager;
   private String sect_id;
    @Override
    protected int getLayoutId() {
        return R.layout.fg_xiuxing;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        tvTitle.setText(R.string.tab_xiu);
        mIcArDesc.setVisibility(View.VISIBLE);
        btRight.setOnClickListener(this);
        mBtLeft.setVisibility(View.GONE);
        llArItem.setOnClickListener(this);
        mIcArDesc.setOnClickListener(this);
        mTvDay.setOnClickListener(this);
        mTvSign.setOnClickListener(this);
        rlChat.setOnClickListener(this);
        btRight.setImageResource(R.mipmap.icon_mine_message2);

        adpter = new MineGradViewAdpter(getContext());
        mGridView.setAdapter(adpter);
        mGridView.setOnItemClickListener(this);

    }


    @Override
    public void onResume() {
        super.onResume();
        if (CartoonApp.getInstance().getUserInfo() != null) {
            loadInfo();
        }
    }

    private void loadInfo() {
        BuilderInstance.getInstance().getPostBuilderInstance(StaticField.URL_USER2_TASK)
                .build().execute(new BaseCallBack<UserInfo>() {
            @Override
            public void onLoadFail() {
                UserInfo userLastInfo = CartoonApp.getInstance().getUserLastInfo();
                if (userLastInfo != null) {
                    mTvName.setText(userLastInfo.getNickname());
                    mTvLevel.setText(userLastInfo.getLvName());
                }
            }

            @Override
            public void onContentNull() {

            }

            @Override
            public void onLoadSuccess(UserInfo response) {
                if (response != null) {
                    mTvName.setText(response.getNickname());
                    mTvLevel.setText(response.getLvName());
                    manager = response.isManager();
                    sect_id  = response.getSectionId();
                    if (sect_id != null || response.getSect_id() != null) {
                        rlChat.setVisibility(View.VISIBLE);
                    } else {
                        rlChat.setVisibility(View.GONE);
                        UserInfo userLastInfo = CartoonApp.getInstance().getUserLastInfo();
                        if (userLastInfo != null) {
                            userLastInfo.setSectionId(null);
                            CartoonApp.getInstance().setUserLastInfo(userLastInfo);
                        }
                    }
                    mTvDay.setText("已修行"+response.getTraining_days()+"天");
                    if (response.isHasSign())
                        mTvSign.setText("已签到");
                    else
                        mTvSign.setText("今日签到");

                    adpter.isNewMessage(response.isHasSectionMsg(),response.isHasTaskMsg());
                    CartoonApp.getInstance().setUserLastInfo(response);

                }
            }

            @Override
            public UserInfo parseNetworkResponse(String response) throws Exception {
                return JSON.parseObject(response.toString(),UserInfo.class);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        EventBus.getDefault().register(this);
        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (CartoonApp.getInstance().getUserInfo() != null) {
                mXxLoad.setVisibility(View.VISIBLE);
                mXxUnload.setVisibility(View.GONE);
            } else {
                mXxLoad.setVisibility(View.GONE);
                mXxUnload.setVisibility(View.VISIBLE);
            }

            int count = ChatClients.getCount(false);
            if (count>1){
                tvChatNum.setVisibility(View.VISIBLE);
                if (count>=99)
                    tvChatNum.setText("99+");
                else
                    tvChatNum.setText(String.valueOf(count-1));
            }else{
                tvChatNum.setVisibility(View.GONE);
            }

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_right:
                if (isUserAlreadyLogin())
                    startActivity(new Intent(getContext(), MyMessageActivity.class));
                else
                    startActivity(new Intent(getContext(), LoginActivity.class));

                break;
            case R.id.bt_left:
                startActivity(new Intent(getContext(), SettingActivity.class));
                break;
            case R.id.ic_ar_desc:
                DescDialog descDialog = new DescDialog(getContext(),2);
                descDialog.setCanceledOnTouchOutside(true);
                descDialog.show();
                break;
            case R.id.ll_aritem:
                if (isUserAlreadyLogin()) {
                    MobclickAgent.onEvent(getContext(),"aractivity");
                    startActivity(new Intent(getContext(), ARActivity.class));
                } else {
                    startActivity(new Intent(getContext(), LoginActivity.class));
                }

                break;
            case R.id.tv_day:
                DescDialog descDialog2 = new DescDialog(getContext(),3);
                descDialog2.setCanceledOnTouchOutside(true);
                descDialog2.show();
                break;
            case R.id.tv_sign:
                BuilderInstance.getInstance().getPostBuilderInstance(StaticField.URL_ACTION_SIGN_IN)
                        .addParams("action_type", 5 + "")
                        .addParams("deviceId", CartoonApp.getInstance().getDeviceId())
                        .build().execute(new BaseCallBack<UserInfo>() {
                    @Override
                    public void onLoadFail() {

                    }

                    @Override
                    public void onContentNull() {

                    }

                    @Override
                    public void onLoadSuccess(UserInfo response) {
                        ToastUtils.showShort(getContext(),"签到成功");
                        mTvSign.setText("已签到");
                      //  mTvDay.setText("已修行"+response.getTraining_days()+"天");

                    }

                    @Override
                    public UserInfo parseNetworkResponse(String response) throws Exception {
                        return JSON.parseObject(response, UserInfo.class);
                    }
                });
                break;
            case R.id.rl_chat:
                tvChatNum.setVisibility(View.GONE);
                SectChat sectChat1 = UserDB.getInstance().querySect(CartoonApp.getInstance().getUserId());
                if (sectChat1==null){//刚加入宗门时创建表判断
                    UserDB.getInstance().buildSectDB();
                }
                startActivity(new Intent(getContext(),SectChatActivity.class));
                break;
        }
    }
    private boolean isUserAlreadyLogin() {
        UserInfo userInfo = CartoonApp.getInstance().getUserInfo();
        return userInfo != null;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 2:
                if (isUserAlreadyLogin())
                    startActivity(new Intent(getContext(), QuestionActivity.class));
                else
                    startActivity(new Intent(getContext(), LoginActivity.class));
                break;
            case 3:
                if (isUserAlreadyLogin())
                    startActivity(new Intent(getContext(), MarketsActivity.class));
                else
                    startActivity(new Intent(getContext(), LoginActivity.class));
                break;
            case 5:
                if (isUserAlreadyLogin())
                    startActivity(new Intent(getContext(), MyPackageActivity.class));
                else
                    startActivity(new Intent(getContext(), LoginActivity.class));
                break;
            case 0:
                if (isUserAlreadyLogin()) {
                    Intent intent = new Intent(getContext(), SectActivity.class);
                    intent.putExtra("join", manager);
                    intent.putExtra("sectionId", sect_id);
                    startActivity(intent);
                } else
                    startActivity(new Intent(getContext(), LoginActivity.class));
                break;
            case 1:
                if (isUserAlreadyLogin())
                    startActivity(new Intent(getContext(), MyTaskActivity.class));
                else
                    startActivity(new Intent(getContext(), LoginActivity.class));
                break;
            case 4:
                if (isUserAlreadyLogin())
                    startActivity(new Intent(getContext(), ChartsActivity.class));
                else
                    startActivity(new Intent(getContext(), LoginActivity.class));
                break;
        }
    }


    @Subscribe
    public void onEvent(EventNewMessage eventNewMessage) {
        if (mTextPoint != null) {
            if (eventNewMessage.isShowNew) {
                mTextPoint.setVisibility(View.VISIBLE);
            } else {
                mTextPoint.setVisibility(View.GONE);
            }
        }
    }

    @Subscribe
    public void onEvent(final EventMessageCount count){
        if (count != null) {
            getActivity().runOnUiThread(new Runnable() {
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
