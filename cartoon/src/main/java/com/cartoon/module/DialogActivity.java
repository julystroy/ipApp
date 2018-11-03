/*
package com.cartoon.module;

import android.app.Activity;
import android.os.Bundle;

import com.afollestad.materialdialogs.MaterialDialog;
import com.cartoon.data.EventPause;

import org.greenrobot.eventbus.EventBus;

*/
/**
 * Created by jinbangzhu on 8/12/16.
 *//*


public class DialogActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final QtPlayerManager playerManager = QtPlayerManager.getInstance();
        if (playerManager.isPlaying()) {
            playerManager.pause();
            new MaterialDialog.Builder(this).title("提示")
                    .content("当前非wifi状态,是否继续?")
                    .positiveText("继续")
                    .negativeText("暂停")
                    .callback(new MaterialDialog.ButtonCallback() {
                        @Override
                        public void onPositive(MaterialDialog dialog) {
                            super.onPositive(dialog);
                            playerManager.resume();
                            finish();
                        }

                        @Override
                        public void onNegative(MaterialDialog dialog) {
                            super.onNegative(dialog);
                            EventBus.getDefault().post(new EventPause());
                            finish();
                        }
                    }).show();
        } else {
            finish();
        }

    }
}
*/
