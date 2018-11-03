package com.cartoon.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.text.TextUtils;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by jinbangzhu on 6/1/16.
 */

public class BitmapUtils {
    /**
     * @param bitmap   bitmap
     * @param savePath local file path
     * @param maxSize  kb
     */
    public static void saveBitmapToFile(Bitmap bitmap, String savePath, int maxSize) {
        if (null == bitmap) throw new NullPointerException("bitmap is null");
        if (TextUtils.isEmpty(savePath)) throw new NullPointerException("savePath is null");


        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int quality = 100;
        while (baos.toByteArray().length / 1024 > maxSize) {    //循环判断如果压缩后图片是否大于 maxSize kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);//这里压缩options%，把压缩后的数据存放到baos中
            quality -= 2;//每次都减少10
        }
//        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
//        bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片

        try {
            FileOutputStream out = null;

            try {
                out = new FileOutputStream(new File(savePath));
                baos.writeTo(out);
//                bitmap.compress(Bitmap.CompressFormat.PNG, quality, out);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    out.flush();
                    out.close();
                    bitmap.recycle();
                } catch (Throwable ignore) {
                    bitmap.recycle();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            bitmap.recycle();
        }
    }

    /**
     * 压缩图片，取到内存，在缩放指定大小，
     *
     * @param file
     * @param width
     * @return
     */
    public static Bitmap decodeFile(String file, final int width, final int height) {
        int outWidth;
        int outHeight;
        float scae = 1;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        // 获取这个图片的宽和高
        Bitmap bitmap = BitmapFactory.decodeFile(file, options); // 此时返回bm为空
        options.inJustDecodeBounds = false;
        // 计算缩放比
        outWidth = options.outWidth;
        outHeight = options.outHeight;
        int be = (int) (options.outHeight > options.outWidth ? options.outHeight / (float) height : options.outWidth / (float) width);
        if (be <= 0)
            be = 1;
        options.inSampleSize = be;

        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inPurgeable = true;
        // 重新读入图片，注意这次要把options.inJustDecodeBounds 设为 false哦
        bitmap = BitmapFactory.decodeFile(file, options);

//        if (outWidth > outHeight && outHeight > height) {
//            scae = height / outHeight;
//        } else if (outWidth > height) {
//            scae = height / outWidth;
//        }
//        bitmap = Bitmap.createScaledBitmap(bitmap, (int) (outWidth * scae), (int) (outHeight * scae), true); // 按指定大小转换

        ExifInterface exif = null;
        try {
            exif = new ExifInterface(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (null != exif) {
            Log.d("EXIF value", exif.getAttribute(ExifInterface.TAG_ORIENTATION));
            if (exif.getAttribute(ExifInterface.TAG_ORIENTATION).equalsIgnoreCase("6")) {
                bitmap = rotate(bitmap, 90);
            } else if (exif.getAttribute(ExifInterface.TAG_ORIENTATION).equalsIgnoreCase("8")) {
                bitmap = rotate(bitmap, 270);
            } else if (exif.getAttribute(ExifInterface.TAG_ORIENTATION).equalsIgnoreCase("3")) {
                bitmap = rotate(bitmap, 180);
            }
        }

        return bitmap;
    }

    private static Bitmap rotate(Bitmap bitmap, int degree) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        Matrix mtx = new Matrix();
        mtx.postRotate(degree);

        return Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, true);
    }
}
