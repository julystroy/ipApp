package com.cartoon.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.com.xuanjiezhimen.R;

/**
 * Created by cc on 16-10-25.
 */
 public class AppVersionDialog extends Dialog implements View.OnClickListener {

    private  String version;
    private  String content;
    private TextView appVersion   ;
    private TextView contentUpdata;
    private TextView waitUpdata   ;
    private TextView Updata       ;
    private TextView forceUpdata       ;
    private LinearLayout llUpdata       ;
    private buttonClick buttonClickListener;
    private int type; //1  updata   2  forceupdata


    /***
     构造方法
     @param context

     */


    public AppVersionDialog(Context context, String appVersion,String contentApdata,int type) {
        super(context, R.style.DialogStyleDesc);
        this.type =type;
        this.content =contentApdata;
        this.version = appVersion;
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
        setContentView(R.layout.dialog_appversion);
        initView();
    }

    private void initView() {

        appVersion   = (TextView) findViewById(R.id.tv_app_version);
        contentUpdata = (TextView) findViewById(R.id.tv_content_updata);
        waitUpdata   = (TextView) findViewById(R.id.tv_wait_updata);
        Updata       = (TextView) findViewById(R.id.tv_updata);
        forceUpdata  = (TextView) findViewById(R.id.tv_force_updata);
        llUpdata     = (LinearLayout) findViewById(R.id.ll_is_updata);

        appVersion.setText("[- v"+version+" -]");
        contentUpdata.setText(Html.fromHtml(content));

        if (type == 1) {
            forceUpdata.setVisibility(View.GONE);
            llUpdata.setVisibility(View.VISIBLE);
            waitUpdata  .setOnClickListener(this);
            Updata      .setOnClickListener(this);
        }else{
            llUpdata.setVisibility(View.GONE);
            forceUpdata.setVisibility(View.VISIBLE);
            forceUpdata .setOnClickListener(this);
        }




    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_updata:
                if (buttonClickListener != null) {
                    buttonClickListener.onButtonClickListener();
                }
                dismiss();
                break;
            case R.id.tv_force_updata:
                if (buttonClickListener != null) {
                    buttonClickListener.onButtonClickListener();
                }
               // dismiss();
                break;
            case R.id.tv_wait_updata:
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
