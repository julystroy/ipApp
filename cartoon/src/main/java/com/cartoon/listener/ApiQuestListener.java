package com.cartoon.listener;

/**
 * Created by David on 16/7/24.
 */
public interface ApiQuestListener {

    public void onLoadFail();

    public void onLoadSuccess(String response);
}
