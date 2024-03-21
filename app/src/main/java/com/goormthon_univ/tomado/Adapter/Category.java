package com.goormthon_univ.tomado.Adapter;

public class Category {
    String category_id;
    String title;
    String color;
    int tomato;
    //추가하는 버튼인지 여부 확인
    boolean add_button;

    public Category(String category_id,String title,String color,int tomato,boolean add_button){
        this.category_id=category_id;
        this.title=title;
        this.color=color;
        this.tomato=tomato;
        this.add_button=add_button;
    }
}
