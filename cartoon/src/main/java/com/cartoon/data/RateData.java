package com.cartoon.data;

/**
 * Created by cc on 18-1-18.
 */
public class RateData {
    /* "attackName":"乌漆嘛黑",//攻方
        "msg":"失败",//状态
        "defenseName":"cc",//守方
        "flag":true,
        "percent":0,//答对率
        "isWin":"2"//是否胜利 1=胜利 2=失败 （意外情况：0=还没打完）*/

    private String attackName;
    private String msg;
    private String defenseName;
    private String percent;
    private String isWin;
    private boolean flag;

    public String getAttackName() {
        return attackName;
    }

    public void setAttackName(String attackName) {
        this.attackName = attackName;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getDefenseName() {
        return defenseName;
    }

    public void setDefenseName(String defenseName) {
        this.defenseName = defenseName;
    }

    public String getPercent() {
        return percent;
    }

    public void setPercent(String percent) {
        this.percent = percent;
    }

    public String getIsWin() {
        return isWin;
    }

    public void setIsWin(String isWin) {
        this.isWin = isWin;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    @Override
    public String toString() {
        return "RateData{" +
                "attackName='" + attackName + '\'' +
                ", msg='" + msg + '\'' +
                ", defenseName='" + defenseName + '\'' +
                ", percent='" + percent + '\'' +
                ", isWin='" + isWin + '\'' +
                ", flag=" + flag +
                '}';
    }
}
