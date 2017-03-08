package com.yiche.ycanalytics.apkdata;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wanglirong on 2016/12/29.
 */

public class Util {
    /**
     * 方法: getRunningProcess
     * 描述: 获取当前Service是否正在运行
     * 参数: @param context
     * 参数: @param processName
     * 参数: @return
     * 返回: boolean true:正在运行此指定名称的Service    false:指定名称的Service没有在运行
     */
    public static boolean getRunningProcess(Context context, String processName) {
        ActivityManager _ActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> list = _ActivityManager.getRunningAppProcesses();
        int i = list.size();
        for (int j = 0; j < list.size(); j++) {
            if (list.get(j).processName.contains(processName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 方法: getSystemApps
     * 描述: 获取到所有的系统预装应用
     * 参数: @param context
     * 参数: @return
     * 返回: List<String>
     * 异常
     */
    public static List<String> getSystemApps(Context context) {
        List<String> apps = new ArrayList<String>();
        PackageManager pManager = context.getPackageManager();
        //获取手机内所有应用
        List<PackageInfo> paklist = pManager.getInstalledPackages(0);
        for (int i = 0; i < paklist.size(); i++) {
            PackageInfo pak = (PackageInfo) paklist.get(i);
            //判断是否为非系统预装的应用程序
            if ((pak.applicationInfo.flags & pak.applicationInfo.FLAG_SYSTEM) > 0) {
                // customs applications
                apps.add(pak.packageName);
            }
        }
        return apps;
    }
}