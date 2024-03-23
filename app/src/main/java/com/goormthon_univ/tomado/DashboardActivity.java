package com.goormthon_univ.tomado;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.goormthon_univ.tomado.Adapter.BreakTimeSel;
import com.goormthon_univ.tomado.Adapter.BreakTimeSelAdapter;
import com.goormthon_univ.tomado.Adapter.CalendarRecyclerview;
import com.goormthon_univ.tomado.Adapter.CalendarRecyclerviewAdapter;
import com.goormthon_univ.tomado.Adapter.Category;
import com.goormthon_univ.tomado.Adapter.CategoryAdapter;
import com.goormthon_univ.tomado.Adapter.CategoryDecoration;
import com.goormthon_univ.tomado.Adapter.Memo;
import com.goormthon_univ.tomado.Manager.PreferencesManager;
import com.goormthon_univ.tomado.Server.ServerManager;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.format.TitleFormatter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DashboardActivity extends AppCompatActivity {
    private MaterialCalendarView dashboard_calendarView;
    RecyclerView dashboard_category_recyclerview;

    ServerManager server_manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        //서버 연동 객체 추가
        server_manager=new ServerManager(getApplicationContext());

        dashboard_calendarView= findViewById(R.id.dashboard_calendarView);

        //리사이클러뷰 어뎁터 연결
        dashboard_category_recyclerview=findViewById(R.id.dashboard_category_recyclerview);

        CategoryAdapter category_adapter=new CategoryAdapter(null,null,2,DashboardActivity.this, PreferencesManager.pref_read_string((SharedPreferences)getSharedPreferences("preferences", Activity.MODE_PRIVATE),"user_id"));

        GridLayoutManager layoutManager=new GridLayoutManager(this,2);
        dashboard_category_recyclerview.setLayoutManager(layoutManager);
        dashboard_category_recyclerview.setAdapter(category_adapter);

        //서버에서 불러오기
        try {
            JSONObject json=new JSONObject(server_manager.http_request_get_json("/categories/"+server_manager.get_user_id((SharedPreferences)getSharedPreferences("preferences", Activity.MODE_PRIVATE))));

            if(json.get("message").toString().equals("카테고리 조회 성공")){
                JSONObject json_data=new JSONObject(json.get("data").toString());
                JSONArray category_list_array=new JSONArray(json_data.get("categoryList").toString());

                for(int i=0;i< category_list_array.length();i++){
                    JSONObject data=new JSONObject(category_list_array.get(i).toString());
                    Category category=new Category(data.get("category_id").toString(),
                            data.get("title").toString(),
                            data.get("color").toString(),
                            Integer.parseInt(data.get("tomato").toString()),
                            false);
                    category_adapter.addItem(category);
                }
            }else{
                //메모 조회 실패 시 실패 원인 보여줌
                Toast.makeText(getApplicationContext(),json.get("message").toString(),Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        Category c_0=new Category("","","",0,true);
        category_adapter.addItem(c_0);

        category_adapter.notifyDataSetChanged();

        //MM월 YYYY 방식-> YYYY년 MM월 으로 표현 수정
        dashboard_calendarView.setTitleFormatter(new TitleFormatter() {
            @Override
            public CharSequence format(CalendarDay day) {
                //LocalDate date=day.getDate();
                String[] date_arr=day.toString().split("-");
                String year=date_arr[0].replace("CalendarDay{","");
                String date_str=year.charAt(2)+""+year.charAt(3)+"년 "+date_arr[1]+"월";
                return date_str;
            }
        });

        //캘린더 데이터 클릭 리스너
        dashboard_calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                dialog_calendar_fn(date);
            }
        });
    }

    public void onclick_close(View view){
        finish();
    }

    public void dialog_calendar_fn(CalendarDay date){
        Dialog calendar_dialog=new Dialog(DashboardActivity.this);
        calendar_dialog.setContentView(R.layout.dialog_dashboard);
        calendar_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        calendar_dialog.getWindow().setGravity(Gravity.BOTTOM);

        //뷰 연결
        TextView dialog_dashboard_text=calendar_dialog.findViewById(R.id.dialog_dashboard_text);
        dialog_dashboard_text.setText(String.format("%4d년 %d월 %d일",date.getYear(),date.getMonth(),date.getDay()));

        //리사이클러뷰 어뎁터 연결
        RecyclerView dialog_dashboard_recyclerview=calendar_dialog.findViewById(R.id.dialog_dashboard_recyclerview);

        CalendarRecyclerviewAdapter calendar_adapter=new CalendarRecyclerviewAdapter();

        LinearLayoutManager layoutManager=new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        dialog_dashboard_recyclerview.setLayoutManager(layoutManager);
        dialog_dashboard_recyclerview.setAdapter(calendar_adapter);

        //서버에서 불러오기
        try {
            JSONObject json=new JSONObject(server_manager.http_request_get_json("/categories?user="+server_manager.get_user_id((SharedPreferences)getSharedPreferences("preferences", Activity.MODE_PRIVATE))+"&date="+String.format("%4d-%02d-%02d",date.getYear(),date.getMonth(),date.getDay())));

            if(json.get("message").toString().equals("카테고리 조회 성공")){
                JSONObject json_data=new JSONObject(json.get("data").toString());
                JSONArray memo_list_array=new JSONArray(json_data.get("categoryList").toString());

                for(int i=0;i< memo_list_array.length();i++){
                    JSONObject data=new JSONObject(memo_list_array.get(i).toString());
                    CalendarRecyclerview cal=new CalendarRecyclerview(data.get("title").toString(),
                            data.get("color").toString(),
                            data.get("tomato").toString()
                    );
                    calendar_adapter.addItem(cal);
                }
            }else{
                //메모 조회 실패 시 실패 원인 보여줌
                Toast.makeText(getApplicationContext(),json.get("message").toString(),Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        calendar_adapter.notifyDataSetChanged();

        calendar_dialog.show();
    }
}