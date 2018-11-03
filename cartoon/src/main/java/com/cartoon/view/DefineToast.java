package com.cartoon.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import cn.com.xuanjiezhimen.R;

/**
 * Created by cc on 16-12-15.
 */
@SuppressLint("ResourceAsColor")
public class DefineToast {
    private static DefineToast toastCommom;

    private Toast toast;

    private DefineToast(){
    }

    public static DefineToast createToastConfig(){
        if (toastCommom==null) {
            toastCommom = new DefineToast();
        }
        return toastCommom;
    }

    /**
     * 显示Toast
     * @param context
     * @param tvString
     */

    public void ToastShow(Context context, String tvString){
        View layout = LayoutInflater.from(context).inflate(R.layout.toast_xml,null);
        TextView text = (TextView) layout.findViewById(R.id.tv_show);
        /*ImageView mImageView = (ImageView) layout.findViewById(R.id.tv_show);
        mImageView.setBackgroundResource(R.drawable.ic_launcher);*/
        text.setText(tvString);
        //text.setTextColor(Color.parseColor("#2f2f2f"));

        toast = new Toast(context);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }
}
