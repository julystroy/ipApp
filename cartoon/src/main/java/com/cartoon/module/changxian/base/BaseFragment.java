package com.cartoon.module.changxian.base;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.cartoon.view.EmptyView;

import cn.com.xuanjiezhimen.R;

public abstract class BaseFragment extends Fragment {
//    private static final Logger LOG = LoggerFactory.getLogger(BaseFragment.class);

    private FragmentActivity mContext;
    private EmptyView        mEmptyView;

    private long enterTime;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

//        LOG.debug("onAttach. [{}]", getClass().getSimpleName());

        mContext = (FragmentActivity) activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        LOG.debug("onCreate. [{}]", getClass().getSimpleName());

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        LOG.debug("onCreateView. [{}]", getClass().getSimpleName());

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        LOG.debug("onViewCreated. [{}]", getClass().getSimpleName());
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

//        LOG.debug("onActivityCreated. [{}]", getClass().getSimpleName());

        addEmptyView();
    }

    @Override
    public void onStart() {
        super.onStart();

//        LOG.debug("onStart. [{}]", getClass().getSimpleName());
    }

    @Override
    public void onResume() {
        super.onResume();

//        LOG.debug("onResume. [{}]", getClass().getSimpleName());

//        enterTime = new Date().getTime() / 1000;
//        UploadLog.uploadPageStartLog(getClass().getSimpleName(), getActivity());
//        MobclickAgent.onPageStart(getClass().getSimpleName());
    }

    @Override
    public void onPause() {
        super.onPause();

//        LOG.debug("onPause. [{}]", getClass().getSimpleName());
//        long duration = new Date().getTime() / 1000 - enterTime;
//        UploadLog.uploadPageEndLog(getClass().getSimpleName(), duration, getActivity());
//        MobclickAgent.onPageEnd(getClass().getSimpleName());
    }

    @Override
    public void onStop() {
        super.onStop();

//        LOG.debug("onStop. [{}]", getClass().getSimpleName());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

//        LOG.debug("onDestroyView. [{}]", getClass().getSimpleName());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

//        LOG.debug("onDestroy. [{}]", getClass().getSimpleName());
    }

    @Override
    public void onDetach() {
        super.onDetach();

//        LOG.debug("onDetach. [{}]", getClass().getSimpleName());
    }

    protected BaseActivity getBaseActivity() {
        return (BaseActivity) getActivity();
    }

    protected void setTitle(CharSequence title) {
        getActivity().setTitle(title);
    }

    protected void setTitle(int resId) {
        setTitle(getString(resId));
    }

    protected final View findViewById(int viewId) {
        return getView().findViewById(viewId);
    }

    protected void finish() {
//        LOG.debug("finish.");

        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else {
            getActivity().finish();
        }
    }

    private ProgressDialog mProgressDialog;

    protected void showProgressBar(String msg) {
        showProgressBar(msg, true);
    }

    protected void showProgressBar(String msg, boolean cancel) {
        mProgressDialog = ProgressDialog.show(getActivity(), null, msg);
        mProgressDialog.setCanceledOnTouchOutside(cancel);
        mProgressDialog.setCancelable(cancel);
    }

    protected void hideProgressBar() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }

    protected void setContentFragment(Class<? extends BaseFragment> fragmentClass, Bundle arguments) {
//        LOG.debug("set content fragment. class={}", fragmentClass.getName());

        Fragment fragment = Fragment.instantiate(mContext, fragmentClass.getName(), arguments);
        FragmentTransaction t = mContext.getSupportFragmentManager().beginTransaction();
        t.replace(R.id.content_frame, fragment);
        t.commit();
    }

    protected void hideSoftInput(View view) {
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /**
     * 原理：empty view 与 contentview(可以为listview,recycleview等) 同级，他们UI层级为兄弟关系
     */
    private void addEmptyView() {
        if (getContentView() != null) {
            if (mEmptyView == null) {
                mEmptyView = new EmptyView(getActivity());
                mEmptyView.setOnRefreshListener(mOnRefreshListener);

                ((ViewGroup) getContentView().getParent()).addView(mEmptyView);
            } else {
                //如果拉取数据不成功则不为STATE_INIT,切换tab回来再次create 新的content view后继续 add EmptyView
                if (mEmptyView.getState() != EmptyView.STATE_INIT) {
                    mEmptyView = null; //避免mEmptyView多个parent 避免StackOverflowError
                    addEmptyView(); //递归调用
                }
            }
        }
    }

    protected EmptyView getEmptyView() {
        return mEmptyView;
    }

    protected View getContentView() {
        return null;
    }

    protected void showContentView() {
        if (getContentView() != null) {
            getContentView().setVisibility(View.VISIBLE);
        }
        if (getEmptyView() != null) {
            getEmptyView().setVisibility(View.GONE);
        }
    }

    protected void showEmptyView(int state) {
        showEmptyView(state, 0);
    }

    protected void showEmptyView(int state, int tips) {
        if (getContentView() != null) {
            getContentView().setVisibility(View.GONE);
        }
        if (getEmptyView() != null) {
            getEmptyView().setVisibility(View.VISIBLE);
            getEmptyView().setEmptyTips(tips);
            getEmptyView().updateViews(state);
        }
    }

    protected void loadData() {
    }

    private EmptyView.OnRefreshListener mOnRefreshListener = new EmptyView.OnRefreshListener() {
        @Override
        public void onRefresh() {
            if (getEmptyView() != null) {
                getEmptyView().updateViews(EmptyView.STATE_INIT);
                loadData();
            }
        }
    };
}
