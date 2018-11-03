package com.cartoon.module.tab.bookfriendmoment;

import com.cartoon.data.BookFriendMoment;

/**
 * Created by jinbangzhu on 7/20/16.
 */

public interface BookFriendMomentPresenter {
    void loadBookFriendMomentList(int page, int pageSize, String sortName, boolean mySelfOnly);

    void likeThisMoment(BookFriendMoment moment, int position);

    void deleteMoment(int id, int position);
}
