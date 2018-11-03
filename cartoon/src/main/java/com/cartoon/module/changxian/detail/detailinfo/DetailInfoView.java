package com.cartoon.module.changxian.detail.detailinfo;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cartoon.data.DetailPage;
import com.cartoon.data.Recommender;
import com.cartoon.module.changxian.comment.CommentsHeaderItem;
import com.cartoon.view.StretchyTextView;

import cn.com.xuanjiezhimen.R;

public class DetailInfoView extends LinearLayout {
    private SlideView          mHeaderPicture;
    private View               mLayoutRecommend;
    private TextView           mRecommendContent;
    private TextView           mRecommender;
    private StretchyTextView   mStretyText;
    private DetailFeaturesItem mAppFeaturesItem;
    private CommentsHeaderItem mHheaderView;

    public DetailInfoView(Context context) {
        super(context);

        init();
    }

    public DetailInfoView(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
        init();
    }

    protected void init() {
        inflate(getContext(), R.layout.layout_detail_info, this);

        mHeaderPicture = (SlideView) findViewById(R.id.slide_head);

        mLayoutRecommend = findViewById(R.id.layout_recommender);
        mRecommendContent = (TextView) findViewById(R.id.tv_recommend_text);
        mRecommender = (TextView) findViewById(R.id.tv_recommender);

        mStretyText = (StretchyTextView) findViewById(R.id.spread_textview);
        mStretyText.setBottomTextGravity(Gravity.CENTER);
        mStretyText.setMaxLineCount(6);

        mAppFeaturesItem = (DetailFeaturesItem) findViewById(R.id.item_features);
        mHheaderView = (CommentsHeaderItem) findViewById(R.id.comment_header_item);
    }

    public void setData(DetailPage detailPage) {
        if (detailPage.carousels != null && detailPage.carousels.size() > 0) {
            mHeaderPicture.setVisibility(View.VISIBLE);
            mHeaderPicture.setSlideData(detailPage.carousels, detailPage.appId, detailPage.id);
        } else {
            mHeaderPicture.setVisibility(View.GONE); //解决没轮播图时轮播容器高度铺满导致盖住推荐，评分等下面的view.
        }

        Recommender recommender = detailPage.recommended;
        if (TextUtils.isEmpty(recommender.name) || TextUtils.isEmpty(recommender.text)) {
            mLayoutRecommend.setVisibility(View.GONE);
        } else {
            mLayoutRecommend.setVisibility(View.VISIBLE);
            mRecommender.setText(recommender.name);
            mRecommendContent.setText(recommender.text);
        }

        mStretyText.setContentWithOthers(detailPage.intro, detailPage.strategy, detailPage.playCount, detailPage.downloadCount);

        mAppFeaturesItem.setData(detailPage.features);

        mHheaderView.setData();
    }

    public void setOnClickCommentBarListener(OnClickListener listener) {
        mHheaderView.setOnClickCommentBarListener(listener);
    }
}
