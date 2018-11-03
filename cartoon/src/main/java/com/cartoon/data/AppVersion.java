package com.cartoon.data;

/**
 * Created by jinbangzhu on 7/24/16.
 */

public class AppVersion {
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getVersion_code() {
        return version_code;
    }

    public void setVersion_code(String version_code) {
        this.version_code = version_code;
    }

    public int getConpel() {
        return conpel;
    }

    public void setConpel(int conpel) {
        this.conpel = conpel;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    String id;
    String version;
    String url;
    String version_code;
    String content;
    int    conpel;
}
