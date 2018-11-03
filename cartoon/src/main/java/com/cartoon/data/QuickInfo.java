package com.cartoon.data;

import java.io.Serializable;

public class QuickInfo implements Serializable {
    /**
     * 0 landscape
     * 1 portrait
     */
    public int vertical;
    public String poster;
    public String downloadUrl;
    public String packageName;
    public int versionCode;
}
