package com.cartoon.module;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import cn.com.xuanjiezhimen.R;

/**
 * Created by David on 16/5/16.
 **/
public abstract class BaseFragment extends Fragment {

    protected static final int START_PAGE = 1;
    protected static final int PAGE_SIZE = 10;

    /**
     * 排序顺序。默认是降序
     */
    protected static final String SORT_DESC = "desc";
    protected static final String SORT_ASC = "asc";

    protected BaseActivity mActivity;

    /**
     * 页面数据排序
     */
    protected String sort_order = SORT_DESC;

    /**
     * 布局ID
     */
    protected abstract int getLayoutId();

    /**
     * 初始化页面信息
     */
    protected abstract void initView(View view, Bundle savedInstanceState);

    /**
     * 获取宿主Activity
     */
    public BaseActivity getHoldingActivity() {
        return mActivity;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mActivity = (BaseActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutId(), container, false);
        ButterKnife.bind(this, view);
        initView(view, savedInstanceState);
        return view;
    }

    protected void addFragment(BaseActivity activity, BaseFragment fragment) {
        activity.addFragment(fragment);
    }

    /**
     * 反转排序顺序
     */
    protected String revertSortOrder() {
        if (TextUtils.equals(sort_order, SORT_DESC)) {
            sort_order = SORT_ASC;
            return mActivity.getResources().getString(R.string.sort_desc);
        } else {
            sort_order = SORT_DESC;
            return mActivity.getResources().getString(R.string.sort_asc);
        }
    }
}
