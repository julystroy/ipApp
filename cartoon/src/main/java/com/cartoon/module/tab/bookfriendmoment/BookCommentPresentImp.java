package com.cartoon.module.tab.bookfriendmoment;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.cartoon.data.BookFriendMoment;
import com.cartoon.data.response.BookFriendMomentListResp;
import com.cartoon.http.BaseCallBack;
import com.cartoon.http.BuilderInstance;
import com.cartoon.http.StaticField;
import com.cartoon.utils.CollectionUtils;

/**
 * Created by cc on 17-7-26.
 */
public class BookCommentPresentImp implements BookFriendMomentPresenter {

    private BookFriendMomentView view;

    public BookCommentPresentImp(BookFriendMomentView view) {
        this.view = view;
    }
    @Override
    public void loadBookFriendMomentList(final int page, int pageSize, String sortName, final boolean mySelfOnly) {
        if (isFirstPage(page)) {
            view.showLoadDataView();
        }

        BuilderInstance.getInstance().getGetBuilderInstance(StaticField.URL_NOTE_LIST)
                .addParams("page", String.valueOf(page))
                .addParams("pageSize", String.valueOf(pageSize))
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
                if (isFirstPage(page)) {
                    if (CollectionUtils.isEmpty(response.getList())) {
                        view.shoeEmptyView();
                    } else {
                        view.renderList(response);
                    }
                } else {
                    view.renderListMore(response);
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

        int type = 10; // 评论书评的类型



        BuilderInstance.getInstance().getPostBuilderInstance(StaticField.URL_APP_APPROVE)
                .addParams("target_id", String.valueOf(moment.getId()))
                .addParams("type", "10")
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
