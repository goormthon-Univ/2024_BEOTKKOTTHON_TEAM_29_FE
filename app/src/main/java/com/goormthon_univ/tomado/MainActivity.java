package com.goormthon_univ.tomado;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.goormthon_univ.tomado.Adapter.BreakTimeSel;
import com.goormthon_univ.tomado.Adapter.BreakTimeSelAdapter;
import com.goormthon_univ.tomado.Adapter.Category;
import com.goormthon_univ.tomado.Adapter.CategoryAdapter;
import com.goormthon_univ.tomado.Adapter.Memo;
import com.goormthon_univ.tomado.Adapter.MenuAdapter;
import com.goormthon_univ.tomado.Manager.PreferencesManager;
import com.goormthon_univ.tomado.Server.ServerManager;
import com.goormthon_univ.tomado.Thread.TimerThread;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import kotlin.jvm.Synchronized;

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
    TextView main_category_text;
    ImageView main_category_image;

    //Handler 객체 생성
    MainHandler handler=new MainHandler();

    //스레드 생성
    TimerThread timer_th;
    Timer timer;

    //현재 타이머가 작동 중인지 체크를 위한 변수
    private boolean timer_isrunning;

    //드롭다운 체크 변수
    private boolean main_breaktime_layout_dropdown;

    //이지 뽀모도로 모드 체크 변수
    private boolean pomodoro_mode;

    ServerManager server_manager;
    SharedPreferences pf;

    String user_id;

    //카테고리 아이디
    public static String category_id;

    //모드 (이지모드 0, 하드모드 1)
    public static String mode;

    //쉬는 시간(기본 5분)
    public static int break_time=5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //서버 연동 객체 추가
        server_manager=new ServerManager(getApplicationContext());

        //SharedPreferences
        pf=getSharedPreferences("preferences", Activity.MODE_PRIVATE);
        user_id=server_manager.get_user_id(pf);

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
        main_category_text=findViewById(R.id.main_category_text);
        main_category_image=findViewById(R.id.main_category_image);

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
        mode="0";
        main_mode_switch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pomodoro_mode){
                    main_mode_text.setText("easy pomodoro");
                    main_breaktime_arrow.setVisibility(View.VISIBLE);
                    mode="0";
                }else{
                    main_mode_text.setText("hard pomodoro");
                    main_breaktime_arrow.setVisibility(View.GONE);
                    mode="1";
                }
                pomodoro_mode=!pomodoro_mode;
            }
        });

        //메모 레이아웃 클릭 리스너 설정
        main_memo_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onclick_memo_dialog();
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

    public class MainHandler extends Handler{
        @Override
        public void handleMessage(Message msg){
            Log.d("",msg.getData().getString("state"));
            if(msg.getData().getString("state").equals("start")){
                timer_isrunning=true;
                main_play_button.setImageResource(R.drawable.pause_button);

                //숨겨야할 뷰 숨기기
                main_menu_button.setVisibility(View.GONE);
                main_mode_layout.setVisibility(View.GONE);
                main_breaktime_layout.setVisibility(View.GONE);
                main_memo_layout.setVisibility(View.VISIBLE);
            }else if(msg.getData().getString("state").equals("stop")){
                timer_isrunning=false;
                main_play_button.setImageResource(R.drawable.play_button);

                //숨겨야할 뷰 숨기기
                main_menu_button.setVisibility(View.VISIBLE);
                main_mode_layout.setVisibility(View.VISIBLE);
                main_breaktime_layout.setVisibility(View.VISIBLE);
                main_memo_layout.setVisibility(View.GONE);

                //시계 초기화
                main_time.setText(String.format("%d : %02d",25,00));

                //프로그래스바 초기화
                main_progressbar.setProgress(0);
            }
        }
    };

    public void timer_start(View view) throws InterruptedException {
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
            if(category_id!=null){
                //새로운 시작인 경우
                //기존에 존재하는 스레드가 있는지 우선 체크
                if(timer_th!=null){
                    timer_th.interrupt();
                }

                int break_time;
                if(pomodoro_mode){
                    //하드 뽀모도로 모드인 경우
                    break_time=5;
                }else{
                    break_time=20;
                }
                timer_th=new TimerThread(main_time,main_progressbar,handler,0,10,break_time,user_id);
                timer_th.create_task(findViewById(R.id.main_task).toString());
                timer_th.start();

                timer_isrunning=true;
                main_play_button.setImageResource(R.drawable.pause_button);

                //숨겨야할 뷰 숨기기
                main_menu_button.setVisibility(View.GONE);
                main_mode_layout.setVisibility(View.GONE);
                main_breaktime_layout.setVisibility(View.GONE);
                main_memo_layout.setVisibility(View.VISIBLE);
            }else{
                Toast.makeText(getApplicationContext(),"카테고리를 선택 후 시작해주세요",Toast.LENGTH_SHORT).show();
            }

        }
    }

    public void timer_pause(){
        //타이머 생성
        timer=new Timer();
        TimerTask pause_task=new TimerTask(){
            @Override
            public void run() {
                //1분 뒤에 작동
                timer_th.timer_stop();
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

    public void onclick_setting(View view){
        Intent setting_intent=new Intent(this,SettingActivity.class);
        startActivityForResult(setting_intent,101);
    }

    public void onclick_menu(View view){
        Dialog menu_dialog=new Dialog(MainActivity.this);
        menu_dialog.setContentView(R.layout.dialog_menu);
        menu_dialog.getWindow().setGravity(Gravity.RIGHT);
        menu_dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.MATCH_PARENT);
        menu_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));

        //서버에서 불러오기
        try {
            JSONObject json=new JSONObject(server_manager.http_request_get_json("/users/"+user_id));
            JSONObject data=new JSONObject(json.get("data").toString());

            if(json.get("message").toString().equals("회원 조회 성공")){
                //뷰 연결하고
                TextView dialog_menu_nickname=menu_dialog.findViewById(R.id.dialog_menu_nickname);
                TextView dialog_menu_tomato=menu_dialog.findViewById(R.id.dialog_menu_tomato);
                ImageView dialog_menu_image=menu_dialog.findViewById(R.id.dialog_menu_image);

                //텍스트,이미지 설정
                dialog_menu_nickname.setText(data.get("nickname").toString());
                dialog_menu_tomato.setText(data.get("tomato").toString());
                dialog_menu_image.setImageBitmap(server_manager.http_request_get_image(data.get("character_url").toString()));

            }else{
                //회원 조회 실패 시 실패 원인 보여줌
                Toast.makeText(getApplicationContext(),json.get("message").toString(),Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        //리사이클러뷰 어뎁터 연결
        RecyclerView menu_recyclerview=menu_dialog.findViewById(R.id.menu_recyclerview);

        MenuAdapter menu_adapter=new MenuAdapter(getApplicationContext());

        LinearLayoutManager layoutManager=new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL,false);
        menu_recyclerview.setLayoutManager(layoutManager);
        menu_recyclerview.setAdapter(menu_adapter);

        menu_dialog.show();
    }

    public void onclick_category_dialog(View view){
        Dialog menu_dialog=new Dialog(MainActivity.this);
        menu_dialog.setContentView(R.layout.dialog_category_main);
        menu_dialog.getWindow().setGravity(Gravity.BOTTOM);
        menu_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));

        //리사이클러뷰 어뎁터 연결
        RecyclerView dialog_category_main_recyclerview=menu_dialog.findViewById(R.id.dialog_category_main_recyclerview);

        CategoryAdapter category_adapter=new CategoryAdapter(main_category_text,main_category_image,1,MainActivity.this, PreferencesManager.pref_read_string(pf,"user_id"));

        GridLayoutManager layoutManager=new GridLayoutManager(this,2);
        dialog_category_main_recyclerview.setLayoutManager(layoutManager);
        dialog_category_main_recyclerview.setAdapter(category_adapter);

        //홈화면 카테고리 다이얼로그 뷰 연결
        ImageButton dialog_category_main_edit=menu_dialog.findViewById(R.id.dialog_category_main_edit);

        //편집 버튼을 눌렀을 경우
        dialog_category_main_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"카테고리 편집,추가는 모아보기에서 할 수 있습니다",Toast.LENGTH_SHORT).show();
            }
        });

        //서버에서 불러오기
        try {
            JSONObject json=new JSONObject(server_manager.http_request_get_json("/categories/"+user_id));

            if(json.get("message").toString().equals("카테고리 조회 성공")){
                JSONObject json_data=new JSONObject(json.get("data").toString());
                JSONArray category_list_array=new JSONArray(json_data.get("categoryList").toString());

                for(int i=0;i< category_list_array.length();i++){
                    JSONObject data=new JSONObject(category_list_array.get(i).toString());
                    Category category=new Category(data.get("category_id").toString(),
                            data.get("title").toString(),
                            data.get("color").toString(),
                            Integer.parseInt(data.get("tomato").toString()),
                            false);
                    category_adapter.addItem(category);
                }
            }else{
                //메모 조회 실패 시 실패 원인 보여줌
                Toast.makeText(getApplicationContext(),json.get("message").toString(),Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        category_adapter.notifyDataSetChanged();

        menu_dialog.show();
    }

    public void onclick_memo_dialog(){
        Dialog memo_dialog=new Dialog(MainActivity.this);
        memo_dialog.setContentView(R.layout.dialog_memo);
        memo_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        memo_dialog.findViewById(R.id.dialog_memo_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                memo_dialog.dismiss();
            }
        });

        //화면 바깥 부분을 누르거나 닫을 경우 저장되도록 설정
        memo_dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                EditText dialog_memo_content=memo_dialog.findViewById(R.id.dialog_memo_content);
                JSONObject parms=new JSONObject();
                try {
                    parms.put("content",dialog_memo_content.getText());
                    JSONObject json=new JSONObject(server_manager.http_request_post_json("/memos/"+server_manager.get_user_id((SharedPreferences)getSharedPreferences("preferences", Activity.MODE_PRIVATE)),parms));

                    if(json.get("message").toString().equals("메모 작성 성공")){
                        Toast.makeText(getApplicationContext(),"긴급 메모에 저장되었습니다",Toast.LENGTH_SHORT).show();
                    }else{
                        //메모 조회 실패 시 실패 원인 보여줌
                        Toast.makeText(getApplicationContext(),json.get("message").toString(),Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        memo_dialog.show();
    }
}