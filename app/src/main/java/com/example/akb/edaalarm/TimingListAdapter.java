package com.example.akb.edaalarm;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by akb on 2018/1/28.
 */

public class TimingListAdapter extends BaseAdapter {
    static List<time> list;
    int count=0;
    int ms=0,minute=0,sec=0,hour=0;
    Context c;
    public  TimingListAdapter(Context con,List<time> ls){
        c=con;
        list=ls;
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
    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        if (null == view)
            view = LayoutInflater.from(c).inflate(R.layout.timing_list_elemt, null);


        TextView textv = (TextView) view.findViewById(R.id.timinglisttext);
        textv.setText(list.get(position).toString());
        textv.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                showDialogForMessage(position);
                return false;
            }});




        return view;
    }
    public void addnew(int mms,int s,int m,int h){
        time t=new time(mms,m,s,h,new Date());
        list.add(t);
        count++;
        notifyDataSetChanged();
    }
    public void del(int i){
        count--;
        list.remove(i);
        notifyDataSetChanged();
    }
    void showDialogForMessage(final int i) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(c);
        dialog.setTitle(String.valueOf(i)).setMessage(list.get(i).toString()).setPositiveButton("删除", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int k) {
                del(i);
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }).create().show();
    }
}
class time{

    int ms;
    int minute;
    int sec;
    int hour;
    Date da;
    time(int mms,int m,int s,int h,Date d){
        ms=mms;
        minute=m;
        sec=s;
        hour=h;
        da=d;
    }

    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return "["+ sdf.format(da)+"]\r\n"+String.format("%02d",hour)+":"+String.format("%02d",minute)+":"+String.format("%02d",sec)+"."+String.format("%03d",ms)+"ms";
    }
}
