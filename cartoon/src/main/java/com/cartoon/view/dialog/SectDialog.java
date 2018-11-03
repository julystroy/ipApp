package com.cartoon.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.cartoon.utils.StringUtils;
import com.cartoon.view.DialogToast;

import cn.com.xuanjiezhimen.R;

/**
 * Created by cc on 16-10-25.
 */
 public class SectDialog extends Dialog implements View.OnClickListener {
    private  Context context;
    /***
     构造方法
     @param context

     */
    private TextView etName;
    private TextView tvCancel;
    private TextView tvSure;
    private BuildSectClickListener sectListener;


    public SectDialog(Context context) {
        super(context, R.style.DialogStyleDesc);
        this.context = context;
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
        setContentView(R.layout.item_sect_dialog);
       // LoadData();
        etName   = (EditText) findViewById(R.id.et_sect_name);
        tvCancel = (TextView) findViewById(R.id.tv_cancel);
        tvSure   = (TextView) findViewById(R.id.tv_sure);
        etName .setOnClickListener(this);
        tvCancel.setOnClickListener(this);
        tvSure.setOnClickListener(this);

    }
//下载数据



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_cancel:
                this.dismiss();
                break;
            case R.id.tv_sure:
                String et = etName.getText().toString();
                if (!et.isEmpty()){
                    if (StringUtils.isAllChinese(et)){
                        if (et.getBytes().length<20){
                            if (sectListener!=null)
                                this.sectListener.buildSectLisener(et);
                            this.dismiss();
                        }else{
                            DialogToast.createToastConfig().ToastShow(context,"门派名录过长",3);
                        }
                    }else{
                        DialogToast.createToastConfig().ToastShow(context,"含有非文字字符",3);
                    }
                }else{
                    DialogToast.createToastConfig().ToastShow(context,"名称不能为空",3);
                }
                break;
        }
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }

    public interface BuildSectClickListener{
        public void buildSectLisener(String et);
    }

    public void setOnBuildSectClickListner(BuildSectClickListener sectClickListner){
        this.sectListener = sectClickListner;
    }
}
