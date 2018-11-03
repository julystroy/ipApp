package com.cartoon.module.splash;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.CookieSyncManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cartoon.module.BaseActivity;
import com.cartoon.module.MainActivity;
import com.jude.swipbackhelper.SwipeBackHelper;

import cn.com.xuanjiezhimen.R;

public class WebViewActivity extends BaseActivity {
    public static final String EXTRA_URL = "url";
    public static final String EXTRA_TITLE = "title";
    private WebView webView;
    private ProgressBar progressBar;
    private String url;//需要显示的网页，由EXTRA_URL传递过来
    ImageButton btLeft;
    TextView tvTitle;
    ImageButton btRight;
    private boolean fromAd;


    private void onWebPageStarted() {
        progressBar.setVisibility(View.VISIBLE);
    }

    public static void invoke(Context context, String url, String title) {
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra(EXTRA_URL, url);
        intent.putExtra(EXTRA_TITLE, title);
        context.startActivity(intent);
    }


    public static void invoke(Context context, String url, String title, boolean fromAd) {
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra(EXTRA_URL, url);
        intent.putExtra(EXTRA_TITLE, title);
        intent.putExtra("fromAd", fromAd);
        context.startActivity(intent);
    }


    @Override
    protected int getContentViewId() {
        return R.layout.webview;
    }

    @Override
    protected int getFragmentContentId() {
        return 0;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SwipeBackHelper.getCurrentPage(this).setSwipeBackEnable(false);

        Intent intent = getIntent();
        url = intent.getStringExtra(EXTRA_URL);
        fromAd = intent.getBooleanExtra("fromAd", false);
        createView();

    }

    protected void createView() {
        btLeft = (ImageButton) findViewById(R.id.bt_left);
        btRight = (ImageButton) findViewById(R.id.bt_right);
        tvTitle = (TextView) findViewById(R.id.tv_title);


        btRight.setVisibility(View.GONE);
        btLeft.setImageResource(R.mipmap.icon_back_black);
        btLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        String title = getIntent().getStringExtra(EXTRA_TITLE);
        tvTitle.setText(TextUtils.isEmpty(title) ? "网页加载中" : title);


        webView = (WebView) findViewById(R.id.pay_pal_webview);
        CookieSyncManager.createInstance(this);
        CookieSyncManager.getInstance().sync();
        webView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
        webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDefaultTextEncodingName("utf-8");
        webView.getSettings().setUserAgentString(webView.getSettings().getUserAgentString() + " app ");
        progressBar = (ProgressBar) findViewById(R.id.load_url_progress);

        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
                progressBar.setVisibility(View.GONE);
                String title = webView.getTitle();
                if (null != tvTitle) {
                    tvTitle.setText(title);
                }
                super.onPageFinished(view, url);

            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                onWebPageStarted();
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode,
                                        String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                webView.loadUrl(url);
                return true;
            }
        });

        webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                progressBar.setProgress(progress);
            }

        });

        if (!TextUtils.isEmpty(url))
            webView.loadUrl(url);

    }


    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            onBackPressed();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            webView.onPause(); // 暂停网页中正在播放的视频
        }
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        if (webView != null) {
            webView.destroy();
        }

    }


    @Override
    public void onBackPressed() {
        if (fromAd) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        super.onBackPressed();
    }

}
