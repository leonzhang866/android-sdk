package com.yiche.bigdata.ad.net;


import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.telephony.TelephonyManager;

import com.yiche.bigdata.ad.YCPlatformInternal;

/**
 * 联网管理类
 * 
 * @author wanglirong
 * 
 */
public class ConnectManager
{
    // Connect type
    public static final int Net_ConnType_2G = 1;
    public static final int Net_ConnType_3G = 2;
    public static final int Net_ConnType_WIFI = 3;

    public static final String MOBILE_UNI_PROXY_IP = "10.0.0.172";
    public static final String TELCOM_PROXY_IP = "10.0.0.200";
    public static final String PROXY_PORT = "80";

    public static final String CHINA_MOBILE_WAP = "CMWAP";
    public static final String CHINA_UNI_WAP = "UNIWAP";
    public static final String CHINA_UNI_3G = "3GWAP";
    public static final String CHINA_TELCOM = "CTWAP";

    private static final String TAG = "ConnectManager";
    private static final boolean DEBUG = false;
    private String mApn;
    private String mProxy;
    private String mPort;
    private boolean mUseWap;
    public static final Uri PREFERRED_APN_URI = Uri.parse("content://telephony/carriers/preferapn");

    private static Context mContext; // 这个上下文context需要cp提供

    static
    {
        mContext = YCPlatformInternal.getInstance().getSDKContext();
    }

    public ConnectManager()
    {
        try
        {
            checkNetworkType();
        }
        catch (SecurityException e)
        {
            checkConnectType();
        }

    }

    /**
     * 校验Apn类型
     */
    private void checkApn()
    {
        Cursor cursor = mContext.getContentResolver().query(PREFERRED_APN_URI, new String[]
        {
                "_id", "apn", "proxy", "port"
        }, null, null, null);
        if (cursor != null)
        {
            if (cursor.moveToFirst())
            {
                int i = cursor.getColumnIndex("apn");
                int j = cursor.getColumnIndex("proxy");
                int k = cursor.getColumnIndex("port");
                mApn = cursor.getString(i);
                mProxy = cursor.getString(j);
                mPort = cursor.getString(k);
                if (mProxy != null && mProxy.length() > 0)
                {
                    if ("10.0.0.172".equals(mProxy.trim()))
                    {
                        mUseWap = true;
                        mPort = "80";
                    }
                    else if ("10.0.0.200".equals(mProxy.trim()))
                    {
                        mUseWap = true;
                        mPort = "80";
                    }
                    else
                    {
                        mUseWap = false;
                    }
                }
                else if (mApn != null)
                {
                    String s = mApn.toUpperCase();
                    if (s.equals("CMWAP") || s.equals("UNIWAP") || s.equals("3GWAP"))
                    {
                        mUseWap = true;
                        mProxy = "10.0.0.172";
                        mPort = "80";
                    }
                    else if (s.equals("CTWAP"))
                    {
                        mUseWap = true;
                        mProxy = "10.0.0.200";
                        mPort = "80";
                    }
                }
                else
                {
                    mUseWap = false;
                }
            }
            cursor.close();
        }
    }

    /*
     * Get Current connection type
     * 
     * @Return ConnectType have three kind of type
     */
    public void checkConnectType()
    {

        final ConnectivityManager conn = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (conn != null)
        {

            NetworkInfo info = conn.getActiveNetworkInfo();

            if (info != null)
            {
                String connStr = info.getTypeName();

                if (connStr.equalsIgnoreCase("WIFI"))
                {

                    // set member param
                    mUseWap = false;

                }
                else if (connStr.equalsIgnoreCase("MOBILE"))
                {

                    String apn = info.getExtraInfo();

                    // 解决apn为空的情况
                    if (apn != null)
                    {
                        if (apn.indexOf("wap") > -1)
                        {

                            if (apn.equals("cmwap") || apn.equals("uniwap") || apn.equals("3gwap"))
                            {

                                mUseWap = true;
                                mProxy = "10.0.0.172";
                                mPort = "80";

                            }
                            else if (apn.equals("ctwap"))
                            {

                                mUseWap = true;
                                mProxy = "10.0.0.200";
                                mPort = "80";

                            }
                            else
                            {
                                // not use wap
                                mUseWap = false;
                            }

                        }
                        else
                        {
                            // not use wap
                            mUseWap = false;
                        }
                    }
                    else
                    {
                        mUseWap = false;
                    }

                }
            }
        }
    }

    private void checkNetworkType()
    {
        ConnectivityManager connectivitymanager = (ConnectivityManager) mContext.getApplicationContext().getSystemService("connectivity");
        NetworkInfo networkinfo = connectivitymanager.getActiveNetworkInfo();
        if (networkinfo != null)
            if ("wifi".equals(networkinfo.getTypeName().toLowerCase()))
                mUseWap = false;
            else
                checkApn();
    }

    public static boolean isNetConnect()
    {
        ConnectivityManager connectivitymanager = (ConnectivityManager) mContext.getApplicationContext().getSystemService("connectivity");
        NetworkInfo networkinfo = connectivitymanager.getActiveNetworkInfo();
        if (networkinfo != null)
            return networkinfo.isConnectedOrConnecting();
        else
            return false;
    }

    /**
     * make true current connect service is wifi
     * 
     * @return
     */
    public static boolean isWifi()
    {
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetInfo != null && activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI)
        {
            return true;
        }
        return false;
    }

    /**
     * Judge whether fast connection
     * 
     * @param context
     *            Application context true if fast connection, otherwise false
     */
    public static boolean isConnectionFast(Context context)
    {
        int type;
        int subType;

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetInfo == null)
        {
            return false;
        }
        type = ConnectivityManager.TYPE_WIFI;

        TelephonyManager telephoneManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        if (telephoneManager == null)
        {
            return false;
        }

        subType = telephoneManager.getNetworkType();

        if (type == ConnectivityManager.TYPE_WIFI)
        {
            return true;
        }
        else if (type == ConnectivityManager.TYPE_MOBILE)
        {
            switch (subType)
            {
                case TelephonyManager.NETWORK_TYPE_1xRTT:
                    return false; // ~ 50-100 kbps
                case TelephonyManager.NETWORK_TYPE_CDMA:
                    return false; // ~ 14-64 kbps
                case TelephonyManager.NETWORK_TYPE_EDGE:
                    return false; // ~ 50-100 kbps
                case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    return true; // ~ 400-1000 kbps
                case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    return true; // ~ 600-1400 kbps
                case TelephonyManager.NETWORK_TYPE_GPRS:
                    return false; // ~ 100 kbps
                    // case TelephonyManager.NETWORK_TYPE_HSDPA:
                    // return true; // ~ 2-14 Mbps
                    // case TelephonyManager.NETWORK_TYPE_HSPA:
                    // return true; // ~ 700-1700 kbps
                    // case TelephonyManager.NETWORK_TYPE_HSUPA:
                    // return true; // ~ 1-23 Mbps
                case TelephonyManager.NETWORK_TYPE_UMTS:
                    return true; // ~ 400-7000 kbps
                    // NOT AVAILABLE YET IN API LEVEL 7
                    // case Connectivity.NETWORK_TYPE_EHRPD:
                    // return true; // ~ 1-2 Mbps
                    // case Connectivity.NETWORK_TYPE_EVDO_B:
                    // return true; // ~ 5 Mbps
                    // case Connectivity.NETWORK_TYPE_HSPAP:
                    // return true; // ~ 10-20 Mbps
                    // case Connectivity.NETWORK_TYPE_IDEN:
                    // return false; // ~25 kbps
                    // case Connectivity.NETWORK_TYPE_LTE:
                    // return true; // ~ 10+ Mbps
                    // Unknown
                case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                    return false;
                default:
                    return false;
            }
        }
        else
        {
            return false;
        }
    }

    public boolean isWapNetwork()
    {
        return mUseWap;
    }

    // public String getApn()
    // {
    // return mApn;
    // }

    public String getProxy()
    {
        return mProxy;
    }

    public String getProxyPort()
    {
        return mPort;
    }

   
    public static int GetNetworkType(Context context)
    {
        int strNetworkType = 0;
        
        NetworkInfo networkInfo = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
        {
            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI)
            {
                strNetworkType = 1;
            }
            else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE)
            {
                String _strSubTypeName = networkInfo.getSubtypeName();
                
                
                // TD-SCDMA   networkType is 17
                int networkType = networkInfo.getSubtype();
                switch (networkType) {
                    case TelephonyManager.NETWORK_TYPE_GPRS:
                    case TelephonyManager.NETWORK_TYPE_EDGE:
                    case TelephonyManager.NETWORK_TYPE_CDMA:
                    case TelephonyManager.NETWORK_TYPE_1xRTT:
                    case TelephonyManager.NETWORK_TYPE_IDEN: //api<8 : replace by 11
                        strNetworkType = 2;
                        break;
                    case TelephonyManager.NETWORK_TYPE_UMTS:
                    case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    case TelephonyManager.NETWORK_TYPE_HSDPA:
                    case TelephonyManager.NETWORK_TYPE_HSUPA:
                    case TelephonyManager.NETWORK_TYPE_HSPA:
                    case 12://TelephonyManager.NETWORK_TYPE_EVDO_B: //api<9 : replace by 14
                    case 14://TelephonyManager.NETWORK_TYPE_EHRPD:  //api<11 : replace by 12
                    case 15://TelephonyManager.NETWORK_TYPE_HSPAP:  //api<13 : replace by 15
                        strNetworkType = 3;
                        break;
                    case 13://TelephonyManager.NETWORK_TYPE_LTE:    //api<11 : replace by 13
                        strNetworkType = 4;
                        break;
                    default:
                        // http://baike.baidu.com/item/TD-SCDMA 中国移动 联通 电信 三种3G制式
                        if (_strSubTypeName.equalsIgnoreCase("TD-SCDMA") || _strSubTypeName.equalsIgnoreCase("WCDMA") || _strSubTypeName.equalsIgnoreCase("CDMA2000")) 
                        {
                            strNetworkType = 3;
                        }
                        else
                        {
                            strNetworkType = 0;
                        }
                        
                        break;
                 }
                 
            }
        }
        return strNetworkType;
    }
	
}
