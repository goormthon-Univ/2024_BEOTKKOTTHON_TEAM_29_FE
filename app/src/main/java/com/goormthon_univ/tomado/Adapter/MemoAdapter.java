package com.goormthon_univ.tomado.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.goormthon_univ.tomado.R;

import java.util.ArrayList;

public class MemoAdapter extends RecyclerView.Adapter<MemoAdapter.ViewHolder>{
    ArrayList<Memo> items=new ArrayList<>();

    @NonNull
    @Override
    public MemoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View itemView=inflater.inflate(R.layout.recyclerview_memo,parent,false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MemoAdapter.ViewHolder holder, int position) {
        Memo item=items.get(position);
        holder.setItem(item);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(Memo item){
        items.add(item);
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView recyclerview_memo_content;
        TextView recyclerview_memo_date;
        ImageView recyclerview_memo_delete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            recyclerview_memo_content=itemView.findViewById(R.id.recyclerview_memo_content);
            recyclerview_memo_date=itemView.findViewById(R.id.recyclerview_memo_date);
            recyclerview_memo_delete=itemView.findViewById(R.id.recyclerview_memo_delete);
        }

        public void setItem(Memo item){
            recyclerview_memo_content.setText(item.content);
            recyclerview_memo_date.setText(item.created_at);
        }
    }
}
