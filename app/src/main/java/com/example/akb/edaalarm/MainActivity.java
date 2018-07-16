package com.example.akb.edaalarm;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.akb.edaalarm.service.CrashHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity{
    private boolean isExit;
    Fragment f1 ;
    alarmfrg f2;
    Fragment f3 ;
    public final static int notificationId = 1;
    ViewPager viewpager;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_timing:
                {

                    viewpager.setCurrentItem(0);
                    return true;


                }
                case R.id.navigation_Alarm:
                {
                    viewpager.setCurrentItem(1);

                    return true;
                }

                case R.id.navigation_setting:
                    viewpager.setCurrentItem(2);
                    return true;
            }
            return false;
        }

    };


    @Override
    public void onBackPressed() {
        Timer tExit = null;
        if (isExit == false) {
            isExit = true; // 准备退出
            Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
            tExit = new Timer();
            tExit.schedule(new TimerTask() {
                @Override
                public void run() {
                    isExit = false; // 取消退出
                }
            }, 2000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务
        } else {
            //退出
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CrashHandler ch=CrashHandler.getInstance();
        ch.init(this);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT >= 11) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads  ().detectDiskWrites().detectNetwork().penaltyLog().build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects().detectLeakedClosableObjects().penaltyLog().penaltyDeath().build());
        }
        Login login=new Login(this);
        //尝试登入
        if(login.getuseraccountfromlocal().length()>0 && login.getuserpassfromlocal().length()>0 && login.getusernamefromlocal().length()>0){
            login.trylogin(login.getuseraccountfromlocal(),login.getuserpassfromlocal());
        }
        viewpager=(ViewPager) findViewById(R.id.vp); //获取ViewPager
        final int[] bottomresolutions=new int[3];
        bottomresolutions[0]=R.id.navigation_timing;
        bottomresolutions[1]=R.id.navigation_Alarm;
        bottomresolutions[2]=R.id.navigation_setting;

        List<Fragment> listfragment=new ArrayList<Fragment>(); //new一个List<Fragment>
        f1 = new timingfrg();
        f2 = new alarmfrg(this);
        f3 = new settingfrg();
        listfragment.add(f1);
        listfragment.add(f2);
        listfragment.add(f3);
        FragmentManager fm=getSupportFragmentManager();
        frgmentAdapter fa=new frgmentAdapter(fm,listfragment);
        viewpager.setAdapter(fa);
        viewpager.setCurrentItem(0);
        final BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                navigation.setSelectedItemId(bottomresolutions[position]);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }
    @Override

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            String result = data.getExtras().getString("result");//得到新Activity 关闭后返回的数据
            if (result.compareTo("Alarm Not Set") == 0) return;
            setTitle(Tools.getmidstring(result, "<Time>", "</>"));
            String ty = Tools.getelementstring(result, "Type");
            String timestr = Tools.getelementstring(result, "Time");
            int hour;
            int minute;

            String[] str = timestr.split(":");
            hour = Integer.valueOf(str[0]);
            minute = Integer.valueOf(str[1]);
            f2.addnewalrmitem(hour, minute, ty);
        }catch (Exception e){
            return;
        }

    }


}
