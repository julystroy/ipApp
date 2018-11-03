package com.cartoon.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.cartoon.data.RateData;

import cn.com.xuanjiezhimen.R;

/**
 * Created by cc on 18-1-17.
 */
public class ResultDialog extends Dialog {


    private  RateData response;
    private TextView tvResult   ;
    private TextView tvSectNmaef;
    private TextView tvSectNmaet;
    private TextView ivOver     ;
    private ImageView ivResult   ;

    /***
     构造方法
     @param context

     */


    public ResultDialog(Context context,RateData response) {
        super(context, R.style.DialogStyleDesc);
        this.response = response;
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
        setContentView(R.layout.dialog_result);
        initView();
    }

    private void initView() {
        tvResult     = (TextView) findViewById(R.id.tv_result);
        tvSectNmaef  = (TextView) findViewById(R.id.tv_sectnamef);
        tvSectNmaet   = (TextView) findViewById(R.id.tv_sectnamet);
        ivOver       = (TextView) findViewById(R.id.iv_over);
        ivResult      = (ImageView) findViewById(R.id.iv_result);

        tvResult.setText("答题答对率: "+response.getPercent()+"%");
        tvSectNmaef.setText(response.getAttackName());
        tvSectNmaet.setText(response.getDefenseName());
        if (response.getIsWin().equals("1")) {
            ivResult.setBackgroundResource(R.mipmap.ic_result_victory);
        }else
            ivResult.setBackgroundResource(R.mipmap.ic_result_looser);

        ivOver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

    }


}