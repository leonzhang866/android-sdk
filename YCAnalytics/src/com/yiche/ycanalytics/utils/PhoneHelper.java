/**
 * Copyright (c) 2013 DuoKu Inc.
 * 
 * @author 		
 * 
 * @date 2013-2-22
 */

package com.yiche.ycanalytics.utils;

import com.yiche.ycanalytics.YCPlatformInternal;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;


/**
 * 用户获取设备信息，渠道号，版本好等
 * 
 * @author wanglirong
 * 
 */
public final class PhoneHelper {
    public static final String CMCC = "1";
    public static final String CUCC = "2";
    public static final String CTCC = "3";
    public static final String CAN_NOT_FIND = "0";

    private static TelephonyManager mTelephonyManager = null;

    public static String getPhoneNumber(Context appcontext) {
        if (mTelephonyManager == null) {
            mTelephonyManager = (TelephonyManager) appcontext
                    .getSystemService(Context.TELEPHONY_SERVICE);
        }

        String res = mTelephonyManager.getLine1Number();

        if (res != null && res.length() > 0) {
            return res;
        }
        return "";
    }

    public static String getProvidersName(Context appcontext) {
        String ProvidersName = null;
        // 返回唯一的用户ID;就是这张卡的编号神马的
        if (mTelephonyManager == null) {
            mTelephonyManager = (TelephonyManager) appcontext
                    .getSystemService(Context.TELEPHONY_SERVICE);
        }

        String IMSI = mTelephonyManager.getSubscriberId();
        // IMSI号前面3位460是国家，紧接着后面2位00 02是中国移动，01是中国联通，03是中国电信。
        if (IMSI.startsWith("46000") || IMSI.startsWith("46002")) {
            ProvidersName = CMCC; // 移动
        } else if (IMSI.startsWith("46001")) {
            ProvidersName = CUCC; // 联通
        } else if (IMSI.startsWith("46003")) {
            ProvidersName = CTCC; // 电信
        } else {
            ProvidersName = CAN_NOT_FIND; // 不能区分
        }
        return ProvidersName;
    }

    public static String getIMEI(Context appcontext) {
        if (mTelephonyManager == null) {
            mTelephonyManager = (TelephonyManager) appcontext
                    .getSystemService(Context.TELEPHONY_SERVICE);
        }
        String res = mTelephonyManager.getDeviceId();
        if (res != null && res.length() > 0) {
            return res;
        }
        return "";
    }

    public static String getChannelData(Context appcontext,String key) {
        try {
            ApplicationInfo ai = appcontext.getPackageManager()
                    .getApplicationInfo(appcontext.getPackageName(),
                            PackageManager.GET_META_DATA);
            Object value = ai.metaData.get(key);
            if (value != null) {
                return value.toString();
            }
        } catch (Exception e) {
            //
        }
        return "-100";
    }

    public static String getAppVersionName(Context appcontext ) {
        try {
            PackageManager packageManager = appcontext.getPackageManager();
            PackageInfo packInfo = packageManager.getPackageInfo(
                    appcontext.getPackageName(), 0);
            return packInfo.versionName;
        } catch (Exception e) {
            //
        }
        return "";
    }

    public static String getAppVersionCode(Context appcontext) {
        try {
            PackageManager packageManager = appcontext.getPackageManager();
            PackageInfo packInfo = packageManager.getPackageInfo(
                    appcontext.getPackageName(), 0);
            return String.valueOf(packInfo.versionCode);
        } catch (Exception e) {
            //
        }
        return "";
    }

    public static int getAndroidSDKVersion() {
        int version = Build.VERSION.SDK_INT;

        return version;
    }

    public static String getUdid(Context appcontext) {

        String res = "";

        if (null != appcontext) {
            res = DeviceId.getDeviceID(appcontext);
        }else if(TextUtils.isEmpty(res)){
        	TelephonyManager tm = (TelephonyManager) appcontext.getSystemService(Context.TELEPHONY_SERVICE);
        	res=tm.getSimSerialNumber();
        }else{
        	res=System.currentTimeMillis()+"";
        }

        return res;
    }

    public static String getAndroidId(Context appcontext) {
        String s = "";

        if (null != appcontext) {
            s = android.provider.Settings.Secure.getString(appcontext.getContentResolver(), "android_id");
            if (TextUtils.isEmpty(s))
                s = "";
        }

        return s;
    }

    /* dp to px */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /* px to dp */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 开始拨打电话号码
     * 
     * @param context
     * @param number
     */
    public static void startCallNumber(Context context, String number) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + number));
        context.startActivity(intent);
    }

    // for baidu push -- to get the
    public static String getMetaValue(Context context, String metaKey) {
        Bundle metaData = null;
        String apiKey = null;
        if (context == null || metaKey == null) {
            return null;
        }
        try {
            ApplicationInfo ai = context.getPackageManager().getApplicationInfo(
                    context.getPackageName(), PackageManager.GET_META_DATA);
            if (null != ai) {
                metaData = ai.metaData;
            }
            if (null != metaData) {
                apiKey = metaData.getString(metaKey);
            }
        } catch (NameNotFoundException e) {

        }
        return apiKey;
    }

    public static boolean hasSimCard(Context appcontext) {
        if (mTelephonyManager == null) {
            mTelephonyManager = (TelephonyManager) appcontext
                    .getSystemService(Context.TELEPHONY_SERVICE);
        }

        if (mTelephonyManager.getSimState() == TelephonyManager.SIM_STATE_READY) {
            return true;
        }
        return false;
    }
}
