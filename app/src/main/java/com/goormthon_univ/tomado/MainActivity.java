package com.goormthon_univ.tomado;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.goormthon_univ.tomado.Adapter.BreakTimeSel;
import com.goormthon_univ.tomado.Adapter.BreakTimeSelAdapter;
import com.goormthon_univ.tomado.Adapter.MenuAdapter;
import com.goormthon_univ.tomado.Thread.TimerThread;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    //메인 화면에 있는 뷰들
    TextView main_time;
    ProgressBar main_progressbar;
    ImageView main_play_button;
    ImageView main_menu_button;
    TextView main_pause_hint;
    TextView main_mode_text;
    LinearLayout main_mode_layout;
    LinearLayout main_memo_layout;
    LinearLayout main_breaktime_layout;
    ImageView main_gradient;
    ImageView main_breaktime_arrow;
    Switch main_mode_switch;

    //Handler 객체 생성
    Handler handler=new Handler();

    //스레드 생성
    TimerThread timer_th;
    Timer timer;

    //현재 타이머가 작동 중인지 체크를 위한 변수
    private boolean timer_isrunning;

    //드롭다운 체크 변수
    private boolean main_breaktime_layout_dropdown;

    //이지 뽀모도로 모드 체크 변수
    private boolean pomodoro_mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //메인에 있는 뷰들 연결
        main_time=findViewById(R.id.main_time);
        main_progressbar=findViewById(R.id.main_progressbar);
        main_play_button=findViewById(R.id.main_play_button);
        main_menu_button=findViewById(R.id.main_menu_button);
        main_pause_hint=findViewById(R.id.main_pause_hint);
        main_mode_text=findViewById(R.id.main_mode_text);
        main_mode_layout=findViewById(R.id.main_mode_layout);
        main_memo_layout=findViewById(R.id.main_memo_layout);
        main_breaktime_layout=findViewById(R.id.main_breaktime_layout);
        main_gradient=findViewById(R.id.main_gradient);
        main_mode_switch=findViewById(R.id.main_mode_switch);
        main_breaktime_arrow=findViewById(R.id.main_breaktime_arrow);

        //리사이클러뷰 어뎁터 연결
        RecyclerView main_breaktime_recyclerview=findViewById(R.id.main_breaktime_recyclerview);

        BreakTimeSelAdapter breaktime_adapter=new BreakTimeSelAdapter();

        LinearLayoutManager layoutManager=new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL,false);
        main_breaktime_recyclerview.setLayoutManager(layoutManager);
        main_breaktime_recyclerview.setAdapter(breaktime_adapter);

        BreakTimeSel b_1=new BreakTimeSel("짧은 휴식",5);
        BreakTimeSel b_2=new BreakTimeSel("긴 휴식",20);

        breaktime_adapter.addItem(b_1);
        breaktime_adapter.addItem(b_2);
        breaktime_adapter.notifyDataSetChanged();

        //스위치 리스너 설정
        pomodoro_mode=false;
        main_mode_switch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pomodoro_mode){
                    main_mode_text.setText("easy pomodoro");
                    main_breaktime_arrow.setVisibility(View.VISIBLE);
                }else{
                    main_mode_text.setText("hard pomodoro");
                    main_breaktime_arrow.setVisibility(View.GONE);
                }
                pomodoro_mode=!pomodoro_mode;
            }
        });

        //메모 레이아웃 클릭 리스너 설정
        main_memo_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"메모",Toast.LENGTH_LONG).show();
            }
        });

        //브레이크 타임 레이아웃 리스너 설정
        main_breaktime_layout_dropdown=false;
        main_breaktime_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //이지뽀모도로 모드인지 체크
                if(!pomodoro_mode){
                    if(main_breaktime_layout_dropdown){
                        //드롭다운 닫기
                        main_breaktime_layout.setBackground(getResources().getDrawable(R.drawable.rect_radius));
                        main_breaktime_recyclerview.setVisibility(View.GONE);
                        main_breaktime_arrow.setImageResource(R.drawable.down_arrow);
                    }else{
                        //드롭다운 열기
                        main_breaktime_layout.setBackground(getResources().getDrawable(R.drawable.rect_radius_top));
                        main_breaktime_recyclerview.setVisibility(View.VISIBLE);
                        main_breaktime_arrow.setImageResource(R.drawable.up_arrow);
                    }
                    main_breaktime_layout_dropdown=!main_breaktime_layout_dropdown;
                }else {
                    Toast.makeText(getApplicationContext(),"하드 뽀모도로 모드에서는 휴식 시간을 변경할 수 없어요",Toast.LENGTH_SHORT).show();
                }
            }
        });

        //타이머 작동 여부 체크 변수 false로 초기화
        timer_isrunning=false;

        //숨겨야 할 뷰들 숨기기
        main_pause_hint.setVisibility(View.GONE);
        main_memo_layout.setVisibility(View.GONE);
        main_gradient.setVisibility(View.GONE);
    }

    public void timer_start(View view){
        //현재 타이머가 작동 중인지 여부 체크
        if(timer_isrunning){
            if(timer_th.timer_pause){
                //일시 중지 상태인 경우
                timer_th.timer_pause=false;

                main_play_button.setImageResource(R.drawable.pause_button);
                main_pause_hint.setVisibility(View.GONE);
                main_memo_layout.setVisibility(View.VISIBLE);
                main_gradient.setVisibility(View.GONE);
            }else{
                timer_th.timer_pause=true;

                main_play_button.setImageResource(R.drawable.resume_button);
                main_menu_button.setVisibility(View.VISIBLE);
                main_memo_layout.setVisibility(View.GONE);
                main_pause_hint.setVisibility(View.VISIBLE);
                main_gradient.setVisibility(View.VISIBLE);

                //1분 안에 재시작하지 않으면 종료됨
                timer_pause();
            }
        }else{
            //새로운 시작인 경우
            //기존에 존재하는 스레드가 있는지 우선 체크
            if(timer_th!=null){
                timer_th.interrupt();
            }
            timer_th=new TimerThread(main_time,main_progressbar,handler,25,00);
            timer_th.start();

            timer_isrunning=true;
            main_play_button.setImageResource(R.drawable.pause_button);

            //숨겨야할 뷰 숨기기
            main_menu_button.setVisibility(View.GONE);
            main_mode_layout.setVisibility(View.GONE);
            main_breaktime_layout.setVisibility(View.GONE);
            main_memo_layout.setVisibility(View.VISIBLE);
        }
    }

    public void timer_pause(){
        //타이머 생성
        timer=new Timer();
        TimerTask pause_task=new TimerTask(){
            @Override
            public void run() {
                //1분 뒤에 작동
                timer_th.interrupt();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if(timer_th.timer_pause){
                            timer_isrunning=false;
                            main_play_button.setImageResource(R.drawable.play_button);
                            main_menu_button.setVisibility(View.VISIBLE);

                            //숨겨야 할 뷰들 숨기기
                            main_pause_hint.setVisibility(View.GONE);
                            main_memo_layout.setVisibility(View.GONE);
                            main_gradient.setVisibility(View.GONE);

                            //만들어야 할 뷰 보이기
                            main_mode_layout.setVisibility(View.VISIBLE);
                            main_breaktime_layout.setVisibility(View.VISIBLE);

                            //시계 초기화
                            main_time.setText(String.format("%d : %02d",25,00));

                            //프로그래스바 초기화
                            main_progressbar.setProgress(0);
                        }
                    }
                });
            }
        };
        timer.schedule(pause_task,60000);
    }

    public void onclick_menu(View view){
        Dialog menu_dialog=new Dialog(MainActivity.this);
        menu_dialog.setContentView(R.layout.dialog_menu);
        menu_dialog.getWindow().setGravity(Gravity.RIGHT);
        menu_dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.MATCH_PARENT);
        menu_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));

        //리사이클러뷰 어뎁터 연결
        RecyclerView menu_recyclerview=menu_dialog.findViewById(R.id.menu_recyclerview);

        MenuAdapter menu_adapter=new MenuAdapter();

        LinearLayoutManager layoutManager=new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL,false);
        menu_recyclerview.setLayoutManager(layoutManager);
        menu_recyclerview.setAdapter(menu_adapter);

        menu_dialog.show();
    }
}