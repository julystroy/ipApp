package com.cartoon.data;

/**
 * Created by debuggerx on 16-11-21.
 */


public class Design {

    public Design(){
    }

    private int level;

    private int config;

    private int type;

    private String name;

    private int position;

    private String api;

    private String contentapi;

    public void setLevel(int level){
        this.level = level;
    }
    public int getLevel(){
        return this.level;
    }
    public void setConfig(int config){
        this.config = config;
    }
    public int getConfig(){
        return this.config;
    }
    public void setType(int type){
        this.type = type;
    }
    public int getType(){
        return this.type;
    }
    public void setName(String name){
        this.name = name;
    }
    public String getName(){
        return this.name;
    }
    public void setPosition(int position){
        this.position = position;
    }
    public int getPosition(){
        return this.position;
    }
    public void setApi(String api){
        this.api = api;
    }
    public String getApi(){
        return this.api;
    }
    public void setContentapi(String contentapi){
        this.contentapi = contentapi;
    }
    public String getContentapi(){
        return this.contentapi;
    }


    @Override
    public String toString() {
        return "Design{" +
                "level=" + level +
                ", config=" + config +
                ", type=" + type +
                ", name='" + name + '\'' +
                ", position=" + position +
                ", api='" + api + '\'' +
                ", contentapi='" + contentapi + '\'' +
                '}';
    }
}
