package com.cartoon.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import cn.com.xuanjiezhimen.R;


/**
 * Created by cc on 16-12-15.
 */
@SuppressLint("ResourceAsColor")
public class DialogToast {
    private static DialogToast toastCommom;

    private Toast toast;


    private DialogToast(){
    }

    public static DialogToast createToastConfig(){
        if (toastCommom==null) {
            toastCommom = new DialogToast();
        }
        return toastCommom;
    }

    /**
     * 显示Toast
     * @param context
     * @param text
     */

    public void ToastShow(Context context, String text, int kind){//1  water   2  smile   3  worn  4 success
        View layout = LayoutInflater.from(context).inflate(R.layout.item_app_dialog,null);
        ImageView imgShow   = (ImageView) layout.findViewById(R.id.iv_img);
        TextView tvTextShow  = (TextView) layout.findViewById(R.id.tv_dialog_text);
        if (kind == 1) {
            if (text!= null)
                tvTextShow.setText(text);
            imgShow.setImageResource(R.mipmap.ic_load_show);
        } else if (kind == 3) {
            if (text != null)
                tvTextShow.setText(text);
            imgShow.setImageResource(R.mipmap.icon_wron_words);
        } else if (kind == 4) {
            if (text != null)
                tvTextShow.setText(text);
            imgShow.setImageResource(R.mipmap.ic_success);
        } else {
            if (text != null)
                tvTextShow.setText(text);
            imgShow.setImageResource(R.mipmap.ic_worn);
        }

        toast = new Toast(context);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }
}
