package com.cartoon.module;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;

import com.afollestad.materialdialogs.MaterialDialog;
import com.cartoon.utils.UltimateBar;
import com.jude.swipbackhelper.SwipeBackHelper;
import com.tencent.stat.StatService;
import com.umeng.analytics.MobclickAgent;

import butterknife.ButterKnife;
import cn.com.xuanjiezhimen.BuildConfig;
import cn.com.xuanjiezhimen.R;

/**
 * 封装加载和移除fragment的基类
 * <p/>
 * Created by David on 16/5/6.
 **/
public abstract class BaseActivity extends AppCompatActivity {

    private MaterialDialog dialog;
    protected static final int START_PAGE = 1;
    protected static final int PAGE_SIZE = 10;

    /**
     * 排序顺序。默认是降序
     */
    private static final String SORT_DESC = "desc";
    private static final String SORT_ASC = "asc";

    /**
     * 页面数据排序
     */
    protected String sort_order = SORT_DESC;

    protected boolean isSysBar = true;

    /**
     * 布局文件ID
     */
    protected abstract int getContentViewId();

    /**
     * 布局文件中Fragment的Id
     */
    protected abstract int getFragmentContentId();

    /**
     * 处理获取到intent.
     */
    protected void handleIntent(Intent intent) {
        //TO_DO  处理获取到intent.
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewId());
        ButterKnife.bind(this);
        initPermission();
        SwipeBackHelper.onCreate(this);

        if (null != getIntent()) {
            handleIntent(getIntent());
        }

        if (isSysBar) {
           //SystemUtils.initSystemBar(this);
            UltimateBar ultimateBar = new UltimateBar(this);
            ultimateBar.setColorBar(Color.parseColor("#ededed"));
        }

        //bugtags，正式版需移除
        //MobclickAgent.setCatchUncaughtExceptions(false);

    }


    @Override
    protected void onResume() {
        super.onResume();
      //  MobclickAgent.onResume(this);
        if (!BuildConfig.DEBUG) {
            MobclickAgent.onResume(this);
        } /*else {
           // bugtags，正式版需移除
           Bugtags.onResume(this);
        }*/
        StatService.onResume(this);
    }
    @Override
    protected void onPause() {
        super.onPause();
        if (!BuildConfig.DEBUG) {
            MobclickAgent.onPause(this);
        } /*else {
           // bugtags，正式版需移除
           Bugtags.onPause(this);
        }*/
        StatService.onPause(this);
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        //bugtags，正式版需移除
       /* if (BuildConfig.DEBUG) {
            Bugtags.onDispatchTouchEvent(this, event);
        }*/
        return super.dispatchTouchEvent(event);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        SwipeBackHelper.onPostCreate(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        SwipeBackHelper.onDestroy(this);
    }

    private static final int REQUEST_PERMISSION_SDCARD_CODE = 1;

    private void initPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (!(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION_SDCARD_CODE);
            }
        }
    }

    /**
     * 添加fragment
     */
    protected void addFragment(BaseFragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(getFragmentContentId(), fragment, fragment.getClass().getSimpleName())
                    .addToBackStack(fragment.getClass().getSimpleName())
                    .commitAllowingStateLoss();
        }
    }

    /**
     * 添加fragment 不添加回退栈
     */
    protected void addFragmentNoPop(BaseFragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(getFragmentContentId(), fragment, fragment.getClass().getSimpleName())
                    .commitAllowingStateLoss();
        }
    }

    /**
     * 移除fragment
     */
    protected void popFragment() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            getSupportFragmentManager().popBackStack();
        } else {
            finish();
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (KeyEvent.KEYCODE_BACK == keyCode) {
            finish();
            return true;
//            if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
//                finish();
//                return true;
//            }
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 反转排序顺序
     */
    protected String revertSortOrder() {
        if (TextUtils.equals(sort_order, SORT_DESC)) {
            sort_order = SORT_ASC;
            return getResources().getString(R.string.sort_desc);
        } else {
            sort_order = SORT_DESC;
            return getResources().getString(R.string.sort_asc);
        }
    }

    public void showLoadingDialog() {
        if (dialog == null)
            dialog = new MaterialDialog.Builder(this)
                    .title(R.string.notice)
                    .content(R.string.please_wait)
                    .progress(true, 0)
                    .build();

        dialog.show();
    }

    public void hideLoadingDialog() {
        if (dialog.isShowing())
            dialog.dismiss();
    }

}
