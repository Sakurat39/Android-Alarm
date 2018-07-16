package com.example.akb.edaalarm.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;

import com.example.akb.edaalarm.AlarmItem;
import com.example.akb.edaalarm.R;
import com.example.akb.edaalarm.other.Database;

import java.util.Calendar;
/**
 * Created by akb on 2018/1/29.
 */

public class addalarmactivity extends AppCompatActivity {
    @Override

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.newalarmview);

        Button b1=(Button) findViewById(R.id.nav_btn1);
        Button b0=(Button)findViewById(R.id.nav_btn0);
        final EditText e1=(EditText) findViewById(R.id.nav_edit);
       final TimePicker tp=(TimePicker)findViewById(R.id.timePicker1);

        b1.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent();

                        intent.putExtra("result","Alarm Not Set");
                        addalarmactivity.this.setResult(RESULT_OK,intent);
                        addalarmactivity.this.finish();
                    }
                }
        );

        b0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();


                intent.putExtra("result","<Type>"+e1.getText().toString()+"</><Time>"+tp.getHour()+":"+tp.getMinute()+"</>");
                addalarmactivity.this.setResult(RESULT_OK,intent);

                addalarmactivity.this.finish();
            }
        });

    }
}
