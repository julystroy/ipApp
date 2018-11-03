package com.cartoon.data.response;

import com.cartoon.data.Markets;
import com.cartoon.http.BaseResponse;

import java.util.List;

/**
 *  Markets数据列表
 * <p>
 *
 */
public class MarketsListResp extends BaseResponse {

    private List<Markets> list;
    private String stone;

    public String getStone(){ return stone;}

    public void setStone(String stone){this.stone=stone;}

    public List<Markets> getList() {
        return list;
    }

    public void setList(List<Markets> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "MarketsListResp{" +
                "list=" + list +
                ", stone='" + stone + '\'' +
                '}';
    }
}
