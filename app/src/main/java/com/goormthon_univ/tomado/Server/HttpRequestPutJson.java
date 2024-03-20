package com.goormthon_univ.tomado.Server;

import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpRequestPutJson extends Thread{
    //응답 결과 반환을 위한 변수
    String response_str="";

    //주소
    String page;
    JSONObject parms;

    public HttpRequestPutJson(String page, JSONObject parms){
        this.page=page;
        this.parms=parms;
    }

    public void run() {
        try {
            //URL 객체 생성
            URL url = new URL(page);
            //HttpURLConnection 객체 생성
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();

            //결과값 저장 문자열
            final StringBuilder sb = new StringBuilder();
            // 연결되면
            if(conn != null) {
                //헤더 설정
                conn.setRequestProperty("Accept", "application/json");
                conn.setRequestProperty("Content-Type", "application/json");
                // 응답 타임아웃 설정
                conn.setConnectTimeout(10000);
                // 요청 방식 설정
                conn.setRequestMethod("PUT");
                // 필요한 인자 전달
                conn.getOutputStream().write(parms.toString().getBytes());

                if(conn.getResponseCode()==HttpURLConnection.HTTP_FORBIDDEN){
                    Log.i("HttpRequestPutJson","HTTP_FORBIDDEN");
                }
                if(conn.getResponseCode()==HttpURLConnection.HTTP_SERVER_ERROR){
                    Log.i("HttpRequestPutJson","HTTP_SERVER_ERROR");
                    BufferedReader br = new BufferedReader(new InputStreamReader(
                            conn.getErrorStream(), "utf-8"
                    ));
                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line);
                    }
                    br.close();
                    response_str=sb.toString();
                }
                if(conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    BufferedReader br = new BufferedReader(new InputStreamReader(
                            conn.getInputStream(), "utf-8"
                    ));
                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line);
                    }
                    br.close();
                    response_str=sb.toString();
                }
                //연결 종료
                conn.disconnect();
            }
        }catch (Exception e) {
            Log.i("HttpRequestPutJson", e.toString());
        }
    }

    public String getResponse(){
        return response_str;
    }
}
