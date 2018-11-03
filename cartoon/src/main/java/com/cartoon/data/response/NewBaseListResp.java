package com.cartoon.data.response;

import com.cartoon.data.NewBase;
import com.cartoon.http.BaseResponse;

import java.util.List;

/**
 *  新模块通用数据列表
 * <p>
 * Created by DebuggerX on 16/11/11.
 */
public class NewBaseListResp extends BaseResponse {

    private List<NewBase> list;

    public List<NewBase> getList() {
        return list;
    }

    public void setList(List<NewBase> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "NewBaseListResp{" +
                "list=" + list +
                '}';
    }
}
