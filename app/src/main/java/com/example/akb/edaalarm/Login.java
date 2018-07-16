package com.example.akb.edaalarm;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by akb on 2018/2/5.
 */

public class Login {
    public static boolean isLogined=false;
    public static String account="";
    public static String name="";
    public static String pswd="";
    public Context cot;
    private Connection con;
    public static String ALARMCONFIG= Environment
            .getExternalStorageDirectory().getPath() + "/EdaAlarm/Account" + "/";
    public Login(Context c){
        cot=c;
        openConnection("jdbc:mysql://39.108.112.159:3306/test","root","hs295093653");


    }
    public static void deleteuserinfinlocal(){
        try {
            File f = new File(ALARMCONFIG + "alarmitems.txt");
            f.delete();

        }catch (Exception e){

        }
    }


    public List<AlarmItem> getalarmsfromdatabase(String acc){
        List<AlarmItem> re=new ArrayList<>();

        if(con==null || acc.compareTo("")==0) return re;
        Statement statement = null;
        ResultSet result = null;
        try {
            statement = con.createStatement();
            result = statement.executeQuery("select * from alarmtable where account='" + acc + "'");
            if (result != null && result.first()) {
                while (!result.isAfterLast()) {
                    int h = result.findColumn("hour");
                    int m = result.findColumn("minute");
                    int typstr = result.findColumn("typestr");
                    int isenable = result.findColumn("isenable");

                    AlarmItem ti = new AlarmItem(result.getInt(h), result.getInt(m), result.getInt(isenable) == 1 ? true : false, result.getString(typstr));

                    re.add(ti);
                    result.next();
                }

                return re;

            } else {
                Toast.makeText(cot, "获取数据失败", Toast.LENGTH_SHORT).show();
                return re;
            }
        }catch (Exception e){
            Toast.makeText(cot, "获取数据失败:"+e.toString(), Toast.LENGTH_SHORT).show();
            return re;
        }
    }
    public void synDatetodatabase(List<AlarmItem> alarms){
        if(con==null || isLogined==false) return;

        Statement statement = null;
        try {
            statement = con.createStatement();
            // Toast.makeText(cot,"  ",Toast.LENGTH_SHORT).show();
            if (statement != null) {
                //清空所有数据
                statement.execute("DELETE FROM alarmtable WHERE account='"+account+"'");
                for(int i=0;i<alarms.size();i++){
                    statement.execute("INSERT INTO alarmtable(hour,minute,second,typestr,isenable,account) values ("+String.valueOf(alarms.get(i).h)
                            +","+String.valueOf(alarms.get(i).m)
                            +",0,'"
                            +alarms.get(i).str+"','"
                            +(alarms.get(i).enable?"1":"0")+"','"
                            +account+"'"



                            +")");
                }
                Toast.makeText(cot,"同步完成",Toast.LENGTH_SHORT).show();
            }

        } catch (SQLException e) {
            Toast.makeText(cot,e.toString(),Toast.LENGTH_SHORT).show();

        }
    }
    public String getuserpassfromlocal(){
        String result="";
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
            result=getelementstring(text,"Password");

            fis.close();
            //Toast.makeText(con,String.valueOf(result.size()),Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            Toast.makeText(cot,e.toString(), Toast.LENGTH_SHORT).show();
        }
        return result;
    }
    public String getusernamefromlocal(){
        String result="";
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
            result=getelementstring(text,"Name");

            fis.close();
            //Toast.makeText(con,String.valueOf(result.size()),Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            Toast.makeText(cot,e.toString(), Toast.LENGTH_SHORT).show();
        }
        return result;
    }
    public String getuseraccountfromlocal(){
        String result="";
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
            result=getelementstring(text,"Account");

            fis.close();
            //Toast.makeText(con,String.valueOf(result.size()),Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            Toast.makeText(cot,e.toString(), Toast.LENGTH_SHORT).show();
        }
        return result;
    }
    public static String getelementstring(String allstring,String elementtype){
        return getmidstring(allstring,"<"+elementtype+">","</"+elementtype+">");
    }
    public static String getmidstring(String allstr,String left,String right){
        int l=allstr.indexOf(left);
        int r=allstr.indexOf(right,l+left.length());
        if(l>=0 && r>=0 && (l+left.length())<r){
            return allstr.substring(l+left.length(),r);
        }
        return "";
    }
    boolean trylogin(String acc,String pass){
        if(con==null) return false;
        Statement statement = null;
        ResultSet result = null;
        try {
            statement = con.createStatement();
            result = statement.executeQuery("select * from alarmaccount where account='"+acc+"'");
            if (result != null && result.first()) {
                int passColumnIndex = result.findColumn("password");
                int nameColumnIndex = result.findColumn("Name");
                String password=result.getString(passColumnIndex);
                if(pass.compareTo(password)==0){

                    //加写插入本地文件的代码
                    isLogined=true;
                    name=result.getString(nameColumnIndex);
                    account=acc;
                    pswd=password;
                    writelocallogininf();
                    Toast.makeText(cot,"登入成功",Toast.LENGTH_SHORT).show();
                    return true;
                }

            }else
            {
                Toast.makeText(cot,"登入失败",Toast.LENGTH_SHORT).show();
                return false;
            }
        } catch (SQLException e) {
            Toast.makeText(cot,"SQL异常",Toast.LENGTH_SHORT).show();
            return false;
        }finally {
            try {
                if (result != null) {
                    result.close();
                    result = null;
                }
                if (statement != null) {
                    statement.close();
                    statement = null;
                }

            } catch (SQLException sqle) {

            }
        }
        return true;
    }
    public  boolean reg(String acc,String pass,String name){
        if(acc.length()>45 || pass.length()>45 || name.length()>45 || con==null || acc.length()==0 || pass.length()==0 || name.length()==0) {
            Toast.makeText(cot,"信息错误",Toast.LENGTH_SHORT).show();

            return false;}
        Statement statement = null;
        try {
            statement = con.createStatement();
           // Toast.makeText(cot,"  ",Toast.LENGTH_SHORT).show();
            if (statement != null) {
                statement.execute("INSERT INTO alarmaccount(Name,account,password) VALUES('"+name+ "','"+acc+"','"+pass+"')");
                return  true;
            }
        } catch (SQLException e) {
            Toast.makeText(cot,e.toString(),Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
    private void writelocallogininf(){
        try{
            File dir = new File(ALARMCONFIG);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            FileOutputStream fos = new FileOutputStream(ALARMCONFIG+"alarmitems.txt");

            fos.write(("<Account>"+account+"</Account><Name>"+name+"</Name><Password>"+pswd+"</Password>").getBytes());
            fos.close();
        }catch (Exception e){

        }
    }
    boolean isAccountexist(String acc){
        if(con==null) return false;
        Statement statement = null;
        ResultSet result = null;
        try {
            statement = con.createStatement();
            result = statement.executeQuery("select * from alarmaccount where account='"+acc+"'");
            if (result != null && result.first()) {
                return true;

            }else
            {
                return false;
            }
        } catch (SQLException e) {
            return false;
        }finally {
            try {
                if (result != null) {
                    result.close();
                    result = null;
                }
                if (statement != null) {
                    statement.close();
                    statement = null;
                }

            } catch (SQLException sqle) {

            }
        }
    }
    private  void openConnection(String url, String user,
                                            String password) {
        Connection conn = null;


            try {
                final String DRIVER_NAME = "com.mysql.jdbc.Driver";
                Class.forName(DRIVER_NAME);
                con=DriverManager.getConnection(url, user, password);
            } catch (ClassNotFoundException e) {
                con=null;
                Toast.makeText(cot,"Class not found",Toast.LENGTH_SHORT).show();
            }catch (SQLException e){
                con=null;
                Toast.makeText(cot,e.toString(),Toast.LENGTH_SHORT).show();
            }




    }
}
