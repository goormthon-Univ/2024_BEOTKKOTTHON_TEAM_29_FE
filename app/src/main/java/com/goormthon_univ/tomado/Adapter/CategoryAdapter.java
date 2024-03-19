package com.goormthon_univ.tomado.Adapter;

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
    ArrayList<Category> items=new ArrayList<>();

    @NonNull
    @Override
    public CategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View itemView=inflater.inflate(R.layout.recyclerview_category_dashboard,parent,false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.ViewHolder holder, int position) {
        Category item=items.get(position);
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

    public void addItem(Category item){
        items.add(item);
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        LinearLayout recyclerview_category_layout;
        TextView recyclerview_category_title;
        TextView recyclerview_category_tomato;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            recyclerview_category_layout=itemView.findViewById(R.id.recyclerview_category_layout);
            recyclerview_category_title=itemView.findViewById(R.id.recyclerview_category_title);
            recyclerview_category_tomato=itemView.findViewById(R.id.recyclerview_category_tomato);
        }

        public void setItem(Category item){
            recyclerview_category_title.setText(item.title);
            recyclerview_category_tomato.setText(String.valueOf(item.tomato));
        }
    }
}
