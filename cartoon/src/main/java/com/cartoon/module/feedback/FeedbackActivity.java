package com.cartoon.module.feedback;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.cartoon.module.BaseActivity;
import com.cartton.library.utils.ToastUtils;

import butterknife.BindView;
import cn.com.xuanjiezhimen.R;

/**
 * Created by jinbangzhu on 7/22/16.
 */

public class FeedbackActivity extends BaseActivity implements View.OnClickListener, FeedbackView {
    @BindView(R.id.bt_left)
    ImageButton btLeft;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.bt_right)
    Button btRight;
    @BindView(R.id.et_feedback)
    EditText etFeedback;
    private MaterialDialog dialog;


    private FeedbackPresenter presenter;

    @Override
    protected int getContentViewId() {
        return R.layout.feedback;
    }

    @Override
    protected int getFragmentContentId() {
        return 0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new FeedbackPresenterImpl(this);
        btLeft.setOnClickListener(this);
        btRight.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_left:
                onBackPressed();
                break;
            case R.id.bt_right:
                if (etFeedback.getText().length() == 0) {
                    ToastUtils.showLong(this, "请输入反馈内容");
                } else {
                    presenter.doFeedback(etFeedback.getText().toString());
                }
                break;
        }
    }

    @Override
    public void showLoading() {
        if (dialog == null)
            dialog = new MaterialDialog.Builder(this)
                    .title(R.string.notice)
                    .content(R.string.please_wait)
                    .progress(true, 0)
                    .build();

        dialog.show();
    }

    @Override
    public void hideLoading() {
        if (dialog.isShowing())
            dialog.dismiss();
    }

    @Override
    public void showErrorMessage(String msg) {
        ToastUtils.showLong(this, msg);
    }

    @Override
    public void closeNow() {
        onBackPressed();
    }
}
