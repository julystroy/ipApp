//package com.cartoon.utils;

/**
 * Created by jinbangzhu on 8/12/16.
 */

//public class NetworkChangeReceiver extends BroadcastReceiver {
//    @Override
//    public void onReceive(Context context, Intent intent) {
//        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
//
//        if (!mWifi.isConnected()) {
//
//            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//            if (null == cm) return;
//            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
//            if (null == activeNetwork) return;
//
//            boolean isConnected = activeNetwork.isConnectedOrConnecting();
//            if (isConnected) {
//                context.startActivity(new Intent(context, DialogActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
//            }
//        }
//    }
//}
