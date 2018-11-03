package com.cartoon.module.changxian.comment.reply;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.cartoon.data.Comment;
import com.cartoon.data.Keys;
import com.cartoon.module.changxian.base.BaseActivity;

/**
 * Created by wusue on 17/5/15.
 */

public class CommentReplyActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentFragment(CommentReplyFragment.class);
    }

    public static void launch(Activity activity, Comment comment, String appName, long appId, boolean showKeyboard) {
        Intent intent = new Intent(activity, CommentReplyActivity.class);
        Comment replyTopicComment = comment.clone();
        replyTopicComment.replies = null;
        intent.putExtra(Keys.EXTRA_COMMENT, replyTopicComment);
        intent.putExtra(Keys.EXTRA_APP_NAME, appName);
        intent.putExtra(Keys.EXTRA_APP_ID, appId);
        intent.putExtra(Keys.EXTRA_SHOW_KEYBOARD, showKeyboard);
        activity.startActivity(intent);
    }
}
