package com.cartoon.data.game;

import java.text.DecimalFormat;

/**
 * Created by wuchuchu on 2018/3/2.
 */

public class Rating {
    public String rating;
    public int count;
    public int count2;
    public int count4;
    public int count6;
    public int count8;
    public int count10;

    public void addRating(int selfRating) {
        count ++;
        switch (selfRating) {
            case 2:
                count2 ++;
                break;
            case 4:
                count4 ++;
                break;
            case 6:
                count6 ++;
                break;
            case 8:
                count8 ++;
                break;
            case 10:
                count10 ++;
                break;

            default:
                count --;
                break;
        }

        float num= (float)(count2 * 2 + count4 * 4 + count6 * 6 + count8 * 8 + count10 * 10) / count;
        DecimalFormat df = new DecimalFormat("0.0");
        rating = df.format(num);
    }
}
