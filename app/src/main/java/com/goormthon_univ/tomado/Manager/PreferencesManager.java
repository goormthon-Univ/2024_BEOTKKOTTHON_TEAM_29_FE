package com.goormthon_univ.tomado.Manager;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatActivity;

public class PreferencesManager extends AppCompatActivity {
    /*
    user_id
     */

    public static void pref_write_boolean(SharedPreferences pref,String item,boolean tf){
        SharedPreferences.Editor editor=pref.edit();

        editor.putBoolean(item,tf);
        editor.commit();
    }

    public static void pref_write_string(SharedPreferences pref,String item,String text){
        SharedPreferences.Editor editor=pref.edit();

        editor.putString(item,text);
        editor.commit();
    }

    public static boolean pref_read_boolean(SharedPreferences pref,String item){
        if(pref!=null&&pref.contains(item)){
            if(pref.getBoolean(item,true)==true){
                return true;
            }
        }
        return false;
    }

    public static String pref_read_string(SharedPreferences pref,String item){
        if(pref!=null&&pref.contains(item)){
            String n=pref.getString(item,"").toString();
            return n;
        }
        return null;
    }
}
