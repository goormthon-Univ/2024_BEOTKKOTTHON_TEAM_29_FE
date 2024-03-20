package com.goormthon_univ.tomado.Server;

import android.content.Context;
import org.json.JSONObject;

public class ServerManager {
    static String url_main="http://43.201.79.243:8080";
    Context context;

    public ServerManager(Context context){
        this.context=context;
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
}
