package com.yiche.bigdata.ad.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * sharePreference工具类，主要是将需要保持的字段和值进行统一管理，便于之后的迭代和修改
 * 
 * @author wanglirong
 * 
 */
public class SharePreferenceUtil
{
    private static SharePreferenceUtil mInstance;
    @SuppressWarnings("deprecation")
	private final int MODE = Context.MODE_WORLD_READABLE + Context.MODE_WORLD_WRITEABLE;
    private final SharedPreferences sharedpreferences;
    private static final String FILE_NAME = "com_yc_shared_preferences";
    public static final String YC_APP_CHANNELID = "channelid";
    public static final String YC_APP_KEY = "appkey";
    
    private SharePreferenceUtil(Context context, String fileName)
    {
        sharedpreferences = context.getSharedPreferences(fileName, MODE);
    }


    public static SharePreferenceUtil getInstance(Context context)
    {
        if (mInstance == null)
        {
            mInstance = new SharePreferenceUtil(context, FILE_NAME);
        }

        return mInstance;
    }

    public boolean saveString(String key, String value)
    {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(key, value);

        return editor.commit();
    }

    public String getString(String key)
    {
        return sharedpreferences.getString(key, "unknow");
    }

    public boolean saveInterger(String key, int value)
    {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putInt(key, value);

        return editor.commit();
    }

    public int getInterger(String key)
    {
        return sharedpreferences.getInt(key, 0);
    }

    public boolean saveBoolean(String key, boolean value)
    {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putBoolean(key, value);
        return editor.commit();
    }

    // add by sun
    public boolean saveLong(String key, long value)
    {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putLong(key, value);
        return editor.commit();
    }

    public long getLong(String key)
    {
        return sharedpreferences.getLong(key, 0);
    }

    // add
    public boolean getBoolean(String key)
    {
        return sharedpreferences.getBoolean(key, false);
    }

    public boolean removeKey(String key)
    {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.remove(key);
        return editor.commit();
    }

    public boolean removeAllKey()
    {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.clear();
        return editor.commit();
    }

    /**
     * 将key值映射的value置为空串
     * 
     * @param key
     * @return
     */
    public boolean clearString(String key)
    {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(key, "");
        return editor.commit();
    }

    /**
     * 将key值映射的value置为false
     * 
     * @param key
     * @return
     */
    public boolean clearBoolean(String key)
    {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putBoolean(key, false);
        return editor.commit();
    }

}
