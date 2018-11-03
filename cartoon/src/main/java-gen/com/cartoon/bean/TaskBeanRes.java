package com.cartoon.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by cc on 17-1-6.
 */
public class TaskBeanRes implements Parcelable{

    private List<TaskBean> tasks;

    private List<String> types;

    protected TaskBeanRes(Parcel in) {
        tasks = in.createTypedArrayList(TaskBean.CREATOR);
        types = in.createStringArrayList();
    }

    public TaskBeanRes() {
    }

    public static final Creator<TaskBeanRes> CREATOR = new Creator<TaskBeanRes>() {
        @Override
        public TaskBeanRes createFromParcel(Parcel in) {
            return new TaskBeanRes(in);
        }

        @Override
        public TaskBeanRes[] newArray(int size) {
            return new TaskBeanRes[size];
        }
    };

    public List<TaskBean> getTasks() {
        return tasks;
    }

    public void setTasks(List<TaskBean> tasks) {
        this.tasks = tasks;
    }

    public List<String> getTypes() {
        return types;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }

    @Override
    public String toString() {
        return "TaskBeanRes{" +
                "tasks=" + tasks +
                ", types=" + types +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(tasks);
        dest.writeStringList(types);
    }
}
