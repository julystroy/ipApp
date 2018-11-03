package com.cartoon.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by cc on 17-1-3.
 */
public class Markets implements Parcelable{
    /* [item_id  3:三日三倍     item_id  4:七日三倍
            {
                "item_id": 1,
                "name": "三日经验卡",
                "icon": null,
                "is_del": 0,
                "description": "使用后可以在三日内经验翻倍",
                "id": 1,
                "stone": 10
                type  : 1  经验   2  ar卡
                click_icon ;;点击放大的图片
            },
            {
                "item_id": 2,
                "name": "七日经验卡",
                "icon": null,
                "is_del": 0,
                "description": "使用后可以在七日内经验翻倍",
                "id": 2,
                "stone": 100
            }
        ]*/

    private int is_del;
    private int stone;
    private int id;
    private int item_id;
    private int type;
    private String name;
    private String icon;
    private String description;
    private String click_icon;

    public Markets() {
    }

    protected Markets(Parcel in) {
        is_del = in.readInt();
        stone = in.readInt();
        id = in.readInt();
        item_id = in.readInt();
        type = in.readInt();
        name = in.readString();
        icon = in.readString();
        description = in.readString();
        click_icon = in.readString();
    }

    public static final Creator<Markets> CREATOR = new Creator<Markets>() {
        @Override
        public Markets createFromParcel(Parcel in) {
            return new Markets(in);
        }

        @Override
        public Markets[] newArray(int size) {
            return new Markets[size];
        }
    };

    public int getIs_del() {
        return is_del;
    }

    public void setIs_del(int is_del) {
        this.is_del = is_del;
    }

    public int getStone() {
        return stone;
    }

    public void setStone(int stone) {
        this.stone = stone;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getItem_id() {
        return item_id;
    }

    public void setItem_id(int item_id) {
        this.item_id = item_id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getClick_icon() {
        return click_icon;
    }

    public void setClick_icon(String click_icon) {
        this.click_icon = click_icon;
    }

    @Override
    public String toString() {
        return "Markets{" +
                "is_del=" + is_del +
                ", stone=" + stone +
                ", id=" + id +
                ", item_id=" + item_id +
                ", type=" + type +
                ", name='" + name + '\'' +
                ", icon='" + icon + '\'' +
                ", description='" + description + '\'' +
                ", click_icon='" + click_icon + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(is_del);
        dest.writeInt(stone);
        dest.writeInt(id);
        dest.writeInt(item_id);
        dest.writeInt(type);
        dest.writeString(name);
        dest.writeString(icon);
        dest.writeString(description);
        dest.writeString(click_icon);
    }
}
