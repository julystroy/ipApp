package com.cartoon.module.changxian.detail;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.cartoon.data.Keys;
import com.cartoon.module.changxian.base.BaseActivity;

public class DetailActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //hide R.layout.content_frame的 toolbar，使用本view中的toolbar
        hideToolbar();

        setContentFragment(DetailFragment.class);
    }

    public static void launch(Activity activity, String appName, long id, int requestCode) {
        activity.startActivityForResult(getIntent(activity, appName, id), requestCode);
    }

    public static void launch(Activity activity, String appName, long id) {
        activity.startActivity(getIntent(activity, appName, id));
    }

    private static Intent getIntent(Activity activity, String appName, long id) {
        Intent intent = new Intent(activity, DetailActivity.class);
        intent.putExtra(Keys.EXTRA_APP_NAME,appName);
        intent.putExtra(Keys.EXTRA_APP_ID,id);
        return intent;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
