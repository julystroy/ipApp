package com.cartoon.data.response;

import com.cartoon.http.BaseResponse;

import java.util.List;

/**
 * Created by David on 16/6/27.
 */
public class CartoonCommentListResp extends BaseResponse {

    private List<CommentData> list;

    public List<CommentData> getList() {
        return list;
    }

    public void setList(List<CommentData> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "CartoonCommentListResp{" +
                "list=" + list +
                '}';
    }
}
