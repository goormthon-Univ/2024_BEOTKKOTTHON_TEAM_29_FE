package com.goormthon_univ.tomado.Adapter;

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

import com.goormthon_univ.tomado.R;

import java.util.ArrayList;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder>{
    ArrayList<Menu> items=new ArrayList<>();

    public MenuAdapter(){
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
                        //
                        break;
                    case 1:
                        //
                        break;
                    case 2:
                        //
                        break;
                    case 3:
                        //
                        break;
                    case 4:
                        //
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
