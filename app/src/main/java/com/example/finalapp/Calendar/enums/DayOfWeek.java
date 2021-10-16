package com.example.finalapp.Calendar.enums;

import com.example.finalapp.R;

public enum DayOfWeek {
    SUN(1, R.id.sun,"Sun"),
    MON(2,R.id.mon,"Mon"),
    TUE(3,R.id.tue,"Tue"),
    WED(4,R.id.wed,"Wed"),
    THUR(5,R.id.thur,"Thur"),
    FRI(6,R.id.fri,"Fri"),
    SAT(7,R.id.sat,"Sat");


    private int code;
    private int viewId;
    private String textContent;
    DayOfWeek(int code, int viewId, String textContent){
        this.code = code;
        this.viewId = viewId;
        this.textContent = textContent;
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

    public static DayOfWeek valueOf(int code) {
        for (DayOfWeek dayOfWeek : values()) {
            if (dayOfWeek.code==code) {
                return dayOfWeek;
            }
        }
        throw new IllegalArgumentException(String.valueOf(code));
    }
}
