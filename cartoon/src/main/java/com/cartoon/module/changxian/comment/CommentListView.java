package com.cartoon.module.changxian.comment;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;

import com.cartoon.data.Comment;
import com.cartoon.view.FullyExpandedListView;

import java.util.List;

import cn.com.xuanjiezhimen.R;

/**
 * Created by shidu on 17/1/4.
 */
public class CommentListView extends LinearLayout {
    private CommentsHeaderItem headerView;
    private View mEmptyCommentView;

    private FullyExpandedListView gvIcon;
    private CommentsAdapter       mAdapter;

    public CommentListView(Context context) {
        super(context);

        init();
    }

    public CommentListView(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
        init();
    }

    protected void init() {
        inflate(getContext(), R.layout.layout_comments, this);
        gvIcon = (FullyExpandedListView) findViewById(R.id.iv_comments);
        mAdapter = new CommentsAdapter(getContext());

        LayoutInflater factory = LayoutInflater.from(getContext());
        mEmptyCommentView = factory.inflate(R.layout.layout_comment_empty, null);

        headerView = new CommentsHeaderItem(getContext());
        if (gvIcon.getHeaderViewsCount() == 0) {
            gvIcon.addHeaderView(headerView);
        }
        gvIcon.setAdapter(mAdapter);
        gvIcon.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Comment item = (Comment) parent.getAdapter().getItem(position);
//                if (item != null) {
//                    UIHelper.showToast(parent.getContext(), "to comment list = " + item.name, Toast.LENGTH_SHORT);
//                }
            }
        });
    }

    /**
     * @param datas
     */
    public void setData(List<Comment> datas, String appName, long appId) {
        headerView.setData();
        mAdapter.setData(datas);
        mAdapter.setAppName(appName);
        mAdapter.setAppId(appId);
        checkCommentEmpty();
    }

    public void checkCommentEmpty() {
        if (mAdapter.getCount() == 0) {
            if (gvIcon.getFooterViewsCount() == 0) {
                gvIcon.addFooterView(mEmptyCommentView);
            }
        } else if (gvIcon.getFooterViewsCount() > 0) {
            gvIcon.removeFooterView(mEmptyCommentView);
        }
    }

    public void setOnClickCommentBarListener(OnClickListener listener) {
        headerView.setOnClickCommentBarListener(listener);
    }
}
