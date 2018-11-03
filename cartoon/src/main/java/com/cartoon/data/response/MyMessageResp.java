package com.cartoon.data.response;

import com.cartoon.data.MyMessage;
import com.cartoon.http.BaseResponse;

import java.util.List;

/**
 * Created by jinbangzhu on 7/22/16.
 */

public class MyMessageResp extends BaseResponse {
    public List<MyMessage> getList() {
        return list;
    }

    public void setList(List<MyMessage> list) {
        this.list = list;
    }

    private List<MyMessage> list;
}
