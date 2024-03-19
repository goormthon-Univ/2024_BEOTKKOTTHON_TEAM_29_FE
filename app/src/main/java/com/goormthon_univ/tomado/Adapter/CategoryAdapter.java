package com.goormthon_univ.tomado.Adapter;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.goormthon_univ.tomado.R;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder>{
    private int selectedItemPosition=0;
    TextView main_category_text;
    ArrayList<Category> items=new ArrayList<>();

    /*
    1번 메인 화면 카테고리 리사이클러뷰
    2번 모아보기 화면 카테고리 리사이클러뷰
     */
    int mode;

    public CategoryAdapter(TextView main_category_text,int mode){
        this.main_category_text=main_category_text;
        this.mode=mode;
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

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedItemPosition=position;
                notifyDataSetChanged();
            }
        });

        if(selectedItemPosition==position && mode==1){
            main_category_text.setText(item.title);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(Category item){
        items.add(item);
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        LinearLayout recyclerview_category_layout;
        TextView recyclerview_category_title;
        TextView recyclerview_category_tomato;
        int mode;

        public ViewHolder(@NonNull View itemView, int mode) {
            super(itemView);

            this.mode=mode;

            recyclerview_category_layout=itemView.findViewById(R.id.recyclerview_category_layout);
            recyclerview_category_title=itemView.findViewById(R.id.recyclerview_category_title);
            recyclerview_category_tomato=itemView.findViewById(R.id.recyclerview_category_tomato);
        }

        public void setItem(Category item){
            recyclerview_category_title.setText(item.title);
            if(mode!=1){
                recyclerview_category_tomato.setText(String.valueOf(item.tomato));
            }
        }
    }
}
