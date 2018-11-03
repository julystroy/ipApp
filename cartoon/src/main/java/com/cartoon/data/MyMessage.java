package com.cartoon.data;

import java.util.List;

/**
 * Created by jinbangzhu on 7/22/16.
 */

public class MyMessage {


    /**
     * totalRow : 80
     * pageNumber : 1
     * firstPage : true
     * lastPage : false
     * totalPage : 8
     * pageSize : 10
     * list : [{"create_time":"2017-11-28 16:59:07","otherId":2988,"resource":{"create_time":"2017-11-28 16:58:48","id":38428,"type":4,"module_title":null,"content":""},"honorName":"策","photo":["/upload/photo/user/2017-11/28/31320_1511859528411.png"],"avatar":"/upload/avatar/user/2017-09/25/2988_1506331587857.jpg","type":2,"content":"38428","u_id":3132,"res_id":2988,"is_del":0,"nickname":"阳光男孩","lvName":"结丹中期","id":9618},{"create_time":"2017-11-28 15:28:54","otherId":2988,"resource":{"create_time":"2017-11-28 15:25:55","id":38400,"type":3,"module_title":"凡人修仙之仙界篇 第九章 阵法","content":"连载啦啦啦啦啦啦啦啦啦啦啦"},"honorName":"策","photo":[null],"avatar":"/upload/avatar/user/2017-09/25/2988_1506331587857.jpg","type":2,"content":"38400","u_id":3132,"res_id":2988,"is_del":0,"nickname":"阳光男孩","lvName":"结丹中期","id":9596},{"create_time":"2017-11-28 15:28:13","otherId":2988,"resource":{"create_time":"2017-11-28 15:25:35","id":38399,"type":4,"module_title":null,"content":"书友全额摸摸哦哦弄噢噢噢哦哦"},"honorName":"策","photo":[null],"avatar":"/upload/avatar/user/2017-09/25/2988_1506331587857.jpg","type":2,"content":"38399","u_id":3132,"res_id":2988,"is_del":0,"nickname":"阳光男孩","lvName":"结丹中期","id":9595},{"create_time":"2017-11-28 15:28:10","otherId":2988,"resource":{"create_time":"2017-11-28 15:26:12","id":38401,"type":3,"module_title":"凡人修仙之仙界篇 第十一章 苏醒","content":"纪念啦啦啦啦啦啦啦啦啦啦啦啦啦"},"honorName":"策","photo":[null],"avatar":"/upload/avatar/user/2017-09/25/2988_1506331587857.jpg","type":2,"content":"38401","u_id":3132,"res_id":2988,"is_del":0,"nickname":"阳光男孩","lvName":"结丹中期","id":9594},{"create_time":"2017-11-28 15:28:09","otherId":2988,"resource":{"create_time":"2017-11-28 15:26:26","id":38403,"type":7,"module_title":"宠物","content":"次元啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦"},"honorName":"策","photo":[null],"avatar":"/upload/avatar/user/2017-09/25/2988_1506331587857.jpg","type":2,"content":"38403","u_id":3132,"res_id":2988,"is_del":0,"nickname":"阳光男孩","lvName":"结丹中期","id":9593},{"create_time":"2017-11-28 15:21:53","otherId":2988,"resource":{"create_time":"2017-11-28 15:08:45","id":38393,"type":4,"module_title":null,"content":"摸摸哦哦弄噢噢噢哦哦摸摸哦哦弄哦哦哦"},"honorName":"策","photo":[null],"avatar":"/upload/avatar/user/2017-09/25/2988_1506331587857.jpg","type":2,"content":"38393","u_id":3132,"res_id":2988,"is_del":0,"nickname":"阳光男孩","lvName":"结丹中期","id":9587},{"create_time":"2017-11-28 15:21:49","otherId":2988,"resource":{"create_time":"2017-11-28 15:11:04","id":38394,"type":4,"module_title":null,"content":"3333333333333333333333333333333"},"honorName":"策","photo":[null],"avatar":"/upload/avatar/user/2017-09/25/2988_1506331587857.jpg","type":2,"content":"38394","u_id":3132,"res_id":2988,"is_del":0,"nickname":"阳光男孩","lvName":"结丹中期","id":9586},{"create_time":"2017-11-28 15:21:47","otherId":2988,"resource":{"create_time":"2017-11-28 15:11:47","id":38395,"type":4,"module_title":null,"content":"哦哦哦哦哦哦哦哦哦哦哦哦哦哦哦哦哦哦哦哦哦哦哦哦哦哦哦哦哦哦哦哦哦哦哦哦哦哦哦哦哦哦哦哦哦哦哦哦"},"honorName":"策","photo":[null],"avatar":"/upload/avatar/user/2017-09/25/2988_1506331587857.jpg","type":2,"content":"38395","u_id":3132,"res_id":2988,"is_del":0,"nickname":"阳光男孩","lvName":"结丹中期","id":9585},{"create_time":"2017-11-28 15:21:03","otherId":336,"resource":{"create_time":"2017-11-28 15:11:47","id":38395,"type":4,"module_title":null,"content":"哦哦哦哦哦哦哦哦哦哦哦哦哦哦哦哦哦哦哦哦哦哦哦哦哦哦哦哦哦哦哦哦哦哦哦哦哦哦哦哦哦哦哦哦哦哦哦哦"},"honorName":"智","photo":null,"avatar":"/upload/avatar/user/2017-09/18/336_1505700533946.jpg","type":1,"comment_id":38395,"content":"哦哦哦哦哦","u_id":3132,"res_id":3390,"is_del":0,"nickname":"凡人哥","lvName":"元婴大圆满","id":9584},{"create_time":"2017-11-28 15:20:56","otherId":336,"resource":{"create_time":"2017-11-28 15:11:47","id":38395,"type":4,"module_title":null,"content":"哦哦哦哦哦哦哦哦哦哦哦哦哦哦哦哦哦哦哦哦哦哦哦哦哦哦哦哦哦哦哦哦哦哦哦哦哦哦哦哦哦哦哦哦哦哦哦哦"},"honorName":"智","photo":null,"avatar":"/upload/avatar/user/2017-09/18/336_1505700533946.jpg","type":1,"comment_id":38395,"content":"vhjk","u_id":3132,"res_id":3389,"is_del":0,"nickname":"凡人哥","lvName":"元婴大圆满","id":9583}]
     */

    /*private int totalRow;
    private int            pageNumber;
    private boolean        firstPage;
    private boolean        lastPage;
    private int            totalPage;
    private int            pageSize;
    private List<ListBean> list;

    public int getTotalRow() {
        return totalRow;
    }

    public void setTotalRow(int totalRow) {
        this.totalRow = totalRow;
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

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {*/
        /**
         * create_time : 2017-11-28 16:59:07
         * otherId : 2988
         * resource : {"create_time":"2017-11-28 16:58:48","id":38428,"type":4,"module_title":null,"content":""}
         * honorName : 策
         * photo : ["/upload/photo/user/2017-11/28/31320_1511859528411.png"]
         * avatar : /upload/avatar/user/2017-09/25/2988_1506331587857.jpg
         * type : 2
         * content : 38428
         * u_id : 3132
         * res_id : 2988
         * is_del : 0
         * nickname : 阳光男孩
         * lvName : 结丹中期
         * id : 9618
         * comment_id : 38395
         */

        private String create_time;
        private int          otherId;
        private MyMessageResource resource;
        private String       honorName;
        private String       avatar;
        private int          type;
        private String       content;
        private int          u_id;
        private int          res_id;
        private int          is_del;
        private String       nickname;
        private String       lvName;
        private int          id;
        private int          comment_id;
        private List<String> photo;

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public int getOtherId() {
            return otherId;
        }

        public void setOtherId(int otherId) {
            this.otherId = otherId;
        }

        public MyMessageResource getResource() {
            return resource;
        }

        public void setResource(MyMessageResource resource) {
            this.resource = resource;
        }

        public String getHonorName() {
            return honorName;
        }

        public void setHonorName(String honorName) {
            this.honorName = honorName;
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

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public int getU_id() {
            return u_id;
        }

        public void setU_id(int u_id) {
            this.u_id = u_id;
        }

        public int getRes_id() {
            return res_id;
        }

        public void setRes_id(int res_id) {
            this.res_id = res_id;
        }

        public int getIs_del() {
            return is_del;
        }

        public void setIs_del(int is_del) {
            this.is_del = is_del;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getLvName() {
            return lvName;
        }

        public void setLvName(String lvName) {
            this.lvName = lvName;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getComment_id() {
            return comment_id;
        }

        public void setComment_id(int comment_id) {
            this.comment_id = comment_id;
        }

        public List<String> getPhoto() {
            return photo;
        }

        public void setPhoto(List<String> photo) {
            this.photo = photo;
        }


}
