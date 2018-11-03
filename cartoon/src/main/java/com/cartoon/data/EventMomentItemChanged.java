package com.cartoon.data;

/**
 * Created by jinbangzhu on 8/23/16.
 */

public class EventMomentItemChanged {
    public final BookFriendMoment moment;
    public final int position;

    public EventMomentItemChanged(BookFriendMoment moment, int position) {
        this.moment = moment;
        this.position = position;
    }
}
