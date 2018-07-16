package com.example.akb.edaalarm;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by akb on 2018/1/28.
 */

public class settingfrg extends Fragment {
    View rootView;
    static Button btn_logininorout;
    static Button updatebutton;
    static TextView usernamelable;
    static Button btn_login;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        rootView= inflater.inflate(R.layout.setting, container, false);


        return inflater.inflate(R.layout.setting,container,false);
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

    }
    public static void refreshuserinf(){
        if(!Login.isLogined) return;
        usernamelable.setText("Name:"+Login.name);
        btn_logininorout.setText("注销");
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        usernamelable=(TextView)getActivity().findViewById(R.id.user_name);
        btn_logininorout=(Button)getActivity().findViewById(R.id.btn_login);
        updatebutton=(Button)getActivity().findViewById(R.id.uploaddata_btn);
        btn_logininorout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!Login.isLogined){
                Intent it=new Intent(getActivity(),LoginActivity.class);
                startActivity(it);
                }else{
                    Login.isLogined=false;
                    Login.name="";
                    Login.account="";
                    Login.pswd="";
                    Login.deleteuserinfinlocal();
                    usernamelable.setText("Name:xxxx");
                    btn_logininorout.setText("登入");
                }
            }
        });
        updatebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!Login.isLogined){
                    Toast.makeText(getActivity(),"未登入",Toast.LENGTH_SHORT).show();
                    return;
                }

                Login login=new Login(getContext());
                login.synDatetodatabase(AlarmListAdapter.list);
            }
        });
        if(Login.isLogined){
            usernamelable.setText("Name:"+Login.name);
            btn_logininorout.setText("注销");
        }
    }
}
