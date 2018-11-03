package com.cartoon.module.changxian.detail.account;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cartoon.data.DetailPage;
import com.cartoon.module.changxian.base.BaseFragment;
import com.github.ksoichiro.android.observablescrollview.ObservableListView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;

import cn.com.xuanjiezhimen.R;

public class TabAccountFragment extends BaseFragment {
    private ObservableListView mListView;
    private DetailAccountAdapter mAdapter;

    private DetailPage mDetailPage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAdapter = new DetailAccountAdapter(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        mListView = (ObservableListView) view.findViewById(R.id.scroll);
        mListView.setAdapter(mAdapter);

        Fragment parentFragment = getParentFragment();
        ViewGroup viewGroup = (ViewGroup) parentFragment.getView();
        if (viewGroup != null) {
            mListView.setTouchInterceptionViewGroup((ViewGroup) viewGroup.findViewById(R.id.root));
            if (parentFragment instanceof ObservableScrollViewCallbacks) {
                mListView.setScrollViewCallbacks((ObservableScrollViewCallbacks) parentFragment);
            }
        }
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (mDetailPage != null) {
            mAdapter.updateList(mDetailPage);
        }
    }

    public void setData(DetailPage detailPage) {
        mDetailPage = detailPage;
    }
}
