package cn.aigestudio.downloader.bizs;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.aigestudio.downloader.interfaces.IDListener;

/**
 * 下载实体类
 * Download entity.
 *
 * @author AigeStudio 2015-05-16
 */
public class DLInfo {
    public static final int STATE_FAILED = -1;
    public static final int STATE_UNKNOWN = 0;
    public static final int STATE_WAITING = STATE_UNKNOWN + 1;
    public static final int STATE_DOWNLOADING = STATE_WAITING + 1;
    public static final int STATE_STOP = STATE_DOWNLOADING + 1;
    public static final int STATE_COMPLETED = STATE_STOP + 1;
    public static final int STATE_INSTALLED = STATE_COMPLETED + 1;

    public int state;
    public int totalBytes;
    public int currentBytes;
    public String fileName;
    public String dirPath;
    public String baseUrl;
    public String realUrl;

    int redirect;
    boolean hasListener;
    boolean isResume;
    boolean isStop;
    String mimeType;
    String eTag;
    String disposition;
    String location;
    List<DLHeader> requestHeaders;
    final List<DLThreadInfo> threads;
    IDListener listener;
    File file;

    DLInfo() {
        state = STATE_UNKNOWN;
        threads = new ArrayList<>();
    }

    synchronized void addDLThread(DLThreadInfo info) {
        threads.add(info);
    }

    synchronized void removeDLThread(DLThreadInfo info) {
        threads.remove(info);
    }

    public synchronized boolean isStop() {
        return isStop;
    }
}