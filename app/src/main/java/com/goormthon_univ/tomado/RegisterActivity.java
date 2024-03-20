package com.goormthon_univ.tomado;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.goormthon_univ.tomado.Server.ServerManager;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {
    //필요한 뷰들 선언
    EditText register_nickname;
    EditText register_email;
    EditText register_password;
    Button register_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //서버 연동 객체 추가
        ServerManager server_manager=new ServerManager(getApplicationContext());

        //뷰 연결
        register_nickname=findViewById(R.id.register_nickname);
        register_email=findViewById(R.id.register_email);
        register_password=findViewById(R.id.register_password);
        register_button=findViewById(R.id.register_button);

        //회원가입 완료 버튼 리스너 설정
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    JSONObject parms=new JSONObject();
                    parms.put("login_id",register_email.getText());
                    parms.put("password",register_password.getText());
                    parms.put("nickname",register_nickname.getText());
                    JSONObject js=new JSONObject(server_manager.http_request_post_json("/users/signup",parms));

                    if(js.get("message").toString().equals("회원 가입 성공")){
                        Log.d("","가입 성공");
                        //회원 가입 성공
                        JSONObject data=new JSONObject(js.get("data").toString());
                        Toast.makeText(getApplicationContext(),data.get("user_id").toString(),Toast.LENGTH_SHORT).show();

                        Log.d("RegisterActivity",js.get("data").toString());
                        Log.d("RegisterActivity",data.get("user_id").toString());
                        startActivity(intent);
                    }else{
                        //회원 가입 실패 시 실패 원인 보여줌
                        Toast.makeText(getApplicationContext(),js.get("message").toString(),Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    public void onclick_close(View view){
        finish();
    }
}