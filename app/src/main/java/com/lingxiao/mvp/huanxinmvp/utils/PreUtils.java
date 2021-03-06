package com.lingxiao.mvp.huanxinmvp.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.lingxiao.mvp.huanxinmvp.global.Content;

/**
 * 保存用户名密码,用于回显
 */

public class PreUtils {
    private static SharedPreferences sp;
    public static void setUserNamePsd(Context context,String username,String psd){
        sp = context.getSharedPreferences("config",Context.MODE_PRIVATE);
        sp.edit().putString(Content.KEY_USERNAME,username)
                .putString(Content.KEY_PSD,psd).commit();
    }
    public static String getUserName(Context context){
        sp = context.getSharedPreferences("config",Context.MODE_PRIVATE);
        return sp.getString(Content.KEY_USERNAME,"");
    }
    public static String getPsd(Context context){
        sp = context.getSharedPreferences("config",Context.MODE_PRIVATE);
        return sp.getString(Content.KEY_PSD,"");
    }
}
