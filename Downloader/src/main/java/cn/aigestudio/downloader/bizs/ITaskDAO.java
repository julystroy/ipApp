package cn.aigestudio.downloader.bizs;

import java.util.List;

interface ITaskDAO {
    void insertTaskInfo(DLInfo info);

    void deleteTaskInfo(String url);

    void updateTaskInfo(DLInfo info);

    DLInfo queryTaskInfo(String url);

    List<DLInfo> queryAllTaskInfo();
}