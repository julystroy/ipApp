package com.cartoon.data;

import android.content.Context;
import android.webkit.JavascriptInterface;

import com.cartoon.CartoonApp;
import com.cartoon.view.dialog.DeleteDialog;
import com.cartoon.view.dialog.ReportDialog;
import com.cartton.library.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by cc on 17-8-1.
 */
public class FanRen {
    private Context context;

    public FanRen(Context context) {
        this.context = context;
    }
    @JavascriptInterface
    public void goShowReport(String ss){//3242##2988##1
        final String[] split = ss.split("##");
        if (!CartoonApp.getInstance().isLogin(context)) {
            return;
        }
        DeleteDialog deleteDialog = new DeleteDialog(context,"举报");
        deleteDialog.setOnButtonClickListener(new DeleteDialog.buttonClick() {
            @Override
            public void onButtonClickListener() {
                if (split != null&&split.length==3) {
                    ReportDialog dialog1 = new ReportDialog(context,split[1],split[0],split[2]);
                    dialog1.show();
                }else{
                    ToastUtils.showShort(context,"举报异常");
                }
            }
        });
        deleteDialog.show();
    }


    @JavascriptInterface
    public void deleteDetail(String ss){
        DeleteDialog deleteDialog = new DeleteDialog(context,"删除");
        deleteDialog.setOnButtonClickListener(new DeleteDialog.buttonClick() {
            @Override
            public void onButtonClickListener() {
                EventBus.getDefault().post(new SplashUi());
            }
        });

        deleteDialog.show();
    }


    @JavascriptInterface
    public void getDataId(final String id){
        //Toast.makeText(mContext, webMessage, Toast.LENGTH_SHORT).show();
        if (id != null) {
           EventBus.getDefault().post(new ReadBus(id));
        }
    }

    @JavascriptInterface
    public void setVoteNum(String s){
       ToastUtils.showShort(context,s);
    }

}
