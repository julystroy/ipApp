package com.cartton.library.download;

import java.io.File;

/**
 * 下载请求
 * <p>
 * Created by David on 16/5/11.
 */
public class DownloadRequest {
    private String mUri;
    private File mFolder;
    private CharSequence mTitle;
    private CharSequence mDescription;
    private boolean mScannable;

    private DownloadRequest() {
    }

    private DownloadRequest(String uri, File folder, CharSequence title, CharSequence description, boolean scannable) {
        this.mUri = uri;
        this.mFolder = folder;
        this.mTitle = title;
        this.mDescription = description;
        this.mScannable = scannable;
    }

    public String getUri() {
        return mUri;
    }

    public File getFolder() {
        return mFolder;
    }

    public CharSequence getTitle() {
        return mTitle;
    }

    public CharSequence getDescription() {
        return mDescription;
    }

    public boolean isScannable() {
        return mScannable;
    }


    public static class Builder {

        private String mUri;

        private File mFolder;

        private CharSequence mTitle;

        private CharSequence mDescription;

        private boolean mScannable;

        public Builder() {
        }

        public Builder setUri(String uri) {
            this.mUri = uri;
            return this;
        }

        public Builder setFolder(File folder) {
            this.mFolder = folder;
            return this;
        }

        public Builder setTitle(CharSequence title) {
            this.mTitle = title;
            return this;
        }

        public Builder setDescription(CharSequence description) {
            this.mDescription = description;
            return this;
        }

        public Builder setScannable(boolean scannable) {
            this.mScannable = scannable;
            return this;
        }

        public DownloadRequest build() {
            return new DownloadRequest(mUri, mFolder, mTitle, mDescription, mScannable);
        }
    }

    @Override
    public String toString() {
        return "DownloadRequest{" +
                "mUri='" + mUri + '\'' +
                ", mFolder=" + mFolder +
                ", mTitle=" + mTitle +
                ", mDescription=" + mDescription +
                ", mScannable=" + mScannable +
                '}';
    }
}
