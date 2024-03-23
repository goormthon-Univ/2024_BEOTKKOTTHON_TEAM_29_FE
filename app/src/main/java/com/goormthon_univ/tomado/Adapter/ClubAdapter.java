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

import com.bumptech.glide.Glide;
import com.goormthon_univ.tomado.FriendCreateActivity;
import com.goormthon_univ.tomado.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
        ImageView recyclerview_member_1;
        ImageView recyclerview_member_2;
        ImageView recyclerview_member_3;
        ImageView recyclerview_member_4;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            recyclerview_club_title=itemView.findViewById(R.id.recyclerview_club_title);
            recyclerview_club_date=itemView.findViewById(R.id.recyclerview_club_date);
            recyclerview_club_memo=itemView.findViewById(R.id.recyclerview_club_memo);
            recyclerview_club_progress=itemView.findViewById(R.id.recyclerview_club_progress);
            recyclerview_club_progress_text=itemView.findViewById(R.id.recyclerview_club_progress_text);
            recyclerview_club_edit=itemView.findViewById(R.id.recyclerview_club_edit);
            recyclerview_member_1=itemView.findViewById(R.id.recyclerview_member_1);
            recyclerview_member_2=itemView.findViewById(R.id.recyclerview_member_2);
            recyclerview_member_3=itemView.findViewById(R.id.recyclerview_member_3);
            recyclerview_member_4=itemView.findViewById(R.id.recyclerview_member_4);
        }

        public void setItem(Club item) {
            recyclerview_club_title.setText(item.title);
            recyclerview_club_date.setText(item.start_date+" - "+item.end_date);
            recyclerview_club_memo.setText(item.memo);
            recyclerview_club_progress.setProgress(Integer.parseInt(String.valueOf((int)(Double.parseDouble(item.current_amount)/Double.parseDouble(item.goal)*100))));
            recyclerview_club_progress_text.setText(item.current_amount+" / "+item.goal);

            try{
                JSONArray member=new JSONArray(item.memberList);
                if(member.length()==1){
                    JSONObject m_1=new JSONObject(member.get(0).toString());
                    Glide.with(itemView.getContext()).load(m_1.get("url")).into(recyclerview_member_1);
                    recyclerview_member_2.setVisibility(View.GONE);
                    recyclerview_member_3.setVisibility(View.GONE);
                    recyclerview_member_4.setVisibility(View.GONE);
                }else if(member.length()==2){
                    JSONObject m_1=new JSONObject(member.get(0).toString());
                    Glide.with(itemView.getContext()).load(m_1.get("url")).into(recyclerview_member_1);
                    JSONObject m_2=new JSONObject(member.get(1).toString());
                    Glide.with(itemView.getContext()).load(m_2.get("url")).into(recyclerview_member_2);
                    recyclerview_member_3.setVisibility(View.GONE);
                    recyclerview_member_4.setVisibility(View.GONE);
                }else if(member.length()==3){
                    JSONObject m_1=new JSONObject(member.get(0).toString());
                    Glide.with(itemView.getContext()).load(m_1.get("url")).into(recyclerview_member_1);
                    JSONObject m_2=new JSONObject(member.get(1).toString());
                    Glide.with(itemView.getContext()).load(m_2.get("url")).into(recyclerview_member_2);
                    JSONObject m_3=new JSONObject(member.get(2).toString());
                    Glide.with(itemView.getContext()).load(m_3.get("url")).into(recyclerview_member_3);
                    recyclerview_member_4.setVisibility(View.GONE);
                }else if(member.length()>=4){
                    JSONObject m_1=new JSONObject(member.get(0).toString());
                    Glide.with(itemView.getContext()).load(m_1.get("url")).into(recyclerview_member_1);
                    JSONObject m_2=new JSONObject(member.get(1).toString());
                    Glide.with(itemView.getContext()).load(m_2.get("url")).into(recyclerview_member_2);
                    JSONObject m_3=new JSONObject(member.get(2).toString());
                    Glide.with(itemView.getContext()).load(m_3.get("url")).into(recyclerview_member_3);
                    JSONObject m_4=new JSONObject(member.get(3).toString());
                    Glide.with(itemView.getContext()).load(m_4.get("url")).into(recyclerview_member_4);
                }
            }catch(Exception e){

            }


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
