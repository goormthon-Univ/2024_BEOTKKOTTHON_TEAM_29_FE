package com.goormthon_univ.tomado;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.goormthon_univ.tomado.Adapter.Club;
import com.goormthon_univ.tomado.Adapter.ClubAdapter;
import com.goormthon_univ.tomado.Adapter.Dictionary;
import com.goormthon_univ.tomado.Adapter.Memo;
import com.goormthon_univ.tomado.Adapter.MemoAdapter;
import com.goormthon_univ.tomado.Server.ServerManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FriendActivity extends AppCompatActivity {

    ServerManager server_manager;
    SharedPreferences pf;

    String user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);

        //서버 연동 객체 추가
        server_manager=new ServerManager(getApplicationContext());

        //SharedPreferences
        pf=getSharedPreferences("preferences", Activity.MODE_PRIVATE);
        user_id=server_manager.get_user_id(pf);

        //뷰 연결
        ConstraintLayout friend_button=findViewById(R.id.friend_button);

        //진행중인 토마클럽 리사이클러뷰 어뎁터 연결
        RecyclerView friend_ing_recyclerview=findViewById(R.id.friend_ing_recyclerview);

        ClubAdapter club_adapter=new ClubAdapter();

        LinearLayoutManager layoutManager=new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        friend_ing_recyclerview.setLayoutManager(layoutManager);
        friend_ing_recyclerview.setAdapter(club_adapter);

        //완료한 토마클럽 리사이클러뷰 어뎁터 연결
        RecyclerView friend_finish_recyclerview=findViewById(R.id.friend_finish_recyclerview);

        ClubAdapter club_finish_adapter=new ClubAdapter();

        LinearLayoutManager layoutManager_finish=new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        friend_finish_recyclerview.setLayoutManager(layoutManager_finish);
        friend_finish_recyclerview.setAdapter(club_finish_adapter);

        //서버에서 불러오기
        try {
            JSONObject json=new JSONObject(server_manager.http_request_get_json("/clubs/"+user_id));

            if(json.get("message").toString().equals("토마 클럽 리스트 조회 성공")){
                JSONObject json_data=new JSONObject(json.get("data").toString());
                JSONArray club_list_array=new JSONArray(json_data.get("clubList").toString());

                for(int i=0;i<club_list_array.length();i++){
                    JSONObject data=new JSONObject(club_list_array.get(i).toString());
                    Club c_1=new Club(data.get("club_id").toString(),
                            data.get("title").toString(),
                            data.get("colorType").toString(),
                            data.get("goal").toString(),
                            data.get("current_amount").toString(),
                            data.get("memo").toString(),
                            data.get("start_date").toString(),
                            data.get("end_date").toString(),
                            data.get("completed").toString(),
                            data.get("memberList").toString());
                    if(data.get("completed").toString().equals("true")){
                        club_finish_adapter.addItem(c_1);
                    }else{
                        club_adapter.addItem(c_1);
                    }

                }
            }else{
                //토마 보감 조회 실패 시 실패 원인 보여줌
                Toast.makeText(getApplicationContext(),json.get("message").toString(),Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        club_finish_adapter.notifyDataSetChanged();
        club_adapter.notifyDataSetChanged();

        //토마클럽 만들기 버튼 리스너
        Intent friend_create_intent=new Intent(this,FriendCreateActivity.class);
        friend_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(friend_create_intent,101);
            }
        });
    }

    public void onclick_close(View view){
        finish();
    }
}