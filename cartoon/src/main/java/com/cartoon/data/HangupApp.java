package com.cartoon.data;

import java.io.Serializable;

public class HangupApp implements Serializable {
    public int hangUpAppId;
    public int appId;// 仅供连接节点试玩使用
    public String name;
    public String logo;
    public String channel;
    public int state; // 0：未启动， 1：运行中
    public int type = 0;// 1:游戏， 2:辅助

    @Override
    public int hashCode() {
        return Integer.valueOf(hangUpAppId).hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof HangupApp))
            return false;
        HangupApp pn = (HangupApp) o;
        return pn.hangUpAppId == hangUpAppId;

    }
}
