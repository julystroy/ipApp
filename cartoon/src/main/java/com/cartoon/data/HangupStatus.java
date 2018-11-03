package com.cartoon.data;

import java.util.List;

public class HangupStatus {
    public int hangUpId;
    public int state; //-1： 设备异常， 0：关机， 1：开机中， 3：使用中
    public int remaining = -1; //剩余时间
    public List<HangupApp> hangUpApps;
}
