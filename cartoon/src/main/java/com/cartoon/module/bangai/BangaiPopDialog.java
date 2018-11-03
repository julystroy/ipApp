///**********************************************************
// * _              _  _  _  ____          __   _       *
// * / \   _ __   __| || |(_)/ ___|  ___ __/ /__| |_     *
// * / _ \ | '_  \/ _  || || |\___ \ / _ \_   _|_   _|    *
// * / ___ \| | | | (_) || || | ___) | (_) || |   | |_     *
// * /_/   \_|_| |_|\____)|_||_||____/ \___/ /_/   \___|    *
// * *
// * *********************************************************
// * Copyright 2015, AndliSoft.com.                         *
// * All rights, including trade secret rights, reserved.   *
// **********************************************************/
//
//package com.cartoon.module.bangai;
//
//
//import android.animation.Animator;
//import android.animation.Animator.AnimatorListener;
//import android.app.Activity;
//import android.app.Dialog;
//import android.content.Context;
//import android.os.Bundle;
//import android.view.ViewGroup.LayoutParams;
//
//import com.cartoon.bean.Bangai;
//import com.cartoon.data.NewBase;
//
//import java.util.List;
//
//import cn.com.xuanjiezhimen.R;
//
///**
// * 选集对话框
// */
//public class BangaiPopDialog extends Dialog {
//    private Activity mActivity;
//    private List<NewBase> mList;
//
//    public BangaiPopDialog(Context context, Activity activity) {
//        super(context, R.style.popuDialog);
//        this.mContext = context;
//        this.mActivity = activity;
//    }
//
//
//    public interface OnHideListener {
//        public void hide();
//    }
//
//    Context mContext;
//    /**
//     * 自定义菜单view
//     */
//    BangaiPopView mCommonPopuView;
//    /**
//     * 点击事件
//     */
//    android.view.View.OnClickListener mListener;
//
//    /**
//     * 隐藏Listener
//     */
//    OnHideListener mOnHideListener = new OnHideListener() {
//        @Override
//        public void hide() {
//            dismiss();
//        }
//    };
//
//    /**
//     * @param context 上下文
//     */
//    public BangaiPopDialog(Context context) {
//        super(context, R.style.popuDialog);
//        this.mContext = context;
//    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.bangai_popu_dailog);
//        getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
//    }
//
//    public void setData(List<NewBase> mList) {
//        this.mList = mList;
//    }
//
//    @Override
//    public void show() {
//        super.show();
//        mCommonPopuView = (BangaiPopView) findViewById(R.id.commonPopuView);
//        mCommonPopuView.setData(mList , mActivity);
//
//        mCommonPopuView.setOnHideListener(mOnHideListener);
//        mCommonPopuView.show();
//    }
//
//    @Override
//    public void dismiss() {
//        mCommonPopuView = (BangaiPopView) findViewById(R.id.commonPopuView);
//        mCommonPopuView.hide(new AnimatorListener() {
//
//            @Override
//            public void onAnimationStart(Animator animation) {
//            }
//
//            @Override
//            public void onAnimationRepeat(Animator animation) {
//            }
//
//            @Override
//            public void onAnimationEnd(Animator animation) {
//                BangaiPopDialog.super.dismiss();
//            }
//
//            @Override
//            public void onAnimationCancel(Animator animation) {
//            }
//        });
//    }
//
//    @Override
//    public void onBackPressed() {
//        dismiss();
//    }
//
//}
