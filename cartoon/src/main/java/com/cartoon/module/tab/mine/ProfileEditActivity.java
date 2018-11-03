package com.cartoon.module.tab.mine;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.cartoon.CartoonApp;
import com.cartoon.data.AppVersion;
import com.cartoon.data.UserInfo;
import com.cartoon.http.BaseCallBack;
import com.cartoon.http.StaticField;
import com.cartoon.module.BaseActivity;
import com.cartoon.utils.BitmapUtils;
import com.cartoon.utils.NicknameColorHelper;
import com.cartoon.utils.UserDB;
import com.cartoon.utils.Utils;
import com.cartton.library.utils.ToastUtils;
import com.zhy.http.okhttp.OkHttpUtils;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import butterknife.BindView;
import cn.com.xuanjiezhimen.R;

/**
 * Created by jinbangzhu on 7/22/16.
 */

public class ProfileEditActivity extends BaseActivity implements View.OnClickListener, MineView {
    @BindView(R.id.bt_left)
    ImageButton    btLeft;
    @BindView(R.id.tv_title)
    TextView       tvTitle;
    @BindView(R.id.bt_right)
    ImageButton    btRight;
    @BindView(R.id.tv_nickname)
    TextView       tvNickname;
    @BindView(R.id.iv_avatar)
    ImageView      ivAvatar;
    @BindView(R.id.ll_change_avatar)
    LinearLayout   llChangeAvatar;
    @BindView(R.id.rl_nickname)
    RelativeLayout rlNickname;
    @BindView(R.id.tv_gender)
    TextView       tvGender;
    @BindView(R.id.rl_gender)
    RelativeLayout rlGender;


    private String pickupPhotoFile;
    private static final int PICK_IMAGE     = 2;
    private static final int MAX_PHOTO_SIZE = 100; // kb

    private MinePresenter presenter;

    private MaterialDialog dialog;
    private boolean isTips = true;
    private String[] mSplitedStytle;

    @Override
    protected int getContentViewId() {
        return R.layout.profile_edit;
    }

    @Override
    protected int getFragmentContentId() {
        return 0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupView();
        presenter = new MinePresenterImpl(this, isTips);
    }

    private void setupView() {
        tvTitle.setText("编辑资料");
        btRight.setVisibility(View.GONE);
        btLeft.setImageResource(R.mipmap.icon_back_black);
        btLeft.setOnClickListener(this);
        rlGender.setOnClickListener(this);
        rlNickname.setOnClickListener(this);
        llChangeAvatar.setOnClickListener(this);

        if (CartoonApp.getInstance().getUserInfo() != null) {
            //获取服务器端用户昵称
            OkHttpUtils.get().url(String.format(StaticField.URL_USER_GET_NICKNAME, CartoonApp.getInstance().getUserId())).build().execute(new BaseCallBack() {
                @Override
                public void onLoadFail() {

                }

                @Override
                public void onContentNull() {

                }

                @Override
                public void onLoadSuccess(Object response) {
                    String nickname = ((JSONObject) response).optString("nickname", "");
                    UserInfo userInfo = CartoonApp.getInstance().getUserInfo();
                    userInfo.setNickname(nickname);
                    CartoonApp.getInstance().setUserInfo(userInfo);
                    tvNickname.setText(CartoonApp.getInstance().getUserInfo().getNickname());
                    NicknameColorHelper.setNicknameColor(tvNickname , CartoonApp.getInstance().getUserInfo().getId());

                    getNameStyle(nickname , tvNickname.getText().toString());

                }

                @Override
                public Object parseNetworkResponse(String response) throws Exception {
                    return new JSONObject(response);
                }
            });


            tvNickname.setText(CartoonApp.getInstance().getUserInfo().getNickname());
            NicknameColorHelper.setNicknameColor(tvNickname , CartoonApp.getInstance().getUserId());
            tvGender.setText(getReadableGender(CartoonApp.getInstance().getUserInfo().getGender()));
            Glide.with(this).load(Utils.addImageDomain(CartoonApp.getInstance().getUserInfo().getAvatar()))
                    .placeholder(R.mipmap.icon_head)
                    .centerCrop()
                    .into(ivAvatar);
        }

    }

    private void getNameStyle(String nickname, String nameText) {
        mSplitedStytle = nickname.split(">" + nameText + "</");

        //System.out.println(mSplitedStytle);
    }

    private String getReadableGender(int gender) {
        return gender == 1 ? "男" : "女";
    }

    private int getRobotGender(String gender) {
        return "男".equals(gender) ? 1 : 0;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_left:
                onBackPressed();
                break;
            case R.id.rl_gender:
                showChooseGenderDialog();
                break;
            case R.id.rl_nickname:
                showModifyNicknameDialog();
                break;
            case R.id.ll_change_avatar:
                choosePhoto();
                break;
        }
    }

    private void showModifyNicknameDialog() {
        String mUserNameColor = null;
        int mUserNameColorCode  = ProfileEditActivity.this.getResources().getColor(R.color.default_nickname);
        if (mSplitedStytle != null && mSplitedStytle.length > 1) {//// FIXME: 16-11-23 nullpoint
                String[] split = mSplitedStytle[0].split("#");
                if (split.length > 0 ) {
                    CharSequence color = split[1].subSequence(0, 6);//// FIXME: 16-12-21  越界
                    if (color.length() == 6) {
                        mUserNameColor = "#" + color;
                    }
                }
        }

        if (!TextUtils.isEmpty(mUserNameColor)) {
            mUserNameColorCode = Color.parseColor(mUserNameColor);
        }

        new MaterialDialog.Builder(this)
                .title("更改昵称").contentColor(mUserNameColorCode)
                .inputType(InputType.TYPE_CLASS_TEXT)
                .input("", tvNickname.getText(), new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(MaterialDialog dialog, CharSequence input) {

                        if (input.length() == 0) {
                            ToastUtils.showLong(ProfileEditActivity.this, "不能为空");
                        } else if (input.toString().getBytes().length >18 ) {

                            ToastUtils.showLong(ProfileEditActivity.this, "雅名过长(12个字符以内)");
                        } else if (input.toString().contains("#") || input.toString().contains("<") || input.toString().contains(">")) {
                            ToastUtils.showLong(ProfileEditActivity.this, "包含非法字符");
                        } else {
                            if (!tvNickname.getText().toString().equals(input.toString())) {
                                presenter.updateUserInfo((null != mSplitedStytle && mSplitedStytle.length == 2) ? mSplitedStytle[0] + ">" + input.toString() + "</" + mSplitedStytle[1] : input.toString(), getRobotGender(tvGender.getText().toString()));
                            }
                        }

                    }
                })
                .show();
    }

    private void showChooseGenderDialog() {
        new MaterialDialog.Builder(this).items("男", "女")
                .title("更改性别")
                .negativeText(R.string.cancel)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                        presenter.updateUserInfo(tvNickname.getText().toString(), getRobotGender(text.toString()));
                    }
                })
                .show();
    }

    @Override
    public void showLoadingView() {
        if (dialog == null)
            dialog = new MaterialDialog.Builder(this)
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
        tvNickname.setText(name);
        NicknameColorHelper.setNicknameColor(tvNickname , CartoonApp.getInstance().getUserId());
        UserInfo userInfo = CartoonApp.getInstance().getUserInfo();
        userInfo.setNickname(name);
        CartoonApp.getInstance().setUserInfo(userInfo);
        UserDB.getInstance().updataNickName(userInfo.getId(),name);//用户基础信息
    }

    @Override
    public void updateGender(int gender) {
        tvGender.setText(getReadableGender(gender));

        UserInfo userInfo = CartoonApp.getInstance().getUserInfo();
        userInfo.setGender(gender);
        CartoonApp.getInstance().setUserInfo(userInfo);

    }

    @Override
    public void showTips(String message) {
        ToastUtils.showLong(this, message);
    }

    @Override
    public void updateAvatar(String avatarUrl) {
        Glide.with(this).load(avatarUrl)
                .centerCrop()
                .into(ivAvatar);

        UserInfo userInfo = CartoonApp.getInstance().getUserInfo();
        userInfo.setAvatar(avatarUrl);
        CartoonApp.getInstance().setUserInfo(userInfo);
    }
///upload/avatar/user/2016-12/30/336_1483066409618.jpg
    //http://xjzm.mopian.tv/book/upload/avatar/user/2016-12/30/336_1483084938333.jpg
    @Override
    public void logoutSuccess() {

    }

    @Override
    public void processUpdate(AppVersion appVersion) {

    }


    private void choosePhoto() {
        pickupPhotoFile = getCacheDir().getAbsolutePath() + File.separator + System.currentTimeMillis() + ".jpg";
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, PICK_IMAGE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
            processSelectedPhoto(data);
        }
    }


    private void processSelectedPhoto(Intent data) {
        if (null != data) {
            Uri selectedImage = data.getData();
            String[] projection = {MediaStore.MediaColumns.DATA};
            Cursor cursor = getContentResolver().query(selectedImage, projection, null, null, null);

            if (cursor == null || !cursor.moveToFirst()) {
                Bitmap bitmap = null;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (null == bitmap) {
                    ToastUtils.showLong(this, "无效的图片");
                } else {
                    BitmapUtils.saveBitmapToFile(bitmap, pickupPhotoFile, MAX_PHOTO_SIZE);
                    presenter.uploadAvatar(new File(pickupPhotoFile));
                }
            } else {
                int columnIndex = cursor.getColumnIndex(MediaStore.MediaColumns.DATA);
                String filePath = cursor.getString(columnIndex);
                cursor.close();

                Log.d("", "filePath = " + filePath);

                if (TextUtils.isEmpty(filePath)) {
                    ToastUtils.showLong(this, "无效的图片");
                } else {
                    Bitmap bitmap = BitmapUtils.decodeFile(filePath, 1024, 1024);
                    if (bitmap == null) {
                        ToastUtils.showLong(this, "无效的图片");
                    } else {
                        BitmapUtils.saveBitmapToFile(bitmap, pickupPhotoFile, MAX_PHOTO_SIZE);
                        presenter.uploadAvatar(new File(pickupPhotoFile));
                    }
                }
            }
        }
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (null != savedInstanceState) {
            pickupPhotoFile = savedInstanceState.getString("pickupPhotoFile");
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("pickupPhotoFile", pickupPhotoFile);
        super.onSaveInstanceState(outState);
    }
}
