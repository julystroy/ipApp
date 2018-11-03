package com.cartoon.module.changxian.detail.comment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.cartoon.data.Keys;
import com.cartoon.module.changxian.base.BaseActivity;

/**
 * Created by wusue on 17/4/17.
 */

public class EditCommentActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentFragment(EditCommentFragment.class);
    }

    public static void launch(Activity activity, String appName, long mId, boolean hasRating) {
        activity.startActivity(getIntent(activity, appName, mId, hasRating));
    }

    public static void launch(Activity activity, String appName, long mId, boolean hasRating, int requestCode) {
        activity.startActivityForResult(getIntent(activity, appName, mId, hasRating), requestCode);
    }

    private static Intent getIntent(Activity activity, String appName, long mId, boolean hasRating) {
        Intent intent = new Intent(activity, EditCommentActivity.class);
        intent.putExtra(Keys.EXTRA_APP_NAME,appName);
        intent.putExtra(Keys.EXTRA_APP_ID,mId);
        intent.putExtra(Keys.EXTRA_HAS_RATING, hasRating);
        return intent;
    }
}
