package com.cartoon.module.changxian.download;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.cartoon.data.game.DownloadGame;
import com.cartoon.view.DownloadButton;
import com.cartoon.view.DownloadLayout;
import com.cartoon.volley.VolleySingleton;
import com.cartton.library.utils.DebugLog;

import java.util.Locale;

import cn.aigestudio.downloader.bizs.DLInfo;
import cn.com.xuanjiezhimen.R;

public class DownloadItem extends DownloadLayout implements View.OnClickListener {
    private NetworkImageView mDownloadLogo;
    private TextView mTitle;
    private TextView mState;
    private RelativeLayout mSpeedLayout;
    private TextView mSpeed;
    private TextView mDownloadSize;
    private ProgressBar mProgressBar;
    private DownloadButton mDownloadButton;
    private Button mDeleteButton;

    private View.OnClickListener mOnClickDeleteListener;

    public DownloadItem(Context context) {
        super(context);
    }

    public DownloadItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void init() {
        super.init();

        inflate(getContext(), R.layout.item_download, this);

        mDownloadLogo = (NetworkImageView) findViewById(R.id.iv_download_logo);
        mTitle = (TextView) findViewById(R.id.tv_title);
        mState = (TextView) findViewById(R.id.tv_state);

        mSpeedLayout = (RelativeLayout) findViewById(R.id.layout_download_speed);
        mSpeed = (TextView) findViewById(R.id.tv_speed);
        mDownloadSize = (TextView) findViewById(R.id.tv_download_size);
        mProgressBar = (ProgressBar) findViewById(R.id.download_progress);

        mDownloadButton = (DownloadButton) findViewById(R.id.btn_download);
        mDownloadButton.setOnClickListener(this);

        mDeleteButton = (Button) findViewById(R.id.btn_delete);
        mDeleteButton.setOnClickListener(this);
    }

    public void setOnClickDeleteListener(View.OnClickListener listener) {
        mOnClickDeleteListener = listener;
    }

    private void setStateText(int textId) {
        mState.setText(textId);
        if (mState.getVisibility() != VISIBLE) {
            mState.setVisibility(VISIBLE);
        }
    }

    private void setProgress(int progress) {
        mProgressBar.setProgress(parseProgress(progress));
        if (mProgressBar.getVisibility() != VISIBLE) {
            mProgressBar.setVisibility(VISIBLE);
        }
    }

    private void setSpeedText(int currentBytes, int totalBytes, int speed) {
        mSpeed.setText(String.format(Locale.US, "%s/s", getSize(speed, true)));
        mDownloadSize.setText(String.format(Locale.US, "%s/%s", getSize(currentBytes), getSize(totalBytes)));
        if (mSpeedLayout.getVisibility() != VISIBLE) {
            mSpeedLayout.setVisibility(VISIBLE);
        }
    }

    public void setData(DownloadGame info) {
        mDownloadLogo.setDefaultImageResId(R.drawable.default_subject_app);
        mDownloadLogo.setImageUrl(info.getIconUrl(), VolleySingleton.getInstance(getContext()).getImageLoader());
        mTitle.setText(info.getAppName());

        mDownloadButton.setDownloadInfo(info);
        setDownloadInfo(info);
    }

    @Override
    protected void updateProgress(int progress, int speed) {
        DebugLog.d("updateProgress. state = " + getState());

        mState.setVisibility(INVISIBLE);
        mSpeedLayout.setVisibility(GONE);
        mProgressBar.setVisibility(GONE);
        switch (getState()) {
            case DLInfo.STATE_UNKNOWN:
            case DLInfo.STATE_STOP:
                setStateText(R.string.state_pause);
                setProgress(progress);
                mDownloadButton.setStyle(DownloadButton.STYLE_STROKE);
                break;
            case DLInfo.STATE_WAITING:
                setSpeedText(progress, getTotalBytes(), speed);
                setProgress(progress);
                mDownloadButton.setStyle(DownloadButton.STYLE_STROKE);
                break;
            case DLInfo.STATE_DOWNLOADING:
                setSpeedText(progress, getTotalBytes(), speed);
                setProgress(progress);
                mDownloadButton.setStyle(DownloadButton.STYLE_STROKE);
                mDownloadButton.setText(R.string.action_pause);
                break;
            case DLInfo.STATE_COMPLETED:
                setStateText(R.string.state_finish);
                mDownloadButton.setStyle(DownloadButton.STYLE_FILL);
                break;
            case DLInfo.STATE_INSTALLED:
                setStateText(R.string.state_install);
                mDownloadButton.setStyle(DownloadButton.STYLE_FILL);
                break;
            case DLInfo.STATE_FAILED:
                setStateText(R.string.state_failed);
                mDownloadButton.setStyle(DownloadButton.STYLE_STROKE);
                break;
            default:
                break;
        }
    }

    private String getSize(int size) {
        return getSize(size, false);
    }

    private String getSize(int size, boolean f) {
        String res = String.format(Locale.US, "%dB", size);
        if (size >= 1024) {
            float s = size / 1024f;
            res = String.format(Locale.US, "%dKB", (int) s);
            if (s >= 1024) {
                s /= 1024;
                if (f) {
                    res = String.format(Locale.US, "%.2fMB", s);
                } else {
                    res = String.format(Locale.US, "%dMB", (int) s);
                }
            }
        }
        return res;
    }

    private void deleteDownload() {
        if (mOnClickDeleteListener != null) {
            mOnClickDeleteListener.onClick(this);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_download:
                mDownloadButton.handleClick();
                break;
            case R.id.btn_delete:
                deleteDownload();
                break;
        }
    }
}
