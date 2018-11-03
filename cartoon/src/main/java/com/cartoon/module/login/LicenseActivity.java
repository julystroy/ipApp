package com.cartoon.module.login;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cartoon.module.BaseActivity;

import butterknife.BindView;
import cn.com.xuanjiezhimen.R;

/**
 * Created by jinbangzhu on 8/16/16.
 */

public class LicenseActivity extends BaseActivity {
    @BindView(R.id.bt_left)
    ImageButton btLeft;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.bt_right)
    ImageButton btRight;
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;
    @BindView(R.id.webView)
    WebView webView;

    @Override
    protected int getContentViewId() {
        return R.layout.ac_license;
    }

    @Override
    protected int getFragmentContentId() {
        return 0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int type = getIntent().getIntExtra("type",-1);
        btLeft.setImageResource(R.mipmap.icon_back_black);
        btLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        btRight.setVisibility(View.GONE);
        if (type==0) {

            tvTitle.setText("宗门战说明");
            //        webView.lo

            webView.loadUrl("file:///android_res/raw/sectfight.html");
        } else if (type == 1) {
            tvTitle.setText("关于宗门");
            //        webView.lo

            webView.loadUrl("file:///android_res/raw/zong.html");
        } else {
            tvTitle.setText("用户协议");
            //        webView.lo

            webView.loadUrl("file:///android_res/raw/license.html");
        }
    }
}
