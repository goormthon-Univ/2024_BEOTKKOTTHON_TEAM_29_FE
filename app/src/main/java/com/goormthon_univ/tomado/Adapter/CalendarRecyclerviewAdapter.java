package com.goormthon_univ.tomado.Adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.goormthon_univ.tomado.R;
import com.goormthon_univ.tomado.Server.ServerManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CalendarRecyclerviewAdapter extends RecyclerView.Adapter<CalendarRecyclerviewAdapter.ViewHolder>{
    Context context;
    ArrayList<CalendarRecyclerview> items=new ArrayList<>();

    //서버 관리 객체 추가
    ServerManager server_manager;

    String user_id;

    public CalendarRecyclerviewAdapter(){
        //this.context=context;
        //this.user_id=user_id;

        //server_manager=new ServerManager(context);
    }

    @NonNull
    @Override
    public CalendarRecyclerviewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View itemView=inflater.inflate(R.layout.recyclerview_calendar_dashboard,parent,false);

        return new ViewHolder(itemView,context);
    }

    @Override
    public void onBindViewHolder(@NonNull CalendarRecyclerviewAdapter.ViewHolder holder, int position) {
        CalendarRecyclerview item=items.get(position);
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

    public void addItem(CalendarRecyclerview item){
        items.add(item);
    }

    public void deleteItem(CalendarRecyclerview item){
        //아이템의 위치 찾기
        int position=items.indexOf(item);

        //아이템 삭제
        items.remove(position);

        //어뎁터에 삭제했음을 알리기
        notifyItemRemoved(position);
        notifyItemRangeRemoved(position, items.size());
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView recyclerview_calendar_title;
        TextView recyclerview_calendar_tomato;
        ConstraintLayout recyclerview_calendar_layout;

        public ViewHolder(@NonNull View itemView, Context context) {
            super(itemView);

            recyclerview_calendar_title=itemView.findViewById(R.id.recyclerview_calendar_title);
            recyclerview_calendar_tomato=itemView.findViewById(R.id.recyclerview_calendar_tomato);
            recyclerview_calendar_layout=itemView.findViewById(R.id.recyclerview_calendar_layout);
        }

        public void setItem(CalendarRecyclerview item){
            recyclerview_calendar_title.setText(item.title);
            recyclerview_calendar_tomato.setText(item.tomato);
            Drawable drawable;
            switch(item.color){
                case "RED":
                    drawable=itemView.getContext().getResources().getDrawable(R.drawable.rect_radius_low);
                    drawable.setTint(itemView.getContext().getColor(R.color.category_orange));
                    recyclerview_calendar_layout.setBackgroundDrawable(drawable);
                    recyclerview_calendar_title.setTextColor(itemView.getContext().getColor(R.color.category_orange_text));
                    recyclerview_calendar_tomato.setTextColor(itemView.getContext().getColor(R.color.category_orange_text));
                    break;
                case "BLUE":
                    drawable=itemView.getContext().getResources().getDrawable(R.drawable.rect_radius_low);
                    drawable.setTint(itemView.getContext().getColor(R.color.category_blue));
                    recyclerview_calendar_layout.setBackgroundDrawable(drawable);
                    recyclerview_calendar_title.setTextColor(itemView.getContext().getColor(R.color.category_blue_text));
                    recyclerview_calendar_tomato.setTextColor(itemView.getContext().getColor(R.color.category_blue_text));
                    break;
                case "GREEN":
                    drawable=itemView.getContext().getResources().getDrawable(R.drawable.rect_radius_low);
                    drawable.setTint(itemView.getContext().getColor(R.color.category_green));
                    recyclerview_calendar_layout.setBackgroundDrawable(drawable);
                    recyclerview_calendar_title.setTextColor(itemView.getContext().getColor(R.color.category_green_text));
                    recyclerview_calendar_tomato.setTextColor(itemView.getContext().getColor(R.color.category_green_text));
                    break;
                case "YELLOW":
                    drawable=itemView.getContext().getResources().getDrawable(R.drawable.rect_radius_low);
                    drawable.setTint(itemView.getContext().getColor(R.color.category_yellow));
                    recyclerview_calendar_layout.setBackgroundDrawable(drawable);
                    recyclerview_calendar_title.setTextColor(itemView.getContext().getColor(R.color.category_yellow_text));
                    recyclerview_calendar_tomato.setTextColor(itemView.getContext().getColor(R.color.category_yellow_text));
                    break;
            }
        }
    }
}
