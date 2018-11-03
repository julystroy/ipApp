package com.cartoon.module.tab.bookfriendmoment;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.cartoon.data.BookFriendMoment;
import com.cartoon.data.response.BookFriendMomentListResp;
import com.cartoon.data.response.BookFriendPost;
import com.cartoon.http.BaseCallBack;
import com.cartoon.http.BuilderInstance;
import com.cartoon.http.StaticField;
import com.cartoon.utils.CollectionUtils;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by jinbangzhu on 7/20/16.
 */

public class BookFriendMomentPresenterImpl implements BookFriendMomentPresenter {
    private BookFriendMomentView view;

    private boolean first = true;
    public BookFriendMomentPresenterImpl(BookFriendMomentView view) {
        this.view = view;
    }

    @Override
    public void loadBookFriendMomentList(final int page, int pageSize, String sortName, final boolean mySelfOnly) {
        if (isFirstPage(page)) {
            view.showLoadDataView();
        }

        BuilderInstance.getInstance().getGetBuilderInstance(mySelfOnly ? StaticField.URL_APP_MY_MOMENT : StaticField.URL_BOOK_FRIENDS_MOMENT)
                .addParams("page", String.valueOf(page))
                .addParams("pageSize", String.valueOf(pageSize))
                .addParams("sortName", sortName)
                .build().execute(new BaseCallBack<BookFriendMomentListResp>() {

            @Override
            public void onLoadFail() {
                if (isFirstPage(page)) {
                    view.showErrorView();
                } else {
                    view.showLoadMoreFailedView();
                }
            }

            @Override
            public void onContentNull() {

            }

            @Override
            public void onLoadSuccess(BookFriendMomentListResp response) {
                view.hideLoadDataView();
                if (response ==null || response.getList()==null) return;
                if (isFirstPage(page)) {
                    if (CollectionUtils.isEmpty(response.getList())) {
                        view.shoeEmptyView();
                    } else {
                        view.renderList(response);
                    }
                } else {
                    view.renderListMore(response);
                }
                if (!mySelfOnly&&first) {
                    first=false;

                    BookFriendMoment moment = response.getList().get(6);
                    if (moment != null) {
                        EventBus.getDefault().post(new BookFriendPost(moment.getId()));
                    }
                }

            }

            @Override
            public BookFriendMomentListResp parseNetworkResponse(String response) throws Exception {
                return JSON.parseObject(response, BookFriendMomentListResp.class);
            }

        });
    }

    @Override
    public void likeThisMoment(final BookFriendMoment moment, final int position) {
        if (moment.getIs_approve() == 1) {
            view.showErrorForDoLike("已赞");
            return;
        }
        view.changeLikeStatusAndCount(moment, position);

        int type = 5; // 评论非书友圈的类型
        if (moment.getType() == 4) { // 4==书友圈
            type = 4;
        }


        BuilderInstance.getInstance().getPostBuilderInstance(StaticField.URL_APP_APPROVE)
                .addParams("target_id", String.valueOf(moment.getId()))
                .addParams("type", String.valueOf(type))
                .build().execute(new BaseCallBack<String>() {

            @Override
            public void onLoadFail() {
                view.changeLikeStatusAndCount(moment, position);
                view.showErrorForDoLike(getMessage());
            }

            @Override
            public void onContentNull() {

            }

            @Override
            public void onLoadSuccess(String response) {
                Log.d("onLoadSuccess", "response=" + response);
            }

            @Override
            public String parseNetworkResponse(String response) throws Exception {
                return response;
            }
        });
    }

    @Override
    public void deleteMoment(int id, final int position) {
        view.showLoadingForDeleteMoment();
        BuilderInstance.getInstance().getPostBuilderInstance(StaticField.URL_DEL_COMMENT)
                .addParams("id", String.valueOf(id))
                .addParams("is_two", String.valueOf("1"))//// FIXME: 16-11-28 1
                .build().execute(new BaseCallBack<String>() {

            @Override
            public void onLoadFail() {
                view.hideLoadingForDeleteMoment();
                view.showErrorForDoLike(getMessage());
            }
            @Override
            public void onContentNull() {

            }
            @Override
            public void onLoadSuccess(String response) {
                view.hideLoadingForDeleteMoment();
                view.successForDeleteMoment(position);
                Log.d("onLoadSuccess", "response=" + response);
            }

            @Override
            public String parseNetworkResponse(String response) throws Exception {
                return response;
            }
        });

    }

    private boolean isFirstPage(int page) {
        return page <= 1;
    }
}
