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
import com.cartoon.data.response.FightListRes;
import com.cartoon.http.BaseCallBack;
import com.cartoon.http.BuilderInstance;
import com.cartoon.http.StaticField;
import com.cartoon.utils.Utils;

import cn.com.xuanjiezhimen.R;

/**
 * Created by cc on 16-10-25.
 */
 public class SectFightDialog extends Dialog  {

    private  int level;
    private String sectionId ="";
    private FightListRes.ListBean sectBean;
    private       TextView              tvSectName  ;
    private       TextView              tvSectLevel;
    private       TextView              tvFightNum  ;
    private       TextView              tvSectNum   ;
    private       TextView              tvFight          ;
    private       buttonClick           buttonClickListener;


    /***
     构造方法
     @param context

     */


    public SectFightDialog(Context context,FightListRes.ListBean sectBean,int level) {
        super(context, R.style.DialogStyleDesc);
        this.sectBean =sectBean;
        this.level =level;
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
        setContentView(R.layout.dialog_sectfight);
        initView();
    }

    private boolean b;
    private boolean c = false;
    private void initView() {
        tvSectName   = (TextView) findViewById(R.id.tv_sectname);
        tvSectLevel  = (TextView) findViewById(R.id.tv_sect_level);
        tvFightNum   = (TextView) findViewById(R.id.tv_fight_num);
        tvSectNum    = (TextView) findViewById(R.id.tv_sect_number);
        tvFight      = (TextView) findViewById(R.id.tv_fight);

        tvSectName.setText(sectBean.getSect_name());
        tvSectLevel.setText("LV"+sectBean.getSect_level());
        tvFightNum.setText(sectBean.getSectTotalAttack()+"/"+sectBean.getSectTotalDefense());
        tvSectNum.setText("成员人数:"+sectBean.getNowUserNum()+"/"+sectBean.getUser_num());

          b = Utils.isCanAttack(level,sectBean.getSect_level());
        if (!b) {
            tvFight.setText("不可攻击");
            tvFight.setSelected(false);
        } else {
            BuilderInstance.getInstance().getGetBuilderInstance(StaticField.URL_SECT_FIGHT_I)
                    .addParams("sectId",sectionId)
                    .addParams("refSectId",sectBean.getSect_id()+"")
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
                        if (response.getIsGrant() == 0) {
                            tvFight.setText("不可攻击");
                            tvFight.setSelected(false);
                        }else{
                            c= true;
                            tvFight.setSelected(true);
                            tvFight.setText("发起攻击令");
                        }

                    }
                }

                @Override
                public FightQ parseNetworkResponse(String response) throws Exception {
                    return JSON.parseObject(response,FightQ.class);
                }
            });

        }
        tvFight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (c &&buttonClickListener != null) {
                    buttonClickListener.onButtonClickListener();
                }
                dismiss();
            }
        });

    }


public interface buttonClick{
    void onButtonClickListener();
}

    public void setOnButtonClickListener(buttonClick buttonClickListener){
        this.buttonClickListener=buttonClickListener;
    }
}
