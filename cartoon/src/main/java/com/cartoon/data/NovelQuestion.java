package com.cartoon.data;

import java.util.List;

/**
 * Created by cc on 17-11-23.
 */
public class NovelQuestion {

    /**
     * questionId : 111111
     * question : 0000
     * stone : 10
     * answers : [{"answerId":"1111","optionContent":"5555"}]
     * answeredId:   已选择答案
     * rightAnswerId：   正确答案
     * isDisable     ：   0  不可点击，1可以点击
     */

    private String questionId;
    private String            question;
    private String            stone;
    private String            bonusPoint;
    private String            answeredId;
    private String            rightAnswerId;
    private int             isDisable;
    private List<AnswersBean> answers;

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getStone() {
        return stone;
    }

    public void setStone(String stone) {
        this.stone = stone;
    }

    public List<AnswersBean> getAnswers() {
        return answers;
    }

    public void setAnswers(List<AnswersBean> answers) {
        this.answers = answers;
    }

    public String getBonusPoint() {
        return bonusPoint;
    }

    public void setBonusPoint(String bonusPoint) {
        this.bonusPoint = bonusPoint;
    }

    public String getAnsweredId() {
        return answeredId;
    }

    public void setAnsweredId(String answeredId) {
        this.answeredId = answeredId;
    }

    public String getRightAnswerId() {
        return rightAnswerId;
    }

    public void setRightAnswerId(String rightAnswerId) {
        this.rightAnswerId = rightAnswerId;
    }

    public int getIsDisable() {
        return isDisable;
    }

    public void setIsDisable(int isDisable) {
        this.isDisable = isDisable;
    }

    @Override
    public String toString() {
        return "NovelQuestion{" +
                "questionId='" + questionId + '\'' +
                ", question='" + question + '\'' +
                ", stone='" + stone + '\'' +
                ", bonusPoint='" + bonusPoint + '\'' +
                ", answeredId='" + answeredId + '\'' +
                ", rightAnswerId='" + rightAnswerId + '\'' +
                ", isDisable=" + isDisable +
                ", answers=" + answers +
                '}';
    }
}
