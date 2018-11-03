package com.cartoon.data.response;

import com.cartoon.data.FightData;
import com.cartoon.http.BaseResponse;

import java.util.List;

/**
 * Created by cc on 18-1-16. */
public class FightList extends BaseResponse {
    private List<FightData> list;

    public List<FightData> getList() {
        return list;
    }

    public void setList(List<FightData> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "FightList{" +
                "list=" + list +
                '}';
    }
}
