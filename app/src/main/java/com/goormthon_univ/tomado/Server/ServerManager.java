package com.goormthon_univ.tomado.Server;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.goormthon_univ.tomado.Manager.PreferencesManager;

import org.json.JSONObject;

public class ServerManager extends AppCompatActivity {
    static String url_main="http://43.201.79.243:8080";
    Context context;

    //유저 아이디 불러오기
    public String user_id;

    public ServerManager(Context context){
        this.context=context;
    }

    public String get_user_id(SharedPreferences pref){
        if(PreferencesManager.pref_read_string(pref,"user_id")==null){
            Toast.makeText(context,"user_id를 불러오는데 문제가 발생하였습니다",Toast.LENGTH_SHORT).show();
        }else{
            user_id=PreferencesManager.pref_read_string(pref, "user_id");
            return user_id;
        }
        return null;
    }

    public String http_request_post_json(String page,JSONObject parms){
        Thread th = new HttpRequestPostJson(url_main+page,parms);
        th.start();

        try{
            th.join();
        }catch(Exception e){
            e.printStackTrace();
        }
        String d= ((HttpRequestPostJson) th).getResponse();
        return d;
    }

    public String http_request_get_json(String page){
        Thread th = new HttpRequestGetJson(url_main+page);
        th.start();

        try{
            th.join();
        }catch(Exception e){
            e.printStackTrace();
        }
        String d= ((HttpRequestGetJson) th).getResponse();
        return d;
    }

    public String http_request_put_json(String page,JSONObject parms){
        Thread th = new HttpRequestPutJson(url_main+page,parms);
        th.start();

        try{
            th.join();
        }catch(Exception e){
            e.printStackTrace();
        }
        String d= ((HttpRequestPutJson) th).getResponse();
        return d;
    }

    public String http_request_delete_json(String page){
        Thread th = new HttpRequestDeleteJson(url_main+page);
        th.start();

        try{
            th.join();
        }catch(Exception e){
            e.printStackTrace();
        }
        String d= ((HttpRequestDeleteJson) th).getResponse();
        return d;
    }
}
