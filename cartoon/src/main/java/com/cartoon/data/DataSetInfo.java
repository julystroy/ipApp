package com.cartoon.data;

import java.io.Serializable;

/**
 * Created by HeTao on 17-9-22 下午5:58
 */



public class DataSetInfo implements Serializable {

    private String dataSetName;
    private long   id;
   private  String name;
    private String url;



    public String getDataSetName() {
        return dataSetName;
    }

    public void setDataSetName(String dataSetName) {
        this.dataSetName = dataSetName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


}
