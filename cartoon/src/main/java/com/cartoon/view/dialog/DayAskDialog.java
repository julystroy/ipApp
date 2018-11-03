package com.cartoon.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import cn.com.xuanjiezhimen.R;

/**
 * Created by cc on 16-10-25.
 */
 public class DayAskDialog extends Dialog implements View.OnClickListener {


    private  OnDialogClickListener OnDialogClickListener;



    /***
     构造方法
     @param context

     */

    private TextView mbtCost;

    public DayAskDialog(Context context) {
        super(context, R.style.DialogStyleDesc);
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
        setContentView(R.layout.dialog_question_desc);
        initData();
    }

    private void initData() {
         mbtCost= (TextView) findViewById(R.id.bt_cost_stone);
        mbtCost.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

            if (OnDialogClickListener!=null)
            this.OnDialogClickListener.onDilogClick(mbtCost);

        //super.dismiss();
    }

    public void setOnClickListener(OnDialogClickListener OnClickListener){
        this.OnDialogClickListener = OnClickListener;
    }


    public interface OnDialogClickListener{
        public void onDilogClick(TextView view);
    }

}
