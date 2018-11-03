package com.cartoon.module.changxian.detail.detailinfo;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.cartoon.data.Carousel;
import com.cartoon.utils.UIHelper;

import java.util.List;

import cn.com.xuanjiezhimen.R;

public class SlideView extends LinearLayout {
    private RecyclerView mRecyclerView;
    private SlideViewAdapter mAdapter;

    public SlideView(Context context) {
        super(context);

        init();
    }

    public SlideView(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
        init();
    }

    protected void init() {
        inflate(getContext(), R.layout.layout_slideshow, this);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        mAdapter = new SlideViewAdapter(getContext());
        mRecyclerView.setAdapter(mAdapter);
    }

    public void setSlideData(List<Carousel> carousels, final long appId, final long id) {
        setSlideData(carousels);
    }

    public void setSlideData(List<Carousel> carousels) {
        mAdapter.updateData(carousels);

        if (carousels != null && carousels.size() > 0) {
            LayoutParams params = (LayoutParams) getLayoutParams();
            if (carousels.get(0).isVertival()) {
                params.height = UIHelper.dip2px(getContext(), 200);
            } else {
                params.height = UIHelper.dip2px(getContext(), 170);
            }
            setLayoutParams(params);
        }
    }
}
