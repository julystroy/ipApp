package com.cartoon.module.changxian.comment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.cartoon.data.Keys;
import com.cartoon.module.changxian.base.BaseActivity;
import com.cartoon.module.changxian.detail.DetailActivity;

/**
 * 评论列表
 * Created by shidu on 17/1/18.
 */

public class CommentsListActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentFragment(CommentsListFragment.class);
    }

    public static void launch(Activity activity, String appName, long id) {
        Intent intent = new Intent(activity, DetailActivity.class);
        intent.putExtra(Keys.EXTRA_APP_NAME,appName);
        intent.putExtra(Keys.EXTRA_APP_ID,id);
        activity.startActivity(intent);
    }
}
