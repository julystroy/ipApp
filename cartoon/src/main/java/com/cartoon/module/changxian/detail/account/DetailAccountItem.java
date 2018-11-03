package com.cartoon.module.changxian.detail.account;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cartoon.data.DetailPage;
import com.cartoon.data.GameAccount;
import com.cartoon.data.game.DownloadGame;
import com.cartoon.sdk.PlaySdk;
import com.cartoon.utils.UserDB;
import com.google.gson.Gson;

import cn.com.xuanjiezhimen.R;
import cn.idianyun.streaming.data.LaunchInfo;

public class DetailAccountItem extends LinearLayout {
    private TextView mTitle, mIntro, mState;
    private Button mPlayButton;

    private DetailPage mDetailPage;
    private GameAccount mGameAccount;

    public DetailAccountItem(Context context) {
        this(context, null, 0);
    }

    public DetailAccountItem(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DetailAccountItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initViews();
    }

    private void initViews() {
        LayoutInflater.from(getContext()).inflate(R.layout.item_detail_account, this, true);

        mTitle = (TextView) findViewById(R.id.tv_title);
        mIntro = (TextView) findViewById(R.id.tv_intro);
        mState = (TextView) findViewById(R.id.tv_state);
        mPlayButton = (Button) findViewById(R.id.btn_play);
        mPlayButton.setOnClickListener(mOnClickPlayListener);
    }

    public void setData(DetailPage detailPage, GameAccount data) {
        mDetailPage = detailPage;
        mGameAccount = data;
        if (data != null) {
            mTitle.setText(data.title);
            mIntro.setText(String.format("%s%s", data.intro1, data.intro2));
            if (data.state == GameAccount.STATE_FREE) {
                mState.setText(R.string.state_free);
                mState.setBackgroundResource(R.drawable.game_account_free_state_bg);
                mPlayButton.setBackgroundResource(R.drawable.play_directly_enable_true_bg);
                mPlayButton.setEnabled(true);
            } else {
                mState.setText(R.string.state_busy);
                mState.setBackgroundResource(R.drawable.game_account_busy_state_bg);
                mPlayButton.setBackgroundResource(R.drawable.btn_bg_gray);
                mPlayButton.setEnabled(false);
            }
        } else {
            reset();
        }
    }

    private void reset() {
        mTitle.setText("");
        mIntro.setText("");
        mState.setText("");
    }

    private OnClickListener mOnClickPlayListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            String devices = null;
            if (mGameAccount.devices != null && mGameAccount.devices.size() > 0) {
                devices = new Gson().toJson(mGameAccount.devices);
            }
            LaunchInfo launchInfo = mDetailPage.changeToLaunchInfo();
            launchInfo.devices = devices;
            PlaySdk.getInstance().launch(getContext(), (int) mDetailPage.id, launchInfo, new PlaySdk.OnDownloadListener() {
                @Override
                public void onDownload(String url, String pkgName) {

                    DownloadGame record = UserDB.getInstance().getGameDownload(url);
                    record.setPackageName(pkgName);
                    record.setCxId(mDetailPage.id);
                    record.setAppName(mDetailPage.name);
                    record.setIconUrl(mDetailPage.logo);
                    UserDB.getInstance().saveGameDownload(record);
                }
            });
        }
    };
}
