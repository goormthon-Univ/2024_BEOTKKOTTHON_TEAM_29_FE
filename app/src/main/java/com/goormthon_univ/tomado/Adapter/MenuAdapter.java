package com.goormthon_univ.tomado.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.goormthon_univ.tomado.DashboardActivity;
import com.goormthon_univ.tomado.FriendActivity;
import com.goormthon_univ.tomado.MemoActivity;
import com.goormthon_univ.tomado.R;

import java.util.ArrayList;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder>{
    ArrayList<Menu> items=new ArrayList<>();

    Context context;

    public MenuAdapter(Context context){
        Menu m1=new Menu(R.drawable.dashboard,"모아보기");
        Menu m2=new Menu(R.drawable.user_friends,"함께하기");
        Menu m3=new Menu(R.drawable.dictionary_language_book,"토마보감");
        Menu m4=new Menu(R.drawable.store,"토마상점");
        Menu m5=new Menu(R.drawable.note,"긴급메모");
        items.add(m1);
        items.add(m2);
        items.add(m3);
        items.add(m4);
        items.add(m5);

        this.context=context;
    }

    @NonNull
    @Override
    public MenuAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View itemView=inflater.inflate(R.layout.recyclerview_menu,parent,false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuAdapter.ViewHolder holder, int position) {
        Menu item=items.get(position);
        holder.setItem(item);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch(position){
                    case 0:
                        Intent intent_dashboardactivity=new Intent(context, DashboardActivity.class);
                        intent_dashboardactivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent_dashboardactivity);
                        break;
                    case 1:
                        Intent intent_friendactivity=new Intent(context, FriendActivity.class);
                        intent_friendactivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent_friendactivity);
                        break;
                    case 2:
                        //
                        Toast.makeText(context,"E: 토마보감-추가 예정인 기능입니다",Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        //
                        Toast.makeText(context,"E: 토마상점-추가 예정인 기능입니다",Toast.LENGTH_SHORT).show();
                        break;
                    case 4:
                        Intent intent_memoactivity=new Intent(context, MemoActivity.class);
                        intent_memoactivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent_memoactivity);
                        break;
                }
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView recyclerview_menu_image;
        TextView recyclerview_menu_name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            recyclerview_menu_image=itemView.findViewById(R.id.recyclerview_menu_image);
            recyclerview_menu_name=itemView.findViewById(R.id.recyclerview_menu_name);
        }

        public void setItem(Menu item){
            recyclerview_menu_image.setImageResource(item.resource);
            recyclerview_menu_name.setText(item.name);
        }
    }

    public static class Menu{
        int resource;
        String name;
        public Menu(int resource,String name){
            this.resource=resource;
            this.name=name;
        }
    }
}
