package com.goormthon_univ.tomado.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.goormthon_univ.tomado.MainActivity;
import com.goormthon_univ.tomado.R;
import com.goormthon_univ.tomado.Server.ServerManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder>{
    //서버 관리 객체
    ServerManager server_manager;

    //리사이클러뷰 뷰
    LinearLayout recyclerview_category_layout;
    TextView recyclerview_category_title;
    TextView recyclerview_category_tomato;
    TextView recyclerview_category_add;

    //다이얼로그 뷰
    TextView dialog_category_add_text;
    EditText dialog_category_add_edittext;
    ImageButton dialog_category_add_delete;

    private int selectedItemPosition=0;
    TextView main_category_text;
    ArrayList<Category> items=new ArrayList<>();

    /*
    1번 메인 화면 카테고리 리사이클러뷰
    2번 모아보기 화면 카테고리 리사이클러뷰
     */
    int mode;

    Context context;

    public CategoryAdapter(TextView main_category_text,int mode,Context context){
        this.main_category_text=main_category_text;
        this.mode=mode;
        this.context=context;

        server_manager=new ServerManager(context);
    }

    @NonNull
    @Override
    public CategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View itemView;
        if (mode==1){
            itemView=inflater.inflate(R.layout.recyclerview_category_main,parent,false);
        }else{
            itemView=inflater.inflate(R.layout.recyclerview_category_dashboard,parent,false);
        }

        return new ViewHolder(itemView,mode);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.ViewHolder holder, int position) {
        Category item=items.get(position);
        holder.setItem(item);

        /*
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedItemPosition=position;
                //notifyDataSetChanged();
            }
        });*/

        if(selectedItemPosition==position && mode==1){
            main_category_text.setText(item.title);
        }

        //뷰들
        recyclerview_category_layout=holder.itemView.findViewById(R.id.recyclerview_category_layout);
        recyclerview_category_title=holder.itemView.findViewById(R.id.recyclerview_category_title);
        recyclerview_category_tomato=holder.itemView.findViewById(R.id.recyclerview_category_tomato);

        recyclerview_category_add=holder.itemView.findViewById(R.id.recyclerview_category_add);


        recyclerview_category_title.setText(item.title);
        if(mode!=1){
            recyclerview_category_tomato.setText(String.valueOf(item.tomato));

            if(item.add_button){
                //추가 버튼인 경우
                recyclerview_category_title.setVisibility(View.INVISIBLE);
                recyclerview_category_add.setVisibility(View.VISIBLE);
                if(mode!=1){
                    recyclerview_category_tomato.setVisibility(View.INVISIBLE);
                }
                recyclerview_category_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //다이얼로그 표시
                        dialog_category_add(true,item.title,item.category_id);
                    }
                });
            }else{
                //추가 버튼이 아닌 경우
                recyclerview_category_title.setVisibility(View.VISIBLE);
                recyclerview_category_add.setVisibility(View.INVISIBLE);
                if(mode!=1){
                    recyclerview_category_tomato.setVisibility(View.VISIBLE);
                }
                recyclerview_category_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //다이얼로그 표시
                        dialog_category_add(false,item.title,item.category_id);
                    }
                });
            }
        }
    }

    private void dialog_category_add(boolean add_button,String title,String category_id){
        Dialog dialog_category_add=new Dialog(context);
        dialog_category_add.setContentView(R.layout.dialog_category_add);
        dialog_category_add.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog_category_add.getWindow().setGravity(Gravity.BOTTOM);

        //다이얼로그 뷰 연결
        dialog_category_add_text=dialog_category_add.findViewById(R.id.dialog_category_add_text);
        dialog_category_add_edittext=dialog_category_add.findViewById(R.id.dialog_category_add_edittext);
        dialog_category_add_delete=dialog_category_add.findViewById(R.id.dialog_category_add_delete);

        //추가 버튼이 아닌 경우 텍스트 불러오기
        if(!add_button){
            dialog_category_add_edittext.setText(title);
        }

        //글자 입력 전 텍스트 글자 수(0/8) 설정
        dialog_category_add_text.setText(String.format("%d / %d",dialog_category_add_edittext.getText().length(),8));

        //텍스트 수를 표시하기 위한 리스너
        dialog_category_add_edittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                dialog_category_add_text.setText(String.format("%d / %d",count,8));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //다이얼로그에서 삭제 버튼 눌렀을 때 리스너
        dialog_category_add_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(add_button){
                    //추가 버튼인 경우
                    dialog_category_add.hide();
                }else{
                    //추가 버튼이 아닌 경우
                    //삭제 후
                    dialog_category_delete_fn(category_id);

                    //다이얼로그 닫음
                    dialog_category_add.hide();
                }
            }
        });

        //다이얼로그 바깥 부분 눌렀을 때 리스너
        dialog_category_add.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if(add_button){
                    //추가 버튼인 경우
                    if(dialog_category_add_edittext.getText().length()>0){
                        //글자 수가 0보다 클 경우
                        dialog_category_add_fn();
                    }else{
                        Toast.makeText(context,"글자가 입력되지 않아서 저장을 취소합니다",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    //추가 버튼이 아닌 경우
                    if(dialog_category_add_edittext.getText().length()>0 && !dialog_category_add_edittext.getText().toString().equals(title)) {
                        //글자 수가 0보다 클 경우
                        Log.d("",dialog_category_add_edittext.getText().toString());
                        dialog_category_edit_fn(category_id,dialog_category_add_edittext.getText().toString());
                    }else{
                        Toast.makeText(context,"글자가 입력되지 않거나 변경되지 않아서 저장을 취소합니다",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        dialog_category_add.show();
    }

    private void dialog_category_add_fn(){
        JSONObject parms=new JSONObject();
        try {
            parms.put("user_id",server_manager.user_id);
            parms.put("title",dialog_category_add_edittext.getText().toString());
            parms.put("color","RED");
            JSONObject json=new JSONObject(server_manager.http_request_post_json("/categories",parms));

            if(json.get("message").toString().equals("카테고리 생성 성공")){
                refreshItem();
                Toast.makeText(context,"카테고리에 추가되었습니다",Toast.LENGTH_SHORT).show();
            }else{
                //카테고리 추가 실패 시 실패 원인 보여줌
                Toast.makeText(context,json.get("message").toString(),Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private void dialog_category_edit_fn(String category_id,String edit_title){
        JSONObject parms=new JSONObject();
        try {
            parms.put("user_id",server_manager.user_id);
            parms.put("title",edit_title);
            parms.put("color","RED");
            JSONObject json=new JSONObject(server_manager.http_request_put_json("/categories/"+category_id,parms));

            if(json.get("message").toString().equals("카테고리 수정 성공")){
                refreshItem();
                Toast.makeText(context,"카테고리가 변경되었습니다",Toast.LENGTH_SHORT).show();
            }else{
                //카테고리 수정 실패 시 실패 원인 보여줌
                Toast.makeText(context,json.get("message").toString(),Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private void dialog_category_delete_fn(String category_id){
        JSONObject parms=new JSONObject();
        try {
            JSONObject json=new JSONObject(server_manager.http_request_delete_json("/categories?user="+ServerManager.user_id+"&category="+category_id));

            if(json.get("message").toString().equals("카테고리 삭제 성공")){
                refreshItem();
                Toast.makeText(context,"카테고리가 삭제되었습니다",Toast.LENGTH_SHORT).show();
            }else{
                //카테고리 삭제 실패 시 실패 원인 보여줌
                Toast.makeText(context,json.get("message").toString(),Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(Category item){
        items.add(item);
    }

    public void deleteItem(Category item){
        //아이템의 위치 찾기
        int position=items.indexOf(item);

        //아이템 삭제
        items.remove(position);

        //어뎁터에 삭제했음을 알리기
        notifyItemRemoved(position);
        notifyItemRangeRemoved(position, items.size());
    }

    public void refreshItem(){
        //서버 연동 객체 추가
        ServerManager server_manager=new ServerManager(context);

        items=new ArrayList<>();

        //서버에서 불러오기
        try {
            JSONObject json=new JSONObject(server_manager.http_request_get_json("/categories/"+ServerManager.user_id));

            if(json.get("message").toString().equals("카테고리 조회 성공")){
                JSONObject json_data=new JSONObject(json.get("data").toString());
                JSONArray category_list_array=new JSONArray(json_data.get("categoryList").toString());

                for(int i=0;i< category_list_array.length();i++){
                    JSONObject data=new JSONObject(category_list_array.get(i).toString());
                    Category category=new Category(data.get("category_id").toString(),
                            data.get("title").toString(),
                            data.get("color").toString(),
                            Integer.parseInt(data.get("tomato").toString()),
                            false);
                    items.add(category);
                }
            }else{
                //메모 조회 실패 시 실패 원인 보여줌
                Toast.makeText(context,json.get("message").toString(),Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        Category c_0=new Category("","","",0,true);
        items.add(c_0);
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        public ViewHolder(@NonNull View itemView, int mode) {
            super(itemView);
        }

        public void setItem(Category item){
        }
    }
}
