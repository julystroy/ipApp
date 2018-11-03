package com.cartoon.module.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;
import com.cartoon.CartoonApp;
import com.cartoon.data.UserInfo;
import com.cartoon.http.BaseCallBack;
import com.cartoon.http.StaticField;
import com.cartoon.module.BaseActivity;
import com.cartoon.utils.UserDB;
import com.cartton.library.utils.ToastUtils;
import com.zhy.http.okhttp.OkHttpUtils;

import butterknife.BindView;
import cn.com.xuanjiezhimen.R;

/**
 * 登录页面
 * <p>
 * Created by David on 16/7/2.
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.bt_left)
    ImageButton btLeft;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.bt_right)
    ImageButton btRight;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_pwd)
    EditText etPwd;
    @BindView(R.id.bt_login)
    Button btLogin;
    @BindView(R.id.bt_code)
    Button btCode;
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;
    @BindView(R.id.tv_accept)
    TextView tvAccept;
    @BindView(R.id.et_picCodes)
    EditText etPicCodes;
    @BindView(R.id.iv_showCode)
    ImageView ivShowCode;
    @BindView(R.id.select_point)
    ImageView selectPoint;

    private MaterialDialog dialog;


    /**
     * 是否开启倒计时
     */
    boolean isCutStart = false;

    private MyCount myCount;


    @Override
    protected int getContentViewId() {
        return R.layout.ac_login;
    }

    @Override
    protected int getFragmentContentId() {
        return 0;
    }


    boolean isSelected = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        btLeft.setImageResource(R.mipmap.icon_back_black);
        btRight.setVisibility(View.GONE);
        tvTitle.setText(R.string.ac_login);
        tvAccept.setText(Html.fromHtml("<font color=#8aa0cc>《凡人仙界篇用户协议》</font>"));
        selectPoint.setOnClickListener(this);
        selectPoint.setSelected(isSelected);

        myCount = new MyCount(60 * 1000, 1000);
// 将验证码用图片的形式显示出来
        initImage();

        btLogin.setOnClickListener(this);
        btLeft.setOnClickListener(this);
        tvAccept.setOnClickListener(this);
        ivShowCode.setOnClickListener(this);
        btCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mobile = etName.getText().toString().trim();
                String trim = etPicCodes.getText().toString().trim();
                if (!isPhoneNumberCorrect(mobile)) {
                    ToastUtils.showLong(LoginActivity.this, "手机号不正确");
                } else if (trim.isEmpty()){
                    ToastUtils.showLong(LoginActivity.this, "请先填写验证码");
                }
                else {
                    getCode(mobile,trim);
                }
            }
        });

    }

    private void initImage() {
        Glide.with(this).load(StaticField.URL_APP_IMAGE + "?deviceId=" + CartoonApp.getInstance().getDeviceId())
                .signature(new StringSignature(System.currentTimeMillis()+""))
                .into(ivShowCode);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_left:
                finish();
                break;
            case R.id.bt_login:


                //测试代码块记得删除
             // login("15811519834","12345");
                //-----------------------------------------------

                String name = etName.getText().toString().trim();
                String code = etPwd.getText().toString().trim();
                if (!isPhoneNumberCorrect(name)) {
                    ToastUtils.showLong(this, "手机号不正确");
                } else if (TextUtils.isEmpty(code)) {
                    ToastUtils.showLong(this, "请输入验证码");
                }  else if (!isSelected){
                    ToastUtils.showLong(this, "请选中凡人仙界篇官方APP协议");
                }else {
                    login(name, code);
                }
                break;
            case R.id.tv_accept:
                Intent intent = new Intent(this, LicenseActivity.class);
                intent.putExtra("type",3);
                startActivity(intent);
                break;
            case R.id.iv_showCode:
                //ivShowCode.setImageBitmap(CodeUtil.getInstance().createBitmap());
                initImage();
                break;
            case R.id.select_point:
                selectPoint.setSelected(!isSelected);
                isSelected = !isSelected;
                break;

        }
    }

    private boolean isPhoneNumberCorrect(String pPhoneNumber) {
        if (pPhoneNumber.length() == 11) return true;
        return false;
    }

    private void getCode(String mobile,String captcha ) {
        showLoadingView();
        OkHttpUtils.get().url(StaticField.URL_APP_CAPTCHA)
                .addParams("mobile", mobile)
                .addParams("captcha", captcha)
                .addParams("deviceId", CartoonApp.getInstance().getDeviceId())
                .build().execute(new BaseCallBack<String>() {
            @Override
            public void onLoadFail() {
                hideLoadingView();
                if (getErrorCode() == 33 || getErrorCode() == 22) {// 超过频率，同一个手机号同一验证码模板每30秒只能发送一条
                    ToastUtils.showShort(getBaseContext(), "验证频率过高，请稍后再试");
                }
            }
            @Override
            public void onContentNull() {

            }
            @Override
            public void onLoadSuccess(String response) {
                hideLoadingView();
                myCount.start();
                if (null != etPwd) etPwd.requestFocus();
                ToastUtils.showShort(getBaseContext(), "发送成功");
            }

            @Override
            public String parseNetworkResponse(String response) throws Exception {
                return response;
            }
        });
    }


    private void login(String name, String code) {

        showLoadingView();
        //获取设备唯一识别id
        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        String deviceId = "";
        if (tm.getDeviceId().isEmpty()) {
            deviceId = "凡人仙界篇";
        } else {
            deviceId = tm.getDeviceId();
        }
        OkHttpUtils.get().url(StaticField.URL_APP_LOGIN)
                .addParams("mobile", name)
                .addParams("code", code)
                .addParams("device_id", deviceId)
                .build().execute(new BaseCallBack<UserInfo>() {
            @Override
            public void onLoadFail() {
                ToastUtils.showShort(getBaseContext(), "登录失败");
                hideLoadingView();
            }
            @Override
            public void onContentNull() {

            }
            @Override
            public void onLoadSuccess(UserInfo response) {
                hideLoadingView();
               /* UserInfo userInfo = response;
                userInfo.setToken("123");
                CartoonApp.getInstance().setUserInfo(userInfo);*/
                CartoonApp.getInstance().setUserInfo(response);
                CartoonApp.getInstance().refreshJPushTag();
                UserDB.getInstance().buildSectDB();//创建宗门成员基础信息
                finish();
            }

            @Override
            public UserInfo parseNetworkResponse(String response) throws Exception {
                return JSON.parseObject(response, UserInfo.class);
            }
        });



       /* //模拟登录
        UserInfo userInfo = new UserInfo();
        userInfo.setId("21");
        userInfo.setMoblie("15811519834");
        userInfo.setNickname("金克拉");
        userInfo.setToken("EBAF71C9A7E1530AE02E11A990FCA64C");
        userInfo.setGender(1);
        userInfo.setDevice_id("869336026355667");
        userInfo.setStatus(0);
        userInfo.setBonus_point("100");
        userInfo.setBonus_stone("0");


        //模拟登录2
        UserInfo userInfo2 = new UserInfo();
        userInfo2.setId("24");
        userInfo2.setMoblie("13021280465");
        userInfo2.setNickname("刘备");
        userInfo2.setToken("1B13DAE00406D369491FE688EB2197C6");
        userInfo2.setGender(1);
        userInfo2.setDevice_id("865790025940040");
        userInfo2.setStatus(0);
        userInfo2.setBonus_point("100");
        userInfo2.setBonus_stone("0");


        CartoonApp.getInstance().setUserInfo(userInfo);

        CartoonApp.getInstance().refreshJPushTag();

        ToastUtils.showShort(getBaseContext(), "模拟登录2");*/
    }

    /* 定义一个倒计时的内部类 */
    class MyCount extends CountDownTimer {
        public MyCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            isCutStart = false;
            btCode.setText(R.string.repat_verification);
            btCode.setEnabled(true);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            btCode.setEnabled(false);
            btCode.setText(millisUntilFinished / 1000 + "s");
        }

    }


    public void showLoadingView() {
        if (dialog == null)
            dialog = new MaterialDialog.Builder(this)
                    .title(R.string.notice)
                    .content(R.string.please_wait)
                    .progress(true, 0)
                    .build();

        dialog.show();
    }

    public void hideLoadingView() {
        if (dialog.isShowing())
            dialog.dismiss();
    }

}
