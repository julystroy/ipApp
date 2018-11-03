package com.cartoon.data;

/**
 * Created by cc on 17-11-23.
 */
public class AnswersBean {
    /**
     * answerId : 1111
     * optionContent : 5555   optionContent
     */

    private String answerId;
    private String optionContent;

    public String getAnswerId() {
        return answerId;
    }

    public void setAnswerId(String answerId) {
        this.answerId = answerId;
    }

    public String getOptionContent() {
        return optionContent;
    }

    public void setOptionContent(String optionContent) {
        this.optionContent = optionContent;
    }

    @Override
    public String toString() {
        return "AnswersBean{" +
                "answerId='" + answerId + '\'' +
                ", optionContent='" + optionContent + '\'' +
                '}';
    }
}
