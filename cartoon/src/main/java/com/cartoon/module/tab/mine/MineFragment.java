package com.cartoon.module.tab.mine;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.cartoon.CartoonApp;
import com.cartoon.data.AppVersion;
import com.cartoon.data.UserInfo;
import com.cartoon.http.BaseCallBack;
import com.cartoon.http.BuilderInstance;
import com.cartoon.http.StaticField;
import com.cartoon.module.BaseFragment;
import com.cartoon.module.feedback.FeedbackActivity;
import com.cartoon.module.login.LoginActivity;
import com.cartoon.module.mymoment.MyMomentActivity;
import com.cartoon.module.mytask.GradeDetailActivity;
import com.cartoon.module.tab.bookfriendmoment.PreviewPhotoActivity;
import com.cartoon.utils.EasySharePreference;
import com.cartoon.utils.FileSizeUtil;
import com.cartoon.utils.ImageLoaderUtils;
import com.cartoon.utils.UserDB;
import com.cartoon.utils.Utils;
import com.cartoon.utils.WebsocketUtil;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.com.xuanjiezhimen.BuildConfig;
import cn.com.xuanjiezhimen.R;

/**
 * 我的
 * <p/>
 * Created by David on 16/6/5.
 */
public class MineFragment extends BaseFragment implements View.OnClickListener, MineView {


    @BindView(R.id.bt_left)
    ImageButton    btLeft;
    @BindView(R.id.tv_title)
    TextView       tvTitle;
    @BindView(R.id.bt_right)
    ImageButton    btRight;
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;
    @BindView(R.id.rl_fastblue)
    ImageView      rlFastblue;
    @BindView(R.id.edit_userinfo)
    TextView       editUserinfo;
    @BindView(R.id.iv_mine_user_icon)
    ImageView      ivMineUserIcon;
    @BindView(R.id.btn_mine_login)
    TextView       btnMineLogin;
    @BindView(R.id.tv_mine_user_name)
    TextView       tvMineUserName;
    @BindView(R.id.tv_mine_user_stonenum)
    TextView       tvMineUserStoneNum;
    @BindView(R.id.tv_mine_user_bonuspoint)
    TextView       tvMineUserBonusPoint;
    @BindView(R.id.textPoint)
    TextView       textPoint;


    @BindView(R.id.ll_container_2)
    LinearLayout   mLlContainer2;

    @BindView(R.id.ll_logout)
    LinearLayout   mLlLogout;


    private MaterialDialog dialog;

    private MinePresenter presenter;

    private TextView tvCacheSize;

    private ImageView ivNewMessage;

    private boolean isTips = true;
    private String[] list;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new MinePresenterImpl(this, isTips);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fg_mine2;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        tvTitle.setText(R.string.tab_mine);

       // btRight.setOnClickListener(this);
        btLeft.setOnClickListener(this);

        //btRight.setImageResource(R.mipmap.icon_mine_message2);
        btLeft.setVisibility(View.GONE);
        btRight.setVisibility(View.GONE);


        ivMineUserIcon.setOnClickListener(this);

        editUserinfo.setOnClickListener(this);
        tvMineUserBonusPoint.setOnClickListener(this);
        btnMineLogin.setOnClickListener(this);
        setupView();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Glide.get(getContext()).clearDiskCache();
                deleteRecursive(getContext().getCacheDir());
                readFileSizeAndShowIt();
            }
        }).start();
    }

    private void setupView() {
        int[] titleArr2;
        int[] imgId2;

        titleArr2 = new int[]{
                R.string.my_state,
                R.string.user_feedback,
                R.string.clear_cache,
                R.string.my_upadte};


        imgId2 = new int[]{
                R.mipmap.icon_mine_state,
                R.mipmap.icon_mine_feedback,
                R.mipmap.icon_mine_cache,
                R.mipmap.icon_mine_update};


        for (int i = 0; i < imgId2.length; i++) {
            View subView = buildItemView(titleArr2[i], imgId2, i);
            mLlContainer2.addView(subView);
        }

        mLlLogout.setOnClickListener(this);

    }
    @NonNull
    private View buildItemView(int resid, int[] imgId1, int i) {
        View subView = LayoutInflater.from(getContext()).inflate(R.layout.part_mine_item, null);

        RelativeLayout item = (RelativeLayout) subView.findViewById(R.id.rl_item);
        item.setId(imgId1[i]);
        item.setOnClickListener(this);
        ImageView icon = (ImageView) subView.findViewById(R.id.iv_icon);
        ImageView arrow = (ImageView) subView.findViewById(R.id.iv_arrow);
        TextView textView = (TextView) subView.findViewById(R.id.tv_text);
        TextView textExtra = (TextView) subView.findViewById(R.id.tv_extra);
        View line = subView.findViewById(R.id.line);
        View space = subView.findViewById(R.id.space1);

        icon.setImageResource(imgId1[i]);
        textView.setText(resid);
        if (i == imgId1.length - 1) {
            line.setVisibility(View.GONE);
        }

        if (i == 0)
            space.setVisibility(View.VISIBLE);
        if (imgId1[i] == R.mipmap.icon_mine_cache) {
            textExtra.setVisibility(View.VISIBLE);
            arrow.setVisibility(View.GONE);
            space.setVisibility(View.VISIBLE);
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
    private void setupUserInfo() {
        tvMineUserName.setVisibility(View.VISIBLE);
        btnMineLogin.setVisibility(View.GONE);
        mLlLogout.setVisibility(View.VISIBLE);

        tvMineUserName.setText(CartoonApp.getInstance().getUserInfo().getNickname());


        //  NicknameColorHelper.setNicknameColor(tvMineUserName, CartoonApp.getInstance().getUserInfo().getId());

        ImageLoaderUtils.displayRound(getContext(), ivMineUserIcon, Utils.addImageDomain(CartoonApp.getInstance().getUserInfo().getAvatar()));

        BuilderInstance.getInstance().getPostBuilderInstance(StaticField.URL_MYINFO)
                .addParams("deviceId", CartoonApp.getInstance().getDeviceId())
                .build().execute(new BaseCallBack<UserInfo>() {
            @Override
            public void onLoadFail() {
                UserInfo userLastInfo = CartoonApp.getInstance().getUserLastInfo();
                if (userLastInfo != null) {
                    String lvName = userLastInfo.getLvName();

                    tvMineUserBonusPoint.setText("等级 " + lvName);
                    tvMineUserStoneNum.setText("         灵石 " + userLastInfo.getBonus_stone());
                } else {
                    tvMineUserBonusPoint.setText("等级 位置");
                    tvMineUserStoneNum.setText("         灵石 未知");
                }
            }

            @Override
            public void onContentNull() {

            }

            @Override
            public void onLoadSuccess(UserInfo response) {
                if (response == null)
                    return;
                tvMineUserBonusPoint.setText("等级 " + response.getLvName());
                tvMineUserStoneNum.setText("          灵石 " + response.getBonus_stone());


        //        adpter.isNewMessage(response.isHasSectionMsg(), response.isHasTaskMsg());

            }

            @Override
            public UserInfo parseNetworkResponse(String response) throws Exception {
                return JSON.parseObject(response, UserInfo.class);
            }
        });


    }


    @Override
    public void onResume() {
        super.onResume();
        if (isUserAlreadyLogin()) {
            setupUserInfo();
        } else {
            btnMineLogin.setVisibility(View.VISIBLE);
            mLlLogout.setVisibility(View.GONE);
            ivMineUserIcon.setImageResource(R.mipmap.icon_head);
            tvMineUserBonusPoint.setText("等级 未知");
            tvMineUserStoneNum.setText("     灵石 未知");
            // tvMineUserName.setText("未知");
            tvMineUserName.setVisibility(View.GONE);
        }

        UserInfo userInfo = CartoonApp.getInstance().getUserInfo();
        if (userInfo != null) {
            list = new String[]{Utils.addImageDomain(userInfo.getAvatar())};
            ImageLoaderUtils.displayGaussian(getContext(), Utils.addImageDomain(userInfo.getAvatar()), rlFastblue);
        } else {
            ImageLoaderUtils.displayGaussian(getContext(), "", rlFastblue);
        }

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            int myMessageCount = EasySharePreference.getMyMessageCount(getContext());
            if (myMessageCount == 0)
                textPoint.setText(String.valueOf(1));
            else
                textPoint.setText(String.valueOf(myMessageCount));
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_mine_user_icon:
                if (list != null) {
                    startActivity(new Intent(getContext(), PreviewPhotoActivity.class)
                            .putExtra(PreviewPhotoActivity.PHOTO_URLS, list)
                            .putExtra(PreviewPhotoActivity.POSITION, 0));
                }
                break;
            case R.id.edit_userinfo:
                if (isUserAlreadyLogin())
                    startActivity(new Intent(getActivity(), ProfileEditActivity.class));
                else
                    startActivity(new Intent(getContext(), LoginActivity.class));
                break;
            case R.id.tv_mine_user_bonuspoint://点击等级
                if (isUserAlreadyLogin())
                    startActivity(new Intent(getActivity(), GradeDetailActivity.class));
                break;

            case R.id.btn_mine_login:
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
                //                getActivity().finish();
                break;

            case R.mipmap.icon_mine_feedback:
                if (CartoonApp.getInstance().isLogin(getContext()))
                    startActivity(new Intent(getContext(), FeedbackActivity.class));
                break;
            case R.mipmap.icon_mine_cache:
                new MaterialDialog.Builder(getContext()).title(R.string.notice)
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
                                        Glide.get(getContext()).clearDiskCache();
                                        deleteRecursive(getContext().getCacheDir());
                                     //   deleteRecursive(downFolder);

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
            case R.mipmap.icon_mine_state:
                if (isUserAlreadyLogin())
                    startActivity(new Intent(getContext(), MyMomentActivity.class));
                else
                    startActivity(new Intent(getContext(), LoginActivity.class));
                break;

            case R.id.ll_logout:
                if (isUserAlreadyLogin()) {
                    WebsocketUtil.getInstance().sendLeave();
                    UserDB.getInstance().deleteSectTable();
                    cleanInfo();
                    presenter.logout();
                    tvMineUserName.setVisibility(View.GONE);
                    tvMineUserBonusPoint.setText("等级未知");
                    tvMineUserStoneNum.setText("灵石:未知");
                }
                break;


        }
    }

    private void cleanInfo() {
        EasySharePreference.setLastQuestionTime(getContext(),0);
        EasySharePreference.setPositionNum(getContext(),0);
        EasySharePreference.setHaveAnswered(getContext(),false);
        EasySharePreference.setAllQuestion(getContext(),null);
        EasySharePreference.setFightQuestion(getContext(),null);
        EasySharePreference.setSectHaveAnswered(getContext(),false);
        EasySharePreference.setSectQuestionTime(getContext(),0);
        EasySharePreference.setSectPositionNum(getContext(),0);
    }

    void deleteRecursive(File fileOrDirectory) {
            if (fileOrDirectory.isDirectory())
                for (File child : fileOrDirectory.listFiles())
                    deleteRecursive(child);

            fileOrDirectory.delete();
        }

    private boolean isUserAlreadyLogin() {
        UserInfo userInfo = CartoonApp.getInstance().getUserInfo();
        return userInfo != null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }


    @Override
    public void showLoadingView() {
        if (dialog == null)
            dialog = new MaterialDialog.Builder(getContext())
                    .title(R.string.notice)
                    .content(R.string.please_wait)
                    .progress(true, 0)
                    .build();

        dialog.show();
    }

    @Override
    public void hideLoadingView() {
        if (dialog.isShowing())
            dialog.dismiss();
    }

    @Override
    public void updateNickName(String name) {

    }

    @Override
    public void updateGender(int gender) {

    }

    @Override
    public void showTips(String message) {

    }

    @Override
    public void updateAvatar(String avatarUrl) {

    }

    @Override
    public void logoutSuccess() {
        if (getActivity() == null)
            return;
        CartoonApp.getInstance().logout();
        btnMineLogin.setVisibility(View.VISIBLE);
        mLlLogout.setVisibility(View.GONE);
    }

    @Override
    public void processUpdate(final AppVersion appVersion) {
    }




    private void readFileSizeAndShowIt() {
        final double sizeCache = FileSizeUtil.getFileOrFilesSize(getActivity().getCacheDir().getAbsolutePath(), 3);

        final int totalSize = (int) (sizeCache );
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (getActivity() != null && tvCacheSize != null) {
                        tvCacheSize.setText(totalSize + "M");
                    }
                }
            });
        }
        }
    }


