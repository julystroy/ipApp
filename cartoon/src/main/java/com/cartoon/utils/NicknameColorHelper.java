package com.cartoon.utils;

import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.widget.TextView;

import cn.com.xuanjiezhimen.R;

/**
 * Created by jinbangzhu on 8/22/16.
 */

public class NicknameColorHelper {
    public static void setNicknameColor(TextView textView, int uid) {
        if (uid == 1||uid==92) {// admin or editor
            textView.setTextColor(textView.getContext().getResources().getColor(R.color.system_message_nickname));
        } else {
            textView.setTextColor(textView.getContext().getResources().getColor(R.color.default_nickname));
            String currentName = textView.getText().toString();
            Spanned coloredName = Html.fromHtml(currentName);
            textView.setText(coloredName);
        }
    }

    public static void setNicknameColor(TextView textView, String uid) {
        if (TextUtils.equals("1" , uid)||TextUtils.equals("92",uid)) {// admin or editor
            textView.setTextColor(textView.getContext().getResources().getColor(R.color.system_message_nickname));
        } else {
            textView.setTextColor(textView.getContext().getResources().getColor(R.color.default_nickname));
            String currentName = textView.getText().toString();
            Spanned coloredName = Html.fromHtml(currentName);
            textView.setText(coloredName);
            //System.out.println(textView.getText());
        }
    }
}
