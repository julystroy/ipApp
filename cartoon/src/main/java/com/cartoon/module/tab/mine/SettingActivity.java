package com.cartoon.module.tab.mine;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.cartoon.CartoonApp;
import com.cartoon.Constants;
import com.cartoon.data.AppVersion;
import com.cartoon.module.BaseActivity;
import com.cartoon.module.feedback.FeedbackActivity;
import com.cartoon.module.login.LoginActivity;
import com.cartoon.utils.FileSizeUtil;
import com.cartton.library.utils.StorageUtils;
import com.cartton.library.utils.ToastUtils;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.com.xuanjiezhimen.BuildConfig;
import cn.com.xuanjiezhimen.R;

/**
 * Created by cc on 17-9-27.
 */
public class SettingActivity extends BaseActivity implements MineView, View.OnClickListener {


    @BindView(R.id.bt_left)
    ImageButton    mBtLeft;
    @BindView(R.id.tv_title)
    TextView       mTvTitle;
    @BindView(R.id.bt_right)
    ImageButton    mBtRight;
    @BindView(R.id.textPoint)
    TextView       mTextPoint;
    @BindView(R.id.rl_title)
    RelativeLayout mRlTitle;
    @BindView(R.id.ll_container_2)
    LinearLayout   mLlContainer2;
    @BindView(R.id.tv_logout)
    TextView       mTvLogout;
    @BindView(R.id.ll_logout)
    LinearLayout   mLlLogout;
    private MinePresenter presenter;
    private boolean isTips = true;
    private TextView tvCacheSize;

    @Override
    protected int getContentViewId() {
        return R.layout.xml_setting;
    }

    @Override
    protected int getFragmentContentId() {
        return 0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        presenter = new MinePresenterImpl(this, isTips);
        setupView();
        new Thread(new Runnable() {
            @Override
            public void run() {
                readFileSizeAndShowIt();
            }
        }).start();
    }

    private void readFileSizeAndShowIt() {
        final double sizeCache = FileSizeUtil.getFileOrFilesSize(this.getCacheDir().getAbsolutePath(), 3);

        final int totalSize = (int) (sizeCache );
        if (SettingActivity.this != null) {
            SettingActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (SettingActivity.this != null && tvCacheSize != null) {
                        tvCacheSize.setText(totalSize + "M");
                    }
                }
            });
        }
    }

    private void setupView() {
        int[] titleArr2;
        int[] imgId2;

        titleArr2 = new int[]{
                R.string.user_feedback,
                R.string.clear_cache,
                R.string.my_upadte};


        imgId2 = new int[]{
                R.mipmap.icon_mine_feedback,
                R.mipmap.icon_mine_cache,
                R.mipmap.icon_mine_update};


        for (int i = 0; i < imgId2.length; i++) {
            View subView = buildItemView(titleArr2[i], imgId2, i);
            mLlContainer2.addView(subView);
        }

          mLlLogout.setOnClickListener(this);
          mBtLeft.setImageResource(R.mipmap.icon_back_black);
         mBtRight.setVisibility(View.GONE);
         mBtLeft.setOnClickListener(this);
    }

    @NonNull
    private View buildItemView(int resid, int[] imgId1, int i) {
        View subView = LayoutInflater.from(this).inflate(R.layout.part_mine_item, null);

        RelativeLayout item = (RelativeLayout) subView.findViewById(R.id.rl_item);
        item.setId(imgId1[i]);
        item.setOnClickListener(this);
        ImageView icon = (ImageView) subView.findViewById(R.id.iv_icon);
        ImageView arrow = (ImageView) subView.findViewById(R.id.iv_arrow);
        TextView textView = (TextView) subView.findViewById(R.id.tv_text);
        TextView textExtra = (TextView) subView.findViewById(R.id.tv_extra);
        View line = subView.findViewById(R.id.line);

        icon.setImageResource(imgId1[i]);
        textView.setText(resid);
        if (i == imgId1.length - 1) {
            line.setVisibility(View.GONE);
        }


        if (imgId1[i] == R.mipmap.icon_mine_cache) {
            textExtra.setVisibility(View.VISIBLE);
            arrow.setVisibility(View.GONE);
            textExtra.setText("...");
            tvCacheSize = textExtra;
        }
        if (imgId1[i] == R.mipmap.icon_mine_update) {
            textExtra.setVisibility(View.VISIBLE);
            arrow.setVisibility(View.GONE);
            textExtra.setText("当前版本 : " + BuildConfig.VERSION_NAME);
        }
        return subView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_left:
                onBackPressed();
                break;
            case R.mipmap.icon_mine_feedback:
                if (CartoonApp.getInstance().isLogin(this))
                    startActivity(new Intent(SettingActivity.this, FeedbackActivity.class));
                break;
            case R.mipmap.icon_mine_cache:
                new MaterialDialog.Builder(this).title(R.string.notice)
                        .content("确定清空吗?")
                        .positiveText("确定")
                        .negativeText("取消")
                        .callback(new MaterialDialog.ButtonCallback() {
                            @Override
                            public void onPositive(MaterialDialog dialog) {
                                super.onPositive(dialog);
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Glide.get(SettingActivity.this).clearDiskCache();

                                        File downFolder = StorageUtils.getIndividualCacheDirectory(CartoonApp.getInstance(), Constants.PATH_CARTOON);

                                        deleteRecursive(SettingActivity.this.getCacheDir());
                                        deleteRecursive(downFolder);

                                        readFileSizeAndShowIt();
                                    }
                                }).start();
                            }

                            @Override
                            public void onNegative(MaterialDialog dialog) {
                                super.onNegative(dialog);
                            }
                        }).show();
                break;
            case R.mipmap.icon_mine_update:
                presenter.checkUpdate();
                break;

            case R.id.ll_logout:
                if (CartoonApp.getInstance().isLogin(SettingActivity.this))
                presenter.logout();

                break;

        }
    }

    @Override
    public void showLoadingView() {

    }

    @Override
    public void hideLoadingView() {

    }

    @Override
    public void updateNickName(String name) {

    }

    @Override
    public void updateGender(int gender) {

    }

    @Override
    public void showTips(String message) {
        ToastUtils.showLong(this, message);
    }

    @Override
    public void updateAvatar(String avatarUrl) {

    }

    @Override
    public void logoutSuccess() {
        if (this == null)
            return;
        CartoonApp.getInstance().logout();
        startActivity(new Intent(SettingActivity.this, LoginActivity.class));
        finish();
        //        ivMineUserIcon.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        // ivMineUserIcon.setBackgroundResource(R.drawable.selector_btn_white);
        // ivMineUserIcon.setImageResource(R.mipmap.icon_user_head);
    }

    @Override
    public void processUpdate(AppVersion appVersion) {

    }

    void deleteRecursive(File fileOrDirectory) {
        if (fileOrDirectory.isDirectory())
            for (File child : fileOrDirectory.listFiles())
                deleteRecursive(child);

        fileOrDirectory.delete();
    }
}
