package com.goormthon_univ.tomado;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.goormthon_univ.tomado.Server.ServerManager;

import org.json.JSONException;
import org.json.JSONObject;

public class SettingActivity extends AppCompatActivity {
    Button setting_button;
    EditText setting_nickname;
    EditText setting_email;
    EditText setting_password;
    ImageView setting_image;

    ServerManager server_manager;
    SharedPreferences pf;

    String user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        //서버 연동 객체 추가
        server_manager=new ServerManager(getApplicationContext());

        //SharedPreferences
        pf=getSharedPreferences("preferences", Activity.MODE_PRIVATE);
        user_id=server_manager.get_user_id(pf);

        setting_button=findViewById(R.id.setting_button);
        setting_nickname=findViewById(R.id.setting_nickname);
        setting_email=findViewById(R.id.setting_email);
        setting_password=findViewById(R.id.setting_password);
        setting_image=findViewById(R.id.setting_image);

        //서버에서 정보 불러오기
        //서버에서 불러오기
        try {
            JSONObject json=new JSONObject(server_manager.http_request_get_json("/users/"+user_id));
            JSONObject data=new JSONObject(json.get("data").toString());

            if(json.get("message").toString().equals("회원 조회 성공")){
                //텍스트,이미지 설정
                setting_nickname.setText(data.get("nickname").toString());
                setting_email.setText(data.get("login_id").toString());
                //setting_password.setText(data.get("password").toString());
                setting_image.setImageBitmap(server_manager.http_request_get_image(data.get("character_url").toString()));
            }else{
                //회원 조회 실패 시 실패 원인 보여줌
                Toast.makeText(getApplicationContext(),json.get("message").toString(),Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        //버튼 리스너 설정
        setting_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    JSONObject parms=new JSONObject();
                    parms.put("login_id",setting_email.getText());
                    parms.put("password",setting_password.getText());
                    parms.put("nickname",setting_nickname.getText());
                    //parms.put("character_url",url);
                    JSONObject json=new JSONObject(server_manager.http_request_put_json("/users/"+user_id,parms));

                    if(json.get("message").toString().equals("회원 정보 수정 성공")){
                        Toast.makeText(getApplicationContext(),"회원 정보 수정에 성공하였습니다",Toast.LENGTH_SHORT).show();

                        //창 닫기
                        finish();
                    }else{
                        //회원 정보 수정 실패 시 실패 원인 보여줌
                        Toast.makeText(getApplicationContext(),json.get("message").toString(),Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    public void onclick_close(View view){
        Toast.makeText(getApplicationContext(),"변경사항을 저장하려면 확인을 눌러주세요",Toast.LENGTH_SHORT).show();
        finish();
    }

    public void onclick_image_edit(View view){
        Toast.makeText(getApplicationContext(),"프로필 이미지 편집을 지원하지 않습니다",Toast.LENGTH_SHORT).show();
    }
}