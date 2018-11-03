package com.cartoon.module.tab.bookfriendmoment;

import com.cartoon.data.BookFriendMoment;
import com.cartoon.data.response.BookFriendMomentListResp;
import com.cartoon.module.IBaseView;

/**
 * Created by jinbangzhu on 7/20/16.
 */

public interface BookFriendMomentView extends IBaseView {
    void renderList(BookFriendMomentListResp listResp);

    void renderListMore(BookFriendMomentListResp listResp);

    void changeLikeStatusAndCount(BookFriendMoment bookFriendMoment, int position);

    void showErrorForDoLike(String msg);


    void showLoadingForDeleteMoment();

    void hideLoadingForDeleteMoment();

    void successForDeleteMoment(int position);
}
