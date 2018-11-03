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
 public class DeleteDialog extends Dialog implements View.OnClickListener {

    private  String text;
    private Context context;
    private TextView tvDelete;
    private TextView tvCancel;
    private buttonClick buttonClickListener;


    /***
     构造方法
     @param context

     */


    public DeleteDialog(Context context, String text) {
        super(context, R.style.DialogStyleDesc);
        this.context = context;

        this.text = text;
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
        setContentView(R.layout.dialog_delete);
        initView();
    }

    private void initView() {


        tvDelete = (TextView) findViewById(R.id.btn_delete);
        tvCancel = (TextView) findViewById(R.id.cancel_delete);

        if (text!=null)
        tvDelete.setText(text);
        tvDelete.setOnClickListener(this);
        tvCancel.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_delete:
                if (buttonClickListener != null) {
                    buttonClickListener.onButtonClickListener();
                }
                dismiss();
                break;
            case R.id.cancel_delete:
                dismiss();
                break;

        }
    }
public interface buttonClick{
    void onButtonClickListener();
}

    public void setOnButtonClickListener(buttonClick buttonClickListener){
        this.buttonClickListener=buttonClickListener;
    }
}
