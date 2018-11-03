package com.cartoon.module.action;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.cartoon.CartoonApp;
import com.cartoon.http.StaticField;
import com.cartoon.module.BaseFragment;
import com.cartoon.utils.EasySharePreference;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.com.xuanjiezhimen.R;

/**
 * Created by cc on 17-8-22.
 */
public class WebviewFragment extends BaseFragment {
    @BindView(R.id.rl_title)
    RelativeLayout mRlTitle;
    @BindView(R.id.load_url_progress)
    ProgressBar    mLoadUrlProgress;
    @BindView(R.id.pay_pal_webview)
    WebView        mPayPalWebview;

    @Override
    protected int getLayoutId() {
        return R.layout.webview;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        mRlTitle.setVisibility(View.GONE);
        String actionId = EasySharePreference.getActionId(getContext());
        WebSettings webSettings =   mPayPalWebview.getSettings();
        //  webSettings.setUseWideViewPort(true);//设置此属性，可任意比例缩放
        //  webSettings.setLoadWithOverviewMode(true);
        webSettings.setJavaScriptEnabled(true);
        mPayPalWebview.loadUrl(StaticField.HTML_BASE_URL+"/activity/getDetail?dataId="+actionId+"&userId="
                + CartoonApp.getInstance().getUserId());

        mPayPalWebview.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                //加载完成
                mLoadUrlProgress.setVisibility(View.GONE);
            }
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                //加载开始
                mLoadUrlProgress.setVisibility(View.VISIBLE);
            }
        });
    }
//加载视频控制
    @Override
    public void onPause() {
        super.onPause();
        if (mPayPalWebview != null)
            mPayPalWebview.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mPayPalWebview != null)
            mPayPalWebview.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPayPalWebview != null)
            mPayPalWebview.destroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }
}
