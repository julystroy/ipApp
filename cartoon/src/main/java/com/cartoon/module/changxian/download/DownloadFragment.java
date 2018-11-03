package com.cartoon.module.changxian.download;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.cartoon.module.changxian.base.BaseFragment;
import com.cartoon.utils.UserDB;
import com.cartoon.view.EmptyView;

import cn.com.xuanjiezhimen.R;

public class DownloadFragment extends BaseFragment {
    private ListView mListView;
    private DownloadAdapter mAdapter;
    private boolean mFirstResume = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle(R.string.my_download);
        mAdapter = new DownloadAdapter(getActivity());
        mAdapter.mEmptyListener = new DownloadAdapter.OnDownloadEmptyListener() {
            @Override
            public void showEmpty() {
                showEmptyView(EmptyView.STATE_NO_DOWNLOAD);
            }
        };
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        return inflater.inflate(R.layout.fragment_download, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews();
    }

    @Override
    public void onResume() {
        super.onResume();

        if (!mFirstResume) {
            mAdapter.notifyDataSetChanged();
        }
        mFirstResume = false;
    }

    @Override
    protected View getContentView() {
        return mListView;
    }

    private void initViews() {
        mListView = (ListView) findViewById(R.id.listview);
        mListView.setAdapter(mAdapter);
        mAdapter.setData(UserDB.getInstance().findAllGameDownload());
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (mAdapter.getCount() == 0) {
            showEmptyView(EmptyView.STATE_NO_DOWNLOAD);
        } else {
            showContentView();
        }
    }
}
