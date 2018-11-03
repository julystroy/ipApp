package com.cartoon.module.tab.mine;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.cartoon.data.AppVersion;
import com.cartoon.http.BaseCallBack;
import com.cartoon.http.BuilderInstance;
import com.cartoon.http.StaticField;
import com.cartton.library.utils.ToastUtils;

import java.io.File;

import okhttp3.Call;

/**
 * Created by jinbangzhu on 7/22/16.
 */

public class MinePresenterImpl implements MinePresenter {
    MineView view;
    boolean isTips;
    public MinePresenterImpl(MineView view,boolean isTips) {
        this.view = view;
        this.isTips = isTips;
    }

    @Override
    public void updateUserInfo(final String nickName, final int gender) {
        view.showLoadingView();
        BuilderInstance.getInstance().getPostBuilderInstance(StaticField.URL_APP_UPDATE_INFO)
                .addParams("nickname", nickName)
                .addParams("gender", String.valueOf(gender))
                .build().execute(new BaseCallBack<String>() {

            @Override
            public void onLoadFail() {
                view.hideLoadingView();
                view.showTips(getMessage());
            }
            @Override
            public void onContentNull() {

            }
            @Override
            public void onLoadSuccess(String response) {
                view.hideLoadingView();
                view.updateGender(gender);
                view.updateNickName(nickName);
                view.showTips("更改成功");
                Log.d("onLoadSuccess", "response=" + response);
            }

            @Override
            public String parseNetworkResponse(String response) throws Exception {
                return response;
            }
        });

    }

    @Override
    public void uploadAvatar(File file) {
        view.showLoadingView();
        BuilderInstance.getInstance().getPostBuilderInstance(StaticField.URL_APP_UPDATE_AVATAR)
                .addFile("file", file.getName(), file)
                .build().execute(new BaseCallBack<String>() {

            @Override
            public void onLoadFail() {
                view.hideLoadingView();
                view.showTips(getMessage());
            }
            @Override
            public void onContentNull() {

            }
            @Override
            public void onLoadSuccess(String response) {
                view.hideLoadingView();
                view.showTips("更改头像成功");
                view.updateAvatar(response);
                Log.d("onLoadSuccess", "response=" + response);
            }
//http://xjzm.mopian.tv/book/upload/avatar/user/2016-12/30/336_1483084938333.jpg

            @Override
            public String parseNetworkResponse(String response) throws Exception {
                return response;
            }
        });
    }

    @Override
    public void logout() {
        view.showLoadingView();
        BuilderInstance.getInstance().getPostBuilderInstance(StaticField.URL_APP_LOGOUT)
                .build().execute(new BaseCallBack<String>() {

            @Override
            public void onLoadFail() {
//                view.showTips(getMessage());
                view.hideLoadingView();
                view.showTips("已退出登录");
                view.logoutSuccess();
            }
            @Override
            public void onContentNull() {

            }
            @Override
            public void onLoadSuccess(String response) {
                view.hideLoadingView();
                view.showTips("已退出登录");
                view.logoutSuccess();
                Log.d("onLoadSuccess", "response=" + response);
            }

            @Override
            public String parseNetworkResponse(String response) throws Exception {
                return response;
            }
        });
    }

    @Override
    public void checkUpdate() {
        view.showLoadingView();
        BuilderInstance.getInstance().getPostBuilderInstance(StaticField.URL_APP_VERSION)
                .build().execute(new BaseCallBack<AppVersion>() {

            @Override
            public void onLoadFail() {
                view.hideLoadingView();
                if(!isTips){
                    ToastUtils.hideToast();
                }else{
                    view.showTips(getMessage());
                }
            }

            @Override
            public void onContentNull() {

            }

            @Override
            public void onError(Call call, Exception e, int id) {
//                super.onError(call, e, id);
                if(!isTips){
                    ToastUtils.hideToast();
                }else{
                    view.showTips(getMessage());
                }
            }

            @Override
            public void onLoadSuccess(AppVersion response) {
                view.hideLoadingView();
                view.processUpdate(response);
            }

            @Override
            public AppVersion parseNetworkResponse(String response) throws Exception {
                return JSON.parseObject(response, AppVersion.class);

            }
        });
    }
}
