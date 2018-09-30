package com.sd.storage.model;

/**
 * Created by Administrator on 2018-09-05.
 */

public class WeekModel {

    public   String  name;
    public String  dayid;

    public WeekModel(String name, String dayid) {
        this.name = name;
        this.dayid = dayid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDayid() {
        return dayid;
    }

    public void setDayid(String dayid) {
        this.dayid = dayid;
    }
}
