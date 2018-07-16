package com.example.akb.edaalarm;

import java.text.SimpleDateFormat;

/**
 * Created by akb on 2018/1/29.
 */

public class time_foralarm{


    int minute;
    int hour;
    time_foralarm(int m,int h){

        minute=m;

        hour=h;
    }

    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return String.format("%02d",hour)+":"+String.format("%02d",minute);
    }

    public boolean equals(Object obj) {
        if((((time_foralarm)obj).hour==hour) && (((time_foralarm)obj).minute==minute) ) return true;
        return false;
    }
}
