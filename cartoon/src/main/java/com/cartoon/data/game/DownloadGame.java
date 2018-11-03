package com.cartoon.data.game;


import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by wuchuchu on 2018/3/6.
 */

@Entity(indexes = {
        @Index(value = "packageName, cxId, appName, iconUrl, downloadUrl, size, updatedAt, createdAt", unique = true)
})

public class DownloadGame {
    @Id
    private Long id;

    @NotNull
    private String packageName;
    private long cxId; //c端 ID
    private String appName;
    private String iconUrl;
    private String downloadUrl;
    private String size;
    private long updatedAt, createdAt;

    public long getCreatedAt() {
            return this.createdAt;
    }
    public void setCreatedAt(long createdAt) {
            this.createdAt = createdAt;
    }
    public long getUpdatedAt() {
            return this.updatedAt;
    }
    public void setUpdatedAt(long updatedAt) {
            this.updatedAt = updatedAt;
    }
    public String getSize() {
            return this.size;
    }
    public void setSize(String size) {
            this.size = size;
    }
    public String getDownloadUrl() {
            return this.downloadUrl;
    }
    public void setDownloadUrl(String downloadUrl) {
            this.downloadUrl = downloadUrl;
    }
    public String getIconUrl() {
            return this.iconUrl;
    }
    public void setIconUrl(String iconUrl) {
            this.iconUrl = iconUrl;
    }
    public String getAppName() {
            return this.appName;
    }
    public void setAppName(String appName) {
            this.appName = appName;
    }
    public long getCxId() {
            return this.cxId;
    }
    public void setCxId(long cxId) {
            this.cxId = cxId;
    }
    public String getPackageName() {
            return this.packageName;
    }
    public void setPackageName(String packageName) {
            this.packageName = packageName;
    }
    public Long getId() {
            return this.id;
    }
    public void setId(Long id) {
            this.id = id;
    }
    @Generated(hash = 451327683)
    public DownloadGame(Long id, @NotNull String packageName, long cxId, String appName, String iconUrl,
                    String downloadUrl, String size, long updatedAt, long createdAt) {
            this.id = id;
            this.packageName = packageName;
            this.cxId = cxId;
            this.appName = appName;
            this.iconUrl = iconUrl;
            this.downloadUrl = downloadUrl;
            this.size = size;
            this.updatedAt = updatedAt;
            this.createdAt = createdAt;
    }

    public DownloadGame(Long id, @NotNull String packageName, long cxId, String appName, String iconUrl,
                        String downloadUrl, String size) {
        this.id = id;
        this.packageName = packageName;
        this.cxId = cxId;
        this.appName = appName;
        this.iconUrl = iconUrl;
        this.downloadUrl = downloadUrl;
        this.size = size;
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = createdAt;
    }

    @Generated(hash = 661236629)
    public DownloadGame() {
    }
}
