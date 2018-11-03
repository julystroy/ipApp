/**********************************************************
 * _              _  _  _  ____          __   _       *
 * / \   _ __   __| || |(_)/ ___|  ___ __/ /__| |_     *
 * / _ \ | '_  \/ _  || || |\___ \ / _ \_   _|_   _|    *
 * / ___ \| | | | (_) || || | ___) | (_) || |   | |_     *
 * /_/   \_|_| |_|\____)|_||_||____/ \___/ /_/   \___|    *
 * *
 * *********************************************************
 * Copyright 2015, AndliSoft.com.                         *
 * All rights, including trade secret rights, reserved.   *
 **********************************************************/

package com.cartoon.common;


import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.ViewGroup.LayoutParams;

import com.cartoon.data.Cartoon;
import cn.com.xuanjiezhimen.R;

import java.util.List;


/**
 * 选集对话框
 */
public class CommonPopDialog extends Dialog {
    private List<Cartoon> mList;

    public interface OnHideListener {
        public void hide();
    }

    Context mContext;
    /**
     * 自定义菜单view
     */
    CommonPopView mCommonPopuView;
    /**
     * 点击事件
     */
    android.view.View.OnClickListener mListener;

    /**
     * 隐藏Listener
     */
    OnHideListener mOnHideListener = new OnHideListener() {
        @Override
        public void hide() {
            dismiss();
        }
    };

    /**
     * @param context 上下文
     */
    public CommonPopDialog(Context context) {
        super(context, R.style.popuDialog);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_popu_dailog);
        getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    }

    public void setData(List<Cartoon> mList) {
        this.mList = mList;
    }

    @Override
    public void show() {
        super.show();
        mCommonPopuView = (CommonPopView) findViewById(R.id.commonPopuView);
        mCommonPopuView.setData(mList);

        mCommonPopuView.setOnHideListener(mOnHideListener);
        mCommonPopuView.show();
    }

    @Override
    public void dismiss() {
        mCommonPopuView = (CommonPopView) findViewById(R.id.commonPopuView);
        mCommonPopuView.hide(new AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                CommonPopDialog.super.dismiss();
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }
        });
    }

    @Override
    public void onBackPressed() {
        dismiss();
    }

}
