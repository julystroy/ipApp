package com.cartoon.module.changxian.detail.comment;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cartoon.account.AccountHelper;
import com.cartoon.data.DetailUserInfo;
import com.cartoon.data.game.Rating;
import com.cartoon.module.changxian.detail.detailinfo.RatingView;
import com.cartoon.view.MaterialRatingBar;
import com.cartoon.view.RoundImageView;
import com.cartoon.volley.VolleySingleton;

import cn.com.xuanjiezhimen.R;

public class CommentRatingHeader extends LinearLayout {
    private LinearLayout mEvaluateView;
    private RatingView mTotalRatingView;
    private TextView mRatingText, mRatingTip;
    private RoundImageView     mImageView;
    private MaterialRatingBar  mSelfRatingView;
    private DetailUserInfo     mDetailUserInfo;
    private int                mPlayType;
    private RatingViewListener callBack;

    public CommentRatingHeader(Context context) {
        super(context);
        init();
    }

    public CommentRatingHeader(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
        init();
    }

    protected void init() {
        inflate(getContext(), R.layout.layout_detail_comment_header, this);

        mEvaluateView = (LinearLayout) findViewById(R.id.ll_evaluate_view);
        mTotalRatingView = (RatingView) findViewById(R.id.rating_view);
        mSelfRatingView = (MaterialRatingBar) findViewById(R.id.self_evaluate_bar);
        mRatingText = (TextView) findViewById(R.id.tv_evaluate);
        mImageView = (RoundImageView) findViewById(R.id.imageView);
        mImageView.setDefaultImageResId(R.drawable.default_user_avatar);
        mImageView.setImageUrl(AccountHelper.getUser().avatar, VolleySingleton.getInstance(getContext()).getImageLoader());
        findViewById(R.id.iv_vip_logo).setVisibility(AccountHelper.getUser().isVIP()?VISIBLE:GONE);
        mSelfRatingView.setMax(5);

        mRatingTip = (TextView) findViewById(R.id.tv_evaluate_tip);
        if (mRatingTip != null && AccountHelper.getPlayTimeNeededForRating() > 0) {
            mRatingTip.setText(String.format(getResources().getString(R.string.evaluate_tip_time), AccountHelper.getPlayTimeNeededForRating() / 60));
        }
    }

    public void setData(Rating totalRating, DetailUserInfo userInfo, int playType) {
        mTotalRatingView.setData(totalRating);
        if (totalRating.count < 10) {
            mTotalRatingView.setVisibility(GONE);
            mRatingTip.setVisibility(GONE);
            findViewById(R.id.v_separator).setVisibility(GONE);
        } else {
            mTotalRatingView.setVisibility(VISIBLE);
            mRatingTip.setVisibility(VISIBLE);
            findViewById(R.id.v_separator).setVisibility(VISIBLE);
        }
        setRatingBarValue(userInfo.rating);
        mDetailUserInfo = userInfo;
        mPlayType = playType;

        TextView ratingTip = (TextView) findViewById(R.id.tv_evaluate_tip);
        if (playType == 2) {
            ratingTip.setText(getResources().getString(R.string.evaluate_tip_no_time));
        } else if (AccountHelper.getPlayTimeNeededForRating() > 0) {
            ratingTip.setText(String.format(getResources().getString(R.string.evaluate_tip_time), AccountHelper.getPlayTimeNeededForRating() / 60));
        }
    }

    private OnClickListener mClickEvaluateViewListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mDetailUserInfo.durationQuick > 0 || mDetailUserInfo.durationPlay > AccountHelper.getPlayTimeNeededForRating()) {
                callBack.callEditRatingView();
            } else {
                showTryPlayTipDialog();
            }
        }
    };

    private void showTryPlayTipDialog() {
        String msg;
        if (mPlayType == 2) {
            msg = getResources().getString(R.string.evaluate_tip_no_time);
        } else {
            msg = String.format(getResources().getString(R.string.evaluate_tip_time), AccountHelper.getPlayTimeNeededForRating() / 60);
        }

        AlertDialog alert = (new AlertDialog.Builder(getContext(), R.style.CXAlertDialogStyle)).setMessage(msg).setPositiveButton(getResources().getString(R.string.try_play), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                callBack.beginTryPlay();
            }
        }).setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        }).show();
    }

    private void setRatingBarValue(int rating) {
        mSelfRatingView.setRating(rating / 2);
        if (rating != 0) {
            int startNum = rating / 2;
            int[] imageIds = {R.drawable.star_focus_show1, R.drawable.star_focus_show2,
                    R.drawable.star_focus_show3, R.drawable.star_focus_show4,
                    R.drawable.star_focus_show5};

            mRatingText.setText(getResources().getString(R.string.my_evaluate));
            mSelfRatingView.setProgressImageId(getContext(), imageIds[startNum - 1]);
            if (mEvaluateView.hasOnClickListeners()) mEvaluateView.setOnClickListener(null);
        } else {
            mEvaluateView.setOnClickListener(mClickEvaluateViewListener);
        }
    }

    public interface RatingViewListener {
        void callEditRatingView();
        void beginTryPlay();
    }

    public void setTabCommentFragmentCallBack(RatingViewListener callback) {
        this.callBack = callback;
    }
}
