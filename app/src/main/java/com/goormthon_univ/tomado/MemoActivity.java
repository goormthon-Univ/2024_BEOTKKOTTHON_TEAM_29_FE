package com.goormthon_univ.tomado;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.goormthon_univ.tomado.Adapter.Category;
import com.goormthon_univ.tomado.Adapter.CategoryAdapter;
import com.goormthon_univ.tomado.Adapter.Memo;
import com.goormthon_univ.tomado.Adapter.MemoAdapter;

public class MemoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo);

        //리사이클러뷰 어뎁터 연결
        RecyclerView dashboard_memo_recyclerview=findViewById(R.id.dashboard_memo_recyclerview);

        MemoAdapter memo_adapter=new MemoAdapter();

        LinearLayoutManager layoutManager=new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        dashboard_memo_recyclerview.setLayoutManager(layoutManager);
        dashboard_memo_recyclerview.setAdapter(memo_adapter);

        Memo m_1=new Memo("0","이따가 점심 뭐먹지 .. 오늘 파스타가 좀 땡긴다 어쩌고","2023. 10. 11");
        Memo m_2=new Memo("1","과사 방문하기\n" +
                "동방에서 충전기 챙기기\n" +
                "11시까지 집가기\n" +
                "\n" +
                "가는길에 볼펜이랑 화이트 사가기\n" +
                "잊지마시오~!","2023. 10. 11");
        Memo m_3=new Memo("2","이따가 점심 뭐먹지 .. 오늘 파스타가 좀 땡긴다 어쩌고","2023. 10. 11");

        memo_adapter.addItem(m_1);
        memo_adapter.addItem(m_2);
        memo_adapter.addItem(m_3);
        memo_adapter.notifyDataSetChanged();
    }

    public void onclick_close(View view){
        finish();
    }
}