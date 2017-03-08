package com.yiche.bigdata.ad.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public final class DeviceId
{

    /**
     * 获取设备号udid
     * 
     * @param context
     * @return
     */
    public static String getDeviceID(Context context)
    {
		SharedPreferences sharedpreferences = context.getSharedPreferences("bids", 0);
		String s1 = sharedpreferences.getString("bid", null);
		if (s1 == null) {
			s1 = getIMEI(context);
			if (s1 != null && !s1.equals("")) {
				android.content.SharedPreferences.Editor editor1 = sharedpreferences.edit();
				editor1.putString("bid", s1);
				editor1.commit();
			} else {
				s1 = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getSimSerialNumber();
				if (s1 != null && !s1.equals("")) {
					android.content.SharedPreferences.Editor editor1 = sharedpreferences.edit();
					editor1.putString("bid", s1);
					editor1.commit();
				} else {
					s1 = getDeviceSerial();
					if (s1 != null && !s1.equals("")) {
						android.content.SharedPreferences.Editor editor1 = sharedpreferences.edit();
						editor1.putString("bid", s1);
						editor1.commit();
					} else {
						s1=getAndroidId(context);
						if (s1 != null && !s1.equals("")) {
							android.content.SharedPreferences.Editor editor1 = sharedpreferences.edit();
							editor1.putString("bid", s1);
							editor1.commit();
						} else {
							s1 = String.valueOf(System.currentTimeMillis());
							android.content.SharedPreferences.Editor editor1 = sharedpreferences.edit();
							editor1.putString("bid", s1);
							editor1.commit();
						}
					}
				}
			}
		}
        return s1;
    }

    /**
     * 获取设备imei
     * 
     * @param context
     * @return
     */
    public static String getIMEI(Context context)
    {
        String s = "";
        TelephonyManager telephonymanager = (TelephonyManager) context.getSystemService("phone");
        if (telephonymanager != null)
        {
            s = telephonymanager.getDeviceId();
            if (TextUtils.isEmpty(s))
                s = "";
        }
        return s;
    }

    public static String getAndroidId(Context context)
    {
        String s = "";
        s = android.provider.Settings.Secure.getString(context.getContentResolver(), "android_id");
        if (TextUtils.isEmpty(s))
            s = "";
        return s;
    }
    @SuppressWarnings("rawtypes")
	public static String getDeviceSerial() {
		String serial = "unknown";
		try {
			Class clazz = Class.forName("android.os.Build");
			Class paraTypes = Class.forName("java.lang.String");
			@SuppressWarnings("unchecked")
			Method method = clazz.getDeclaredMethod("getString", paraTypes);
			if (!method.isAccessible()) {
				method.setAccessible(true);
			}
			serial = (String) method.invoke(new Build(), "ro.serialno");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return serial;
	}
}