package com.cartoon.module.listener;

/**
 * Created by cc on 17-6-16.
 */
public interface ResponseListener {
    void onLoadFail();
    void onLoadSuccess(String response);
}
