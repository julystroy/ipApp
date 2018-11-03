package com.cartoon.module.feedback;

/**
 * Created by jinbangzhu on 7/22/16.
 */

public interface FeedbackView {
    void showLoading();

    void hideLoading();

    void showErrorMessage(String msg);

    void closeNow();
}
