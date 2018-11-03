package com.cartoon.data.response;

import com.cartoon.data.DayQuestionAnswer;

/**
 * Created by cc on 17-1-24.
 */
public class RewardResponse {


    public final int getStone;
    public final int getPoint;

    public RewardResponse(DayQuestionAnswer response){
        this.getStone =response.getWinBonusStone();
        this.getPoint =response.getWinBonusPoint();
    }
}
