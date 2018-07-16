package com.example.akb.edaalarm;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.akb.edaalarm.other.Database;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

/**
 * Created by akb on 2018/1/29.
 */

public class AlarmListAdapter extends BaseAdapter {
    Context c;
    int count=0;
    Activity ac;
    static List<AlarmItem> list=new ArrayList<AlarmItem>();
    static List<PendingIntent> plist=new ArrayList<PendingIntent>();
    public AlarmListAdapter(Context con,ArrayList<AlarmItem> ls,Activity a){
        c=con;
        count=ls.size();
        list=ls;
        ac=a;
         Collections.sort(list);
        //Toast.makeText(c,String.valueOf(getCount()).toString(),Toast.LENGTH_SHORT).show();
        notifyDataSetChanged();
    }
    public void refresh(){
        Collections.sort(list);
        notifyDataSetChanged();
        resetalarms(); //重新设置任务
    }

    @Override
    public int getCount() {
        return list.size();
    }
    @Override
    public Object getItem(int position) {
        return list.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    private void addnewtolist(int m,int h,String str){
        AlarmItem t=new AlarmItem(h,m,true,str);

        //插入防重复代码
        //if(list.containsKey(t)) return;;
        list.add(t);
        count++;
    }

    public boolean addnew(int m,int h,String str){
        AlarmItem t=new AlarmItem(h,m,true,str);

        //插入防重复代码
        if(Database.isAlarmExist(t)) {
            return false;
        }
        //if(list.containsKey(t)) return;;
        list.add(t);
        count++;
        Database.AddAlarmrecord(h,m,true,str);
        refresh();
        notifyDataSetChanged();
        resetalarms();


        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());


        c.set(Calendar.HOUR_OF_DAY,h);
        c.set(Calendar.MINUTE,m);
        c.set(Calendar.SECOND,0);
        long dist=(c.getTimeInMillis()-System.currentTimeMillis())/1000;
        long h1,m1,s1;
        h1=dist/3600;
        dist-=3600*h1;
        m1=dist/60;
        dist-=60*m1;
        s1=dist;
        Toast.makeText(ac,"添加闹钟成功:"+(h1==0?"":h1+"h")+(  m1==0   ?"":m1+"m")+s1+"s"+ "后响铃",Toast.LENGTH_SHORT).show();
        return true;
    }
    void resetalarms(){

        for(int j=0;j<plist.size();j++){
            plist.get(j).cancel(); //取消所有闹钟

        }
        plist.clear();
        //重新设置
        for(int i=0;i<list.size();i++){
            AlarmManager alarmManager=(AlarmManager)ac.getSystemService(Context.ALARM_SERVICE);
            Intent intent=new Intent(ac, Alarmboastcast.class);
            //设置当前时间
            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(System.currentTimeMillis());
            // 根据用户选择的时间来设置Calendar对象

            c.set(Calendar.HOUR_OF_DAY,list.get(i).h);
            c.set(Calendar.MINUTE, list.get(i).m);
            c.set(Calendar.SECOND,0);
            Alarmboastcast ab=new Alarmboastcast();

            if((c.getTimeInMillis()-System.currentTimeMillis())<=0){
                c.add(Calendar.DAY_OF_MONTH,1);
            }

            PendingIntent pi=PendingIntent.getBroadcast(ac,i,intent,0);
            plist.add(pi);
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(),pi);

        }





        //Toast.makeText(getContext(),"添加闹钟成功:"+(h1==0?"":h1+"h")+(  m1==0   ?"":m1+"m")+s1+"s"+ "后响铃",Toast.LENGTH_SHORT).show();
    }
    public void del(int po){
        list.remove(po);
        count--;
        notifyDataSetChanged();
    }
    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        if (null == view)
            view = LayoutInflater.from(c).inflate(R.layout.alarm_list_element, null);
        final View v=view;
        final CheckBox cb=(CheckBox)view.findViewById(R.id.checkBox_alarm_active);
        final TextView text_time = (TextView) view.findViewById(R.id.ale_timeview);
        final TextView text_type = (TextView) view.findViewById(R.id.ale_type);
        text_type.setText(list.get(position).str);
        cb.setChecked(list.get(position).enable);
        text_time.setText(list.get(position).toString());
        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                AlarmItem t_ai=new AlarmItem(text_time.getText().toString()+":"+text_type.getText().toString()+":"+(cb.isChecked()?"1":"0"));
               Database.setenable(t_ai,cb.isChecked());
                list.get(position).enable=cb.isChecked();
                //Toast.makeText(v.getContext(),Database.setenable(t_ai,cb.isChecked())?"success":"F", Toast.LENGTH_SHORT).show();
            }
        });
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cb.setChecked(!cb.isChecked());

                //Database.setenable(t_ai,!cb.isChecked());
            }
        });
        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                new AlertDialog.Builder(view.getContext()).setTitle("Inf").setMessage("确定要删除此闹钟?  Time="+list.get(position).toString()).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        del(position);
                        AlarmItem t_ai=new AlarmItem(text_time.getText().toString()+":"+text_type.getText().toString()+":"+(cb.isChecked()?"1":"0"));
                        Database.deletealarm(t_ai);
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).create().show();
                return false;
            }
        });



        return view;
    }
}
