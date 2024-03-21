package com.goormthon_univ.tomado;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.goormthon_univ.tomado.Manager.PreferencesManager;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //1초 후 화면 넘어가기
        Handler handler=new Handler();
        Intent login_intent=new Intent(this,LoginActivity.class);
        Intent main_intent=new Intent(this,MainActivity.class);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(PreferencesManager.pref_read_string((SharedPreferences)getSharedPreferences("preferences", Activity.MODE_PRIVATE),"user_id")==null){
                    //로그인 기록이 없을 경우
                    startActivityForResult(login_intent,101);
                    finish();
                }else{
                    //로그인 기록이 있을 경우
                    startActivityForResult(main_intent,101);
                    finish();
                }

            }
        },1000);
    }
}