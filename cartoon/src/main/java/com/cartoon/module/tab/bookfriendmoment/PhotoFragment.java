package com.cartoon.module.tab.bookfriendmoment;

import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.cartoon.module.BaseFragment;
import com.cartoon.utils.BitmapUtils;
import com.cartoon.utils.FilePathUtils;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.com.xuanjiezhimen.R;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by jinbangzhu on 7/23/16.
 */

public class PhotoFragment extends BaseFragment {
    @BindView(R.id.iv_preview)
    PhotoView ivPreview;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    public static PhotoFragment getNewInstance(String url) {
        PhotoFragment fragment = new PhotoFragment();
        Bundle bundle = new Bundle();
        bundle.putString("url", url);
        fragment.setArguments(bundle);

        return fragment;
    }

    public static Bitmap getViewBitmap(View view) {
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache(true);

        return view.getDrawingCache();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.photo_item;
    }

    @Override
    protected void initView(final View view, Bundle savedInstanceState) {
        final String url = getArguments().getString("url");
        Glide.with(getActivity())
                .load(url)
                .crossFade()
                .error(R.mipmap.ic_launcher)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        if (null == progressBar) return false;
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        if (null == progressBar) return false;
                        progressBar.setVisibility(View.GONE);
                        ivPreview.setScaleType(ImageView.ScaleType.FIT_CENTER);
                        return false;
                    }
                })
                .into(ivPreview);


        ivPreview.setOnViewTapListener(new PhotoViewAttacher.OnViewTapListener() {
            @Override
            public void onViewTap(View view, float x, float y) {
                getActivity().finish();
            }
        });
        ivPreview.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (progressBar.getVisibility() == View.VISIBLE) return false;


                final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext());

                View viewBottomSheet = LayoutInflater.from(getContext()).inflate(R.layout.save_to_local, null);
                Button btnSave = (Button) viewBottomSheet.findViewById(R.id.btn_save);
                Button btnCancel = (Button) viewBottomSheet.findViewById(R.id.btn_cancel);
                btnSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bottomSheetDialog.dismiss();
                        saveImageToLocal();
                    }

                    private void saveImageToLocal() {
                        /**
                         * 使用截图的方式来保存图片
                         */
                        ivPreview.destroyDrawingCache();
                        ivPreview.buildDrawingCache();

                        final String savePath = FilePathUtils.getCartoonImagePath();
                        File file = new File(savePath);
                        if (!file.exists()) {
                            file.mkdirs();
                        }
                        final String imageFilePath = savePath + System.currentTimeMillis() + ".jpg";

                        new AsyncTask<Void, Void, Boolean>() {
                            @Override
                            protected Boolean doInBackground(Void... params) {
                                final Bitmap bitmap = getViewBitmap(ivPreview);
                                if (bitmap != null) {
                                    BitmapUtils.saveBitmapToFile(bitmap, imageFilePath, 4000);
                                    return true;
                                } else {
                                    return false;
                                }
                            }

                            @Override
                            protected void onPostExecute(Boolean bool) {
                                super.onPostExecute(bool);
                                if (getActivity() != null) {
                                    File imageFile = new File(imageFilePath);
                                    if (imageFile.exists()) {
                                        Toast.makeText(getActivity(), "保存成功", Toast.LENGTH_LONG).show();
                                        // Tell the media scanner about the new file so that it is
                                        // immediately available to the user.
                                        try {
                                            MediaScannerConnection.scanFile(getActivity(),
                                                    new String[]{imageFilePath}, null, new MediaScannerConnection.OnScanCompletedListener() {
                                                        @Override
                                                        public void onScanCompleted(String path, Uri uri) {

                                                        }
                                                    });
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    } else {
                                        Toast.makeText(getActivity(), "保存失败", Toast.LENGTH_LONG).show();
                                    }

                                }

                            }
                        }.execute();
                    }
                });
                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bottomSheetDialog.dismiss();
                    }
                });

                bottomSheetDialog.setContentView(viewBottomSheet);
                bottomSheetDialog.show();
                return false;
            }
        });


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }
}
