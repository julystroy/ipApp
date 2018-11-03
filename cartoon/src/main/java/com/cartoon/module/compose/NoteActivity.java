package com.cartoon.module.compose;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
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
import com.cartoon.http.BaseCallBack;
import com.cartoon.http.BuilderInstance;
import com.cartoon.http.StaticField;
import com.cartoon.module.BaseActivity;
import com.cartoon.utils.Compressor;
import com.cartoon.utils.DisplayUtil;
import com.cartoon.utils.ImageUtil;
import com.cartoon.utils.RegularUtils;
import com.cartoon.utils.SDCardUtil;
import com.cartoon.view.DialogToast;
import com.cartoon.view.richtext.RichTextEditor;
import com.cartton.library.utils.ToastUtils;
import com.cndroid.pickimagelib.Intents;
import com.cndroid.pickimagelib.PickupImageBuilder;
import com.zhy.http.okhttp.builder.PostFormBuilder;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.com.xuanjiezhimen.R;

/**
 * Created by cc on 17-7-25.
 */
public class NoteActivity extends BaseActivity /*implements View.OnLayoutChangeListener*/ {
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

    //Activity最外层的Layout视图
    private View activityRootView;
    //屏幕高度
    private int screenHeight = 0;
    //软件盘弹起后所占高度阀值
    private int keyHeight = 0;

    private static final int MAX_IMAGE_SIZE = 500;
    final PostFormBuilder postFormBuilder =
            BuilderInstance.getInstance().getPostBuilderInstance(StaticField.URL_SEND_NOTE)
            .addParams("uid", CartoonApp.getInstance().getUserId())
            .addParams("token",CartoonApp.getInstance().getToken());
    @Override
    protected int getContentViewId() {
        return R.layout.activity_note;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         activityRootView = findViewById(R.id.ll_note);
        //获取屏幕高度
        screenHeight = this.getWindowManager().getDefaultDisplay().getHeight();
        //阀值设置为屏幕高度的1/3
        keyHeight = screenHeight/3;

        mEtNewTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String title = s.toString();
                if (title!=null&&title.getBytes().length > 90)
                DialogToast.createToastConfig().ToastShow(NoteActivity.this,"输入标题多于30个汉字",2);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        ivAddImg.setVisibility(View.GONE);
    }

    @Override
    public void onResume() {
        super.onResume();
        //添加layout大小发生改变监听器
        //activityRootView.addOnLayoutChangeListener(this);
    }

    @Override
    protected int getFragmentContentId() {
        return 0;
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

    @OnClick(R.id.btn_confirm)
    void onClickConfirm() {

        String editData = getEditData();
        String content = RegularUtils.getContent(editData);
        String title = mEtNewTitle.getText().toString();
         List<String> imgStr = RegularUtils.getImgStr(editData);
        if (editData==null && title==null) {
            //ToastUtils.showLong(this, "没有内容");
            DialogToast.createToastConfig().ToastShow(NoteActivity.this,"没有内容",2);
        } else if (imgStr.size() > 9) {
           // ToastUtils.showLong(this, "上传图片个数大于九张");
            DialogToast.createToastConfig().ToastShow(NoteActivity.this,"上传图片个数大于九张",2);
        } else if (title==null||title.isEmpty()){
            DialogToast.createToastConfig().ToastShow(NoteActivity.this,"标题为空",2);
        }else if (content!=null&&content.getBytes().length<270){
            DialogToast.createToastConfig().ToastShow(NoteActivity.this,"内容少于100字",2);
        }
        else if (title!=null&&title.getBytes().length > 90){
            DialogToast.createToastConfig().ToastShow(NoteActivity.this,"输入标题多于30个汉字",2);
        }
        else {
            //如果有图片
           // String htmlContent = RegularUtils.getHtmlContent(editData);
          //  postMoment(htmlContent,title,imgStr);
            String s = RegularUtils.rePlace(editData);
            postMoment(s,title,imgStr);
        }
    }

    private void postMoment(String htmlContent,String title,final List<String> imgStr) {
        showLoadingView();
        postFormBuilder.addParams("content",htmlContent)
                .addParams("title",title);

        if (imgStr.size()>0) {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... params) {
                    for (final String image : imgStr) {
                        File fileOrigin = new File(image);
                        File compressedImage;
                        if (fileOrigin.length() > MAX_IMAGE_SIZE * 1024) {
                            compressedImage = new Compressor.Builder(NoteActivity.this)
                                    .setMaxWidth(720)
                                    .setMaxHeight(1080)
                                    .setQuality(85)
                                    .setCompressFormat(Bitmap.CompressFormat.PNG)
                                    .build()
                                    .compressToFile(fileOrigin);
                        } else {
                            compressedImage = fileOrigin;
                        }
                        Log.d("postMoment", compressedImage.getName() + " size = " + compressedImage.length() / 1024.0 + "kb");

                        postFormBuilder.addFile(image, compressedImage.getName(), compressedImage);
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    super.onPostExecute(aVoid);
                    sendPost(postFormBuilder);
                }
            }.execute();

        } else {
            postFormBuilder.addHeader("Content-Type", "multipart/form-data");
            sendPost(postFormBuilder);
        }
    }

    private void sendPost(PostFormBuilder postFormBuilder) {

        postFormBuilder.build().execute(new BaseCallBack<String>() {

            @Override
            public void onLoadFail() {
                mBtnConfirm.setEnabled(true);
                if (TextUtils.isEmpty(getMessage())) {
                    ToastUtils.showLong(NoteActivity.this, "操作失败");
                } else {
                    ToastUtils.showLong(NoteActivity.this, getMessage());
                }
                hideLoadingView();
            }
            @Override
            public void onContentNull() {
            }
            @Override
            public void onLoadSuccess(String response) {
                ToastUtils.showShort(NoteActivity.this,"发送成功");
                if (getBaseContext() == null) {
                    return;
                }
                mBtnConfirm.setEnabled(true);
                hideLoadingView();
                Log.d("onLoadSuccess", "response=" + response);
               finish();
            }
            @Override
            public String parseNetworkResponse(String response) throws Exception {
                return response;
            }
        });
    }

    @OnClick(R.id.iv_add_img)
    void onClickAddImg(){
        PickupImageBuilder.with(this)
                .setTitle("添加图片")
                //.selectedImages(images)
                .showCamera(true)
                .setMaxChosenLimit(2)
                .startPickupImage(new ImageLoader());
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (data != null) {
                if (requestCode == 1){
                    //处理调用系统图库
                } else if (requestCode == Intents.ImagePicker.REQUEST_CODE_PICKUP){
                    //异步方式插入图片
                    insertImagesSync(data);
                }
            }
        }
    }

    private void insertImagesSync(Intent data) {
        mEtNewContent.measure(0, 0);
        int width = DisplayUtil.getScreenWidth(NoteActivity.this);
        int height = DisplayUtil.getScreenHeight(NoteActivity.this);
        String[] photos = data.getStringArrayExtra(Intents.ImagePicker.RESULT_ITEMS);
        //可以同时插入多张图片
        for (String imagePath : photos) {
            //Log.i("NewActivity", "###path=" + imagePath);
            Bitmap bitmap = ImageUtil.getSmallBitmap(imagePath, width, height);//压缩图片
            //bitmap = BitmapFactory.decodeFile(imagePath);
            imagePath = SDCardUtil.saveToSdCard(bitmap);
            mEtNewContent.insertImage(imagePath, mEtNewContent.getMeasuredWidth());
        }
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

    /*@Override
    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
        //现在认为只要控件将Activity向上推的高度超过了1/3屏幕高，就认为软键盘弹起
        if(oldBottom != 0 && bottom != 0 &&(oldBottom - bottom > keyHeight)){

            if (ivAddImg.getVisibility() == View.GONE)
            ivAddImg.setVisibility(View.VISIBLE);

        }else if(oldBottom != 0 && bottom != 0 &&(bottom - oldBottom > keyHeight)){

            if (ivAddImg.getVisibility() == View.VISIBLE)
                ivAddImg.setVisibility(View.GONE);
        }
    }*/

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
