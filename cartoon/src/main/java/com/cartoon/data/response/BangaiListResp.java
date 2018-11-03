package com.cartoon.data.response;

import com.cartoon.bean.Bangai;
import com.cartoon.http.BaseResponse;

import java.util.List;

/**
 *  嘻说数据列表
 * <p>
 * Created by David on 16/6/21.
 */
public class BangaiListResp extends BaseResponse {

    private List<Bangai> list;

    public List<Bangai> getList() {
        return list;
    }

    public void setList(List<Bangai> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "BangaiListResp{" +
                "list=" + list +
                '}';
    }
}
