package com.example.akb.edaalarm;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by akb on 2018/1/28.
 */

public class timingfrg extends Fragment {
    View rootView;
    int sec=0,minute=0,hour=0,ms=0;
    private Handler handler=null;
    TextView timeview;
    ListView listview;
    TextView mstimeview;
    static List<time> ls=new ArrayList<time>();
    private Timer timingtimer;
    private boolean isStarted=false;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        rootView= inflater.inflate(R.layout.timingfragment, container, false);


        return inflater.inflate(R.layout.timingfragment,container,false);
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        handler=new Handler();
        super.onActivityCreated(savedInstanceState);
        final Button bstart=getActivity().findViewById(R.id.button_t_start);
        Button b_re0=getActivity().findViewById(R.id.btn_re0);
        Button b_re1=getActivity().findViewById(R.id.btn_re1);
        timeview=getActivity().findViewById(R.id.timeviewer);
        mstimeview=getActivity().findViewById(R.id.mstime);
        listview=getActivity().findViewById(R.id.recview);

        final TimingListAdapter mAdapter = new TimingListAdapter(this.getContext(),ls);
        listview.setAdapter(mAdapter);
        b_re1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAdapter.addnew(ms,sec,minute,hour);



            }
        });
        b_re0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ms = 0;
                minute = 0;
                hour = 0;
                sec = 0;
                showtime();
            }
        });
        bstart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isStarted && timingtimer!=null){ //停止
                    timingtimer.cancel();
                    isStarted=false;
                    bstart.setBackgroundResource(R.drawable.timingstartbg);
                    bstart.setText("Start");
                }else if(!isStarted){
                    timingtimer=new Timer();
                    bstart.setText("Stop");
                    isStarted=true;
                    bstart.setBackgroundResource(R.drawable.timingstartbg2);
                    timingtimer.schedule(new TimerTask() {
                        @Override
                        public void run() {

                            ms+=10;
                            if(ms>=1000){
                                ms=0;
                                sec++;
                            }
                            if(sec>=60){
                                sec=0;
                                minute++;
                            }
                            if(minute>=60){
                                minute=0;
                                hour++;
                            }
                            showtime();
                        }
                    }, 0,10 );

                }
            }
        });
        setHasOptionsMenu(true);


    }

    private void showtime(){

        handler.post(new Runnable() {
            @Override
            public void run() {

                timeview.setText(String.format("%02d",hour)+":"+String.format("%02d",minute)+":"+String.format("%02d",sec));
                mstimeview.setText(String.format("%03d",ms));
            }
        });
    }
}

class MyAdapter extends BaseAdapter {

    static List<time> list;
    int count=0;
    int ms=0,minute=0,sec=0,hour=0;
    Context c;
    MyAdapter(Context con,List<time> ls){
        c=con;
        list=ls;
        count=ls.size();

    }
    @Override

    public int getCount(){
        return count;
    }
    @Override
    public Object getItem(int position){
        return position;
    }
    @Override
    public long getItemId(int position){
        return position;
    }
    public void del(int i){
        count--;
        list.remove(i);
        notifyDataSetChanged();
    }
    public void addnew(int mms,int s,int m,int h){
        time t=new time(mms,m,s,h,new Date());
        list.add(t);
        count++;
        notifyDataSetChanged();
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        final int p=position;
        TextView result = (TextView) convertView;
        //动态创建TextView添加早ListView中
        if (result == null)
        {
            result = new TextView(c);
            result.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showDialogForMessage(p);
                }
            });
            result.setTextAppearance(c, android.R.style.TextAppearance_Large);
            AbsListView.LayoutParams layoutParams = new AbsListView.LayoutParams
                    (AbsListView.LayoutParams.MATCH_PARENT,
                            AbsListView.LayoutParams.MATCH_PARENT);
            result.setLayoutParams(layoutParams);
            result.setGravity(Gravity.CENTER);
        }


        result.setText(list.get(position).toString());
        return result;
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