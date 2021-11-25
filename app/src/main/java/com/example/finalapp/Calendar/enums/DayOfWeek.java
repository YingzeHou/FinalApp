package com.example.finalapp.Calendar.enums;

import com.example.finalapp.R;

public enum DayOfWeek {
    SUN(1, R.id.sun,"Sun", R.id.weekPanel_7),
    MON(2,R.id.mon,"Mon",R.id.weekPanel_1),
    TUE(3,R.id.tue,"Tue",R.id.weekPanel_2),
    WED(4,R.id.wed,"Wed",R.id.weekPanel_3),
    THUR(5,R.id.thur,"Thur",R.id.weekPanel_4),
    FRI(6,R.id.fri,"Fri",R.id.weekPanel_5),
    SAT(7,R.id.sat,"Sat",R.id.weekPanel_6);


    private int code;
    private int viewId;
    private String textContent;
    private int weekPanelId;

    DayOfWeek(int code, int viewId, String textContent, int weekPanelId){
        this.code = code;
        this.viewId = viewId;
        this.textContent = textContent;
        this.weekPanelId = weekPanelId;
    }

    public String getTextContent() {
        return textContent;
    }

    public int getViewId() {
        return viewId;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setViewId(int viewId) {
        this.viewId = viewId;
    }

    public void setTextContent(String textContent) {
        this.textContent = textContent;
    }

    public int getWeekPanelId() {
        return weekPanelId;
    }

    public void setWeekPanelId(int weekPanelId) {
        this.weekPanelId = weekPanelId;
    }

    public static DayOfWeek valueOf(int code) {
        for (DayOfWeek dayOfWeek : values()) {
            if (dayOfWeek.code==code) {
                return dayOfWeek;
            }
        }
        //throw new IllegalArgumentException(String.valueOf(code));
        return null;
    }
}
