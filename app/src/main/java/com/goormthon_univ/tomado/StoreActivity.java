package com.goormthon_univ.tomado;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.goormthon_univ.tomado.Adapter.DictionaryAdapter;
import com.goormthon_univ.tomado.Adapter.StoreAdapter;
import com.goormthon_univ.tomado.Adapter.Tomado;

public class StoreActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);

        //토마상점 리사이클러뷰 어뎁터 연결
        RecyclerView store_recyclerview=findViewById(R.id.store_recyclerview);

        StoreAdapter store_adapter=new StoreAdapter();

        GridLayoutManager layoutManager_=new GridLayoutManager(this,2);
        store_recyclerview.setLayoutManager(layoutManager_);
        store_recyclerview.setAdapter(store_adapter);

        Tomado t_1=new Tomado("1","https://i.ibb.co/1d94Q4f/image.png","열정맨 토마두","집중을 위한 여정을 떠난 탐험가 토마두, 앞으로 어떤 토마두들을 만나게 될까요?","25");

        store_adapter.addItem(t_1);
        store_adapter.addItem(t_1);
        store_adapter.addItem(t_1);
        store_adapter.addItem(t_1);
        store_adapter.addItem(t_1);
        store_adapter.addItem(t_1);

        //서버에서 불러오기
        store_adapter.notifyDataSetChanged();
    }
}