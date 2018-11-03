
/**********************************************************
 * _              _  _  _  ____          __   _       *
 * / \   _ __   __| || |(_)/ ___|  ___ __/ /__| |_     *
 * / _ \ | '_  \/ _  || || |\___ \ / _ \_   _|_   _|    *
 * / ___ \| | | | (_) || || | ___) | (_) || |   | |_     *
 * /_/   \_|_| |_|\____)|_||_||____/ \___/ /_/   \___|    *
 * *
 * *********************************************************
 * Copyright 2014, AndliSoft.com.                         *
 * All rights, including trade secret rights, reserved.   *
 **********************************************************/

package com.cartton.library.utils;

import android.net.Uri;
import android.text.TextUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

public class MD5Util {

    /***
     * MD5加密 生成32位md5码
     *
     * @param inStr 待加密字符串
     * @return 返回32位md5码
     */
    public static String md5Encode(String inStr) throws Exception {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            //System.out.println(e.toString());
            e.printStackTrace();
            return "";
        }

        byte[] byteArray = inStr.getBytes("UTF-8");
        byte[] md5Bytes = md5.digest(byteArray);
        StringBuffer hexValue = new StringBuffer();
        for (int i = 0; i < md5Bytes.length; i++) {
            int val = ((int) md5Bytes[i]) & 0xff;
            if (val < 16) {
                hexValue.append("0");
            }
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString().toLowerCase(Locale.ENGLISH);
    }

    /***
     * MD5加密 生成32位md5码
     *
     * @param inputFile 文件目录
     * @return 返回32位md5码
     */
    public static String fileMD5(String inputFile) throws IOException {
        // 缓冲区大小（这个可以抽出一个参数）
        int bufferSize = 256 * 1024;
        FileInputStream fileInputStream = null;
        DigestInputStream digestInputStream = null;
        try {
            // 拿到一个MD5转换器（同样，这里可以换成SHA1）
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            // 使用DigestInputStream
            fileInputStream = new FileInputStream(inputFile);
            digestInputStream = new DigestInputStream(fileInputStream, messageDigest);
            // read的过程中进行MD5处理，直到读完文件
            byte[] buffer = new byte[bufferSize];
            while (digestInputStream.read(buffer) > 0) ;
            // 获取最终的MessageDigest
            messageDigest = digestInputStream.getMessageDigest();
            // 拿到结果，也是字节数组，包含16个元素
            byte[] resultByteArray = messageDigest.digest();
            // 同样，把字节数组转换成字符串
            return bytesToHexString(resultByteArray);
        } catch (NoSuchAlgorithmException e) {
            return null;
        } finally {
            try {
                if (digestInputStream != null) {
                    digestInputStream.close();
                }
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /***
     * 用于将字节数组换成成16进制的字符串
     ***/
    public static String bytesToHexString(byte[] src) {
        StringBuilder sb = new StringBuilder("");
        if (null == src || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0XFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                sb.append(0);
            }
            sb.append(hv);
        }
        return sb.toString().toLowerCase(Locale.ENGLISH);
    }

    public static boolean md5Check(String fileUri, String md5) {
        File file = new File(Uri.parse(fileUri).getPath());
        try {
            if (!file.exists()) {
                return false;
            } else {
                String mMd5 = fileMD5(fileUri);
                if (!TextUtils.isEmpty(mMd5) && md5.equalsIgnoreCase(md5)) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }
}
