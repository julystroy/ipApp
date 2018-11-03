package com.cartoon.data.response;

import com.cartoon.data.Charts;

import java.util.List;

/**
 *  Markets数据列表
 * <p>
 *
 */
public class ChartsListResp  {

    private List<Charts> list;


    public List<Charts> getList() {
        return list;
    }

    public void setList(List<Charts> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "ChartsListResp{" +
                "list=" + list +
                '}';
    }
}
