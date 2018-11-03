package com.cartoon.module.feedback;

import com.cartoon.http.BaseCallBack;
import com.cartoon.http.BuilderInstance;
import com.cartoon.http.StaticField;

/**
 * Created by jinbangzhu on 7/22/16.
 */

public class FeedbackPresenterImpl implements FeedbackPresenter {
    private FeedbackView view;

    public FeedbackPresenterImpl(FeedbackView view) {
        this.view = view;
    }

    @Override
    public void doFeedback(String message) {
        view.showLoading();

        BuilderInstance.getInstance().getGetBuilderInstance(StaticField.URL_APP_FEEDBACK)
                .addParams("content", message)
                .build().execute(new BaseCallBack<String>() {

            @Override
            public void onLoadFail() {
                view.hideLoading();
                view.showErrorMessage(getMessage());
            }
            @Override
            public void onContentNull() {

            }
            @Override
            public void onLoadSuccess(String response) {
                view.hideLoading();
                view.closeNow();
                view.showErrorMessage("提交成功");
            }

            @Override
            public String parseNetworkResponse(String response) throws Exception {
                return response;
            }
        });
    }
}
