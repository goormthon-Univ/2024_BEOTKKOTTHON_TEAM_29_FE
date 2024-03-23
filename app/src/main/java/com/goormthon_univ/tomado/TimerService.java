package com.goormthon_univ.tomado;

import android.app.Dialog;
import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.goormthon_univ.tomado.Server.ServerManager;
import com.goormthon_univ.tomado.Thread.TimerThread;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.util.Timer;
import java.util.TimerTask;

public class TimerService extends Service {
    private static final String TAG="TimerService";

    //시간 측정을 위한 변수
    int value=0;

    //분,초
    int min;
    int sec;

    //타이머 일시 중지 여부
    public static boolean timer_pause;

    //타이머와 타이머 태스크
    Timer timer;
    Timer pauseTimer;
    TimerTask timer_task;

    ServerManager server_manager;

    MainActivity.MainHandler handler=new MainActivity.MainHandler();

    //쉬는 시간
    public static int break_time_min;

    //현재 타이머가 작동 중인지 체크를 위한 변수
    public boolean timer_isrunning=false;

    //user id
    public static String user_id;

    //task_id 설정
    String task_id;

    public void timer_stop(){
        //타이머 중단
        //기존에 존재하는 스레드가 있는지 우선 체크
        if(timer!=null){
            timer.cancel();
            timer=null;

            //메인액티비티에 전달
            Bundle bundle=new Bundle();
            bundle.putString("state","stop");
            Message message=handler.obtainMessage();
            message.setData(bundle);
            handler.sendMessage(message);
        }
    }

    public void timer_restart(){
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                timer_start();
            }
        },5000);
    }

    public void timer_pause_stop(){
        if(pauseTimer!=null) {
            Log.i("TimerThread","일시 중지 타이머 중단 시작");
            //일시중지 타이머 중단
            pauseTimer.cancel();
            pauseTimer = null;

            TimerThread.timer_pause=false;
            Log.i("TimerThread","일시 중지 타이머 중단 완료");
        }
    }

    public void timer_pause(){
        Log.i("TimerThread","일시 중지 타이머 호출");

        //메인액티비티에 전달
        Bundle bundle=new Bundle();
        bundle.putString("state","pause");
        Message message=handler.obtainMessage();
        message.setData(bundle);
        handler.sendMessage(message);

        //타이머 일시중지 중임을 알림
        timer_pause=true;

        pauseTimer=new Timer();
        timer_task=new TimerTask(){
            @Override
            public void run() {
                Log.i("TimerThread","일시 중지 타이머 작동");

                if(timer!=null){
                    timer_stop();
                    timer=null;
                }

                //메인액티비티에 전달
                Bundle bundle=new Bundle();
                bundle.putString("state","stop");
                Message message=handler.obtainMessage();
                message.setData(bundle);
                handler.sendMessage(message);

                timer_pause=false;
            }
        };
        pauseTimer.schedule(timer_task,6000);
    }

    public void timer_start(){
        //task 생성
        create_task();

        //메인액티비티에 전달
        Bundle bundle=new Bundle();
        bundle.putString("state","start");
        Message message=handler.obtainMessage();
        message.setData(bundle);
        handler.sendMessage(message);

        value=0;
        timer=new Timer();
        timer_task=new TimerTask() {
            @Override
            public void run() {
                Log.i("TimerThread","메인 타이머 작동 중");

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        //시간을 초로 변환
                        int time=min*60+sec-value;

                        if(TimerThread.timer_pause){
                            //
                        }else{
                            //일시 중지 타이머가 켜져 있을 경우
                            timer_pause_stop();

                            //타이머가 일시 정지 되지 않았을 경우
                            value++;

                            //초를 시간으로 변환하여 표시
                            MainActivity.main_time.setText(String.format("%d : %02d",time/60,time%60));
                            MainActivity.main_progressbar.setProgress((int)((double)value/(min*60+sec)*100));

                            //시간이 다 되었을 경우 종료
                            if(time<=0){
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

    //이지 모드면 0, 하드 모드면 1
    public void dialog_point_fn(){
        Dialog dialog_point=new Dialog(MainActivity.main_time.getContext());
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
                Toast.makeText(MainActivity.main_time.getContext(),js.get("message").toString(),Toast.LENGTH_SHORT).show();
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

    public void create_task(){
        String title=MainActivity.main_time.getText().toString();
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
                Toast.makeText(MainActivity.main_time.getContext(),js.get("message").toString(),Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public TimerService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();

        //this.handler=handler;
        min=0;
        sec=10;

        user_id="35";

        //서버 연동 객체 추가
        server_manager=new ServerManager(getApplicationContext());

        timer_pause=false;

        Log.d(TAG,"타이머 서비스가 호출되었습니다");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent!=null){
            processCommand(intent);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    public void processCommand(Intent intent){
        String mode=intent.getStringExtra("mode");
        if(mode.equals("start")){
            Log.d(TAG,"start");
            timer_stop();
            timer_pause_stop();
            timer_start();
        } else if(mode.equals("pause_true")){
            Log.d(TAG,"pause_true");
            timer_pause_stop();
        } else if(mode.equals("pause_false")){
            Log.d(TAG,"pause_false");
            timer_pause();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}