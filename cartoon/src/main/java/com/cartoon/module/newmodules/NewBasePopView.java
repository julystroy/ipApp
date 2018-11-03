package com.cartoon.module.newmodules;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cartoon.data.NewBase;
import com.cartoon.view.CustomItemDecoration;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.com.xuanjiezhimen.R;

/**
 * Created by debuggerx on 16-11-21.
 */
public class NewBasePopView extends RelativeLayout implements View.OnClickListener {


    private OnClickListener mOnClick = null;
    private final int ANIMATION_TIME = 300;

    @BindView(R.id.tv_desc)
    TextView       tvDesc;
    @BindView(R.id.tv_title)
    TextView       tvTitle;
    @BindView(R.id.tv_sort)
    TextView       tvSort;
    @BindView(R.id.rl_cancel)
    RelativeLayout rlCancel;
    @BindView(R.id.recycle_view)
    RecyclerView   recycleView;
    @BindView(R.id.common_root)
    View           mRoot;
    @BindView(R.id.popu_layout)
    LinearLayout   mItemLayout;

    private Context               mContext;
    private NewBaseCollectAdapter adapter;

    private NewBasePopDialog.OnHideListener mOnHideListener;
    private int modulesType;

    public NewBasePopView(Context context) {
        this(context, null, 0);
        mContext = context;
    }

    public NewBasePopView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        mContext = context;
    }

    public NewBasePopView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        inflate(context, R.layout.common_popu_view, this);
        mContext = context;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this, this);
        initView();
    }


    private void initView() {
        adapter = new NewBaseCollectAdapter(mContext);
        GridLayoutManager layoutManager = new GridLayoutManager(mContext, 3);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycleView.setLayoutManager(layoutManager);
        recycleView.addItemDecoration(new CustomItemDecoration((int) getResources().getDimension(R.dimen.dp10), 3));
        recycleView.setAdapter(adapter);
    }

    public void show() {
        AlphaAnimation animation = new AlphaAnimation(0f, 1f);
        animation.setDuration(ANIMATION_TIME);
        mRoot.startAnimation(animation);

        int height = mItemLayout.getResources().getDisplayMetrics().heightPixels;
        mItemLayout.setTranslationY(height);
        PropertyValuesHolder holder = PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, 0);
        ObjectAnimator animator = new ObjectAnimator();
        animator.setTarget(mItemLayout);
        animator.setValues(holder);
        animator.setDuration(ANIMATION_TIME);
        animator.setStartDelay(10);
        animator.start();
    }

    public void hide(Animator.AnimatorListener listener) {
        AlphaAnimation animation = new AlphaAnimation(1f, 0f);
        animation.setDuration(ANIMATION_TIME);
        mRoot.startAnimation(animation);

        int dHeight = mItemLayout.getResources().getDisplayMetrics().heightPixels;
        int height = mItemLayout.getHeight();
        PropertyValuesHolder holder = PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, height);
        ObjectAnimator animator = new ObjectAnimator();
        animator.setTarget(mItemLayout);
        animator.setValues(holder);
        animator.setDuration(ANIMATION_TIME);
        animator.start();
        if (listener != null)
            animator.addListener(listener);
    }

    private List<NewBase> mList;

    public void setData(List<NewBase> mList , int modulesType) {
        this.modulesType = modulesType;
        this.mList = mList;
        adapter.setData(mList , modulesType);
        tvDesc.setText("共" + mList.size() + "话");
        //        findViewById(R.id.common_root).setOnClickListener(this);
        findViewById(R.id.rl_cancel).setOnClickListener(this);
        findViewById(R.id.tv_sort).setOnClickListener(this);
    }

    @Override
    public void onClick(final View view) {

        switch (view.getId()) {
            case R.id.rl_cancel:
                if (mOnHideListener != null) {
                    mOnHideListener.hide();
                }
                break;
            case R.id.tv_sort:
                if (mList != null) {
                    Collections.reverse(mList);
                    adapter.setData(mList , modulesType);
                }
                break;
        }

        if (mOnClick != null) {
            mOnClick.onClick(view);
        }
    }

    public void setOnHideListener(NewBasePopDialog.OnHideListener listener) {
        mOnHideListener = listener;
        adapter.setOnHideListener(listener);

    }

}

