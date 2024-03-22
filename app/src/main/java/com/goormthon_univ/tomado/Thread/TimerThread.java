package com.goormthon_univ.tomado.Thread;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.goormthon_univ.tomado.MainActivity;
import com.goormthon_univ.tomado.R;
import com.goormthon_univ.tomado.Server.ServerManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDateTime;
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

    //타이머와 타이머 태스크
    Timer timer;
    TimerTask timer_task;

    ServerManager server_manager;

    //쉬는 시간
    int break_time_min;

    //user id
    String user_id;

    //task_id 설정
    String task_id;

    public TimerThread(TextView textview, ProgressBar main_progressbar, Handler handler, int min, int sec, int break_time_min, String user_id){
        this.textview=textview;
        this.main_progressbar=main_progressbar;
        this.handler=handler;
        this.min=min;
        this.sec=sec;
        this.break_time_min=break_time_min;
        this.user_id=user_id;

        //서버 연동 객체 추가
        server_manager=new ServerManager(textview.getContext());

        timer_pause=false;
    }

    public void create_task(String title){

        try {
            JSONObject parms=new JSONObject();
            parms.put("user_id",user_id);
            parms.put("category_id",MainActivity.category_id);
            parms.put("title",title);
            LocalDateTime now_datetime;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                now_datetime = LocalDateTime.now();
            }else{
                now_datetime=null;
            }
            parms.put("created_at",now_datetime.toString());
            JSONObject js=new JSONObject(server_manager.http_request_post_json("/tasks",parms));

            if(js.get("message").toString().equals("Task 생성 성공")){
                Log.d("","Task 생성 성공");
                //task 생성 성공
                JSONObject data=new JSONObject(js.get("data").toString());
                task_id=data.get("task_id").toString();
                Log.d("task 아이디",task_id);
            }else{
                //task 생성 실패 시 실패 원인 보여줌
                Toast.makeText(textview.getContext(),js.get("message").toString(),Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public void timer_stop(){
        //타이머 중단을 위한 변수
        timer.cancel();
    }

    public void timer_restart(){
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //task 생성
                create_task(textview.getText().toString());
                value=0;
                timer=new Timer();
                timer_task=new TimerTask() {
                    @Override
                    public void run() {
                        Log.i("TimerThread","타이머 작동 중");
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

                                    //시간이 다 되었을 경우 종료
                                    if(time==0){
                                        //다이얼로그 열기
                                        dialog_point_fn();

                                        //타이머 중단
                                        timer_stop();
                                        Bundle bundle=new Bundle();
                                        bundle.putString("state","stop");
                                        Message message=handler.obtainMessage();
                                        message.setData(bundle);
                                        handler.sendMessage(message);
                                    }
                                }
                            }
                        });
                    }
                };
                timer.schedule(timer_task,0,1000);

                //타이머 재시작
                Bundle bundle=new Bundle();
                bundle.putString("state","start");
                Message message=handler.obtainMessage();
                message.setData(bundle);
                handler.sendMessage(message);
            }
        },5000);
    }

    public void run(){
        synchronized (this){
            timer=new Timer();
            timer_task=new TimerTask() {
                @Override
                public void run() {
                    Log.i("TimerThread","타이머 작동 중");
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

                                //시간이 다 되었을 경우 종료
                                if(time==0){
                                    //다이얼로그 열기
                                    dialog_point_fn();

                                    //타이머 중단
                                    timer_stop();
                                    Bundle bundle=new Bundle();
                                    bundle.putString("state","stop");
                                    Message message=handler.obtainMessage();
                                    message.setData(bundle);
                                    handler.sendMessage(message);
                                }
                            }
                        }
                    });
                }
            };
            timer.schedule(timer_task,0,1000);
        }
    }

    //이지 모드면 0, 하드 모드면 1
    public void dialog_point_fn(){
        Dialog dialog_point=new Dialog(textview.getContext());
        if(MainActivity.mode.equals("0")){
            //이지 모드인 경우
            dialog_point.setContentView(R.layout.dialog_point);
        }else{
            //하드 모드인 경우
            dialog_point.setContentView(R.layout.dialog_point_3);
        }

        dialog_point.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView dialog_point_text=dialog_point.findViewById(R.id.dialog_point_text);
        Button dialog_point_stop=dialog_point.findViewById(R.id.dialog_point_stop);
        Button dialog_point_restart=dialog_point.findViewById(R.id.dialog_point_restart);

        try {
            JSONObject parms=new JSONObject();
            parms.put("user_id",user_id);
            parms.put("task_id",task_id);
            parms.put("mode",MainActivity.mode);
            LocalDateTime now_datetime;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                now_datetime = LocalDateTime.now();
            }else{
                now_datetime=null;
            }
            parms.put("created_at",now_datetime.toString());
            JSONObject js=new JSONObject(server_manager.http_request_post_json("/tasks/toma",parms));

            if(js.get("message").toString().equals("토마 적립 성공")){
                //적립 성공
                Log.d("","적립 성공");
            }else{
                //적립 실패 시 실패 원인 보여줌
                Toast.makeText(textview.getContext(),js.get("message").toString(),Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        dialog_point_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //다이얼로그 창 닫기
                dialog_point.hide();
            }
        });
        dialog_point_restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timer_restart();

                //다이얼로그 창 닫기
                dialog_point.hide();

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                    }
                },5000);
            }
        });

        dialog_point.show();
    }
}
