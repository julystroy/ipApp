package com.cartoon.module.newmodules;

import android.animation.Animator;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.ViewGroup;

import com.cartoon.data.NewBase;

import java.util.List;

import cn.com.xuanjiezhimen.R;

/**
 * 选集对话框
 * Created by debuggerx on 16-11-21.
 */

public class NewBasePopDialog extends Dialog {
    private int modulesType;
    private List<NewBase> mList;


    /**
     * @param context 上下文
     * @param modulesType 当前模块对应的Type
     */
    public NewBasePopDialog(Context context , int modulesType) {
        super(context, R.style.popuDialog);
        this.mContext = context;
        this.modulesType = modulesType;
    }


    public interface OnHideListener {
        public void hide();
    }

    Context                           mContext;
    /**
     * 自定义菜单view
     */
    NewBasePopView                    mNewBasePopuView;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newbase_popu_dailog);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }

    public void setData(List<NewBase> mList) {
        this.mList = mList;
    }

    @Override
    public void show() {
        super.show();
        mNewBasePopuView = (NewBasePopView) findViewById(R.id.commonPopuView);
        mNewBasePopuView.setData(mList , modulesType);

        mNewBasePopuView.setOnHideListener(mOnHideListener);
        mNewBasePopuView.show();
    }

    @Override
    public void dismiss() {
        mNewBasePopuView = (NewBasePopView) findViewById(R.id.commonPopuView);
        mNewBasePopuView.hide(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                NewBasePopDialog.super.dismiss();
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

