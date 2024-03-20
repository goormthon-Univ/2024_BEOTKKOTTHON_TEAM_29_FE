package com.goormthon_univ.tomado;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //1초 후 화면 넘어가기
        Handler handler=new Handler();
        Intent intent=new Intent(this,LoginActivity.class);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivityForResult(intent,101);
                finish();
            }
        },1000);
    }
}