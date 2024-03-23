package com.goormthon_univ.tomado.Adapter;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.goormthon_univ.tomado.MainActivity;
import com.goormthon_univ.tomado.R;
import com.goormthon_univ.tomado.Thread.TimerThread;

import org.w3c.dom.Comment;

import java.util.ArrayList;

public class BreakTimeSelAdapter extends RecyclerView.Adapter<BreakTimeSelAdapter.ViewHolder>{
    ArrayList<BreakTimeSel> items=new ArrayList<>();

    int select_position=0;

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
                select_position=position;
                notifyDataSetChanged();
            }
        });

        if(select_position==position){
            if(position==1){
                Drawable drawable_del=holder.itemView.getContext().getResources().getDrawable(R.drawable.rect_radius_bottom);
                drawable_del.setTint(Color.parseColor("#E2E4E8"));
                holder.breaktimesel_layout.setBackgroundDrawable(drawable_del);
                TimerThread.break_time_min=15;
            }else{
                holder.breaktimesel_layout.setBackgroundColor(Color.parseColor("#E2E4E8"));
                TimerThread.break_time_min=5;
            }
            holder.breaktimesel_layout.setVisibility(View.VISIBLE);
        }else{
            holder.breaktimesel_layout.setVisibility(View.GONE);
        }
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
        ImageView breaktimesel_layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            breaktimesel_layout=itemView.findViewById(R.id.breaktimesel_layout);
            breaktimesel_description=itemView.findViewById(R.id.breaktimesel_description);
            breaktimesel_time=itemView.findViewById(R.id.breaktimesel_time);
        }

        public void setItem(BreakTimeSel item){
            breaktimesel_description.setText(item.description);
            breaktimesel_time.setText(String.valueOf(item.time)+"m");
        }
    }
}
