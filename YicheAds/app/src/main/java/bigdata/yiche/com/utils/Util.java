package bigdata.yiche.com.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by yiche on 16/10/11.
 */

public class Util {
    public static String getToken(String string) {

        return MD5.StringToMD5("j99f8Uj%" + string);
    }
    public static String getTagType(String string) {
        char s;


        if(TextUtils.isEmpty(string)){
            return "old";
        }else{
            s=string.charAt(string.length()-1);
        }

        if (String.valueOf(s).equalsIgnoreCase("a")||String.valueOf(s).equalsIgnoreCase("b")||String.valueOf(s).equalsIgnoreCase("c")
                ||String.valueOf(s).equalsIgnoreCase("0")||String.valueOf(s).equalsIgnoreCase("1")||String.valueOf(s).equalsIgnoreCase("2")
                ||String.valueOf(s).equalsIgnoreCase("3")||String.valueOf(s).equalsIgnoreCase("4")){
            return "new";
        }else{
            return "old";
        }
    }

    public static String getTimeFromMillons(long time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date d1 = new Date(time);
        return format.format(d1);
    }

    /**
     * 方法: getMD5_Json
     * 描述: 根据MD5文件地址读出字符串
     * 参数: @param url
     * 参数: @return
     * 返回: String
     * 异常
     * 作者: 汪李蓉
     * 时间: 2014-2-25 上午11:29:50
     */
    public static String getVersionJson(String sourcePath) {
        StringBuffer sb = new StringBuffer();
        String line;
        BufferedReader reader = null;
        try {
            URL url = new URL(sourcePath);
            reader = new BufferedReader(new InputStreamReader(url.openStream()));
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (Exception ie) {
            ie.printStackTrace();
        } finally {
            try {
                if (reader != null)
                    reader.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    public static int getAppVersionName(Context context) {
        int versioncode = 0;
        try {
            // ---get the package info---
            versioncode= context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;

        } catch (Exception e) {
        }
        return versioncode;
    }

    /**
     * 对网络连接状态进行判断
     *
     * @return true, 可用； false， 不可用
     */
    public static boolean isOpenNetwork(Context context) {
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connManager.getActiveNetworkInfo();

        if (networkInfo != null) {
            //2.获取当前网络连接的类型信息
            int networkType = networkInfo.getType();
            if (ConnectivityManager.TYPE_WIFI == networkType) {
                //当前为wifi网络
            } else if (ConnectivityManager.TYPE_MOBILE == networkType) {
                //当前为mobile网络
            }
            return connManager.getActiveNetworkInfo().isAvailable();
        }
        return false;
    }
}
