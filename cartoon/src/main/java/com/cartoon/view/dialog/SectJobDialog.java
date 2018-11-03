package com.cartoon.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.cartoon.CartoonApp;
import com.cartoon.data.AppPoint;
import com.cartoon.data.UserInfo;
import com.cartoon.data.chat.SectChat;
import com.cartoon.http.BaseCallBack;
import com.cartoon.http.BuilderInstance;
import com.cartoon.http.StaticField;
import com.cartoon.module.tab.adapter.SectJobAdpter;
import com.cartoon.utils.UserDB;
import com.cartoon.view.DialogToast;
import com.cartton.library.utils.ToastUtils;

import java.util.List;

import cn.com.xuanjiezhimen.R;

/**
 * Created by cc on 16-10-25.
 */
 public class SectJobDialog extends Dialog implements View.OnClickListener {

    private  String memberId;
    private List<AppPoint> response;
    private Context context;
    private ListView llJob;
    private TextView tvSure;
    private TextView tvCancel;
    private TextView userState;
    private SectChat sectChat;
    private int rankId =-1;
    private onSureClickListener onClickListener;

    //移除宗门 0 掌门1  长老2 核心弟子3   内门弟子4  外门弟子5

    /***
     构造方法
     @param context

     */
    public SectJobDialog(Context context,String memberId, List<AppPoint> response) {
        super(context, R.style.DialogStyleDesc);
        this.context = context;
        this.response = response;
        this.memberId = memberId;
        //修改显示位置 本质就是修改 WindowManager.LayoutParams让内容水平居中 底部对齐
        WindowManager.LayoutParams attributes = getWindow().getAttributes();
        //android:gravity=bottom|center_horizonal
        attributes.gravity= Gravity.CENTER;
        attributes.width =260;
        getWindow().setAttributes(attributes);
    }

    /*** 方法
     @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_sectjob);
        initView();
    }

    private boolean equals;
    private void initView() {
        llJob = (ListView) findViewById(R.id.ll_sectjob);
        tvSure = (TextView) findViewById(R.id.tv_sure);
        tvCancel = (TextView) findViewById(R.id.tv_cancel);
        userState = (TextView) findViewById(R.id.user_state);



        if (response != null) {
            sectChat = UserDB.getInstance().querySect(memberId);
            if (sectChat != null) {
                equals = sectChat.getUser_status().equals("0");
                if (equals)
                    userState.setText("禁言");
                else
                    userState.setText("取消禁言");
            }
            final SectJobAdpter adpter = new SectJobAdpter(context,response);
            llJob.setAdapter(adpter);
            llJob.setDivider(null);
            llJob.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    adpter.setSelectedPosition(position);
                    AppPoint item = (AppPoint) adpter.getItem(position);
                    rankName = item.getRankName();
                    UserDB.getInstance().updataRankName(memberId,rankName);
                    if (item.isCanChoose())
                        rankId = item.getRankId();
                    else
                        rankId=-1;
                    adpter.notifyDataSetInvalidated();
                }
            });
        }

        tvSure .setOnClickListener(this);
        tvCancel.setOnClickListener(this);
        userState.setOnClickListener(this);

    }


private String rankName;
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_sure:
                if (rankId >= 0) {
                    pointJob(rankId);
                } else {
                    ToastUtils.showShort(context,"操作过于频繁");
                }
                sectChat=null;
                dismiss();
                    //ToastUtils.showShort(context,"等级不够或者无权限任命");
                break;
            case R.id.tv_cancel:
                this.dismiss();
                break;
            case R.id.user_state:
                BuilderInstance.getInstance().getPostBuilderInstance(StaticField.URL_SECT_CHEAT_CONTROL)
                        .addParams("sectId",CartoonApp.getInstance().getUserLastInfo().getSectionId())
                        .addParams("toUserId",memberId)
                        .addParams("optType",equals?"2":"1")
                        .build().execute(new BaseCallBack() {
                    @Override
                    public void onLoadFail() {
                        ToastUtils.showShort(context,"操作失败");
                    }

                    @Override
                    public void onContentNull() {
                    }

                    @Override
                    public void onLoadSuccess(Object response) {

                        userState.setText(equals?"取消禁言":"禁言");
                        UserDB.getInstance().updataSpeak(memberId,equals?"2":"0");
                        ToastUtils.showShort(context,equals?"禁言成功":"取消禁言成功");

                    }

                    @Override
                    public Object parseNetworkResponse(String response) throws Exception {
                        return response;
                    }
                });
                dismiss();
                break;
        }
    }

    private void pointJob(final int rankId) {
        if (rankId == 0) {
            new MaterialDialog.Builder(context).title(R.string.notice)
                    .content("确定要逐出师门吗?")
                    .positiveText("确定")
                    .negativeText("取消")
                    .callback(new MaterialDialog.ButtonCallback() {
                        @Override
                        public void onPositive(MaterialDialog dialog) {
                            super.onPositive(dialog);
                            sendPoint(rankId);
                           // WebSocketUtils.getInstance().sendLeave();
                        }

                        @Override
                        public void onNegative(MaterialDialog dialog) {
                            super.onNegative(dialog);
                        }
                    }).show();
        }else{
            sendPoint(rankId);
        }

    }


    private void sendPoint(final int rankId){
        UserInfo userLastInfo = CartoonApp.getInstance().getUserLastInfo();
        if (userLastInfo != null&&userLastInfo.getSectionId()!=null ) {
            BuilderInstance.getInstance().getPostBuilderInstance(StaticField.URL_SECT_SECTPOINT)
                    .addParams("myId", CartoonApp.getInstance().getUserId())
                    .addParams("token",CartoonApp.getInstance().getToken())
                    .addParams("memberId",memberId)
                    .addParams("sectionId",userLastInfo.getSectionId())
                    .addParams("rankId",rankId+"")
                    .build().execute(new BaseCallBack<String>() {
                @Override
                public void onLoadFail() {

                }

                @Override
                public void onContentNull() {

                }

                @Override
                public void onLoadSuccess(String response) {
                    if (onClickListener != null) {
                        onClickListener.onSureClickListener(rankId, rankName);
                    }
                    DialogToast.createToastConfig().ToastShow(context,"操作成功",4);
                    UserDB.getInstance().deleteSect(memberId);
                    dismiss();
                }

                @Override
                public String parseNetworkResponse(String response) throws Exception {
                    return response;
                }
            });
        }
    }
    @Override
    public void dismiss() {
        super.dismiss();

    }

    public interface onSureClickListener{
        void onSureClickListener(int rankid, String name);
    }
    public void setOnSureClickListener(onSureClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }


}
