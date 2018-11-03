package com.cartoon.bean;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
// KEEP INCLUDES END
/**
 * Entity mapped to table "CARTOON_ITEM".
 */
public class CartoonItem {

    private String id;
    private String cartoon_id;
    private Integer sort;
    /**
     * 图片地址
     */
    private String remote_connect;
    /**
     * 本地地址
     */
    private String content_pic;

    // KEEP FIELDS - put your custom fields here
    // KEEP FIELDS END

    public CartoonItem() {
    }

    public CartoonItem(String id) {
        this.id = id;
    }

    public CartoonItem(String id, String cartoon_id, Integer sort, String remote_connect, String content_pic) {
        this.id = id;
        this.cartoon_id = cartoon_id;
        this.sort = sort;
        this.remote_connect = remote_connect;
        this.content_pic = content_pic;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCartoon_id() {
        return cartoon_id;
    }

    public void setCartoon_id(String cartoon_id) {
        this.cartoon_id = cartoon_id;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getRemote_connect() {
        return remote_connect;
    }

    public void setRemote_connect(String remote_connect) {
        this.remote_connect = remote_connect;
    }

    public String getContent_pic() {
        return content_pic;
    }

    public void setContent_pic(String content_pic) {
        this.content_pic = content_pic;
    }

    // KEEP METHODS - put your custom methods here
    // KEEP METHODS END

}