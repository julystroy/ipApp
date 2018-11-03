package com.cartoon.module.tab.mine;

import java.io.File;

/**
 * Created by jinbangzhu on 7/22/16.
 */

public interface MinePresenter {
    void updateUserInfo(String nickName, int gender);

    void uploadAvatar(File filePath);

    void logout();

    void checkUpdate();
}
