package com.example.akb.edaalarm;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.akb.edaalarm.other.Database;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by akb on 2018/1/28.
 */
@SuppressLint("ValidFragment")
public class alarmfrg extends Fragment {
    View rootView;
    MainActivity ma;
    Handler handler;
    Timer timer_now=new Timer();
    Button btn_add;
    Calendar calendar;

    ListView mathAlarmListView;
    AlarmListAdapter mAdapter;
    static HashMap<time_foralarm,String> ls=new HashMap<time_foralarm, String>();
    TextView t;
    Button add;
    public alarmfrg(MainActivity m){
        ma=m;
    }
    public alarmfrg(){

    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        rootView= inflater.inflate(R.layout.alarmfragment, container, false);


        return inflater.inflate(R.layout.alarmfragment,container,false);
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

    }


    public void addnewalrmitem(int h,int m,String str){
        if(!mAdapter.addnew(m,h,str)){return;};
        //添加闹钟
        AlarmManager alarmManager=(AlarmManager)getActivity().getSystemService(Context.ALARM_SERVICE);
        Intent intent=new Intent(ma, Alarmboastcast.class);
        //设置当前时间
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());
        // 根据用户选择的时间来设置Calendar对象

        c.set(Calendar.HOUR_OF_DAY,h);
        c.set(Calendar.MINUTE, m);
        c.set(Calendar.SECOND,0);
        Alarmboastcast ab=new Alarmboastcast();

        if((c.getTimeInMillis()-System.currentTimeMillis())<=0){
            c.add(Calendar.DAY_OF_MONTH,1);
        }
        PendingIntent pi=PendingIntent.getBroadcast(ma,9,intent,0);

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(),pi);
        long dist=(c.getTimeInMillis()-System.currentTimeMillis())/1000;
        long h1,m1,s1;
        h1=dist/3600;
        dist-=3600*h1;
        m1=dist/60;
        dist-=60*m1;
        s1=dist;
        Toast.makeText(getContext(),"添加闹钟成功:"+(h1==0?"":h1+"h")+(  m1==0   ?"":m1+"m")+s1+"s"+ "后响铃",Toast.LENGTH_SHORT).show();
        //Toast.makeText(getContext(),"添加闹钟成功:"+(h1==0?"":h1+"h")+(  m1==0   ?"":m1+"m")+s1+"s"+ "后响铃",Toast.LENGTH_SHORT).show();

    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState){

        super.onActivityCreated(savedInstanceState);
        t=getActivity().findViewById(R.id.time_now);
        //设置list与按钮监听
        SetAddButton();
        //显示实时时间
        calendar = Calendar.getInstance();
        handler=new Handler();
        timer_now.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Date d=(new Date());
                        t.setText(String.format("%02d",d.getHours())+":"+String.format("%02d",d.getMinutes())+":"+String.format("%02d",d.getSeconds()));

                    }
                });
            }
        },0,500);
        Button synbtn=(Button)getActivity().findViewById(R.id.alarm_synbtn);
        synbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!Login.isLogined){
                    Toast.makeText(getContext(),"未登入",Toast.LENGTH_SHORT).show();
                    return;
                }
                List<AlarmItem> li=new Login(getContext()).getalarmsfromdatabase(Login.account);

                AlarmListAdapter.list=li;

                mAdapter.refresh();
                if(Database.rewritealarmsfile(li)){
                Toast.makeText(getContext(),"同步完成",Toast.LENGTH_SHORT).show();}
            }
        });
        Database db=new Database(getContext());

        ArrayList<AlarmItem> t_alarmItems=db.getAlarms();


        mAdapter = new AlarmListAdapter(this.getContext(),t_alarmItems,getActivity());
        mathAlarmListView=(ListView)getActivity().findViewById(R.id.alarmlistView);
        mathAlarmListView.setAdapter(mAdapter);


    }

    void SetAddButton(){
        add = (Button) getActivity().findViewById(R.id.alarm_add);
        if (add != null) {
            add.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {   //启动设置界面
                    startActivityForResult(new Intent(ma,com.example.akb.edaalarm.activity.addalarmactivity.class),1);
//                    startActivity(newAlarmIntent);
                }

            });
        }
    }
}
