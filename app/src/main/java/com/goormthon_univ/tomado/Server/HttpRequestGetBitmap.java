package com.goormthon_univ.tomado.Server;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpRequestGetBitmap extends Thread{
    //응답 결과 반환을 위한 변수
    Bitmap response_bitmap;

    //주소
    String page;

    public HttpRequestGetBitmap(String page){
        this.page=page;
    }

    public void run() {
        Log.d("주소 체크",page);
        try {
            //URL 객체 생성
            URL url = new URL(page);
            //HttpURLConnection 객체 생성
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();

            // 연결되면
            if(conn != null) {
                // 응답 타임아웃 설정
                conn.setConnectTimeout(10000);
                // 요청 방식 설정
                conn.setRequestMethod("GET");

                if(conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    response_bitmap=BitmapFactory.decodeStream(conn.getInputStream());
                }
                //연결 종료
                conn.disconnect();
            }
        }catch (Exception e) {
            Log.i("HttpRequestGetBitmap", e.toString());
        }
    }

    public Bitmap getResponse(){
        return response_bitmap;
    }
}
