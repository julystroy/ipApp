package com.cartoon.view;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.cartoon.uploadlog.UploadLog;
import com.cartoon.utils.UserDB;
import com.cartton.library.utils.DebugLog;

import java.util.Locale;

import cn.com.xuanjiezhimen.R;

public class DownloadButton extends DownloadLayout implements View.OnClickListener {
    public static final int STYLE_DEFAULT = 0;
    public static final int STYLE_PROGRESS = STYLE_DEFAULT + 1;
    public static final int STYLE_STROKE = STYLE_PROGRESS + 1;
    public static final int STYLE_FILL = STYLE_STROKE + 1;
    private int mStyle = STYLE_DEFAULT;

    private long mAppId = 0;

    private ProgressBar mProgressBar;
    private Button mButton;

    public DownloadButton(Context context) {
        super(context);
    }

    public DownloadButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void init() {
        super.init();

        LayoutInflater.from(getContext()).inflate(R.layout.button_download, this, true);

        mProgressBar = (ProgressBar) findViewById(R.id.download_progress);
        mButton = (Button) findViewById(R.id.button);
        mButton.setOnClickListener(this);

        setStyle(STYLE_DEFAULT);
    }

    public void setStyle(int style) {
        mStyle = style;
        switch (style) {
            case STYLE_DEFAULT:
            case STYLE_FILL:
                mProgressBar.setVisibility(GONE);
                mButton.setBackgroundResource(R.drawable.play_directly_enable_true_bg);
                mButton.setTextColor(getResources().getColor(R.color.white));
                break;
            case STYLE_PROGRESS:
            case STYLE_STROKE:
                mProgressBar.setVisibility(GONE);
                mButton.setBackgroundResource(R.drawable.btn_bg_blue);
                mButton.setTextColor(getResources().getColor(R.color.base_color_blue));
                break;
            default:
                break;
        }
    }

    public void setAppId(long id) {
        mAppId = id;
    }

    public void setEnabled(boolean enabled) {
        mButton.setEnabled(enabled);
    }

    public void setBackgroundResource(int drawableId) {
        mButton.setBackgroundResource(drawableId);
    }

    public void setTextColor(int color) {
        mButton.setTextColor(color);
    }

    public void setText(String text) {
        mButton.setText(text);
    }

    public void setText(int textId) {
        mButton.setText(textId);
    }

    private void updateText(int progress) {
        switch (getState()) {
            case UserDB.STATE_UNKNOWN:
                setText(getResources().getString(R.string.action_download));
                break;
            case UserDB.STATE_WAITING:
                setText(getResources().getString(R.string.action_wait));
                break;
            case UserDB.STATE_DOWNLOADING:
                if (mStyle < STYLE_STROKE) {
                    setText(String.format(Locale.US, "%d%%", parseProgress(progress)));
                }
                break;
            case UserDB.STATE_STOP:
                setText(getResources().getString(R.string.action_continue));
                break;
            case UserDB.STATE_COMPLETED:
                setText(getResources().getString(R.string.action_install));
                break;
            case UserDB.STATE_INSTALLED:
                setText(getResources().getString(R.string.action_open));
                break;
            case UserDB.STATE_FAILED:
                setText(getResources().getString(R.string.action_failed));
                break;
        }
    }

    @Override
    protected void updateProgress(int progress, int speed) {
        DebugLog.d("updateProgress. state = " + getState() + ", progress = " + progress);

        updateText(progress);
    }

    public void handleClick() {
        DebugLog.d("handleClick. state = " + getState());

        switch (getState()) {
            case UserDB.STATE_UNKNOWN:
                UploadLog.uploadGamePlayButtonClickLog(UploadLog.DOWNLOAD_BUTTON, mAppId, (Activity)getContext());
            case UserDB.STATE_STOP:
            case UserDB.STATE_FAILED:
                start();
                break;
            case UserDB.STATE_WAITING:
            case UserDB.STATE_DOWNLOADING:
                stop();
                break;
            case UserDB.STATE_COMPLETED:
                UploadLog.uploadGamePlayActionCompleteLog(UploadLog.DOWNLOAD_BUTTON,UploadLog.NO_VALUE,mAppId,(Activity)getContext());
                install();
                break;
            case UserDB.STATE_INSTALLED:
                open();
                break;
        }
    }

    @Override
    public void onClick(View v) {
        handleClick();
    }
}
