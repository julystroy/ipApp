package com.cartoon.data;

/**
 * Created by cc on 18-1-18.
 */
public class FightQ {
    //  "isGrant": 1 是否可以发起攻击令（1.可以0.不可以）
    //"exp": 0, 所得经验
  //  "stone": 0, 所得灵石
   // "totalAnswerRight": 0 本次答对数量
    /* "surplusDefenses": 0, 剩余攻击力
        "isCanAttack": 0, 是否可以攻击
        "userRightNum": 0, 当前用户宗门战答对题目数量
        "remainingTimes": 0, 剩余时间，用于倒计时器
        "totalRightNum": 0, 宗门成员所有答对题目数量
        "totalDefenses": 0 总防御力
        sectWarId*/
    private int isGrant;
    private int surplusDefenses;
    private int isCanAttack;
    private int userRightNum;
    private int remainingTimes;
    private int totalRightNum;
    private int totalDefenses;



    private int exp;
    private int totalAnswerRight;
    private int stone;
    private int sectWarId;

    public int getIsGrant() {
        return isGrant;
    }

    public void setIsGrant(int isGrant) {
        this.isGrant = isGrant;
    }

    public int getSurplusDefenses() {
        return surplusDefenses;
    }

    public void setSurplusDefenses(int surplusDefenses) {
        this.surplusDefenses = surplusDefenses;
    }

    public int getIsCanAttack() {
        return isCanAttack;
    }

    public void setIsCanAttack(int isCanAttack) {
        this.isCanAttack = isCanAttack;
    }

    public int getUserRightNum() {
        return userRightNum;
    }

    public void setUserRightNum(int userRightNum) {
        this.userRightNum = userRightNum;
    }

    public int getRemainingTimes() {
        return remainingTimes;
    }

    public void setRemainingTimes(int remainingTimes) {
        this.remainingTimes = remainingTimes;
    }

    public int getTotalRightNum() {
        return totalRightNum;
    }

    public void setTotalRightNum(int totalRightNum) {
        this.totalRightNum = totalRightNum;
    }

    public int getTotalDefenses() {
        return totalDefenses;
    }

    public void setTotalDefenses(int totalDefenses) {
        this.totalDefenses = totalDefenses;
    }

    public int getExp() {
        return exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

    public int getTotalAnswerRight() {
        return totalAnswerRight;
    }

    public void setTotalAnswerRight(int totalAnswerRight) {
        this.totalAnswerRight = totalAnswerRight;
    }

    public int getStone() {
        return stone;
    }

    public void setStone(int stone) {
        this.stone = stone;
    }

    public int getSectWarId() {
        return sectWarId;
    }

    public void setSectWarId(int sectWarId) {
        this.sectWarId = sectWarId;
    }

    @Override
    public String toString() {
        return "FightQ{" +
                "isGrant=" + isGrant +
                ", surplusDefenses=" + surplusDefenses +
                ", isCanAttack=" + isCanAttack +
                ", userRightNum=" + userRightNum +
                ", remainingTimes=" + remainingTimes +
                ", totalRightNum=" + totalRightNum +
                ", totalDefenses=" + totalDefenses +
                ", exp=" + exp +
                ", totalAnswerRight=" + totalAnswerRight +
                ", stone=" + stone +
                ", sectWarId=" + sectWarId +
                '}';
    }
}
