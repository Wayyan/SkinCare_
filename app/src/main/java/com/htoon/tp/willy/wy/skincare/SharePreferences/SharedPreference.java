package com.htoon.tp.willy.wy.skincare.SharePreferences;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreference {
    public Context context;
    private String SP_NAME="Compare",KEY1R="firstred",KEY1G="firstgreen",KEY1B="firstblue",KEY2R="secondred",KEY2G="secondgreen",KEY2B="secondblue";
    private SharedPreferences shp;
    private SharedPreferences.Editor editor;
    public SharedPreference(Context context)
    {
        this.context=context;
        shp=context.getSharedPreferences(SP_NAME,Context.MODE_PRIVATE);
        editor=shp.edit();
    }
    public void setValue1(int red,int green,int blue){
        editor.putInt(KEY1R,red);
        editor.putInt(KEY1G,green);
        editor.putInt(KEY1B,blue);
        editor.commit();
    }
    public void setValue2(int red,int green,int blue){
        editor.putInt(KEY2R,red);
        editor.putInt(KEY2G,green);
        editor.putInt(KEY2B,blue);
        editor.commit();
    }
    public int getValue1R(){
       return shp.getInt(KEY1R,-1);
    }
    public int getValue1G(){
        return shp.getInt(KEY1G,-1);
    }
    public int getValue1B(){
        return shp.getInt(KEY1B,-1);
    }
    public int getValue2R(){
        return shp.getInt(KEY2R,-2);
    }
    public int getValue2G(){
        return shp.getInt(KEY2G,-2);
    }
    public int getValue2B(){
        return shp.getInt(KEY2B,-2);
    }
}
