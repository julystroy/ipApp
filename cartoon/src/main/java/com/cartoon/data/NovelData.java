package com.cartoon.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by cc on 17-11-7.
 */
public class NovelData implements Parcelable{
    /**
     "id": 3,//章节ID
     "title": "第二章",//章节标题
     "isRead": 0,//是否已读 (0.未读 1.已读)

     */

    private int id;
    private String title;
    private String isRead;


    public NovelData() {
    }

    protected NovelData(Parcel in) {
        id = in.readInt();
        title = in.readString();
        isRead = in.readString();

    }

    public static final Creator<NovelData> CREATOR = new Creator<NovelData>() {
        @Override
        public NovelData createFromParcel(Parcel in) {
            return new NovelData(in);
        }

        @Override
        public NovelData[] newArray(int size) {
            return new NovelData[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIsRead() {
        return isRead;
    }

    public void setIsRead(String isRead) {
        this.isRead = isRead;
    }

    @Override
    public String toString() {
        return "NovelData{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", isRead=" + isRead +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(isRead);
    }
}
