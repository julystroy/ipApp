package com.cartoon.module.mymessage;

/**
 * Created by jinbangzhu on 7/22/16.
 */

public interface MyMessagePresenter {
    void loadMyMessage(int page, int pageSize);

    void deleteMyMessage(int messageId, int position);
}
