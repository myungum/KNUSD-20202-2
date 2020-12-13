package com.example.sd2020.Schedule;


import java.io.Serializable;

import me.jlurena.revolvingweekview.WeekViewEvent;

public class Work implements Serializable {
    public enum WORK_TYPE {ONESHOT, DAILY, WEEKLY, MONTHLY};

    private String id;
    private WORK_TYPE workType;
    private String name;
    private int year;
    private int month;
    private int day;
    private String date;
    private int stHour;
    private int stMinute;
    private int edHour;
    private int edMinute;
    private transient WeekViewEvent weekViewEvent = null;
    private transient int week = -1;

    public Work(WORK_TYPE workType, String name, int year, int month, int day, int stHour, int stMinute, int edHour, int edMinute) {
        this.id = name + "|" + System.currentTimeMillis();
        this.workType = workType;
        this.name = name;
        this.year = year;
        this.month = month;
        this.day = day;
        this.date = String.format("%04d-%02d-%02d", year, month, day);
        this.stHour = stHour;
        this.stMinute = stMinute;
        this.edHour = edHour;
        this.edMinute = edMinute;
    }

    public String getId() {return id;}
    public String getDate() {return this.date;}
    public boolean isSame(Work work) {return work.getId().contentEquals(this.id);}
    public boolean isSame(String id) {return id.contentEquals(this.id);}
    public WeekViewEvent getWeekViewEvent(int week) {
        if (weekViewEvent == null || this.week != week) {
            this.week = week;
            weekViewEvent = new WeekViewEvent(this.id, this.name, this.week, this.stHour, this.stMinute, this.week, this.edHour, this.edMinute);
        }

        return weekViewEvent;
    }

}