package com.cartoon.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import cn.com.xuanjiezhimen.R;

/**
 * VDH的容器
 * VDH
 *
 * @author shidu
 * Created by shidu on 17/4/17.
 */
public class VDHContainerLayout extends LinearLayout {

    private VDHLayoutLite mVDHLayout;

    public VDHContainerLayout(Context context) {
        this(context, null);
    }

    public VDHContainerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        View view = inflate(context, R.layout.layout_vdh_container, this);
        view.setPadding(0, -1, 0, 0);
        mVDHLayout = (VDHLayoutLite) view.findViewById(R.id.vdh_root);
        View logoBtn = mVDHLayout.getDragView();
        logoBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onClick(v);
                }
            }
        });
    }

    private OnClickListener mListener;

    public void setRootviewClickListener(OnClickListener onClickListener) {
        mListener = onClickListener;
    }
}