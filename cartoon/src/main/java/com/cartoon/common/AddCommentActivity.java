package com.cartoon.common;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.alibaba.fastjson.JSON;
import com.cartoon.CartoonApp;
import com.cartoon.Constants;
import com.cartoon.http.BaseCallBack;
import com.cartoon.http.BuilderInstance;
import com.cartoon.http.StaticField;
import com.cartoon.module.BaseActivity;
import com.cartoon.module.compose.ImageLoader;
import com.cartoon.module.compose.PickedGridImageAdapter;
import com.cartoon.module.tab.bookfriendmoment.PreviewPhotoActivity;
import com.cartoon.utils.Compressor;
import com.cartoon.view.ExpandGridView;
import com.cartton.library.utils.ToastUtils;
import com.cndroid.pickimagelib.Intents;
import com.cndroid.pickimagelib.PickupImageBuilder;
import com.zhy.http.okhttp.builder.PostFormBuilder;

import java.io.File;
import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.OnClick;
import cn.com.xuanjiezhimen.R;

/**
 * Created by cc on 17-7-31.
 */
public class AddCommentActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.btn_cancel)
    Button         mBtnCancel;
    @BindView(R.id.btn_confirm)
    Button         mBtnConfirm;
    @BindView(R.id.et_content)
    EditText       mEtContent;
    @BindView(R.id.tv_title)
    TextView  tvTitle;

    @BindView(R.id.gridView)
    ExpandGridView gridView;


    private String resouceId;
    private String type;
    PickedGridImageAdapter adapter;
    String[]                images;
    @Override
    protected int getContentViewId() {
        return R.layout.compose_activity;
    }

    @Override
    protected int getFragmentContentId() {
        return 0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

         resouceId = getIntent().getStringExtra(Constants.RESOURCEID);
         type = getIntent().getStringExtra("type");
        tvTitle.setText("评论");
        adapter = new PickedGridImageAdapter(new WeakReference<Context>(this), this);

        gridView.setAdapter(adapter);
    }

    @OnClick(R.id.btn_cancel)
    void onClickCancel() {
        if (mEtContent.getText().length() > 0) {
            new MaterialDialog.Builder(this).title(R.string.notice)
                    .content("确定退出编辑吗?")
                    .positiveText("确定")
                    .negativeText("取消")
                    .callback(new MaterialDialog.ButtonCallback() {
                        @Override
                        public void onPositive(MaterialDialog dialog) {
                            super.onPositive(dialog);
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
    private String lastComment;

    @OnClick(R.id.btn_confirm)
    void onClickConfirm() {

        String text = mEtContent.getText().toString();
        if (mEtContent.getText().length() == 0 && isImageEmpty()) {
            ToastUtils.showLong(this, "没有内容");
        } else {
            // int length = etContent.getText().toString().getBytes().length;
            if (text.getBytes().length < 30&& isImageEmpty())
                ToastUtils.showLong(this, "输入内容少于10个汉字");
            /*else if (text.contains("●")||text.contains("✨")) {
                ToastUtils.showLong(this, "输入内容存在非法字符");
            }*/ else {
                sendCartonComment( text);
            }
        }

    }
    private static final int MAX_IMAGE_SIZE = 500;
    private void sendCartonComment(  String message) {
        mBtnConfirm.setEnabled(false);
        if (message.equals(lastComment))
            return;
        lastComment = message;
       showLoading();
        final PostFormBuilder builder = BuilderInstance.getInstance()
                .getPostBuilderInstance(StaticField.URL_ADD_COMMENT)
                .addParams("target_id", resouceId)
                .addParams("type", type )
                .addParams("content", message);

        if (!isImageEmpty()) {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... params) {
                    for (final String image : images) {
                        File fileOrigin = new File(image);
                        File compressedImage;
                        if (fileOrigin.length() > MAX_IMAGE_SIZE * 1024) {
                            compressedImage = new Compressor.Builder(AddCommentActivity.this)
                                    .setMaxWidth(720)
                                    .setMaxHeight(1080)
                                    .setQuality(85)
                                    .setCompressFormat(Bitmap.CompressFormat.JPEG)
                                    .build()
                                    .compressToFile(fileOrigin);
                            //                    compressedImage = new File(getCacheDir(), String.valueOf(image.hashCode()) + ".jpg");
                            //                    Bitmap bitmap = BitmapUtils.decodeFile(image, 900, 900);
                            //                    BitmapUtils.saveBitmapToFile(bitmap, compressedImage.getAbsolutePath(), MAX_IMAGE_SIZE);
                        } else {
                            compressedImage = fileOrigin;
                        }
                        Log.d("postMoment", compressedImage.getName() + " size = " + compressedImage.length() / 1024.0 + "kb");

                        builder.addFile(image, compressedImage.getName(), compressedImage);
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    super.onPostExecute(aVoid);
                    sendPost(builder);
                }
            }.execute();

        } else {
            builder.addHeader("Content-Type", "multipart/form-data");
            sendPost(builder);
        }


    }

    private void sendPost(PostFormBuilder builder) {

        builder.build().execute(new BaseCallBack() {

            @Override
            public void onLoadFail() {
                lastComment = null;

                hideLoading();

                mBtnConfirm.setEnabled(true);
                if (TextUtils.isEmpty(getMessage())) {
                    ToastUtils.showLong(getBaseContext(), "评论失败");
                } else {
                    ToastUtils.showLong(getBaseContext(), getMessage());
                }

            }

            @Override
            public void onContentNull() {
                lastComment = null;
                hideLoading();
                ToastUtils.showShort(CartoonApp.getInstance(), R.string.content_null);
            }

            @Override
            public void onLoadSuccess(Object response) {
                ToastUtils.showShort(getBaseContext(), "发送成功");
                hideLoading();
                if (getBaseContext() == null) {
                    return;
                }
                mBtnConfirm.setEnabled(true);

                Intent intent = new Intent();
                setResult(2,intent);
                //type = Constants.APPROVE_BANGAI;
                finish();


                // Utils.closeSoftKeyboard(etInput, BangaiDetailActivity.this);

                ToastUtils.showShort(getBaseContext(), "发送成功");

            }

            @Override
            public Object parseNetworkResponse(String response) throws Exception {
                return JSON.parseObject(response, Object.class);
            }
        });
    }

    private boolean isImageEmpty() {
        return images == null || images.length == 0;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Intents.ImagePicker.REQUEST_CODE_PICKUP && resultCode == RESULT_OK && data != null) {
            images = data.getStringArrayExtra(Intents.ImagePicker.RESULT_ITEMS);
            if (null != images && images.length > 0) {
                adapter.clear();

                for (int i = 0; i <images.length ; i++) {
                    adapter.addItem(images[i]);
                }


               /* for (String image : images) {

                    adapter.addItem(image);
                }*/
                adapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArray("images", images);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (null != savedInstanceState) {
            images = savedInstanceState.getStringArray("images");
            if (null != images && images.length > 0) {
                for (String image : images) {
                    adapter.addItem(image);
                }
                adapter.notifyDataSetChanged();
            }
        }

    }

    @Override
    public void onClick(View v) {
        int positionPhoto = (int) v.getTag(R.id.position_photo);

        if (positionPhoto == adapter.getCount() - 1 && positionPhoto != 9) {

            PickupImageBuilder.with(this)
                    .setTitle("添加图片")
                    .selectedImages(images)
                    .showCamera(true)
                    .setMaxChosenLimit(9)
                    .startPickupImage(new ImageLoader());
        } else {

            startActivity(new Intent(this, PreviewPhotoActivity.class)
                    .putExtra(PreviewPhotoActivity.PHOTO_URLS, images)
                    .putExtra(PreviewPhotoActivity.POSITION, positionPhoto));
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

    private MaterialDialog dialog;

    public void showLoading() {
        if (dialog == null)
            dialog = new MaterialDialog.Builder(this)
                    .title(R.string.notice)
                    .content(R.string.please_wait)
                    .progress(true, 0)
                    .build();

        dialog.show();
    }
    public void hideLoading() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            if (isDestroyed())
                return;
        }
        if (dialog != null && dialog.isShowing())
            dialog.dismiss();
    }

}
