package com.crte.monitoring.test4;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by jmy on 2017/4/19.
 */

public class SharedPreferencUtil {
    private static SharedPreferences sp;
    private static Context mContext;

    public static void init(Context context, String FILENAME) {
        mContext = context;
        sp = mContext.getSharedPreferences(FILENAME, Context.MODE_PRIVATE);
    }

    public static void putValue(String key, Object object) {
        if(sp==null){
            Log.e("SharedPreferencUtil","尚未初始化");
            return;
        }
        SharedPreferences.Editor editor = sp.edit();
        if (object instanceof String) {
            editor.putString(key, (String) object);
        } else if (object instanceof Integer) {
            editor.putInt(key, (Integer) object);
        } else if (object instanceof Boolean) {
            editor.putBoolean(key, (Boolean) object);
        } else if (object instanceof Float) {
            editor.putFloat(key, (Float) object);
        } else if (object instanceof Long) {
            editor.putLong(key, (Long) object);
        } else {
            editor.putString(key, object.toString());
        }
        editor.commit();
    }

    public static Object getValue(String key, Object defaultObject) {
        if(sp==null){
            Log.e("SharedPreferencUtil","尚未初始化");
            return null;
        }
        if (defaultObject instanceof String) {
            return sp.getString(key, (String) defaultObject);
        } else if (defaultObject instanceof Integer) {
            return sp.getInt(key, (Integer) defaultObject);
        } else if (defaultObject instanceof Boolean) {
            return sp.getBoolean(key, (Boolean) defaultObject);
        } else if (defaultObject instanceof Float) {
            return sp.getFloat(key, (Float) defaultObject);
        } else if (defaultObject instanceof Long) {
            return sp.getLong(key, (Long) defaultObject);
        } else {
            return sp.getString(key, null);
        }
    }

    /**
     * 移除某个key值已经对应的值
     *
     * @param key
     */
    public static void remove(String key) {
        if(sp==null){
            Log.e("SharedPreferencUtil","尚未初始化");
            return;
        }
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(key);
        editor.commit();
    }

    public static void clear() {
        if(sp==null){
            Log.e("SharedPreferencUtil","尚未初始化");
            return;
        }
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.commit();
    }
}