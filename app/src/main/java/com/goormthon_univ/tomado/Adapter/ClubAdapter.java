package com.goormthon_univ.tomado.Adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.goormthon_univ.tomado.FriendCreateActivity;
import com.goormthon_univ.tomado.R;

import java.util.ArrayList;

public class ClubAdapter extends RecyclerView.Adapter<ClubAdapter.ViewHolder>{
    ArrayList<Club> items=new ArrayList<>();

    @NonNull
    @Override
    public ClubAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View itemView=inflater.inflate(R.layout.recyclerview_club,parent,false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ClubAdapter.ViewHolder holder, int position) {
        Club item=items.get(position);
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

    public void addItem(Club item){
        items.add(item);
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView recyclerview_club_title;
        TextView recyclerview_club_date;
        TextView recyclerview_club_memo;
        ProgressBar recyclerview_club_progress;
        TextView recyclerview_club_progress_text;
        ImageView recyclerview_club_edit;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            recyclerview_club_title=itemView.findViewById(R.id.recyclerview_club_title);
            recyclerview_club_date=itemView.findViewById(R.id.recyclerview_club_date);
            recyclerview_club_memo=itemView.findViewById(R.id.recyclerview_club_memo);
            recyclerview_club_progress=itemView.findViewById(R.id.recyclerview_club_progress);
            recyclerview_club_progress_text=itemView.findViewById(R.id.recyclerview_club_progress_text);
            recyclerview_club_edit=itemView.findViewById(R.id.recyclerview_club_edit);
        }

        public void setItem(Club item){
            recyclerview_club_title.setText(item.title);
            recyclerview_club_date.setText(item.start_date+" - "+item.end_date);
            recyclerview_club_memo.setText(item.memo);
            recyclerview_club_progress.setProgress(Integer.parseInt(String.valueOf((int)(Double.parseDouble(item.current_amount)/Double.parseDouble(item.goal)*100))));
            recyclerview_club_progress_text.setText(item.current_amount+" / "+item.goal);

            Intent friend_create_intent=new Intent(itemView.getContext(), FriendCreateActivity.class);
            friend_create_intent.putExtra("mode",item.club_id);
            recyclerview_club_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemView.getContext().startActivity(friend_create_intent);
                }
            });
        }
    }
}
