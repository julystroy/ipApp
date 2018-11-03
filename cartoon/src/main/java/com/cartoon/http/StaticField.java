package com.cartoon.http;


import cn.com.xuanjiezhimen.BuildConfig;

/**
 * API 配置信息
 * <p>
 * Created by David on 16/6/1.
 */
public class StaticField {

    public static final boolean TEST = BuildConfig.DEBUG;
    //        public static final String BASE_URL = TEST ?
//            "http://192.168.0.118:80" : "http://192.168.0.118:80";
    public static final String BASE_URL = TEST ?
      // "http://116.228.59.173:8888/book" : "http://xjzm.mopian.tv/book";
     // "http://116.228.59.173:8888/book" : "http://116.228.59.173:8888/book";
        "http://xjzm.mopian.tv/book" : "http://xjzm.mopian.tv/book";
     // "http://10.0.0.5:8888/book" : "http://xjzm.mopian.tv/book";
     //    "http://10.0.0.230:8080/book" : "http://xjzm.mopian.tv/book";//姜
     //    "http://10.0.0.122:8080/book" : "http://xjzm.mopian.tv/book";//姜
    // "http://10.0.0.234:8888/book" : "http://xjzm.mopian.tv/book";//测试服务器接口测试
      //   "http://xjzm.mopian.tv/book" : "http://xjzm.mopian.tv/book";
        //  "http://10.0.0.228:8080/book" : "http://xjzm.mopian.tv/book";//吞
      //  "http://116.228.59.173:8888/book" : "http://116.228.59.173:8888/book";
   //  public static final String BASE_URL = TEST ?
   //          "http://121.42.44.41:8080/book" : "http://121.42.44.41:8080/book";
   //  public static final String BASE_URL = TEST ?
   //          "http://121.42.44.41:8088/book" : "http://121.42.44.41:8088/book";

   //  public static final String HTML_BASE_URL =   "http://xjzm.mopian.tv/h5";
    public static final String HTML_BASE_URL =  TEST ?
         // "http://116.228.59.173:8888/h5" : "http://xjzm.mopian.tv/h5";
            "http://xjzm.mopian.tv/h5" : "http://xjzm.mopian.tv/h5";
   //   public static final String HTML_BASE_URL =   "http://10.0.0.230:8088";
    //登录
    public static final String URL_APP_LOGIN = BASE_URL + "/api/app/login";
    //获取图形码
    public static final String URL_APP_IMAGE = BASE_URL +"/api/app/checkin";
    //获取验证码
    public static final String URL_APP_CAPTCHA = BASE_URL + "/api/app/captcha";
    //活动
    public static final String URL_APP_ACTIVITY = BASE_URL + "/api/app/activity";
    //点赞
    public static final String URL_APP_APPROVE = BASE_URL + "/api/app/approve";
    //广告位
    public static final String URL_APP_ADVERTISEMENT = BASE_URL + "/api/app/advertisement";
    //update userInfo
    public static final String URL_APP_UPDATE_INFO = BASE_URL + "/book/api/user/update_info";
    public static final String URL_APP_UPDATE_AVATAR = BASE_URL + "/book/api/user/update_avatar";
    public static final String URL_APP_FEEDBACK = BASE_URL + "/book/api/user/feedback";
    public static final String URL_APP_LOGOUT = BASE_URL + "/book/api/user/logout";
    public static final String URL_APP_MY_MOMENT = BASE_URL + "/book/api/user/dynamic";
    public static final String URL_APP_ADD_POST = BASE_URL + "/book/api/core/add_posted";
    public static final String URL_APP_ADD_POST_TEXT = BASE_URL + "/book/api/core/add_posted_text";
    public static final String URL_APP_VERSION = BASE_URL + "/api/app/app_version";
    public static final String URL_APP_AD = BASE_URL + "/api/app/advertisement";

    //漫画列表
    public static final String URL_CARTOON_LIST = BASE_URL + "/book/api/cartoon/list";
    //漫画内容
    public static final String URL_CARTOON_CONTENT = BASE_URL + "/book/api/cartoon/content";
    //批量下载
    public static final String URL_CARTOON_DOWNLOAD = BASE_URL + "/book/api/cartoon/download";

    // 嘻说列表
    public static final String URL_PARSE_LIST = BASE_URL + "/book/api/parse/list";
    public static final String URL_JINIAN_LIST = BASE_URL + "/book/api/bangai/list";
    public static final String URL_PARSE_CONTENT = BASE_URL + "/book/api/parse/content";
    //书友圈列表
    public static final String URL_BOOK_FRIENDS_MOMENT = BASE_URL + "/book/api/core/list";
    public static final String URL_BOOK_USER_MESSAGE = BASE_URL + "/book/api/user/message";
    public static final String URL_BOOK_USER_MESSAGE_DELETE = BASE_URL + "/book/api/user/del_mess";

    public static final String URL_LISTENER_LIST = BASE_URL + "/book/api/listener/list";
    public static final String URL_LISTENER_CONTENT = BASE_URL + "/book/api/listener/content";
    public static final String URL_LISTENER_ADD_NUM = BASE_URL + "/book/api/listener/add_num";

    public static final String URL_COMMENT_LIST = BASE_URL + "/book/api/comment/list";
    public static final String URL_COMMENT_DETAIL = BASE_URL + "/book/api/comment/comment_dateil";
    public static final String URL_COMMENT_DETAIL_PAGE = BASE_URL + "/book/api/comment/comment_detail_page";
    public static final String URL_ADD_COMMENT = BASE_URL + "/book/api/comment/add_comment";
    public static final String URL_DEL_COMMENT = BASE_URL + "/book/api/comment/del_comment";


    public static final String URL_SHARE_CARTOON = BASE_URL + "/book/api/share?cartoon_id=";
    public static final String URL_SHARE_EXPOUND = BASE_URL + "/book/api/parseShare?parse_id=";
    public static final String URL_SHARE_BANGAI = HTML_BASE_URL + "/bookera/goShare?dataId=";
    public static final String URL_SHARE_AUDIO = BASE_URL + "/shareplayer/player.html?si=%s&fromwhere=%s&img=%s&src=%s&id=%s";
    public static final String URL_SHARE_NEWBASE = BASE_URL + "/book/api/newBaseShare?type=%s&id=%s";

    //根据用户id获得该用户昵称
    public static final String URL_USER_GET_NICKNAME = BASE_URL + "/book/api/user/getNickname?id=%s";

    //获取用户积分值
    public static final String URL_USER_LOAD_BONUS_POINT = BASE_URL + "/book/api/user/load_BonusPoint?uid=%s";

    //获取用灵石分值
    public static final String URL_USER_LOADE_STONES = BASE_URL + "/book/api/user/load_BonusStone?uid=%s&token=%s";

    public static final String SP_BONUS            = "sp_bonus";
    //追書
    public static final String URL_QUICKLISTENER_LIST =BASE_URL+"/book/api/pursue/list";

    //追書收听次数
    public static final String URL_QUICKLISTENER_LIST_NUM =BASE_URL+"/book/api/pursue/add_num";

    //获取上一条追书
    public static final String URL_QUICKLISTENER_GETPRE = BASE_URL+"/book/api/pursue/getPrePursueById";

    //获取下一条追书
    public static final String URL_QUICKLISTENER_GETNEXT = BASE_URL+"/book/api/pursue/getNextPursueById";

    //获取这一条追书
    public static final String URL_QUICKLISTENER_GETPURSUE = BASE_URL+"/book/api/pursue/getPursueById";

    //活动列表/book/api/activity/list
    public static final String URL_ACTIVITY_LIST = BASE_URL + "/book/api/activity/list";
    public static final String URL_ACTIVITY_CONTENT = BASE_URL + "/book/api/activity/content";

    //Q漫列表/book/api/qman/list
    public static final String URL_QMAN_LIST = BASE_URL + "/book/api/qman/list";
    public static final String URL_QMAN_CONTENT = BASE_URL + "/book/api/qman/content";

    //静电列表
    public static final String URL_STATICFILM_LIST = BASE_URL + "/book/api/staticfilm/list";
    public static final String URL_STATICFILM_CONTENT = BASE_URL + "/book/api/staticfilm/content";

    //连载列表
    public static final String URL_BANGAI_LIST = BASE_URL + "/book/api/novel/findChapter";
    public static final String URL_BANGAI_CONTENT = HTML_BASE_URL + "/bookevent/getDetail";
    public static final String URL_BANGAI_CATALOG = BASE_URL + "/book/api/novel/findCatalog";
    public static final String URL_JINIAN_DETAIL = HTML_BASE_URL + "/bookera/getInfo";

    //积分
    public static final String URL_USER_POINT = BASE_URL + "/book/api/action/calculate";

    //我的任务
    public static final String URL_USER_TASK= BASE_URL + "/book/api/action/myTask";
    //(APP打开和分享)积分
    public static final String URL_USER2_TASK= BASE_URL + "/book/api/action/addAction";


    public static String URL_APP_LASTED_DESIGN   = BASE_URL + "/design.json";

    //记录已读番外的id
    public static final String SP_READEDID           = "SP_READEDID";

    //坊市列表
    public static final String URL_MARKETS_LIST=BASE_URL+"/book/api/store/list";
    //购买接口
    public static final String URL_MARKETS_BUYITEM=BASE_URL+"/book/api/store/buyItem";
    //背包接口
    public static final String URL_PACKAGE_BUYITEM=BASE_URL+"/book/api/bag/list";
    //使用道具
    public static final String URL_PACKAGE_USE_BUYITEM=BASE_URL+"/book/api/bag/useItem";
    //排行
    public static final String URL_CHARTS_USE=BASE_URL+"/book/api/user/rank";
    //购买任务
    public static final String URL_BUY_TASK=BASE_URL+"/book/api/action/buyTask";
    //购买任务列表
    public static final String URL_BUY_TASK_list=BASE_URL+"/book/api/action/buyTaskList";
    //每日答题列表
    public static final String URL_DAY_QUESTION_list=BASE_URL+"/book/api/test/list";
    //每日答题获取答案
    public static final String URL_DAY_QUESTION_GET_ANSWER=BASE_URL+"/book/api/test/getAnswer";
    //下一题/购买下一题
    public static final String URL_NEXT_QUESTION=BASE_URL+"/book/api/test/getNext";
     //获取答题最终奖励
    public static final String URL_QUESTION_RECORD=BASE_URL+"/book/api/test/testRecord";

    //投票展示
    public static final String URL_VOTE_LIST        = BASE_URL+"/book/api/vote/getVote";
    //投票选择
    public static final String URL_VOTE_ITEM        = BASE_URL + "/book/api/vote/vote";
    //章节选择
    public static final String URL_SELECT_ITEM        = HTML_BASE_URL + "/chapteranswer/getQuestion";
    public static final String URL_SELECT_ANSWER        = HTML_BASE_URL + "/chapteranswer/chooseAnswer";
    //推荐
    public static final String URL_RECOMMEND_LIST        = BASE_URL + "/book/api/recom/getRecom";
    public static String LASTED_BOOKID = "LASTED_BookID";
    //举报
    public static final String URL_REPORT =BASE_URL+"/book/api/action/report";

    //书评
    public static final String  URL_SEND_NOTE = BASE_URL+"/book/api/core/bookReview";
    public static final String  URL_NOTE_LIST= BASE_URL+"/book/api/core/bookReviewList";
    public static final String  URL_NOTE_SHARE= BASE_URL+"/bookreview/goShareDetail";
    //活动DETAIL
    public static final String    URL_ACTION_RANK = BASE_URL+"/book/api/essay/list";
    public static final  String    URL_SEND_ACTION_NOTE   = BASE_URL + "/book/api/essay/addEssay";
    public static final  String    URL_ACTION_COMMENT_LIST   = BASE_URL + "/book/api/comment/comment_dateil";
    public static final  String    URL_ADD_ACTION_COMMENT  = BASE_URL + "/book/api/comment/add_comment";
    public static final  String    URL_ADD_ACTION_VOTE  = BASE_URL + "/book/api/essay/vote";
    public static final  String    URL_DELETE_ACTION_DETAIL  = BASE_URL + "/book/api/essay/delete";

    //宗门
    public static final String URL_SECT_SECTLIST = BASE_URL + "/book/api/section/sectionList";//获取宗门列表
    public static final String URL_SECT_SECTBUILD = BASE_URL + "/book/api/section/createSection";//创建宗门
    public static final String URL_SECT_SECTJOIN = BASE_URL + "/book/api/section/applySection";//申请加入宗门
    public static final String URL_SECT_APPLYMESSAGE = BASE_URL + "/book/api/section/hasReadApplyMsg";//申请加入宗门信息


    public static final String URL_SECT_SECTAPPLYLIST = BASE_URL + "/book/api/section/applyList";//宗门内信息列表
    public static final String URL_SECT_SECTAPPLY_HISTORY = BASE_URL + "/book/api/section/historyList";//宗门内信息历史列表
    public static final String URL_SECT_SECTMINELIST = BASE_URL + "/book/api/section/mySection";//宗门内信息列表
    public static final String URL_SECT_SECTAGREE = BASE_URL + "/book/api/section/agreeJoin";//宗门内信息列表
    public static final String URL_SECT_SECTADISSOLVEW = BASE_URL + "/book/api/section/dissolveSection";//解散宗门
    public static final String URL_SECT_SECTEXIT = BASE_URL + "/book/api/section/exitSection";//退出宗门
    public static final String URL_SECT_SECTPOINTLIST = BASE_URL + "/book/api/section/appointList";//职称列表
    public static final String URL_SECT_SECTPOINT = BASE_URL + "/book/api/section/appoint";//任命
    public static final String URL_SECT_SECTTASK = BASE_URL + "/book/api/action/getMySectTask";//宗门任务
    public static final String URL_SECT_CANCREAT = BASE_URL + "/book/api/section/canCreate";//创建宗门按钮
    public static final String URL_SECT_CHEAT = BASE_URL + "/book/api/section/findSectUsers";//获取用户信息
    public static final String URL_SECT_CHEAT_CONTROL = BASE_URL + "/book/api/section/controlSpeak";//获取用户信息
    public static final String URL_SECT_SEND_CHEAT_CONTROL = BASE_URL + "/book/api/section/sendChatContent";//发送用户聊天信息

 //我的界面
    public static final String URL_MYINFO = BASE_URL + "/book/api/action/myInfo";
    public static final String URL_ACTION_SIGN_IN = BASE_URL + "/book/api/action/signIn";
    public static final String URL_SECT_SIGN_IN = BASE_URL + "/book/api/section/signIn";
 //仙界动态
     public static final String URL_TREABS = BASE_URL + "/book/api/recom/list";
 //简介
     public static final String URL_GETEXP = BASE_URL + "/book/api/user/getExp";
 //宗门战
     public static final  String URL_SECT_FIGHT_L = BASE_URL +"/book/api/section/sectWarList";
     public static final  String URL_SECT_FIGHT_D = BASE_URL +"/book/api/section/sectWarDynamic";
     public static final  String URL_SECT_FIGHT_Q = BASE_URL +"/book/api/sectWar/grantAttackDirective";
     public static final  String URL_SECT_FIGHT_I = BASE_URL +"/book/api/sectWar/isAllowCommand";
     public static final  String URL_SECT_FIGHT_B = BASE_URL +"/book/api/sectWar/attack";
     public static final  String URL_SECT_FIGHT_R = BASE_URL +"/book/api/sectWar/countUserAnswerBill";
     public static final  String URL_SECT_FIGHT_RATE = BASE_URL +"/book/api/section/correctRate";

     public static final  String URL_SECT_FIGHT_QUESTION = BASE_URL +"/book/api/sectWar/getQuestions";
     public static final  String URL_SECT_FIGHT_ANSWER = BASE_URL +"/book/api/sectWar/submitAnswer";
     public static final  String URL_SECT_NEXT_QUESTION = BASE_URL +"/book/api/sectWar/nextQuestion";


 //尝鲜
 public static final String BASE_CXAPPURL = "http://www.changxianapp.com";
 public static final String BASE_CXURL = "http://c1.idianyun.cn";

 public static final String SHARE_URL = "http://www.changxianapp.com/html5/webapp/item.html?id=";

 public static final  String URL_CHANGXIAN = BASE_CXURL + "/api/apps/list";//游戏首页

 public static final  String URL_CHANGXIAN_HISTORY = BASE_CXURL + "/api/histories/list";//游戏试玩历史
 public static final  String URL_CHANGXIAN_USER_CONFIG = BASE_CXURL + "/api/users/info";//获取未评分数
 public static final  String URL_CHANGXIAN_HISTORY_DELETE = BASE_CXURL + "/api/histories/delete";//删除游戏试玩历史
 public static final  String URL_CHANGXIAN_FAVORITES = BASE_CXURL + "/api/favorites/list";//收藏列表
 public static final  String URL_CHANGXIAN_FAVORITES_DELETE = BASE_CXURL + "/api/favorites/remove";//删除收藏历史

}
