package com.cartoon.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by cc on 17-10-20.
 */
public class SectTask implements Parcelable {
    /**
     * contributions : 10
     * taskList : null
     * taskName : 签到
     * isFinished : 1
     * exp : 0
     * userId : null
     */

    private int    contributions;
    private List<TaskList>   taskList;
    private String taskName;
    private int    isFinished;
    private int    exp;
    private int    userId;

    public SectTask() {
    }

    protected SectTask(Parcel in) {
        contributions = in.readInt();
        taskName = in.readString();
        isFinished = in.readInt();
        exp = in.readInt();
        userId = in.readInt();
    }

    public static final Creator<SectTask> CREATOR = new Creator<SectTask>() {
        @Override
        public SectTask createFromParcel(Parcel in) {
            return new SectTask(in);
        }

        @Override
        public SectTask[] newArray(int size) {
            return new SectTask[size];
        }
    };

    public List<TaskList> getTaskList() {
        return taskList;
    }

    public void setTaskList(List<TaskList> taskList) {
        this.taskList = taskList;
    }

    public int getContributions() {
        return contributions;
    }

    public void setContributions(int contributions) {
        this.contributions = contributions;
    }



    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public int getIsFinished() {
        return isFinished;
    }

    public void setIsFinished(int isFinished) {
        this.isFinished = isFinished;
    }

    public int getExp() {
        return exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }



    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(contributions);
        dest.writeString(taskName);
        dest.writeInt(isFinished);
        dest.writeInt(exp);
        dest.writeInt(userId);
    }

    @Override
    public String toString() {
        return "SectTask{" +
                "contributions=" + contributions +
                ", taskList=" + taskList +
                ", taskName='" + taskName + '\'' +
                ", isFinished=" + isFinished +
                ", exp=" + exp +
                ", userId=" + userId +
                '}';
    }

    public class TaskList{
       /*"action_name": "评论和发帖",
                    "limit": 1,
                    "buy_stone": 2,
                    "id": 6,
                    "isFinished": 0, //0=没有完成 1=完成*/
       private String action_name;
       private int isFinished;

       public String getAction_name() {
           return action_name;
       }

       public void setAction_name(String action_name) {
           this.action_name = action_name;
       }

       public int getIsFinished() {
           return isFinished;
       }

       public void setIsFinished(int isFinished) {
           this.isFinished = isFinished;
       }

       @Override
       public String toString() {
           return "TaskList{" +
                   "action_name='" + action_name + '\'' +
                   ", isFinished=" + isFinished +
                   '}';
       }
   }


}
