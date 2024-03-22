package com.goormthon_univ.tomado.Adapter;

public class Club {
    String club_id;
    String title;
    String colorType;
    String goal;
    String current_amount;
    String memo;
    String start_date;
    String end_date;
    String completed;
    String memberList;

    public Club(String club_id,String title,String colorType,String goal,String current_amount,String memo,String start_date,String end_date,String completed,String memberList){
        this.club_id=club_id;
        this.title=title;
        this.colorType=colorType;
        this.goal=goal;
        this.current_amount=current_amount;
        this.memo=memo;
        this.start_date=start_date;
        this.end_date=end_date;
        this.completed=completed;
        this.memberList=memberList;
    }
}
