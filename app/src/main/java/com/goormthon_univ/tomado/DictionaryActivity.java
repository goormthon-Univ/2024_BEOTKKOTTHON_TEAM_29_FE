package com.goormthon_univ.tomado;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.goormthon_univ.tomado.Adapter.Category;
import com.goormthon_univ.tomado.Adapter.Club;
import com.goormthon_univ.tomado.Adapter.ClubAdapter;
import com.goormthon_univ.tomado.Adapter.Dictionary;
import com.goormthon_univ.tomado.Adapter.DictionaryAdapter;
import com.goormthon_univ.tomado.Server.ServerManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DictionaryActivity extends AppCompatActivity {
    SharedPreferences pf;

    String user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionary);

        //서버 연동 객체 추가
        ServerManager server_manager=new ServerManager(getApplicationContext());

        //SharedPreferences
        pf=getSharedPreferences("preferences", Activity.MODE_PRIVATE);
        user_id=server_manager.get_user_id(pf);

        //최근 얻은 토마두 리사이클러뷰 어뎁터 연결
        RecyclerView dictionary_recent_recyclerview=findViewById(R.id.dictionary_recent_recyclerview);

        DictionaryAdapter dictionary_adapter=new DictionaryAdapter();

        GridLayoutManager layoutManager=new GridLayoutManager(this,3);
        dictionary_recent_recyclerview.setLayoutManager(layoutManager);
        dictionary_recent_recyclerview.setAdapter(dictionary_adapter);

        //나의 토마두 리사이클러뷰 어뎁터 연결
        RecyclerView dictionary_my_recyclerview=findViewById(R.id.dictionary_my_recyclerview);

        DictionaryAdapter dictionary_my_adapter=new DictionaryAdapter();

        GridLayoutManager layoutManager_my=new GridLayoutManager(this,3);
        dictionary_my_recyclerview.setLayoutManager(layoutManager_my);
        dictionary_my_recyclerview.setAdapter(dictionary_my_adapter);

        //서버에서 불러오기
        try {
            JSONObject json=new JSONObject(server_manager.http_request_get_json("/book/users/"+user_id+"/tomados"));

            if(json.get("message").toString().equals("토마 도감 보기 성공")){
                JSONArray tomado_list_array=new JSONArray(json.get("data").toString());

                for(int i=0;i< tomado_list_array.length();i++){
                    JSONObject data=new JSONObject(tomado_list_array.get(i).toString());
                    Dictionary dictionary=new Dictionary(data.get("name").toString(),
                            data.get("url").toString()
                    );
                    dictionary_my_adapter.addItem(dictionary);
                    dictionary_adapter.addItem(dictionary);
                }
            }else{
                //토마 보감 조회 실패 시 실패 원인 보여줌
                Toast.makeText(getApplicationContext(),json.get("message").toString(),Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        dictionary_adapter.notifyDataSetChanged();
        dictionary_my_adapter.notifyDataSetChanged();
    }

    public void onclick_close(View view){
        finish();
    }
}