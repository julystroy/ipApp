package com.cartoon.data;

/**
 * Created by cc on 17-11-24.
 */
public class SelectAnswer {

    /**
     * isDisable : 0
     * answerId : null
     * bonusPoint : null
     * stone : null
     */

    private int isDisable;
    private String answerId;


    public int getIsDisable() {
        return isDisable;
    }

    public void setIsDisable(int isDisable) {
        this.isDisable = isDisable;
    }

    public String getAnswerId() {
        return answerId;
    }

    public void setAnswerId(String answerId) {
        this.answerId = answerId;
    }

    @Override
    public String toString() {
        return "SelectAnswer{" +
                "isDisable=" + isDisable +
                ", answerId='" + answerId + '\'' +
                '}';
    }
}
