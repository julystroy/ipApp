package com.cartoon.data;

import java.util.List;

/**
 * Created by debuggerx on 16-11-21.
 */
public class Designs {

    public int code;

    public String ver;

    public List<Design> designs;




    public int getCode() {
        return code;
    }
    public void setCode(int code) {
        this.code = code;
    }

    public String getVer() {
        return ver;
    }
    public void setVer(String ver) {
        this.ver = ver;
    }


    public List<Design> getDesign() {
        return designs;
    }
    public void setDesign(List<Design> designs) {
        this.designs = designs;
    }

    @Override
    public String toString() {
        return "Designs{" +
                "code=" + code +
                ", ver='" + ver + '\'' +
                ", designs=" + designs +
                '}';
    }
}
