package com.goormthon_univ.tomado.Adapter;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.goormthon_univ.tomado.MainActivity;
import com.goormthon_univ.tomado.R;

import java.util.ArrayList;

public class StoreAdapter extends RecyclerView.Adapter<StoreAdapter.ViewHolder>{
    private int selectedItemPosition=0;
    ArrayList<Tomado> items=new ArrayList<>();

    @NonNull
    @Override
    public StoreAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View itemView;
        itemView=inflater.inflate(R.layout.recyclerview_store,parent,false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull StoreAdapter.ViewHolder holder, int position) {
        Tomado item=items.get(position);
        holder.setItem(item);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedItemPosition=position;
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(Tomado item){
        items.add(item);
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        LinearLayout recyclerview_store_layout;
        TextView recyclerview_store_tomato;
        TextView recyclerview_store_name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            recyclerview_store_layout=itemView.findViewById(R.id.recyclerview_store_layout);
            recyclerview_store_tomato=itemView.findViewById(R.id.recyclerview_store_tomato);
            recyclerview_store_name=itemView.findViewById(R.id.recyclerview_store_name);
        }

        public void setItem(Tomado item){
            recyclerview_store_tomato.setText(item.tomato);
            recyclerview_store_name.setText(item.name);

            Dialog store_dialog=new Dialog(itemView.getContext());
            store_dialog.setContentView(R.layout.dialog_store);
            store_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            store_dialog.findViewById(R.id.dialog_store_close).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    store_dialog.dismiss();
                }
            });
            //다이얼로그 뷰 연결
            TextView dialog_store_tomato=store_dialog.findViewById(R.id.dialog_store_tomato);
            TextView dialog_store_name=store_dialog.findViewById(R.id.dialog_store_name);
            TextView dialog_store_content=store_dialog.findViewById(R.id.dialog_store_content);
            ImageView dialog_store_image=store_dialog.findViewById(R.id.dialog_store_image);
            Button dialog_store_button=store_dialog.findViewById(R.id.dialog_store_button);

            dialog_store_tomato.setText(item.tomato);
            dialog_store_name.setText(item.name);
            dialog_store_content.setText(item.content);
            dialog_store_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(itemView.getContext(),"구매",Toast.LENGTH_SHORT).show();
                }
            });
            //TODO 이미지 연결 필요



            recyclerview_store_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    store_dialog.show();
                }
            });
        }
    }
}
