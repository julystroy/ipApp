package com.cartoon.module.compose;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.afollestad.materialdialogs.MaterialDialog;
import com.cartoon.http.BaseCallBack;
import com.cartoon.http.BuilderInstance;
import com.cartoon.http.StaticField;
import com.cartoon.module.BaseActivity;
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
 * Created by jinbangzhu on 7/24/16.
 */

public class ComposeActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.btn_cancel)
    Button btnCancel;
    @BindView(R.id.btn_confirm)
    Button btnConfirm;
    @BindView(R.id.et_content)
    EditText etContent;
    @BindView(R.id.gridView)
    ExpandGridView gridView;
    private MaterialDialog dialog;

    PickedGridImageAdapter adapter;
    String[] images;
    String[] imagess;

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
        adapter = new PickedGridImageAdapter(new WeakReference<Context>(this), this);

        gridView.setAdapter(adapter);
    }

    @OnClick(R.id.btn_cancel)
    void onClickCancel() {
        if (!isImageEmpty() || etContent.getText().length() > 0) {
            new MaterialDialog.Builder(this).title(R.string.notice)
                    .content("确定退出编辑吗?")
                    .positiveText("确定")
                    .negativeText("取消")
                    .callback(new MaterialDialog.ButtonCallback() {
                        @Override
                        public void onPositive(MaterialDialog dialog) {
                            super.onPositive(dialog);
                            images = null;
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

    private static final int MAX_IMAGE_SIZE = 500;// kb


    @OnClick(R.id.btn_confirm)
    void onClickConfirm() {


        if (etContent.getText().length() == 0 && isImageEmpty()) {
            ToastUtils.showLong(this, "没有内容");
        } else {
           // int length = etContent.getText().toString().getBytes().length;
            if (etContent.getText().toString().getBytes().length < 30 && isImageEmpty())
                ToastUtils.showLong(this, "输入内容少于10个汉字");
            else
            postMoment();
        }
    }


    private void postMoment() {
        showLoadingView();
        btnConfirm.setEnabled(false);
        boolean isImageEmpty = isImageEmpty();
        final PostFormBuilder postFormBuilder =
                BuilderInstance.getInstance().getPostBuilderInstance(StaticField.URL_APP_ADD_POST)
                        .addParams("content", etContent.getText().toString())
                        .addParams("type", "4"); //4=书友圈

        if (!isImageEmpty) {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... params) {
                    for (final String image : images) {
                        File fileOrigin = new File(image);
                        File compressedImage;
                      if (fileOrigin.length() > MAX_IMAGE_SIZE * 1024) {
                            compressedImage = new Compressor.Builder(ComposeActivity.this)
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
         //   postFormBuilder.addHeader("Content-Type", "application/x-www-form-urlencoded");
            sendPost(postFormBuilder);
        }

    }

    private void sendPost(PostFormBuilder postFormBuilder) {
        postFormBuilder.build().execute(new BaseCallBack<String>() {

            @Override
            public void onLoadFail() {
                btnConfirm.setEnabled(true);
                if (TextUtils.isEmpty(getMessage())) {
                    ToastUtils.showLong(ComposeActivity.this, "操作失败");
                } else {
                    ToastUtils.showLong(ComposeActivity.this, getMessage());
                }
                hideLoadingView();
            }
            @Override
            public void onContentNull() {

            }
            @Override
            public void onLoadSuccess(String response) {
                if (getBaseContext() == null) {
                    return;
                }
                btnConfirm.setEnabled(true);
                hideLoadingView();

                Log.d("onLoadSuccess", "response=" + response);
                ToastUtils.showLong(ComposeActivity.this, "发送成功");
                finish();
            }

            @Override
            public String parseNetworkResponse(String response) throws Exception {
                return response;
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
        if (dialog != null && dialog.isShowing()) {
            try {
                dialog.dismiss();
            } catch (Exception e) {

            }
        }

    }

}
