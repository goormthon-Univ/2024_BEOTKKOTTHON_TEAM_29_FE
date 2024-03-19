package com.goormthon_univ.tomado.Adapter;

public class Club {
    String title;
    String memberList; //수정 필요
    int goal;
    int current_amount;
    String memo;
    String start_date;
    String end_date;
    boolean is_completed;

    public Club(String title,String memberList,int goal,int current_amount,String memo,String start_date,String end_date,boolean is_completed){
        this.title=title;
        this.memberList=memberList;
        this.goal=goal;
        this.current_amount=current_amount;
        this.memo=memo;
        this.start_date=start_date;
        this.end_date=end_date;
        this.is_completed=is_completed;
    }
}
