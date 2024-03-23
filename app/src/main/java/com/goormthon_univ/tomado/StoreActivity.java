package com.goormthon_univ.tomado;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.goormthon_univ.tomado.Adapter.DictionaryAdapter;
import com.goormthon_univ.tomado.Adapter.Memo;
import com.goormthon_univ.tomado.Adapter.StoreAdapter;
import com.goormthon_univ.tomado.Adapter.Tomado;
import com.goormthon_univ.tomado.Server.ServerManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class StoreActivity extends AppCompatActivity {
    ServerManager server_manager;

    SharedPreferences pf;

    String user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);

        //서버 연동 객체 추가
        server_manager=new ServerManager(getApplicationContext());

        //SharedPreferences
        pf=getSharedPreferences("preferences", Activity.MODE_PRIVATE);
        user_id=server_manager.get_user_id(pf);

        //토마상점 리사이클러뷰 어뎁터 연결
        RecyclerView store_recyclerview=findViewById(R.id.store_recyclerview);

        StoreAdapter store_adapter=new StoreAdapter();

        GridLayoutManager layoutManager_=new GridLayoutManager(this,2);
        store_recyclerview.setLayoutManager(layoutManager_);
        store_recyclerview.setAdapter(store_adapter);

        //구매하지 않은 목록 서버에서 불러오기
        try {
            JSONObject json=new JSONObject(server_manager.http_request_get_json("/shop?user="+server_manager.get_user_id((SharedPreferences)getSharedPreferences("preferences", Activity.MODE_PRIVATE))));

            if(json.get("message").toString().equals("캐릭터 조회 성공")){
                JSONObject json_data=new JSONObject(json.get("data").toString());
                JSONArray tomado_list_array=new JSONArray(json_data.get("tomadoNotHaveList").toString());

                for(int i=0;i<tomado_list_array.length();i++){
                    JSONObject data=new JSONObject(tomado_list_array.get(i).toString());

                    JSONObject detail_find=new JSONObject(server_manager.http_request_get_json("/shop/"+data.get("tomado_id")));
                    JSONObject data_detail_find=new JSONObject(detail_find.get("data").toString());
                    Tomado tomado=new Tomado(data_detail_find.get("tomado_id").toString(),
                            data_detail_find.get("url").toString(),
                            data_detail_find.get("name").toString(),
                            data_detail_find.get("content").toString(),
                            data_detail_find.get("tomato").toString(),
                            false
                    );
                    store_adapter.addItem(tomado);
                }
            }else{
                //캐릭터 조회 실패 시 실패 원인 보여줌
                Toast.makeText(getApplicationContext(),json.get("message").toString(),Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        //구매한 목록 서버에서 불러오기
        try {
            JSONObject json=new JSONObject(server_manager.http_request_get_json("/shop?user="+server_manager.get_user_id((SharedPreferences)getSharedPreferences("preferences", Activity.MODE_PRIVATE))));

            if(json.get("message").toString().equals("캐릭터 조회 성공")){
                JSONObject json_data=new JSONObject(json.get("data").toString());
                JSONArray tomado_list_array=new JSONArray(json_data.get("tomadoHaveList").toString());

                for(int i=0;i<tomado_list_array.length();i++){
                    JSONObject data=new JSONObject(tomado_list_array.get(i).toString());

                    JSONObject detail_find=new JSONObject(server_manager.http_request_get_json("/shop/"+data.get("tomado_id")));
                    JSONObject data_detail_find=new JSONObject(detail_find.get("data").toString());
                    Tomado tomado=new Tomado(data_detail_find.get("tomado_id").toString(),
                            data_detail_find.get("url").toString(),
                            data_detail_find.get("name").toString(),
                            data_detail_find.get("content").toString(),
                            data_detail_find.get("tomato").toString(),
                            true
                    );
                    store_adapter.addItem(tomado);
                }
            }else{
                //캐릭터 조회 실패 시 실패 원인 보여줌
                Toast.makeText(getApplicationContext(),json.get("message").toString(),Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        store_adapter.notifyDataSetChanged();

        //서버에서 현재 포인트 불러오기
        try {
            JSONObject json=new JSONObject(server_manager.http_request_get_json("/users/"+user_id));
            JSONObject data=new JSONObject(json.get("data").toString());

            if(json.get("message").toString().equals("회원 조회 성공")){
                //뷰 연결하고
                TextView store_tomato=findViewById(R.id.store_tomato);

                //텍스트설정
                store_tomato.setText(data.get("tomato").toString());

            }else{
                //회원 조회 실패 시 실패 원인 보여줌
                Toast.makeText(getApplicationContext(),json.get("message").toString(),Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public void onclick_close(View view){
        finish();
    }
}