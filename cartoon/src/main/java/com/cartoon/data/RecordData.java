package com.cartoon.data;

/**
 * Created by cc on 17-12-26.
 */
public class RecordData {

    /**
     * sectBp : 0
     * sectCont : 0
     * rightNum : 0
     * bonusPoint : 0
     * stone : 0
     */

    private String sectBp;
    private String sectCont;
    private String rightNum;
    private String bonusPoint;
    private String stone;

    public String getSectBp() {
        return sectBp;
    }

    public void setSectBp(String sectBp) {
        this.sectBp = sectBp;
    }

    public String getSectCont() {
        return sectCont;
    }

    public void setSectCont(String sectCont) {
        this.sectCont = sectCont;
    }

    public String getRightNum() {
        return rightNum;
    }

    public void setRightNum(String rightNum) {
        this.rightNum = rightNum;
    }

    public String getBonusPoint() {
        return bonusPoint;
    }

    public void setBonusPoint(String bonusPoint) {
        this.bonusPoint = bonusPoint;
    }

    public String getStone() {
        return stone;
    }

    public void setStone(String stone) {
        this.stone = stone;
    }

    @Override
    public String toString() {
        return "RecordData{" +
                "sectBp='" + sectBp + '\'' +
                ", sectCont='" + sectCont + '\'' +
                ", rightNum='" + rightNum + '\'' +
                ", bonusPoint='" + bonusPoint + '\'' +
                ", stone='" + stone + '\'' +
                '}';
    }
}
