package com.cartoon.module.action;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.cartoon.CartoonApp;
import com.cartoon.data.RankHot;
import com.cartoon.http.BaseCallBack;
import com.cartoon.http.BuilderInstance;
import com.cartoon.http.StaticField;
import com.cartoon.module.BaseActivity;
import com.cartoon.utils.EasySharePreference;
import com.cartoon.utils.RegularUtils;
import com.cartoon.view.DialogToast;
import com.cartoon.view.richtext.RichTextEditor;
import com.cartton.library.utils.ToastUtils;
import com.zhy.http.okhttp.builder.PostFormBuilder;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.com.xuanjiezhimen.R;

/**
 * Created by cc on 17-8-30.
 */
public class ActionNoteActivity extends BaseActivity {
    @BindView(R.id.btn_cancel)
    Button         mBtnCancel;
    @BindView(R.id.btn_confirm)
    Button         mBtnConfirm;
    @BindView(R.id.et_new_title)
    EditText       mEtNewTitle;
    @BindView(R.id.et_new_content)
    RichTextEditor mEtNewContent;
    @BindView(R.id.iv_add_img)
    ImageView ivAddImg;
    final PostFormBuilder postFormBuilder =
            BuilderInstance.getInstance().getPostBuilderInstance(StaticField.URL_SEND_ACTION_NOTE)
                    .addParams("uid", CartoonApp.getInstance().getUserId());
    @Override
    protected int getContentViewId() {
        return R.layout.activity_note;
    }

    @Override
    protected int getFragmentContentId() {
        return 0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        mEtNewTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String title = s.toString();
                if (title!=null&&title.getBytes().length > 90)
                    DialogToast.createToastConfig().ToastShow(ActionNoteActivity.this,"输入标题多于30个汉字",2);
                mBtnConfirm.setSelected(true);
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString() == null || s.toString().isEmpty()) {
                    mBtnConfirm.setSelected(false);
                }
            }
        });

        ivAddImg.setVisibility(View.GONE);
    }

    @OnClick(R.id.btn_cancel)
    void onClickCancel() {
        if (/*!isImageEmpty() || etContent.getText().length()*/ 1> 0) {
            new MaterialDialog.Builder(this).title(R.string.notice)
                    .content("确定退出编辑吗?")
                    .positiveText("确定")
                    .negativeText("取消")
                    .callback(new MaterialDialog.ButtonCallback() {
                        @Override
                        public void onPositive(MaterialDialog dialog) {
                            super.onPositive(dialog);
                            //images = null;
                            onBackPressed();
                        }

                        @Override
                        public void onNegative(MaterialDialog dialog) {
                            super.onNegative(dialog);
                        }
                    }).show();
        } else {
            onBackPressed();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            onClickCancel();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
private String content;
    @OnClick(R.id.btn_confirm)
    void onClickConfirm() {

        String editData = getEditData();
         content = RegularUtils.getContent(editData);

        String title = mEtNewTitle.getText().toString();
      //  List<String> imgStr = RegularUtils.getImgStr(editData);
        if (editData==null && title==null) {
            //ToastUtils.showLong(this, "没有内容");
            DialogToast.createToastConfig().ToastShow(ActionNoteActivity.this,"没有内容",2);
        }  else if (title==null||title.isEmpty()){
            DialogToast.createToastConfig().ToastShow(ActionNoteActivity.this,"标题为空",2);
        }else if (content!=null&&content.getBytes().length<900){
            DialogToast.createToastConfig().ToastShow(ActionNoteActivity.this,"内容少于300字",2);
        }
        else if (title!=null&&title.getBytes().length > 90){
            DialogToast.createToastConfig().ToastShow(ActionNoteActivity.this,"输入标题多于30个汉字",2);
        }
        else {
           // String htmlContent = RegularUtils.getHtmlContent(editData);
            String rePlace = RegularUtils.rePlace(editData);
            postMoment(rePlace,title);
        }
    }

    private void postMoment(String htmlContent,String title) {
        showLoadingView();
        String actionId = EasySharePreference.getActionId(ActionNoteActivity.this);
        postFormBuilder.addParams("activityId",actionId)
                .addParams("essayContent",htmlContent)
                .addParams("essayTitle",title);


            postFormBuilder.addHeader("Content-Type", "multipart/form-data");
            sendPost(postFormBuilder);

    }

    private void sendPost(PostFormBuilder postFormBuilder) {

        postFormBuilder.build().execute(new BaseCallBack<String>() {

            @Override
            public void onLoadFail() {
                content=null;
                mBtnConfirm.setEnabled(true);
                if (TextUtils.isEmpty(getMessage())) {
                    ToastUtils.showLong(ActionNoteActivity.this, "操作失败");
                } else {
                    ////道友，您的妙笔里有敏感词!
                    if (getMessage().equals("道友，您的妙笔里有敏感词!")) {
                        DialogToast.createToastConfig().ToastShow(ActionNoteActivity.this,getMessage(),3);
                    }else
                    ToastUtils.showLong(ActionNoteActivity.this, getMessage());
                }
                hideLoadingView();
            }
            @Override
            public void onContentNull() {
            }
            @Override
            public void onLoadSuccess(String response) {
                content=null;
                ToastUtils.showShort(ActionNoteActivity.this,"发送成功");
                if (getBaseContext() == null) {
                    return;
                }
                mBtnConfirm.setEnabled(true);
                hideLoadingView();
                EventBus.getDefault().post(new RankHot());
                Log.d("onLoadSuccess", "response=" + response);
                finish();
            }
            @Override
            public String parseNetworkResponse(String response) throws Exception {
                return response;
            }
        });
    }

    //处理数据编辑提交
    private String getEditData() {
        List<RichTextEditor.EditData> editList = mEtNewContent.buildEditData();
        StringBuffer content = new StringBuffer();
        for (RichTextEditor.EditData itemData : editList) {
            if (itemData.inputStr != null) {
                content.append(itemData.inputStr);
                //Log.d("RichEditor", "commit inputStr=" + itemData.inputStr);
            } else if (itemData.imagePath != null) {
                content.append("<img src=" ).append(itemData.imagePath).append(" />");

                //Log.d("RichEditor", "commit imgePath=" + itemData.imagePath);
                //imageList.add(itemData.imagePath);
            }
        }
        return content.toString();
    }

    private MaterialDialog dialog;
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
