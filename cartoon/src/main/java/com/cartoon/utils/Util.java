package com.cartoon.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;

import com.cartton.library.utils.DebugLog;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Util {
    public static final int MS_FACTOR = 1000;// 服务器时间10位

    /**
     * Hidden constructor.
     */
    private Util() {
    }

    public static final int SDK_INT =
            (Build.VERSION.SDK_INT == 23 && Build.VERSION.CODENAME.charAt(0) == 'N') ? 24
                    : Build.VERSION.SDK_INT;

    public static String getOSVersion() {
        return Build.VERSION.RELEASE;
    }
    public static String getOSModel() {
        return Build.MODEL;
    }

    public static String getOSFingerprint() {
        return Build.FINGERPRINT;
    }

    public static String getAppVersion(Context context) {
        String appVersion = "";
        try {
            appVersion = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
        }
        return appVersion;
    }

    public static int getAppVersionCode(Context context) {
        int appVersionCode = 0;
        try {
            appVersionCode = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
        }
        return appVersionCode;
    }

    public static String getIP(String host) {
        InetAddress address = null;
        try {
            address = InetAddress.getByName(host);
            return address.getHostAddress().toString();
        } catch (UnknownHostException e) {
        }
        return null;
    }

    public static boolean isIP(String host) {
        if (!TextUtils.isEmpty(host)) {
            Pattern p = Pattern.compile("^((25[0-5]|2[0-4]\\d|[01]?\\d\\d?)($|(?!\\.$)\\.)){4}$");
            Matcher m = p.matcher(host);
            if (m.matches()) {
                return true;
            }
        }
        return false;
    }

    public static String getLocalIp() {
        String strIpAddress = "";
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {// && InetAddressUtils.isIPv4Address(inetAddress.getHostAddress())
                        strIpAddress = inetAddress.getHostAddress().toString();
                        break;
                    }
                }
            }
        } catch (Exception e) {
            DebugLog.e("getLocalIp failed. e = " + e.getMessage());
        }
        return strIpAddress;
    }

    public static String getDeviceId(Context context) {
        try {
            return ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
        } catch (Exception E) {
            return "00000000";
        }
    }

    public static String getDateString() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        return sdf.format(new Date());
    }

    public static String getDateTime(String pattern, long times) {
        DateFormat format = new SimpleDateFormat(pattern, Locale.US);
        String dateTime = format.format(new Date(times * MS_FACTOR));

        return dateTime;
    }

    public static String friendly_time(long times) {
        final long minute = 60 * 1000;// 1分钟
        final long hour = 60 * minute;// 1小时
        final long day = 24 * hour;// 1天
        final long month = 31 * day;// 月
        final long year = 12 * month;// 年

        String showTime = "未知";

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date d = new Date(times * MS_FACTOR);

        long diff = new Date().getTime() - d.getTime();
        long r = 0;
        if (diff > year) {
            showTime = Util.getDateTime("yyyy.MM.dd", times);
        } else if (diff > month) {
            r = (diff / month);
            showTime = r + "个月前";
        } else if (diff > day) {
            r = (diff / day);
            showTime = r + "天前";
        } else if (diff > hour) {
            r = (diff / hour);
            showTime = r + "个小时前";
        } else if (diff > minute) {
            r = (diff / minute);
            showTime = r + "分钟前";
        } else
            showTime = "刚刚";
        return showTime;
    }

    public static String remainingTime(long times) {
        final long minute = 60 * 1000;// 1分钟
        final long hour = 60 * minute;// 1小时
        final long day = 24 * hour;// 1天
        final long month = 31 * day;// 月
        final long year = 12 * month;// 年


        StringBuilder sb = new StringBuilder();
        String showTime = "未知";

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date d = new Date(times * MS_FACTOR);

        long diff = new Date().getTime() - d.getTime();
        long r = 0;
        if (diff > year) {
            showTime = r + "年";
        }

        if (diff > month) {
            r = (diff / month);
            showTime = r + "个月前";
        }
        if (diff > day) {
            r = (diff / day);
            showTime = r + "天前";
        }
        if (diff > hour) {
            r = (diff / hour);
            showTime = r + "个小时前";
        }
        if (diff > minute) {
            r = (diff / minute);
            showTime = r + "分钟前";
        }
        return showTime;
    }

    public static final String ObjToJson(Object obj) {
        Gson gson = new Gson();

        return gson.toJson(obj);
    }

    // Cmd cmd = Util.loadObject(msg, Cmd.class);
    public static <T> T loadObject(String json, Class<T> klass) {
        T resp = null;
        Gson mGson = new Gson();
        try {
            resp = mGson.fromJson(json, klass);
        } catch (JsonSyntaxException e) {
        }

        return resp;
    }

    public static final String md5(String str) {
        String md5 = null;

        if (!TextUtils.isEmpty(str)) {
            try {
                MessageDigest digest = MessageDigest.getInstance("MD5");
                digest.update(str.getBytes("utf-8"));

                BigInteger bigInt = new BigInteger(1, digest.digest());
                md5 = bigInt.toString(16);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        return md5;
    }

    public static void startActivity(Activity activity, Class<?> cls, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(activity, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        activity.startActivity(intent);
//        activity.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

    public static void startActivityForResult(Activity activity, Class<?> cls, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(activity, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        activity.startActivityForResult(intent, 0);
//        activity.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

    public static void startInstall(Context context, Uri uri) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

//    public static void startDownloadService(Context context, DownloadInfo downloadInfo) {
//        Intent intent = new Intent(context, ApkDownloadService.class);
//        intent.putExtra(Constant.IntentKey.DOWNLOAD_INFO, downloadInfo);
//        context.startService(intent);
//    }

    public static String timeFormatted(int totalSeconds) {
        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;

        if (hours > 0) {
            return String.format("%02d:%02d:%02d", hours, minutes, seconds);
        }

        return String.format("%02d:%02d", minutes, seconds);
    }

    public static String timeFormattedRemain(int totalSeconds) {
        String timeStr = totalSeconds + "秒";
        if (totalSeconds > 60) {
            long second = totalSeconds % 60;
            long min = totalSeconds / 60;
            timeStr = min + "分" + second + "秒";
            if (min > 60) {
                min = (totalSeconds / 60) % 60;
                long hour = (totalSeconds / 60) / 60;
                timeStr = hour + "小时" + min + "分" + second + "秒";
                if (hour > 24) {
                    hour = ((totalSeconds / 60) / 60) % 24;
                    long day = (((totalSeconds / 60) / 60) / 24);
                    timeStr = day + "天" + hour + "小时" + min + "分" + second + "秒";
                }
            }
        }
        return timeStr;
    }

    private static final int BASE_B = 1; // 转换为字节基数
    private static final int BASE_KB = 1024; // 转换为KB
    private static final int BASE_MB = 1024 * 1024; // 转换为M的基数
    private static final int BASE_GB = 1024 * 1024 * 1024;

    private static final String UNIT_BIT = "KB";
    private static final String UNIT_KB = "KB";
    private static final String UNIT_MB = "MB";
    private static final String UNIT_GB = "GB";

    public static String convertFileSize(long file_size, int precision) {
        long int_part = 0;
        double fileSize = file_size;
        double floatSize = 0L;
        long temp = file_size;
        int i = 0;
        int base = 1;
        String baseUnit = "M";
        String fileSizeStr = null;
        int indexMid = 0;

        while (temp / 1024 > 0) {
            int_part = temp / 1024;
            temp = int_part;
            i++;
        }
        switch (i) {
            case 0:
                // B
                base = BASE_B;
                baseUnit = UNIT_BIT;
                break;

            case 1:
                // KB
                base = BASE_KB;
                baseUnit = UNIT_KB;
                break;

            case 2:
                // MB
                base = BASE_MB;
                baseUnit = UNIT_MB;
                break;

            case 3:
                // GB
                base = BASE_GB;
                baseUnit = UNIT_GB;
                break;

            case 4:
                // TB
                break;
            default:
                break;
        }
        floatSize = fileSize / base;
        fileSizeStr = Double.toString(floatSize);
        if (precision == 0) {
            indexMid = fileSizeStr.indexOf('.');
            if (-1 == indexMid) {
                // 字符串中没有这样的字符
                return fileSizeStr + baseUnit;
            }
            return fileSizeStr.substring(0, indexMid) + baseUnit;
        }
        indexMid = fileSizeStr.indexOf('.');
        if (-1 == indexMid) {
            // 字符串中没有这样的字符
            return fileSizeStr + baseUnit;
        }

        if (fileSizeStr.length() <= indexMid + precision + 1) {
            return fileSizeStr + baseUnit;
        }
        if (indexMid < 3) {
            indexMid += 1;
        }
        if (indexMid + precision < 6) {
            indexMid = indexMid + precision;
        }
        return fileSizeStr.substring(0, indexMid) + baseUnit;
    }

    public static String getPercent(int current, int total) {
        NumberFormat numberFormat = NumberFormat.getInstance();
        // 设置精确到小数点后0位
        numberFormat.setMaximumFractionDigits(0);
        return numberFormat.format((float) current / (float) total * 100) + "%";
    }

    public static String getProcessName(Context context, int pid) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningApps = am.getRunningAppProcesses();
        if (runningApps == null) {
            return null;
        }
        for (ActivityManager.RunningAppProcessInfo procInfo : runningApps) {
            if (procInfo.pid == pid) {
                return procInfo.processName;
            }
        }
        return null;
    }

    public static List<String> getInstalledPackages(Context context) {
        List<String> pkgList = new ArrayList<>();
        List<PackageInfo> packages = context.getPackageManager().getInstalledPackages(0);
        for (int i = 0; i < packages.size(); i++) {
            PackageInfo pkgInfo = packages.get(i);
            if ((pkgInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                pkgList.add(pkgInfo.packageName);
            }
        }
        return pkgList;
    }

    public static boolean isInstalled(Context context, String pkgName) {
        List<String> pkgList = getInstalledPackages(context);
        return pkgList.contains(pkgName);
    }

    public static SpannableString getPriceString(String price, int smallTextSize, int bigTextSize) {
        SpannableString sp = new SpannableString(price.contains("¥") ? price : "¥" + price);
        sp.setSpan(new AbsoluteSizeSpan(smallTextSize), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        sp.setSpan(new AbsoluteSizeSpan(bigTextSize), 1, sp.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        return sp;
    }

    /**
     * 得到手机data目录下的图片
     *
     * @param context
     * @param fileName
     * @return
     */
    public static Bitmap getBmpFromFile(Context context, String fileName) {
        FileInputStream imgInputStream = null;
        try {
            imgInputStream = context.openFileInput(fileName);
            return BitmapFactory.decodeStream(imgInputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 保存图片到手机data目录
     *
     * @param context
     * @param bmp
     * @param fileName
     * @return
     */
    public static void saveBmpToFile(Context context, Bitmap bmp, String fileName) {
        if (bmp == null) return;
        try {
            FileOutputStream fileOut = context.openFileOutput(fileName,
                    Context.MODE_PRIVATE);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fileOut);
            fileOut.flush();
            fileOut.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void deleteBmp(Context context, String fileName) {
        File bmpFile = context.getFileStreamPath(fileName);
        if (bmpFile.exists()) {
            bmpFile.delete();
        }
    }

}
