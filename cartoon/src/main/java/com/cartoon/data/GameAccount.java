package com.cartoon.data;

import java.util.List;

public class GameAccount {
    public static final int STATE_FREE = 0;
    public static final int STATE_BUSY = 1;
    public int state;
    public String title;
    public String intro1;
    public String intro2;
    public List<Device> devices;
}
