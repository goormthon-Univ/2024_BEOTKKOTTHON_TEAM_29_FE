package com.goormthon_univ.tomado.Thread;

import android.os.Handler;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class TimerThread extends Thread{
    //Handler 객체 생성
    Handler handler;

    //타이머 시간 설정을 위한 TextView
    TextView textview;
    ProgressBar main_progressbar;
    //시간 측정을 위한 변수
    int value=0;

    //분,초
    int min;
    int sec;

    //타이머 일시 중지 여부
    public static boolean timer_pause;

    public TimerThread(TextView textview,ProgressBar main_progressbar,Handler handler,int min,int sec){
        this.textview=textview;
        this.main_progressbar=main_progressbar;
        this.handler=handler;
        this.min=min;
        this.sec=sec;

        timer_pause=false;
    }
    public void run(){
        Timer timer=new Timer();
        TimerTask timer_task=new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        //시간을 초로 변환
                        int time=min*60+sec-value;

                        if(timer_pause){
                            //
                        }else{
                            //타이머가 일시 정지 되지 않았을 경우
                            value++;

                            //초를 시간으로 변환하여 표시
                            textview.setText(String.format("%d : %02d",time/60,time%60));
                            main_progressbar.setProgress((int)((double)value/(min*60+sec)*100));
                        }
                    }
                });
            }
        };
        timer.schedule(timer_task,0,1000);
    }
}
