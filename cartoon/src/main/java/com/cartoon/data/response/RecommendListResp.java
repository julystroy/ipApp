package com.cartoon.data.response;

import com.cartoon.data.Recommend;
import com.cartoon.http.BaseResponse;

import java.util.List;

/**
 *
 * <p>
 * Created by David on 16/6/11.
 */
public class RecommendListResp extends BaseResponse {

    private List<Recommend> list;

    public List<Recommend> getList() {
        return list;
    }

    public void setList(List<Recommend> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return super.toString() +
                "RecommendListResp{" +
                "list=" + list +
                '}';
    }
}
