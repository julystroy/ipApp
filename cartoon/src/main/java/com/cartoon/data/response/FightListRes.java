package com.cartoon.data.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.cartoon.http.BaseResponse;

import java.util.List;

/**
 * Created by cc on 18-1-17.
 */
public class FightListRes extends BaseResponse {

    /**
     * title : 七级宗门：乌漆嘛黑
     * warStatus :  攻打cc宗门中 正在被cc宗门攻打
     * warEndTime : 2018-01-16 06:21:38
     * attacking : true
     * attAndDef : 112/112
     *totalDefense
     * totalAttack
     * msgList : ["cc宗门正在攻击乌漆嘛黑宗门","乌漆嘛黑宗门正在攻击cc宗门"]
     * list : [{"user_num":20,"create_user_id":21,"sect_id":10001,"nickname":"金克拉","sect_name":"cc","sect_level":1,"sect_status":0,"nowUserNum":1,"sectTotalDefense":19,"sectTotalAttack":19}]
     */

    private String sectName;
    private String sectLevel;
    private String         warStatus;
    private String         warEndTime;
    private boolean        attacking;
    private String         attAndDef;
    private String         totalDefense;
    private String         totalAttack;
    private List<String>   msgList;
    private List<ListBean> list;



    public String getWarStatus() {
        return warStatus;
    }

    public void setWarStatus(String warStatus) {
        this.warStatus = warStatus;
    }

    public String getWarEndTime() {
        return warEndTime;
    }

    public void setWarEndTime(String warEndTime) {
        this.warEndTime = warEndTime;
    }

    public boolean isAttacking() {
        return attacking;
    }

    public void setAttacking(boolean attacking) {
        this.attacking = attacking;
    }

    public String getAttAndDef() {
        return attAndDef;
    }

    public void setAttAndDef(String attAndDef) {
        this.attAndDef = attAndDef;
    }

    public List<String> getMsgList() {
        return msgList;
    }

    public void setMsgList(List<String> msgList) {
        this.msgList = msgList;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public String getSectName() {
        return sectName;
    }

    public void setSectName(String sectName) {
        this.sectName = sectName;
    }

    public String getSectLevel() {
        return sectLevel;
    }

    public void setSectLevel(String sectLevel) {
        this.sectLevel = sectLevel;
    }

    public String getTotalDefense() {
        return totalDefense;
    }

    public void setTotalDefense(String totalDefense) {
        this.totalDefense = totalDefense;
    }

    public String getTotalAttack() {
        return totalAttack;
    }

    public void setTotalAttack(String totalAttack) {
        this.totalAttack = totalAttack;
    }

    @Override
    public String toString() {
        return "FightListRes{" +
                "sectName='" + sectName + '\'' +
                ", sectLevel='" + sectLevel + '\'' +
                ", warStatus='" + warStatus + '\'' +
                ", warEndTime='" + warEndTime + '\'' +
                ", attacking=" + attacking +
                ", attAndDef='" + attAndDef + '\'' +
                ", totalDefense='" + totalDefense + '\'' +
                ", totalAttack='" + totalAttack + '\'' +
                ", msgList=" + msgList +
                ", list=" + list +
                '}';
    }

    public static class ListBean implements Parcelable {
        /**
         * user_num : 20
         * create_user_id : 21
         * sect_id : 10001
         * nickname : 金克拉
         * sect_name : cc
         * sect_level : 1
         * sect_status : 0
         * nowUserNum : 1
         * sectTotalDefense : 19
         * sectTotalAttack : 19
         */

        private int user_num;
        private int    create_user_id;
        private int    sect_id;
        private String nickname;
        private String sect_name;
        private int    sect_level;
        private int    sect_status;
        private int    nowUserNum;
        private int    sectTotalDefense;
        private int    sectTotalAttack;

        public ListBean() {
        }

        protected ListBean(Parcel in) {
            user_num = in.readInt();
            create_user_id = in.readInt();
            sect_id = in.readInt();
            nickname = in.readString();
            sect_name = in.readString();
            sect_level = in.readInt();
            sect_status = in.readInt();
            nowUserNum = in.readInt();
            sectTotalDefense = in.readInt();
            sectTotalAttack = in.readInt();
        }

        public static final Creator<ListBean> CREATOR = new Creator<ListBean>() {
            @Override
            public ListBean createFromParcel(Parcel in) {
                return new ListBean(in);
            }

            @Override
            public ListBean[] newArray(int size) {
                return new ListBean[size];
            }
        };

        public int getUser_num() {
            return user_num;
        }

        public void setUser_num(int user_num) {
            this.user_num = user_num;
        }

        public int getCreate_user_id() {
            return create_user_id;
        }

        public void setCreate_user_id(int create_user_id) {
            this.create_user_id = create_user_id;
        }

        public int getSect_id() {
            return sect_id;
        }

        public void setSect_id(int sect_id) {
            this.sect_id = sect_id;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getSect_name() {
            return sect_name;
        }

        public void setSect_name(String sect_name) {
            this.sect_name = sect_name;
        }

        public int getSect_level() {
            return sect_level;
        }

        public void setSect_level(int sect_level) {
            this.sect_level = sect_level;
        }

        public int getSect_status() {
            return sect_status;
        }

        public void setSect_status(int sect_status) {
            this.sect_status = sect_status;
        }

        public int getNowUserNum() {
            return nowUserNum;
        }

        public void setNowUserNum(int nowUserNum) {
            this.nowUserNum = nowUserNum;
        }

        public int getSectTotalDefense() {
            return sectTotalDefense;
        }

        public void setSectTotalDefense(int sectTotalDefense) {
            this.sectTotalDefense = sectTotalDefense;
        }

        public int getSectTotalAttack() {
            return sectTotalAttack;
        }

        public void setSectTotalAttack(int sectTotalAttack) {
            this.sectTotalAttack = sectTotalAttack;
        }

        @Override
        public String toString() {
            return "ListBean{" +
                    "user_num=" + user_num +
                    ", create_user_id=" + create_user_id +
                    ", sect_id=" + sect_id +
                    ", nickname='" + nickname + '\'' +
                    ", sect_name='" + sect_name + '\'' +
                    ", sect_level=" + sect_level +
                    ", sect_status=" + sect_status +
                    ", nowUserNum=" + nowUserNum +
                    ", sectTotalDefense=" + sectTotalDefense +
                    ", sectTotalAttack=" + sectTotalAttack +
                    '}';
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(user_num);
            dest.writeInt(create_user_id);
            dest.writeInt(sect_id);
            dest.writeString(nickname);
            dest.writeString(sect_name);
            dest.writeInt(sect_level);
            dest.writeInt(sect_status);
            dest.writeInt(nowUserNum);
            dest.writeInt(sectTotalDefense);
            dest.writeInt(sectTotalAttack);
        }
    }
}
