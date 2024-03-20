package com.goormthon_univ.tomado;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.goormthon_univ.tomado.Adapter.Category;
import com.goormthon_univ.tomado.Adapter.CategoryAdapter;
import com.goormthon_univ.tomado.Adapter.Memo;
import com.goormthon_univ.tomado.Adapter.MemoAdapter;
import com.goormthon_univ.tomado.Server.ServerManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MemoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo);

        //서버 연동 객체 추가
        ServerManager server_manager=new ServerManager(getApplicationContext());

        //유저 아이디(임시 설정)
        int user_id=35;

        //리사이클러뷰 어뎁터 연결
        RecyclerView dashboard_memo_recyclerview=findViewById(R.id.dashboard_memo_recyclerview);

        MemoAdapter memo_adapter=new MemoAdapter(getApplicationContext());

        LinearLayoutManager layoutManager=new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        dashboard_memo_recyclerview.setLayoutManager(layoutManager);
        dashboard_memo_recyclerview.setAdapter(memo_adapter);

        //서버에서 불러오기
        try {
            JSONObject json=new JSONObject(server_manager.http_request_get_json("/memos/"+user_id));

            if(json.get("message").toString().equals("메모 조회 성공")){
                JSONObject json_data=new JSONObject(json.get("data").toString());
                JSONArray memo_list_array=new JSONArray(json_data.get("memoList").toString());

                for(int i=0;i< memo_list_array.length();i++){
                    JSONObject data=new JSONObject(memo_list_array.get(i).toString());
                    Memo memo=new Memo(data.get("id").toString(),
                            data.get("content").toString(),
                            data.get("created_at").toString()
                    );
                    memo_adapter.addItem(memo);
                }
            }else{
                //메모 조회 실패 시 실패 원인 보여줌
                Toast.makeText(getApplicationContext(),json.get("message").toString(),Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        memo_adapter.notifyDataSetChanged();
    }

    public void onclick_close(View view){
        finish();
    }
}