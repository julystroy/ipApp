/*
package com.cartoon;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.cartoon.utils.Utils;

*/
/**
 * Created by cc on 17-4-26.
 *//*

public class MyService extends Service{
    private static Thread mThread;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }



    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startDaemon();
        return super.onStartCommand(intent, flags, startId);
    }

    private void startDaemon() {
        if (mThread == null || !mThread.isAlive()) {
            mThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        if (!Utils.isServiceAlive(MyService.this,
                                "com.cartoon.CartoonService")) {
                            System.out.println("检测到服务2不存在.....");
                            startService(new Intent("com.cartoon.CartoonService"));
                        }
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

            mThread.start();
        }
    }
}
*/
