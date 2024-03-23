package com.goormthon_univ.tomado;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.goormthon_univ.tomado.Manager.PreferencesManager;
import com.goormthon_univ.tomado.Server.ServerManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class FriendCreateActivity extends AppCompatActivity {
    ServerManager server_manager;
    SharedPreferences pf;

    String user_id;

    //뷰
    EditText friend_create_title;
    EditText friend_create_date;
    EditText friend_create_member_number;
    EditText friend_create_goal;
    EditText friend_create_memo;
    CheckBox friend_create_check;
    TextView friend_create_save;
    TextView friend_create_logout;

    /*
    편집인지 생성인지 구분

    수정: 편집
    CREAT: 생성
     */
    String mode="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_create);

        //서버 연동 객체 추가
        server_manager=new ServerManager(getApplicationContext());

        //SharedPreferences
        pf=getSharedPreferences("preferences", Activity.MODE_PRIVATE);
        user_id=server_manager.get_user_id(pf);

        //뷰 연결
        friend_create_title=findViewById(R.id.friend_create_title);
        friend_create_date=findViewById(R.id.friend_create_date);
        friend_create_member_number=findViewById(R.id.friend_create_member_number);
        friend_create_goal=findViewById(R.id.friend_create_goal);
        friend_create_memo=findViewById(R.id.friend_create_memo);
        friend_create_check=findViewById(R.id.friend_create_check);
        friend_create_save=findViewById(R.id.friend_create_save);
        friend_create_logout=findViewById(R.id.friend_create_logout);

        //탈퇴버튼
        friend_create_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if(mode.equals("CREAT")){
                        Toast.makeText(getApplicationContext(),"클럽 가입 후 탈퇴하실 수 있습니다",Toast.LENGTH_SHORT).show();
                    }else{
                        JSONObject js=new JSONObject(server_manager.http_request_delete_json("/clubs?user="+user_id+"&club="+mode));
                        //JSONArray member_list=new JSONArray(js.get("memberList").toString());
                        if(js.get("message").toString().equals("클럽 나가기 성공")){
                        }else{
                            //회원 가입 실패 시 실패 원인 보여줌
                            Toast.makeText(getApplicationContext(),js.get("message").toString(),Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        //날짜 표시
        friend_create_date.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date())+" - "+new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        friend_create_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog date_picker=new DatePickerDialog(FriendCreateActivity.this);
                date_picker.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        friend_create_date.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date())+" - "+String.format("%4d-%02d-%02d",year,month,dayOfMonth));
                    }
                });
                date_picker.show();
            }
        });

        //생성이 아닐 경우 정보 불러오기
        Intent intent=getIntent();
        mode=intent.getStringExtra("mode");
        if(mode.equals("CREAT")){
            //
        }else{
            try {
                JSONObject js=new JSONObject(server_manager.http_request_get_json("/clubs?user="+user_id+"&club="+mode));
                //JSONArray member_list=new JSONArray(js.get("memberList").toString());
                if(js.get("message").toString().equals("토마 클럽 조회 성공")){
                    JSONObject data=new JSONObject(js.get("data").toString());
                    friend_create_title.setText(data.get("title").toString());
                    //friend_create_member_number.setText(member_list.length());
                    friend_create_goal.setText(data.get("goal").toString());
                    friend_create_memo.setText(data.get("memo").toString());
                }else{
                    //회원 가입 실패 시 실패 원인 보여줌
                    Toast.makeText(getApplicationContext(),js.get("message").toString(),Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }

        friend_create_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mode.equals("CREAT")){
                    try {
                        String[] date_arr=friend_create_date.getText().toString().split(" - ");
                        JSONObject parms=new JSONObject();
                        parms.put("user_id",user_id);
                        parms.put("title",friend_create_title.getText().toString());
                        parms.put("member_number",friend_create_member_number.getText());
                        parms.put("goal",friend_create_goal.getText().toString());
                        parms.put("memo",friend_create_memo.getText().toString());
                        parms.put("start_date",date_arr[0]);
                        parms.put("end_date",date_arr[1]);
                        JSONObject js=new JSONObject(server_manager.http_request_post_json("/clubs",parms));

                        Log.d("",js.toString());

                        if(js.get("message").toString().equals("클럽 생성 성공")){
                            Log.d("","클럽 생성 성공");
                            //닫기
                            finish();
                        }else{
                            //회원 가입 실패 시 실패 원인 보여줌
                            Toast.makeText(getApplicationContext(),js.get("message").toString(),Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }else{
                    try {
                        String[] date_arr=friend_create_date.getText().toString().split(" - ");
                        JSONObject parms=new JSONObject();
                        parms.put("user_id",user_id);
                        parms.put("club_id",mode);
                        parms.put("title",friend_create_title.getText().toString());
                        parms.put("member_number",friend_create_member_number.getText());
                        parms.put("goal",friend_create_goal.getText().toString());
                        parms.put("memo",friend_create_memo.getText().toString());
                        parms.put("end_date",date_arr[1]);
                        JSONObject js=new JSONObject(server_manager.http_request_put_json("/clubs",parms));

                        Log.d("",js.toString());

                        if(js.get("message").toString().equals("클럽 수정 성공")){
                            Log.d("","클럽 수정 성공");
                            //닫기
                            finish();
                        }else{
                            //회원 가입 실패 시 실패 원인 보여줌
                            Toast.makeText(getApplicationContext(),js.get("message").toString(),Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
    }

    public void onclick_close(View view){
        finish();
    }
}