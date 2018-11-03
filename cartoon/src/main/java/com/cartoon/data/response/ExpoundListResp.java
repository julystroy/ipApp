package com.cartoon.data.response;

import com.cartoon.data.Expound;
import com.cartoon.http.BaseResponse;

import java.util.List;

/**
 *  嘻说数据列表
 * <p>
 * Created by David on 16/6/21.
 */
public class ExpoundListResp extends BaseResponse {

    private List<Expound> list;

    public List<Expound> getList() {
        return list;
    }

    public void setList(List<Expound> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "ExpoundListResp{" +
                "list=" + list +
                '}';
    }
}
