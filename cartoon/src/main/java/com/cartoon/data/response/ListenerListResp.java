package com.cartoon.data.response;

import com.cartoon.bean.Listener;
import com.cartoon.http.BaseResponse;

import java.util.List;

/**
 * 听书列表 嘻说实体
 * <p/>
 * Created by David on 16/7/2.
 */
public class ListenerListResp extends BaseResponse {

    private int limit_num;//传入次参数，为分页显示

    @Override
    public int getTotalRow() {
        return totalRow;
    }

    @Override
    public void setTotalRow(int totalRow) {
        this.totalRow = totalRow;
    }

    private int totalRow;
    private List<Listener> list;

    public int getLimit_num() {
        return limit_num;
    }

    public void setLimit_num(int limit_num) {
        this.limit_num = limit_num;
    }

    public List<Listener> getList() {
        return list;
    }

    public void setList(List<Listener> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "ListenerListResp{" +
                "limit_num=" + limit_num +
                ", list=" + list +
                '}';
    }
}
