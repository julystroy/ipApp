package com.cartoon.module.mymessage;

import com.cartoon.data.response.MyMessageResp;
import com.cartoon.module.IBaseView;

/**
 * Created by jinbangzhu on 7/22/16.
 */

public interface MyMessageView extends IBaseView {
    void renderList(MyMessageResp resp);

    void renderListMore(MyMessageResp resp);

    void showDeleteWaitDialog();

    void hideDeleteWaitDialog();

    void successDeleteMessage(int position);

}
