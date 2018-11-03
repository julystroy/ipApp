package com.cartoon.data;

import com.cartoon.http.GsonResponse;

import cn.idianyun.streaming.data.AppInfo;

public class HangupDeviceResponse extends GsonResponse<HangupDeviceResponse.DeviceData> {
    public static class DeviceData {
        public int hangUpId;
        public String ip;
        public int port;
        public String session;
        /**
         * app model in cx_sdk.
         */
        public AppInfo app;
    }
}
