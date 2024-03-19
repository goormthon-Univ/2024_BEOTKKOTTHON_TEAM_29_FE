package com.goormthon_univ.tomado;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.goormthon_univ.tomado.Adapter.Club;
import com.goormthon_univ.tomado.Adapter.ClubAdapter;
import com.goormthon_univ.tomado.Adapter.Memo;
import com.goormthon_univ.tomado.Adapter.MemoAdapter;

public class FriendActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);

        //진행중인 토마클럽 리사이클러뷰 어뎁터 연결
        RecyclerView friend_ing_recyclerview=findViewById(R.id.friend_ing_recyclerview);

        ClubAdapter club_adapter=new ClubAdapter();

        LinearLayoutManager layoutManager=new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        friend_ing_recyclerview.setLayoutManager(layoutManager);
        friend_ing_recyclerview.setAdapter(club_adapter);

        Club m_1=new Club("Spring 기초강의","",50,30,"매일 강의 2개씩 수강하고 복습하기!","2024. 03. 15","2024. 04. 15",false);
        Club m_2=new Club("Spring 기초강의","",50,30,"매일 강의 2개씩 수강하고 복습하기!","2024. 03. 15","2024. 04. 15",false);
        Club m_3=new Club("Spring 기초강의","",50,30,"매일 강의 2개씩 수강하고 복습하기!","2024. 03. 15","2024. 04. 15",false);

        club_adapter.addItem(m_1);
        club_adapter.addItem(m_2);
        club_adapter.addItem(m_3);
        club_adapter.notifyDataSetChanged();

        //완료한 토마클럽 리사이클러뷰 어뎁터 연결
        RecyclerView friend_finish_recyclerview=findViewById(R.id.friend_finish_recyclerview);

        ClubAdapter club_finish_adapter=new ClubAdapter();

        LinearLayoutManager layoutManager_finish=new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        friend_finish_recyclerview.setLayoutManager(layoutManager_finish);
        friend_finish_recyclerview.setAdapter(club_finish_adapter);

        club_finish_adapter.addItem(m_1);
        club_finish_adapter.addItem(m_2);
        club_finish_adapter.addItem(m_3);
        club_finish_adapter.notifyDataSetChanged();
    }

    public void onclick_close(View view){
        finish();
    }
}