package com.lsm.barrister.data.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lvshimin on 16/5/8.
 * 预约设置
 * key:addTime yyyy-MM-dd
 * value:0,1,0,1……对应36个时段的状态
 */
public class AppointmentSetting implements Serializable{

    String date;
    String settings;
    List<HourItem> hours;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSettings() {
        return settings;
    }

    public void setSettings(String settings) {
        this.settings = settings;
    }

    public List<HourItem> getHours() {
        return hours;
    }

    public void setHours(List<HourItem> hours) {
        this.hours = hours;
    }

    public static class HourItem implements Serializable {

        String hour;
        boolean isEnable;

        public String getHour() {
            return hour;
        }

        public void setHour(String hour) {
            this.hour = hour;
        }

        public boolean isEnable() {
            return isEnable;
        }

        public void setEnable(boolean enable) {
            isEnable = enable;
        }
    }
}
