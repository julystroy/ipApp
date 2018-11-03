package com.cartoon.data;

/**
 * 漫画内容
 * <p/>
 * Created by David on 16/6/12.
 */
public class ComicsImg {

    /**
     * "id": 1001, // 漫画ID
     * " remote_connect": "xxx.jpg", //图片
     * " sort": "1" //图序
     */
    private String id;
    private String remote_connect;
    private int sort;

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    private int width;
    private int height;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRemote_connect() {
        return remote_connect;
    }

    public void setRemote_connect(String remote_connect) {
        this.remote_connect = remote_connect;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    @Override
    public String toString() {
        return "ComicsImg{" +
                "id='" + id + '\'' +
                ", remote_connect='" + remote_connect + '\'' +
                ", sort=" + sort +
                '}';
    }
}
