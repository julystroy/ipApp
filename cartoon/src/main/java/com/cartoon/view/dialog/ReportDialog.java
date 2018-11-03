package com.cartoon.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.cartoon.utils.SendToBG;

import cn.com.xuanjiezhimen.R;

/**
 * Created by cc on 16-10-25.
 */
 public class ReportDialog extends Dialog implements View.OnClickListener {

    private  Context context;
    private String badUid;
    private  String commentId;
    private  String level;
    private TextView tvFirst;
    private TextView tvSecond;
    private TextView tvThrid;
    private TextView tvCancel;



    /***
     构造方法
     @param context

     */


    public ReportDialog(Context context,String id, String commentId, String i) {
        super(context, R.style.DialogStyleDesc);
        this.context = context;
        this.badUid = id;
        this.commentId = commentId;
        this.level = i;
        //修改显示位置 本质就是修改 WindowManager.LayoutParams让内容水平居中 底部对齐
        WindowManager.LayoutParams attributes = getWindow().getAttributes();
        //android:gravity=bottom|center_horizonal
        attributes.gravity= Gravity.BOTTOM;
        getWindow().setAttributes(attributes);
    }



    /*** 方法
     @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_report);
        initView();
    }

    private void initView() {

        tvFirst = (TextView) findViewById(R.id.first_reason);
        tvSecond = (TextView) findViewById(R.id.second_reason);
        tvThrid = (TextView) findViewById(R.id.thrid_reason);
        tvCancel = (TextView) findViewById(R.id.cancel_report);

        tvFirst.setOnClickListener(this);
        tvSecond.setOnClickListener(this);
        tvThrid .setOnClickListener(this);
        tvCancel.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.first_reason:
                SendToBG.sendReport(context,badUid,"1",commentId,String.valueOf(level));
                dismiss();
                break;
            case R.id.second_reason:
                SendToBG.sendReport(context,badUid,"2",commentId,String.valueOf(level));
                dismiss();
                break;
            case R.id.thrid_reason:
                SendToBG.sendReport(context,badUid,"3",commentId,String.valueOf(level));
                dismiss();
                break;
            case R.id.cancel_report:
                dismiss();
                break;
        }
    }


}
