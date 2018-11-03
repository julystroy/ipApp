package com.cartoon.utils;

/**
 * Created by CC on 2016/9/28.
 */
public class BoundsRateNew {


    public static int[] codes = {0, 90, 98, 105, 113, 448, 476, 504, 532, 560, 1050, 1100, 1150,
            2450, 3650, 4850, 6050,
            7900, 9750, 11600, 13450,
            16250, 19050, 21850, 24650,
            28600, 32550, 36500, 40450,
            45750, 51050, 56350, 61650,
            68500, 75350, 82200, 89050, Integer.MAX_VALUE};


    public static String[] ranges = {"一层", "二层", "三层", "四层", "五层", "六层", "七层", "八层", "九层", "十层", "十一层", "十二层", "十三层",
            "初期", "中期", "后期", "大圆满",
            "初期", "中期", "后期", "大圆满",
            "初期", "中期", "后期", "大圆满",
            "初期", "中期", "后期", "大圆满",
            "初期", "中期", "后期", "大圆满",
            "初期", "中期", "后期", "大圆满"};

    public static int SwitchCen(Integer code) {

        int i = 0;
        for (; code > 0; code -= codes[i]) {
            i++;
        }

        return i - 1;
    }

    public static int SwitchUltraStones(Integer bonus) {

        int[] stones = {1, 2, 3, 4, 5, 6, 7, 8, 10, 11, 12, 13,
                45, 54, 63, 72,
                81, 90, 99, 108,
                117, 126, 135, 144,
                153, 162, 171, 180,
                189, 198, 207, 216,
                225, 234, 243, 252};

        int i = 0;
        for (; bonus > 0; bonus -= codes[i]) {
            i++;
        }

        return stones[i - 1];
    }


    //每层剩余积分值
    public static int NextLevelNeedPoint(Integer bonus) {

        int i = 0;
        for (; bonus > 0; bonus -= codes[i]) {
            i++;
        }

        bonus+=codes[i];
        return bonus;

    }

}



