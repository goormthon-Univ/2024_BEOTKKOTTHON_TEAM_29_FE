package com.goormthon_univ.tomado;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.goormthon_univ.tomado.Adapter.BreakTimeSel;
import com.goormthon_univ.tomado.Adapter.BreakTimeSelAdapter;
import com.goormthon_univ.tomado.Adapter.Category;
import com.goormthon_univ.tomado.Adapter.CategoryAdapter;
import com.goormthon_univ.tomado.Adapter.CategoryDecoration;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.format.TitleFormatter;

public class DashboardActivity extends AppCompatActivity {
    private MaterialCalendarView dashboard_calendarView;
    RecyclerView dashboard_category_recyclerview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        dashboard_calendarView= findViewById(R.id.dashboard_calendarView);

        //리사이클러뷰 어뎁터 연결
        dashboard_category_recyclerview=findViewById(R.id.dashboard_category_recyclerview);

        CategoryAdapter category_adapter=new CategoryAdapter(null,2);

        GridLayoutManager layoutManager=new GridLayoutManager(this,2);
        dashboard_category_recyclerview.setLayoutManager(layoutManager);
        dashboard_category_recyclerview.setAdapter(category_adapter);

        Category c_1=new Category("프로그래밍 기초","color",5);
        Category c_2=new Category("오픽","color",8);
        Category c_3=new Category("독서","color",0);
        Category c_4=new Category("시험 공부","color",8);

        category_adapter.addItem(c_1);
        category_adapter.addItem(c_2);
        category_adapter.addItem(c_3);
        category_adapter.addItem(c_4);
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
    }

    public void onclick_close(View view){
        finish();
    }
}