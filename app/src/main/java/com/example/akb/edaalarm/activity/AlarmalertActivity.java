package com.example.akb.edaalarm.activity;

import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.akb.edaalarm.AlarmItem;
import com.example.akb.edaalarm.FontPic;
import com.example.akb.edaalarm.R;
import com.example.akb.edaalarm.other.Database;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.Random;

/**
 * Created by akb on 2018/1/30.
 */

public class AlarmalertActivity extends AppCompatActivity {
    Operationquestion op=new Operationquestion();
    public static final int DISABLE_EXPAND = 0x00010000;//4.2以上的整形标识
    public static final int DISABLE_EXPAND_LOW = 0x00000001;//4.2以下的整形标识
    public static final int DISABLE_NONE = 0x00000000;//取消StatusBar所有disable属性，即还原到最最原始状态
    public static final int FLAG_HOMEKEY_DISPATCHED = 0x80000000;
    private MediaPlayer mMediaPlayer;
    Calendar cal=Calendar.getInstance();
    private void stopAlarm(){
        mMediaPlayer.stop();
    }
    private Uri getSystemDefultRingtoneUri() {
        return RingtoneManager.getActualDefaultRingtoneUri(this,
                RingtoneManager.TYPE_RINGTONE);
    }
    private void setStatusBarDisable(int disable_status) {//调用statusBar的disable方法
        Object service = getSystemService("statusbar");
        try {
            Class<?> statusBarManager = Class.forName
                    ("android.app.StatusBarManager");
            Method expand = statusBarManager.getMethod("disable", int.class);
            expand.invoke(service, disable_status);
        } catch (Exception e) {
            unBanStatusBar();
            e.printStackTrace();
        }
    }
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        banStatusBar();
    }
    @Override
    public void onAttachedToWindow() {
        //关键：在onAttachedToWindow中设置FLAG_HOMEKEY_DISPATCHED
        this.getWindow().addFlags(FLAG_HOMEKEY_DISPATCHED);

        super.onAttachedToWindow();
    }
    @Override
    protected void onPause() {
        super.onPause();
        unBanStatusBar();
    }
    private void unBanStatusBar() {//利用反射解除状态栏禁止下拉
        Object service = getSystemService("statusbar");
        try {
            Class<?> statusBarManager = Class.forName
                    ("android.app.StatusBarManager");
            Method expand = statusBarManager.getMethod("disable", int.class);
            expand.invoke(service, DISABLE_NONE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void banStatusBar() {//禁止statusbar下拉，适配了高低版本
        int currentApiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentApiVersion <= 16) {
            setStatusBarDisable(DISABLE_EXPAND_LOW);
        } else {
            setStatusBarDisable(DISABLE_EXPAND);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarmalert);
        getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED|               //这个在锁屏状态下
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                        | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                        | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);



        AlarmItem ai=new AlarmItem(cal.get(Calendar.HOUR_OF_DAY),cal.get(Calendar.MINUTE),false,"");
        final ImageView iv=(ImageView)findViewById(R.id.imgview1);
        iv.setImageBitmap(op.getFouroperationPic(0));
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iv.setImageBitmap(op.getFouroperationPic(0));
            }
        });
        Button b1=(Button)findViewById(R.id.btn_cancel);
        final EditText et=(EditText)findViewById(R.id.editText);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    if(Integer.valueOf(et.getText().toString())==op.answer){
                        stopAlarm();
                        Database.setenable(ai,false);
                        System.exit(0);
                    }
                    else{
                        Toast.makeText(AlarmalertActivity.this,"答案错误",Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){
                    Toast.makeText(AlarmalertActivity.this,"答案错误",Toast.LENGTH_SHORT).show();
                }

            }
        });
        startAlarm();

    }
    private void startAlarm() {
        mMediaPlayer = MediaPlayer.create(this, getSystemDefultRingtoneUri());
        mMediaPlayer.setLooping(true);
        try {
            mMediaPlayer.prepare();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mMediaPlayer.start();
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return disableKeycode(keyCode, event);
    }
    private boolean disableKeycode(int keyCode, KeyEvent event) {
        return true;
    }
}

class Operationquestion {
    int answer;
    Random random = new Random();
    public Bitmap getFouroperationPic(int level) {
        //乘法10以内  加法100以内  3个数
        String[] op=new String[2];
        int[] nums=new int[3];
        int r1=1+(int)(Math.random()*4);
        int r2=1+(int)(Math.random()*4);
        if(r1==1){
            op[0]="+";
        }else if(r1==2){
            op[0]="*";
        }else if(r1==3){
            op[0]="-";
        }else if(r1==4){
            op[0]="÷";
        }

        if(r2==1){
            op[1]="+";
        }else if(r2==2){
            op[1]="*";
        }else if(r2==3){
            op[1]="-";
        }else if(r2==4){
            op[1]="÷";
        }
        if(r1==1 || r1==3 || r1==4)  //+ - /
        {
            nums[0]=1+(int)(Math.random()*100);
            if(r1!=4){
                nums[1]=1+(int)(Math.random()*100);
            }else{
                nums[1]=1+(int)(Math.random()*10);
            }
        }else{
            nums[0]=1+(int)(Math.random()*10);
            nums[1]=1+(int)(Math.random()*10);
        }

        if(r2==1 || r2==3)  //+ - /
        {
            nums[2]=1+(int)(Math.random()*100);
        }else if(r2==2){
            nums[1]=1+(int)(Math.random()*10);
            nums[2]=1+(int)(Math.random()*10);
        }else {
            nums[1]=1+(int)(Math.random()*100);
            nums[2]=1+(int)(Math.random()*10);
        }

        //answer
        int re=0;
        if(r1==2 || r1==4){ //x
            if(r1==2){
                re=nums[0]*nums[1];
            }else {
                re=nums[0]/nums[1];
            }

            switch (r2){
                case 1:re+=nums[2]; break;
                case 2:re*=nums[2]; break;
                case 3:re-=nums[2]; break;
                case 4:re/=nums[2]; break;
            }
        }else if(r2==2 || r2==4){
            if(r2==2){
                re=nums[1]*nums[2];
            }else {
                re=nums[1]/nums[2];
            }
            switch (r1){
                case 1:re=nums[0]+re; break;
                case 2:re=nums[0]*re; break;
                case 3:re=nums[0]-re; break;
                case 4:re=nums[0]/re; break;
            }
        }else{
            switch (r1){
                case 1:re=nums[0]+nums[1]; break;
                case 3:re=nums[0]-nums[1]; break;
            }
            switch (r2){
                case 1:re=re+nums[2]; break;
                case 3:re=re-nums[2]; break;
            }

        }
        answer=re;
        return FontPic.getInstance().createBitmap(String.valueOf(nums[0])+op[0]+String.valueOf(nums[1])+op[1]+String.valueOf(nums[2])+"=?");

    }
}