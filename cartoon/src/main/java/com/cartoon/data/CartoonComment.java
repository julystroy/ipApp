package com.cartoon.data;

import java.util.List;

/**
 * 评论
 * <p>
 * Created by David on 16/6/27.
 */
public class CartoonComment  {


    /**
     * approve_num : 1
     * uid : 2988
     * bonus_stone : 5559
     * is_approve : 0
     * nickname : 我勒个去
     * honorName : 策
     * children : {"pageSize":10,"pageNumber":1,"list":[{"uid":2988,"bonus_stone":5559,"two_id":3006,"to_uid":2988,"nickname":"我勒个去","to_content":"哈哈哈","to_create_time":"3天前","honorName":"策","to_nickname":"我勒个去","avatar":"http://116.228.59.173:8888/book/upload/avatar/user/2017-09/25/2988_1506331587857.jpg","bonus_point":170204,"honor_id":6,"lvName":"元婴大圆满"},{"uid":2988,"bonus_stone":5559,"two_id":3004,"to_uid":2988,"nickname":"我勒个去","to_content":"呵呵哈哈哈","to_create_time":"3天前","honorName":"策","to_nickname":"我勒个去","avatar":"http://116.228.59.173:8888/book/upload/avatar/user/2017-09/25/2988_1506331587857.jpg","bonus_point":170204,"honor_id":6,"lvName":"元婴大圆满"},{"uid":2988,"bonus_stone":5559,"two_id":3002,"to_uid":2988,"nickname":"我勒个去","to_content":"哈哈哈","to_create_time":"3天前","honorName":"策","to_nickname":"我勒个去","avatar":"http://116.228.59.173:8888/book/upload/avatar/user/2017-09/25/2988_1506331587857.jpg","bonus_point":170204,"honor_id":6,"lvName":"元婴大圆满"},{"uid":2988,"bonus_stone":5559,"two_id":3001,"to_uid":2988,"nickname":"我勒个去","to_content":"哈哈哈哈","to_create_time":"3天前","honorName":"策","to_nickname":"我勒个去","avatar":"http://116.228.59.173:8888/book/upload/avatar/user/2017-09/25/2988_1506331587857.jpg","bonus_point":170204,"honor_id":6,"lvName":"元婴大圆满"}],"firstPage":true,"lastPage":true,"totalRow":4,"totalPage":1}
     * bonus_point : 170204
     * avatar : http://116.228.59.173:8888/book/upload/avatar/user/2017-09/25/2988_1506331587857.jpg
     * type : 11
     * photo : null
     * lvName : 元婴大圆满
     * content : 我是凡人，我有话说，我写了《哈哈哈》。快来给我投票，助我成仙！
     * id : 38059
     * module_id : 125
     * sect_user_rank : null
     * create_time : 3天前
     * comment_num : 4
     * honor_id : 6
     * small_pic : null
     * module_title : 哈哈哈
     */

    private String approve_num;
    private String          uid;
    private int          bonus_stone;
    private int          is_approve;
    private String       nickname;
    private String       honorName;
    private ChildrenBean children;
    private int          bonus_point;
    private String       avatar;
    private int          type;
    private List       photo;
    private String       lvName;
    private String       content;
    private String          id;
    private int          module_id;
    private String       sect_user_rank;
    private String       create_time;
    private String          comment_num;
    private int          honor_id;
    private String       small_pic;
    private String       module_title;

    public String getSmall_pic() {
        return small_pic;
    }

    public void setSmall_pic(String small_pic) {
        this.small_pic = small_pic;
    }

    public String getApprove_num() {
        return approve_num;
    }

    public void setApprove_num(String approve_num) {
        this.approve_num = approve_num;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public int getBonus_stone() {
        return bonus_stone;
    }

    public void setBonus_stone(int bonus_stone) {
        this.bonus_stone = bonus_stone;
    }

    public int getIs_approve() {
        return is_approve;
    }

    public void setIs_approve(int is_approve) {
        this.is_approve = is_approve;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getHonorName() {
        return honorName;
    }

    public void setHonorName(String honorName) {
        this.honorName = honorName;
    }

    public ChildrenBean getChildren() {
        return children;
    }

    public void setChildren(ChildrenBean children) {
        this.children = children;
    }

    public int getBonus_point() {
        return bonus_point;
    }

    public void setBonus_point(int bonus_point) {
        this.bonus_point = bonus_point;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List getPhoto() {
        return photo;
    }

    public void setPhoto(List photo) {
        this.photo = photo;
    }

    public String getLvName() {
        return lvName;
    }

    public void setLvName(String lvName) {
        this.lvName = lvName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getModule_id() {
        return module_id;
    }

    public void setModule_id(int module_id) {
        this.module_id = module_id;
    }

    public String getSect_user_rank() {
        return sect_user_rank;
    }

    public void setSect_user_rank(String sect_user_rank) {
        this.sect_user_rank = sect_user_rank;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getComment_num() {
        return comment_num;
    }

    public void setComment_num(String comment_num) {
        this.comment_num = comment_num;
    }

    public int getHonor_id() {
        return honor_id;
    }

    public void setHonor_id(int honor_id) {
        this.honor_id = honor_id;
    }

    public String getModule_title() {
        return module_title;
    }

    public void setModule_title(String module_title) {
        this.module_title = module_title;
    }


    @Override
    public String toString() {
        return "CartoonComment{" +
                "approve_num=" + approve_num +
                ", uid=" + uid +
                ", bonus_stone=" + bonus_stone +
                ", is_approve=" + is_approve +
                ", nickname='" + nickname + '\'' +
                ", honorName='" + honorName + '\'' +
                ", children=" + children +
                ", bonus_point=" + bonus_point +
                ", avatar='" + avatar + '\'' +
                ", type=" + type +
                ", photo=" + photo +
                ", lvName='" + lvName + '\'' +
                ", content='" + content + '\'' +
                ", id=" + id +
                ", module_id=" + module_id +
                ", sect_user_rank='" + sect_user_rank + '\'' +
                ", create_time='" + create_time + '\'' +
                ", comment_num=" + comment_num +
                ", honor_id=" + honor_id +
                ", small_pic='" + small_pic + '\'' +
                ", module_title='" + module_title + '\'' +
                '}';
    }

    public static class ChildrenBean {
        /**
         * pageSize : 10
         * pageNumber : 1
         * list : [{"uid":2988,"bonus_stone":5559,"two_id":3006,"to_uid":2988,"nickname":"我勒个去","to_content":"哈哈哈","to_create_time":"3天前","honorName":"策","to_nickname":"我勒个去","avatar":"http://116.228.59.173:8888/book/upload/avatar/user/2017-09/25/2988_1506331587857.jpg","bonus_point":170204,"honor_id":6,"lvName":"元婴大圆满"},{"uid":2988,"bonus_stone":5559,"two_id":3004,"to_uid":2988,"nickname":"我勒个去","to_content":"呵呵哈哈哈","to_create_time":"3天前","honorName":"策","to_nickname":"我勒个去","avatar":"http://116.228.59.173:8888/book/upload/avatar/user/2017-09/25/2988_1506331587857.jpg","bonus_point":170204,"honor_id":6,"lvName":"元婴大圆满"},{"uid":2988,"bonus_stone":5559,"two_id":3002,"to_uid":2988,"nickname":"我勒个去","to_content":"哈哈哈","to_create_time":"3天前","honorName":"策","to_nickname":"我勒个去","avatar":"http://116.228.59.173:8888/book/upload/avatar/user/2017-09/25/2988_1506331587857.jpg","bonus_point":170204,"honor_id":6,"lvName":"元婴大圆满"},{"uid":2988,"bonus_stone":5559,"two_id":3001,"to_uid":2988,"nickname":"我勒个去","to_content":"哈哈哈哈","to_create_time":"3天前","honorName":"策","to_nickname":"我勒个去","avatar":"http://116.228.59.173:8888/book/upload/avatar/user/2017-09/25/2988_1506331587857.jpg","bonus_point":170204,"honor_id":6,"lvName":"元婴大圆满"}]
         * firstPage : true
         * lastPage : true
         * totalRow : 4
         * totalPage : 1
         */

        private int pageSize;
        private int            pageNumber;
        private boolean        firstPage;
        private boolean        lastPage;
        private int            totalRow;
        private int            totalPage;
        private List<CartoonCommentChild> list;

        public int getPageSize() {
            return pageSize;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }

        public int getPageNumber() {
            return pageNumber;
        }

        public void setPageNumber(int pageNumber) {
            this.pageNumber = pageNumber;
        }

        public boolean isFirstPage() {
            return firstPage;
        }

        public void setFirstPage(boolean firstPage) {
            this.firstPage = firstPage;
        }

        public boolean isLastPage() {
            return lastPage;
        }

        public void setLastPage(boolean lastPage) {
            this.lastPage = lastPage;
        }

        public int getTotalRow() {
            return totalRow;
        }

        public void setTotalRow(int totalRow) {
            this.totalRow = totalRow;
        }

        public int getTotalPage() {
            return totalPage;
        }

        public void setTotalPage(int totalPage) {
            this.totalPage = totalPage;
        }

        public List<CartoonCommentChild> getList() {
            return list;
        }

        public void setList(List<CartoonCommentChild> list) {
            this.list = list;
        }


    }
}
