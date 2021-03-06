package com.cartoon.module.changxian.base;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.cartoon.utils.UltimateBar;

import cn.com.xuanjiezhimen.R;

public class BaseActivity extends AppCompatActivity {
//    private static final Logger LOG = LoggerFactory.getLogger(BaseFragment.class);

    private Toolbar mToolbar;
    private TextView mTitleView;
    private OnBackListener mOnBackListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        ActivityManager.getInstance().add(this);
//        PushAgent.getInstance(this).onAppStart();

        setContentView();
        initViews();
    }

    @Override
    protected void onResume() {
        super.onResume();

//        LOG.debug("onResume. [{}]", getClass().getSimpleName());

//        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

//        LOG.debug("onPause. [{}]", getClass().getSimpleName());

//        MobclickAgent.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

//        LOG.debug("onDestroy. [{}]", getClass().getSimpleName());

//        ActivityManager.getInstance().remove(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.content_frame);
        if (fragment != null) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

//        LOG.debug("onNewIntent. [{}]", getClass().getSimpleName());

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.content_frame);
        if (fragment != null) {
            fragment = Fragment.instantiate(this, fragment.getClass().getName(), intent.getExtras());
            fm.beginTransaction().replace(R.id.content_frame, fragment).commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                FragmentManager fm = getSupportFragmentManager();
                if (fm.getBackStackEntryCount() > 0) {
                    fm.popBackStack();
                } else {
                    boolean pressed = false;
                    if (mOnBackListener != null) {
                        pressed = mOnBackListener.onBacked();
                    }
                    if (!pressed) {
                        finish();
                    }
                }
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    protected void setContentView() {
        setContentView(R.layout.content_frame);
    }

    protected void initViews() {
        UltimateBar ultimateBar = new UltimateBar(this);
        ultimateBar.setColorBar(Color.parseColor("#ededed"));
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mTitleView = (TextView) findViewById(R.id.tv_title);
    }

    protected void setToolbar(Toolbar toolbar) {
        mToolbar = toolbar;
    }

    protected Toolbar getToolbar() {
        return mToolbar;
    }

    public void showToolbar() {
        mToolbar.setVisibility(View.VISIBLE);
    }

    public void hideToolbar() {
        mToolbar.setVisibility(View.GONE);
    }

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle("");

        mTitleView.setText(title);
    }

    protected void setContentFragment(Class<? extends BaseFragment> fragmentClass) {
        Bundle arguments = null;
        if (getIntent() != null) {
            arguments = getIntent().getExtras();
        }
        setContentFragment(fragmentClass, arguments);
    }

    protected void setContentFragment(Class<? extends BaseFragment> fragmentClass, Bundle arguments) {
//        LOG.debug("set content fragment. class={}", fragmentClass.getName());

        Fragment fragment = Fragment.instantiate(this, fragmentClass.getName(), arguments);
        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        t.replace(R.id.content_frame, fragment);
        t.commit();
    }

    protected boolean onBack() {
        boolean pressed = false;
        if (mOnBackListener != null) {
            pressed = mOnBackListener.onBacked();
        }
        return pressed;
    }

    @Override
    public void onBackPressed() {
        boolean pressed = onBack();
        if (!pressed) {
            super.onBackPressed();
        }
    }

    public void setOnBackListener(OnBackListener listener) {
        mOnBackListener = listener;
    }

    public interface OnBackListener {
        public boolean onBacked();
    }
}
