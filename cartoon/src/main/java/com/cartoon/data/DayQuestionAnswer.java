package com.cartoon.data;

/**
 * Created by cc on 17-1-20.
 */
public class DayQuestionAnswer {

    /**
     * msg : 很遗憾答错了！+3经验
     * answer : 错误
     * winBonusPoint : 13
     * winBonusStone : 1
     * newStone : 142
     * "answer":null,"winBonusPoint":15,"code":200,"msg":null,"isRight":1(正确),"winBonusStone":2,"rightNumbers":1
     */

    private String msg;
    private String answer;
    private int    winBonusPoint;
    private int    winBonusStone;
    private int    newStone;
    private int    isRight;
    private int    rightNumbers;
    private int    code;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public int getWinBonusPoint() {
        return winBonusPoint;
    }

    public void setWinBonusPoint(int winBonusPoint) {
        this.winBonusPoint = winBonusPoint;
    }

    public int getWinBonusStone() {
        return winBonusStone;
    }

    public void setWinBonusStone(int winBonusStone) {
        this.winBonusStone = winBonusStone;
    }

    public int getNewStone() {
        return newStone;
    }

    public void setNewStone(int newStone) {
        this.newStone = newStone;
    }

    public int getIsRight() {
        return isRight;
    }

    public void setIsRight(int isRight) {
        this.isRight = isRight;
    }

    public int getRightNumbers() {
        return rightNumbers;
    }

    public void setRightNumbers(int rightNumbers) {
        this.rightNumbers = rightNumbers;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public DayQuestionAnswer() {
    }

    @Override
    public String toString() {
        return "DayQuestionAnswer{" +
                "msg='" + msg + '\'' +
                ", answer='" + answer + '\'' +
                ", winBonusPoint=" + winBonusPoint +
                ", winBonusStone=" + winBonusStone +
                ", newStone=" + newStone +
                ", isRight=" + isRight +
                ", rightNumbers=" + rightNumbers +
                ", code=" + code +
                '}';
    }
}
