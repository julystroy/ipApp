package com.cartoon.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.cartoon.CartoonApp;
import com.cartoon.data.FightQ;
import com.cartoon.data.UserInfo;
import com.cartoon.http.BaseCallBack;
import com.cartoon.http.BuilderInstance;
import com.cartoon.http.StaticField;
import com.cartoon.utils.countdown.CountdownView;
import com.cartton.library.utils.ToastUtils;

import cn.com.xuanjiezhimen.R;

/**
 * Created by cc on 16-10-25.
 */
 public class FightingDialog extends Dialog  {


    private int defance;
    private Context context;
    private String sectLevel;
    private String sectName;
    private int sectId;
    private TextView tvSectName  ;
    private TextView tvSectLevel;
    private CountdownView slowTimer  ;
    private TextView tvSectNum   ;
    private TextView tvFight          ;
    private TextView  tvRightNum            ;
    private TextView  tvRightMENum          ;
    private buttonClick buttonClickListener;


    private String sectionId ="";
    private int sectWarId;
    private int isCanAttack; //1.可以0.不可以
    /***
     构造方法
     @param context

     */

    public FightingDialog(Context context,int defance,int sectId,String sectName,String sectLevel) {
        super(context, R.style.DialogStyleDesc);
        this.sectId = sectId;
        this.context = context;
        this.sectName = sectName;
        this.sectLevel = sectLevel;
        this.defance = defance;
        UserInfo userLastInfo = CartoonApp.getInstance().getUserLastInfo();
        if (userLastInfo!=null && userLastInfo.getSectionId()!=null){
            sectionId = userLastInfo.getSectionId();
        }
        //修改显示位置 本质就是修改 WindowManager.LayoutParams让内容水平居中 底部对齐
        WindowManager.LayoutParams attributes = getWindow().getAttributes();
        //android:gravity=bottom|center_horizonal
        attributes.gravity= Gravity.CENTER;
        getWindow().setAttributes(attributes);
    }



    /*** 方法
     @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_fighting);
        initView();
    }

    private void initView() {
        tvSectName   = (TextView) findViewById(R.id.tv_sectname);
        tvSectLevel  = (TextView) findViewById(R.id.tv_sect_level);
        slowTimer   = (CountdownView) findViewById(R.id.slowtimer);
        tvSectNum    = (TextView) findViewById(R.id.tv_fight_num);
        tvFight      = (TextView) findViewById(R.id.tv_fight);
        tvRightNum      = (TextView) findViewById(R.id.tv_fight_right);
        tvRightMENum      = (TextView) findViewById(R.id.tv_fight_rightm);

        tvSectName.setText(sectName);
        tvSectLevel.setText("LV"+sectLevel);
        tvFight.setSelected(true);
        tvFight.setText("发起攻击");
        BuilderInstance.getInstance().getPostBuilderInstance(StaticField.URL_SECT_FIGHT_B)
                .addParams("sectId",sectionId)
                .build().execute(new BaseCallBack<FightQ>() {
            @Override
            public void onLoadFail() {
            }
            @Override
            public void onContentNull() {
            }
            @Override
            public void onLoadSuccess(FightQ response) {
                if (response != null) {
                    int de = defance - response.getSurplusDefenses();
                    if (de<0)
                        de=0;

                    tvSectNum.setText(de+"/"+defance);
                    tvRightNum.setText(response.getTotalRightNum()+"");
                    tvRightMENum.setText(response.getUserRightNum()+"");
                     isCanAttack = response.getIsCanAttack();
                     sectWarId = response.getSectWarId();

                    if (slowTimer.getRemainTime() != 0) {
                        slowTimer.allShowZero();
                        slowTimer.stop();
                    }
                    slowTimer.start(response.getRemainingTimes());
                }
            }
            @Override
            public FightQ parseNetworkResponse(String response) throws Exception {
                return JSON.parseObject(response,FightQ.class);
            }
        });
        tvFight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isCanAttack ==1&&buttonClickListener != null) {
                    buttonClickListener.onButtonClickListener(sectWarId);
                }else{
                    ToastUtils.showShort(context,"暂时不能攻击");
                }

                dismiss();
            }
        });

    }

    @Override
    public void dismiss() {
        super.dismiss();
        if (slowTimer!=null)slowTimer.stop();
    }

    public interface buttonClick{
    void onButtonClickListener(int sectWarId);
}

    public void setOnButtonClickListener(buttonClick buttonClickListener){
        this.buttonClickListener=buttonClickListener;
    }
}
