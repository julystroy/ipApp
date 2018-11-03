package com.cartoon.module.changxian.download;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.cartoon.module.changxian.base.BaseActivity;

public class DownloadActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentFragment(DownloadFragment.class);
    }

    public static void launch(Activity activity) {
        Intent intent = new Intent(activity, DownloadActivity.class);
        activity.startActivity(intent);
    }
}
