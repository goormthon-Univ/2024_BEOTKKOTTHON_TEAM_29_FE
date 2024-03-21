package com.goormthon_univ.tomado;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.goormthon_univ.tomado.Manager.PreferencesManager;
import com.goormthon_univ.tomado.Server.ServerManager;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
    //필요한 뷰들 선언
    EditText login_email;
    EditText login_password;
    Button login_button;
    TextView login_register_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //서버 연동 객체 추가
        ServerManager server_manager=new ServerManager(getApplicationContext());

        //뷰 연결
        login_email=findViewById(R.id.login_email);
        login_password=findViewById(R.id.login_password);
        login_button=findViewById(R.id.login_button);
        login_register_button=findViewById(R.id.login_register_button);

        //회원가입 글자에 밑줄 추가하기
        login_register_button.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);

        //로그인 버튼 리스너 설정
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    JSONObject parms=new JSONObject();
                    parms.put("login_id",login_email.getText());
                    parms.put("password",login_password.getText());
                    JSONObject js=new JSONObject(server_manager.http_request_post_json("/users/login",parms));
                    if(js.get("message").toString().equals("로그인 성공")){
                        //user_id 가져오기
                        JSONObject data=new JSONObject(js.get("data").toString());
                        PreferencesManager.pref_write_string((SharedPreferences)getSharedPreferences("preferences", Activity.MODE_PRIVATE),
                                "user_id",data.get("user_id").toString());

                        //로그인 성공
                        startActivity(intent);
                    }else{
                        //로그인 실패 시 실패 원인 보여줌
                        Toast.makeText(getApplicationContext(),js.get("message").toString(),Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        //회원가입 버튼 리스너 설정
        Intent intent_register=new Intent(this,RegisterActivity.class);
        login_register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(intent_register,101);
            }
        });
    }
}