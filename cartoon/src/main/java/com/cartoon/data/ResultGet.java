package com.cartoon.data;

/**
 * Created by cc on 18-1-18.
 */
public class ResultGet {
    /* "exp": 0, 所得经验
        "stone": 0, 所得灵石
        "totalAnswerRight": 0 本次答对数量*/
    private int exp;
    private int stone;
    private int totalAnswerRight;

    public int getExp() {
        return exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

    public int getStone() {
        return stone;
    }

    public void setStone(int stone) {
        this.stone = stone;
    }

    public int getTotalAnswerRight() {
        return totalAnswerRight;
    }

    public void setTotalAnswerRight(int totalAnswerRight) {
        this.totalAnswerRight = totalAnswerRight;
    }

    @Override
    public String toString() {
        return "ResultGet{" +
                "exp=" + exp +
                ", stone=" + stone +
                ", totalAnswerRight=" + totalAnswerRight +
                '}';
    }
}
