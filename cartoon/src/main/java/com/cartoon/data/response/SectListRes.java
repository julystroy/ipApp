package com.cartoon.data.response;

import com.cartoon.data.SectList;
import com.cartoon.http.BaseResponse;

import java.util.List;

/**
 * Created by cc on 17-10-16.
 */
public class SectListRes extends BaseResponse {
    private List<SectList> list;

    public List<SectList> getList() {
        return list;
    }

    public void setList(List<SectList> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "SectListRes{" +
                "list=" + list +
                '}';
    }
}
