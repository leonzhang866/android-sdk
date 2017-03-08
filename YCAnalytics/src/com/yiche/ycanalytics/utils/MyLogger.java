package com.yiche.ycanalytics.utils;

import java.util.Hashtable;


import android.util.Log;

/**
 * log工具类，用户log输出包括v，d，i，w，等同用log输出，可以根据统一开关控制log是否显示
 * 
 * @author wushengbing
 * 
 */
public class MyLogger {

//    public boolean mIsLoggerEnable = true& Constants.DEBUG;
    private final static String LOG_TAG = "[ycplatform]";
    private static Hashtable<String, MyLogger> sLoggerTable;
    private String mClassName;

    static {
        sLoggerTable = new Hashtable<String, MyLogger>();
    }

    public static MyLogger getLogger(String className) {
        MyLogger classLogger = (MyLogger) sLoggerTable.get(className);
        if (classLogger == null) {
            classLogger = new MyLogger(className);
            sLoggerTable.put(className, classLogger);
        }
        return classLogger;
    }

    private MyLogger(String name) {
        mClassName = name;
    }

    public void v(String log) {
        if (Constants.DEBUG) {
            Log.v(LOG_TAG, "{Thread:" + Thread.currentThread().getName() + "}" + "[" + mClassName
                    + ":] " + log);
        }
    }

    public void d(String log) {
        if (Constants.DEBUG) {
            Log.d(LOG_TAG, "{Thread:" + Thread.currentThread().getName() + "}" + "[" + mClassName
                    + ":] " + log);
        }
    }

    public void i(String log) {
        if (Constants.DEBUG) {
            Log.i(LOG_TAG, "{Thread:" + Thread.currentThread().getName() + "}" + "[" + mClassName
                    + ":] " + log);
        }
    }

    public void i(String log, Throwable tr) {
        if (Constants.DEBUG) {
            Log.i(LOG_TAG, "{Thread:" + Thread.currentThread().getName() + "}" + "[" + mClassName
                    + ":] " + log + "\n" + Log.getStackTraceString(tr));
        }
    }

    public void w(String log) {
        if (Constants.DEBUG) {
            Log.w(LOG_TAG, "{Thread:" + Thread.currentThread().getName() + "}" + "[" + mClassName
                    + ":] " + log);
        }
    }

    public void w(String log, Throwable tr) {
        if (Constants.DEBUG) {
            Log.w(LOG_TAG, "{Thread:" + Thread.currentThread().getName() + "}" + "[" + mClassName
                    + ":] " + log + "\n" + Log.getStackTraceString(tr));
        }
    }

    public void e(String log) {
        if (Constants.DEBUG) {
            Log.e(LOG_TAG, "{Thread:" + Thread.currentThread().getName() + "}" + "[" + mClassName
                    + ":] " + log);
        }
    }

    public void e(String log, Throwable tr) {
        if (Constants.DEBUG) {
            Log.e(LOG_TAG, "{Thread:" + Thread.currentThread().getName() + "}" + "[" + mClassName
                    + ":] " + log + "\n" + Log.getStackTraceString(tr));
        }
    }
}