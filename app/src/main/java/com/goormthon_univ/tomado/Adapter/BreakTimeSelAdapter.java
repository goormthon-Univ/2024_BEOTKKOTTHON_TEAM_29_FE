package com.goormthon_univ.tomado.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.goormthon_univ.tomado.R;

import org.w3c.dom.Comment;

import java.util.ArrayList;

public class BreakTimeSelAdapter extends RecyclerView.Adapter<BreakTimeSelAdapter.ViewHolder>{
    ArrayList<BreakTimeSel> items=new ArrayList<>();

    @NonNull
    @Override
    public BreakTimeSelAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View itemView=inflater.inflate(R.layout.recyclerview_breaktimesel,parent,false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull BreakTimeSelAdapter.ViewHolder holder, int position) {
        BreakTimeSel item=items.get(position);
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

    public void addItem(BreakTimeSel item){
        items.add(item);
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView breaktimesel_description;
        TextView breaktimesel_time;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            breaktimesel_description=itemView.findViewById(R.id.breaktimesel_description);
            breaktimesel_time=itemView.findViewById(R.id.breaktimesel_time);
        }

        public void setItem(BreakTimeSel item){
            breaktimesel_description.setText(item.description);
            breaktimesel_time.setText(String.valueOf(item.time)+"m");
        }
    }
}
