package com.cartoon.data.response;

import com.cartoon.http.BaseResponse;
import com.cartoon.data.CardItem;

import java.io.Serializable;
import java.util.List;

/**
 *  嘻说数据列表
 * <p>
 * Created by David on 16/6/21.
 */
public class QuestionListResp extends BaseResponse implements Serializable{

    private List<CardItem> list;

    private String msg;

    public List<CardItem> getList() {
        return list;
    }

    public void setList(List<CardItem> list) {
        this.list = list;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "QuestionListResp{" +
                "list=" + list +
                ", msg='" + msg + '\'' +
                '}';
    }
}
