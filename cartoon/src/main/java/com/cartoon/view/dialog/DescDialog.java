package com.cartoon.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import cn.com.xuanjiezhimen.R;

/**
 * Created by cc on 16-10-25.
 */
 public class DescDialog extends Dialog implements View.OnClickListener {
    private  int type;
    /***
     构造方法
     @param context

     */

    private Button mbtKnow;

    public DescDialog(Context context,int type) {
        super(context, R.style.DialogStyleDesc);
        this.type =type;
        //修改显示位置 本质就是修改 WindowManager.LayoutParams让内容水平居中 底部对齐
        WindowManager.LayoutParams attributes = getWindow().getAttributes();
        //android:gravity=bottom|center_horizonal
        attributes.gravity= Gravity.CENTER|Gravity.CENTER_HORIZONTAL;
        getWindow().setAttributes(attributes);
    }
    /*** 方法
     @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_style_desc);
        initData();
    }

    private void initData() {

         mbtKnow= (Button) findViewById(R.id.bt_quicklisten_know);
        TextView tvTitle = (TextView) findViewById(R.id.desc_title);
        TextView tvONe = (TextView) findViewById(R.id.desc_one);
        TextView tvTwo = (TextView) findViewById(R.id.desc_two);
        TextView tvThree = (TextView) findViewById(R.id.desc_three);
        mbtKnow.setOnClickListener(this);
        if (type == 1) {
            tvTitle.setText("操作流程");
            tvONe.setText("1.扫描卡可通过官方或者其他渠道获得;");
            tvTwo.setVisibility(View.GONE);
            tvThree.setText("2.扫描后模型可以用双指放大，可以单指控制旋转。");
        } else if (type == 2) {
            tvTitle.setText("AR使用说明");
            tvONe.setText("1.从商店购买或者参加活动获得AR卡;");
            tvTwo.setText("2.打开APP—我的—扫描AR卡—打开摄像头;");
            tvThree.setText("3.对准卡片正面扫描，即可获得全新体验;");
        }else{
            tvTitle.setText("提示");
            tvThree.setText("完成签到、日常任务、答题中的任意一个行为都属于修行了，每一个日升日落都值得怀念！");
            tvTwo.  setVisibility(View.GONE);
            tvONe.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        super.dismiss();
    }
}
