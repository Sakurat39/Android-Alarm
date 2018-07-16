package com.example.akb.edaalarm;

import java.text.SimpleDateFormat;

/**
 * Created by akb on 2018/2/1.
 */

public class AlarmItem implements Comparable<AlarmItem>{
    public int h, m;
    public boolean enable;
    public String str;
    public AlarmItem(int hour,int minute,Boolean eb,String s){
        h=hour;
        m=minute;
        str=s;
        enable=eb;
    }
    public AlarmItem(String formatstr){
        String[] strspl=formatstr.split(":");
        try {
            h=Integer.valueOf(strspl[0]);
            m=Integer.valueOf(strspl[1]);
            str= strspl[2];
            enable=((strspl[3].compareTo("1")==0)?true:false);
        }catch (Exception e){
            h=0;
            m=0;
            str="";
            enable=true;

        }

    }
    @Override
    public int compareTo(AlarmItem a2){

        if(h>a2.h) return 1;
        if(h<a2.h) return -1;
        if(h==a2.h){
            if(m<a2.m) return -1;
            else if(m>a2.m)return 1;

        }
        return 0;
    }
    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return String.format("%02d",h)+":"+String.format("%02d",m);
    }
}
