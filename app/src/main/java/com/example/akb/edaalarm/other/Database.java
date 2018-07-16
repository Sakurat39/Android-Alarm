package com.example.akb.edaalarm.other;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import com.example.akb.edaalarm.AlarmItem;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by akb on 2018/2/1.
 */

public class Database {

    public static String ALARMCONFIG=Environment
            .getExternalStorageDirectory().getPath() + "/EdaAlarm/AlarmItems" + "/";
    Context con;
    public Database(Context c){
        con=c;
    }
    public static void AddAlarmrecord(int h,int m,boolean isEnb,String typestr){
        try{
            File dir = new File(ALARMCONFIG);
            if (!dir.exists()) {
                dir.mkdirs();
            }
        FileOutputStream fos = new FileOutputStream(ALARMCONFIG+"alarmitems.txt",true);

            fos.write(("<Alarm>"+Alarm_inttostr(h,m)+":"+typestr+":"+(isEnb?"1":"0")+"</Alarm>").getBytes());
            fos.close();
        }catch (Exception e){

        }
    }
    public static void ResetAlarmrecord(int h,int m,Boolean isEnb,String typestr){
        try{
            File dir = new File(ALARMCONFIG);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            FileOutputStream fos = new FileOutputStream(ALARMCONFIG+"alarmitems.txt");
            fos.write(("<Alarm>"+Alarm_inttostr(h,m)+":"+typestr+":"+(isEnb?"1":"0")+"</Alarm>").getBytes());
            fos.close();
        }catch (Exception e){

        }
    }
    public static boolean setenable(AlarmItem ai,Boolean en){
        File f=new File(ALARMCONFIG+"alarmitems.txt");

        if(!f.exists()) return false;
        try {
            FileInputStream fis = new FileInputStream(f);
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));
            String text = "";
            String temp_st;
            while ((temp_st = br.readLine()) != null) {
                text += temp_st;
            }
            int left=text.indexOf( "<Alarm>"+Alarm_inttostr(ai.h,ai.m));
            if(left<0) return false;
            int right=text.indexOf("</Alarm>",left);
            String re;
            re=text.substring(0,right-1);
            re+=en?"1":"0";
            re+=text.substring(right);

            br.close();
            fis.close();

            FileOutputStream fos=new FileOutputStream(f);
            fos.write(re.getBytes());
            fos.close();
            return true;
        }catch (Exception e){
            return false;
        }
        }
    public  ArrayList<AlarmItem> getAlarms(){
         ArrayList<AlarmItem> result=new ArrayList<AlarmItem>();
        try{
            File f=new File(ALARMCONFIG+"alarmitems.txt");
            if(!f.exists()) return result;

            FileInputStream fis = new FileInputStream(f);
            BufferedReader br=new BufferedReader(new InputStreamReader(fis));
            String text="";
            String temp_st;
            while ((temp_st=br.readLine()) != null) {
                text+=temp_st;
            }
            List<String> alarms=getallelements(text,"Alarm");

            for(int i=0;i<alarms.size();i++){
                String[] tempspl=alarms.get(i).split(":");
                AlarmItem t_ai=new AlarmItem(Integer.valueOf(tempspl[0]),Integer.valueOf(tempspl[1]),tempspl[3].compareTo("1")==0?true:false,tempspl[2]);
                result.add(t_ai);
            }
            fis.close();
            //Toast.makeText(con,String.valueOf(result.size()),Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            Toast.makeText(con,e.toString(), Toast.LENGTH_SHORT).show();
        }
        return result;
    }

    public static boolean rewritealarmsfile(List<AlarmItem> alarms){
        try{
            File dir = new File(ALARMCONFIG);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            FileOutputStream fos = new FileOutputStream(ALARMCONFIG+"alarmitems.txt");
            for(int i=0;i<alarms.size();i++){
                fos.write(("<Alarm>"+Alarm_inttostr(alarms.get(i).h,alarms.get(i).m)+":"+alarms.get(i).str+":"+(alarms.get(i).enable?"1":"0")+"</Alarm>").getBytes());
            }

            fos.close();
        }catch (Exception e){
            return false;
        }
        return true;
    }
    public  static boolean deletealarm(AlarmItem delalarm){
        File f=new File(ALARMCONFIG+"alarmitems.txt");

        if(!f.exists()) return false;
        try {
            FileInputStream fis = new FileInputStream(f);
            BufferedReader br=new BufferedReader(new InputStreamReader(fis));
            String text="";
            String temp_st;
            while ((temp_st=br.readLine()) != null) {
                text+=temp_st;
            }

            int left=text.indexOf( "<Alarm>"+Alarm_inttostr(delalarm.h,delalarm.m));
            if(left<0) return false;
            int right=text.indexOf("</Alarm>",left);
            String re=text.substring(0,left);
            re+=text.substring(right+8);
            br.close();
            fis.close();

            //写入

            FileOutputStream fos=new FileOutputStream(f);
            fos.write(re.getBytes());
            fos.close();
            return true;

        }catch (Exception e){
            return false;
        }

        //return modifyFileContent(ALARMCONFIG+"alarmitems.txt","<Alarm>"+Alarm_inttostr(delalarm.h,delalarm.m),"");

    }
    public static boolean isAlarmExist(AlarmItem aalarm){
        File f=new File(ALARMCONFIG+"alarmitems.txt");

        if(!f.exists()) return false;
        try {
            FileInputStream fis = new FileInputStream(f);
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));
            String text = "";
            String temp_st;
            while ((temp_st = br.readLine()) != null) {
                text += temp_st;
            }
            int left=text.indexOf( "<Alarm>"+Alarm_inttostr(aalarm.h,aalarm.m));
            if(left<0) return false;

        }catch (Exception e){
            return false;

        }
        return true;
    }
    private static boolean modifyFileContent(String filewholepath, String oldstr, String newStr) {
        RandomAccessFile raf = null;
        try {
            raf = new RandomAccessFile(filewholepath, "rw");
            String line = null;
            long lastPoint = 0; //记住上一次的偏移量
            while ((line = raf.readLine()) != null) {
                final long ponit = raf.getFilePointer();
                if(line.contains(oldstr)){
                    String str=line.replace(oldstr, newStr);
                    raf.seek(lastPoint);
                    raf.writeBytes(str);
                }
                lastPoint = ponit;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                raf.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }
    public static List<String> getallelements(String alltext,String head){
        ArrayList<String> temp=new ArrayList<String>();
        int po=0;
        int l,r;
        while (true){
        l=alltext.indexOf("<"+head+">",po);
        r=alltext.indexOf("</"+head+">",l+head.length()+2);
         if(l>=0 && r>=0 && (l+head.length()+2)<r){
            temp.add( alltext.substring(l+head.length()+2,r));
             po=r+head.length()+2;
         }else {return temp;}
        }
    }
    public static String getmidstring(String allstr,String left,String right){
        int l=allstr.indexOf(left);
        int r=allstr.indexOf(right,l+left.length());
        if(l>=0 && r>=0 && (l+left.length())<r){
            return allstr.substring(l+left.length(),r);
        }
        return "";
    }

    public static boolean isExist(String path){
        try
        {
            File f=new File(path);
            if(!f.exists())
            {
                return false;
            }

        }
        catch (Exception e)
        {
            return false;
        }

        return true;

    }
    public static String Alarm_inttostr(int h,int m){
        return String.format("%02d",h)+":"+String.format("%02d",m);
    }
}
