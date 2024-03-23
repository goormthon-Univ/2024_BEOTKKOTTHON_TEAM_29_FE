package com.goormthon_univ.tomado.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.goormthon_univ.tomado.R;

import java.util.ArrayList;

public class DictionaryAdapter extends RecyclerView.Adapter<DictionaryAdapter.ViewHolder>{
    private int selectedItemPosition=0;
    ArrayList<Dictionary> items=new ArrayList<>();

    @NonNull
    @Override
    public DictionaryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View itemView;
        itemView=inflater.inflate(R.layout.recyclerview_dictionary,parent,false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DictionaryAdapter.ViewHolder holder, int position) {
        Dictionary item=items.get(position);
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

    public void addItem(Dictionary item){
        items.add(item);
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView recyclerview_dictionary_name;
        ImageView recyclerview_dictionary_image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            recyclerview_dictionary_name=itemView.findViewById(R.id.recyclerview_dictionary_name);
            recyclerview_dictionary_image=itemView.findViewById(R.id.recyclerview_dictionary_image);
        }

        public void setItem(Dictionary item){
            recyclerview_dictionary_name.setText(item.name);
            Glide.with(itemView.getContext()).load(item.url).into(recyclerview_dictionary_image);
        }
    }
}
