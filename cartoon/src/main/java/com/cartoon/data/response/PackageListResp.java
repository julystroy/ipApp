package com.cartoon.data.response;

import com.cartoon.data.PackageList;
import com.cartoon.http.BaseResponse;

import java.util.List;

/**
 *  Markets数据列表
 * <p>
 *
 */
public class PackageListResp extends BaseResponse {

    private List<PackageList> list;
    private int stone;
    private int itemsNum;

    public int getStone(){ return stone;}

    public void setStone(int stone){this.stone=stone;}

    public int getItemsNum(){ return itemsNum;}

    public void setItemsNum(int itemsNum){this.itemsNum=itemsNum;}

    public List<PackageList> getList() {
        return list;
    }

    public void setList(List<PackageList> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "PackageListResp{" +
                "list=" + list +
                ", stone=" + stone +
                ", itemsNum=" + itemsNum +
                '}';
    }
}
