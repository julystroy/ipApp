package com.cartoon.module.mytask;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.cartoon.CartoonApp;
import com.cartoon.data.UserInfo;
import com.cartoon.http.BaseCallBack;
import com.cartoon.http.BuilderInstance;
import com.cartoon.http.StaticField;
import com.cartoon.module.BaseActivity;
import com.cartoon.utils.MySeekBar;
import com.cartoon.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.com.xuanjiezhimen.R;

/**
 * Created by cc on 16-11-29.
 */
public class GradeDetailActivity extends BaseActivity {
    @BindView(R.id.bt_left)
    ImageButton mBtLeft;
    @BindView(R.id.tv_title)
    TextView    mTvTitle;
    @BindView(R.id.iv_mine_user_icon)
    ImageView   mIvMineUserIcon;
    @BindView(R.id.tv_mine_user_bonuspoint)
    TextView    mTvMineUserBonuspoint;
    @BindView(R.id.tv_bonus_point)
    TextView    mTvBonusPoint;
    /*@BindView(R.id.sb_bonus_point)
    ProgressBar mSbBonusPoint;*/

    @BindView(R.id.webview_detail)
    WebView   mWebviewDetail;
    @BindView(R.id.sb_bonus_point)
    MySeekBar mSbBonusPoint;

    @Override
    protected int getContentViewId() {
        return R.layout.ac_grade_detail;
    }

    @Override
    protected int getFragmentContentId() {
        return 0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        loadData();
        setUpView();
        UserInfo userInfo = CartoonApp.getInstance().getUserInfo();
        if (userInfo!=null)
        mTvMineUserBonuspoint.setText(userInfo.getNickname());
    }

    private void loadData() {

        BuilderInstance.getInstance().getPostBuilderInstance(StaticField.URL_GETEXP)
                .build().execute(new BaseCallBack<UserInfo>() {
            @Override
            public void onLoadFail() {
                mTvMineUserBonuspoint.setText("未知");
                mTvBonusPoint.setText("0"+ "/" + "0");
                //设置进度条
                mSbBonusPoint.setMax(100);
                mSbBonusPoint.setProgress(0);
            }

            @Override
            public void onContentNull() {

            }

            @Override
            public void onLoadSuccess(UserInfo response) {
                if (response != null) {
                    mTvBonusPoint.setText(response.getNowPoint() + "/" + response.getMaxPoint());
                    //设置进度条
                    if (response.getMaxPoint() != null && response.getNowPoint() != null) {
                        mSbBonusPoint.setMax(Integer.parseInt(response.getMaxPoint()));
                        int progress = Integer.parseInt(response.getNowPoint());
                        mSbBonusPoint.setProgress(progress);
                    }
                }
            }

            @Override
            public UserInfo parseNetworkResponse(String response) throws Exception {
                return JSON.parseObject(response.toString(),UserInfo.class);
            }
        });
    }

    private void setUpView() {


        mTvTitle.setText(R.string.grade_detail);
        mWebviewDetail.loadUrl("file:///android_asset/TextDeails.html");
        mBtLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        UserInfo userInfo = CartoonApp.getInstance().getUserInfo();
        String avatar = null;
        if (userInfo!=null) {
             avatar = userInfo.getAvatar();
        }
        Glide.with(this).load(Utils.addImageDomain(avatar))
                .placeholder(R.mipmap.icon_head)
                .error(R.mipmap.icon_head)
                .centerCrop()
                .into(mIvMineUserIcon);


    }

    @Override
    public void onResume() {
        super.onResume();

    }

}
