package com.cartoon.data.response;

import com.cartoon.data.BookFriendMoment;
import com.cartoon.http.BaseResponse;

import java.util.List;

/**
 * Created by jinbangzhu on 7/20/16.
 */

public class BookFriendMomentListResp extends BaseResponse {
    public List<BookFriendMoment> getList() {
        return list;
    }

    public void setList(List<BookFriendMoment> list) {
        this.list = list;
    }

    private List<BookFriendMoment> list;
}
