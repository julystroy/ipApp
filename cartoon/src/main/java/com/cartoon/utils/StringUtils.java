package com.cartoon.utils;

import java.util.Calendar;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by David on 16/7/23.
 */
public class StringUtils {
    private static final int HOUR = 60 * 60 * 1000;
    private static final int MIN  = 60 * 1000;
    private static final int SEC  = 1000;

    /**
     * 获取进度条对应的时长
     */
    public static String getCurDuration(int position, int total) {
        int value = total * position / 100;
        return secToTime(value);
    }


    /**
     * 将时间戳转换为 01:01:01 或 01:01 的格式
     */
    public static String formatDuration(int duartion) {
        // 计算小时数
        int hour = duartion / HOUR;

        // 计算分钟数
        int min = duartion % HOUR / MIN;

        // 计算秒数
        int sec = duartion % MIN / SEC;

        // 生成格式化字符串
        if (hour == 0) {
            // 不足一小时 01：01
            return String.format("%02d:%02d", min, sec);
        } else {
            // 大于一小时 01:01:01
            return String.format("%02d:%02d:%02d", hour, min, sec);
        }
    }

    /**
     * 将秒数转换成 时、分、秒
     */
    public static String secToTime(int time) {
        String timeStr = null;
        int hour = 0;
        int minute = 0;
        int second = 0;
        if (time <= 0)
            return "00:00";
        else {
            minute = time / 60;
            if (minute < 60) {
                second = time % 60;
                timeStr = unitFormat(minute) + "′" + unitFormat(second) + "″";
            } else {
                hour = minute / 60;
                if (hour > 99)
                    return "99:59:59";
                minute = minute % 60;
                second = time - hour * 3600 - minute * 60;
                timeStr = unitFormat(hour) + ":" + unitFormat(minute) + "′" + unitFormat(second) + "″";
            }
        }
        return timeStr;
    }

    public static String unitFormat(int i) {
        String retStr = null;
        if (i >= 0 && i < 10)
            retStr = "0" + Integer.toString(i);
        else
            retStr = "" + i;
        return retStr;
    }

    /**
     * 截取文件名
     */
    public static String subFileName(String url) {
        return url.substring(url.lastIndexOf("/") + 1, url.length());
    }

    /**
     * 处理http地址
     */
    public static String subHttpUrl(String url) {
        return url.contains("http://") ? url : "http://" + url;
    }

    public static int getSecFromTime(String duration) {
        int res = 0;
        String[] split = duration.split(":");
        for (int i = split.length - 1; i >= 0; i--) {
            Integer num = Integer.valueOf(split[i]);
            res += num * Math.pow(60, split.length - i - 1);
        }
        return res;
    }

    //获取字符串中的数字
    public static String getNum( String str) {
        /* String str2=null;
        if (TextUtils.isEmpty(str)) {
            for (int i = 0; i < str.length(); i++) {
                if (str.charAt(i) >= 48 && str.charAt(i) <= 57) {//匹配数字
                    str2 += str.charAt(i);
                }
            }
        }*/
        Pattern p = Pattern.compile("[0-9]");
        Matcher m = p.matcher(str);
        String group=null;
        while (m.find()) {
             group = m.group();
        }
        return group;
    }


    //获取时间/一年的第多少天
    public static String getNowSystemTime(long s){
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
        calendar.setTimeInMillis(s);
        String date = String.valueOf(calendar.get(Calendar.YEAR)) + "/" + String.valueOf(calendar.get(Calendar.DAY_OF_YEAR));
        return date;
    }

     /*
    *判断字符串全为汉字
    */

    public static boolean isAllChinese(String text) {
        String reg = "[\\u4e00-\\u9fa5]+";
        return  text.matches(reg);
    }

}
