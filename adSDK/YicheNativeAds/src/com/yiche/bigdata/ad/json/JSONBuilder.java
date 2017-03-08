package com.yiche.bigdata.ad.json;

import java.util.Random;
import org.json.JSONArray;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import com.yiche.bigdata.ad.YCPlatformInternal;
import com.yiche.bigdata.ad.bean.AdBean;
import com.yiche.bigdata.ad.bean.YCAdBean;
import com.yiche.bigdata.ad.net.ConnectManager;
import com.yiche.bigdata.ad.utils.Constants;
import com.yiche.bigdata.ad.utils.DeviceId;
import com.yiche.bigdata.ad.utils.Utils;

/**
 * 
 * json构造类 用途：发起联网前准备需要上传到中间层的参数
 * 
 * @author wanglirong
 * 
 */
public final class JSONBuilder{
//	private MyLogger mLogger = MyLogger.getLogger(JSONBuilder.class.getName());
	
	public JSONObject buildGetADJsonObject(Context context,String pubid,int[] pids) {
		JSONObject mJson = new JSONObject();
		long ts=System.currentTimeMillis();
		JSONArray jsonArray=new JSONArray();
		for(int i=0;i<pids.length;i++){
			jsonArray.put(pids[i]);
		}
		String deviceId=DeviceId.getDeviceID(context);
		try {
			mJson.put(JSONConstants.JSON_CITYID, 0);
			mJson.put(JSONConstants.JSON_PUBID, pubid);
			mJson.put(JSONConstants.JSON_PIDS,jsonArray);
			mJson.put(JSONConstants.JSON_DVID, deviceId);
			mJson.put(JSONConstants.JSON_OS, Constants.ANDROID);
			mJson.put(JSONConstants.JSON_APPVER, Utils.getAppVersion(context));
			mJson.put(JSONConstants.JSON_DVTYPE, Utils.isPad(context));
			mJson.put(JSONConstants.JSON_NETTYPE, ConnectManager.GetNetworkType(context));
//			mJson.put(JSONConstants.JSON_IP, Utils.getLocalIpAddress());
//			mJson.put(JSONConstants.JSON_UA, "");
			mJson.put(JSONConstants.JSON_OSVS, Build.VERSION.RELEASE);
			mJson.put(JSONConstants.JSON_TS, ts);
//			mJson.put(JSONConstants.JSON_GEO, Utils.getLocation(context));
			mJson.put(JSONConstants.JSON_KEY, Constants.KEY);
			mJson.put(JSONConstants.JSON_TOKEN, Utils.getToken(pubid, deviceId, ts, Constants.KEY));
			//add 20160825
			mJson.put(JSONConstants.JSON_IMEI, DeviceId.getIMEI(context));
			mJson.put(JSONConstants.JSON_MAC, Utils.getMac(context));
			mJson.put(JSONConstants.JSON_ANDROIDID, DeviceId.getAndroidId(context));
			mJson.put(JSONConstants.JSON_MODEL, Build.MODEL);
			mJson.put(JSONConstants.JSON_RES, Utils.getRes(context));
			mJson.put(JSONConstants.JSON_MANU, Build.MANUFACTURER);
			
//			mJson.put(JSONConstants.JSON_BLUETOOTHMAC, Utils.getBluetoothMac(context));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mJson;
	}

	public String buildExposeUrl(YCAdBean adBean,boolean isCovered) {
		StringBuffer stringBuffer=new StringBuffer();
		stringBuffer.append(Constants.YC_EXPOSE_URL);
		stringBuffer.append("/sy");
		stringBuffer.append("1");
		stringBuffer.append("/pc");
		stringBuffer.append(adBean.getPid());
		stringBuffer.append("/mt");
		stringBuffer.append(adBean.getCreativeId());
		stringBuffer.append("/vi");
		if(isCovered){
			stringBuffer.append("0");
		}else{
			stringBuffer.append("100");
		}
		stringBuffer.append("/ip");
		stringBuffer.append("");
		stringBuffer.append("/ti");
		stringBuffer.append(String.valueOf(System.currentTimeMillis()));
		stringBuffer.append("/dv");
		stringBuffer.append(DeviceId.getDeviceID(YCPlatformInternal.getInstance().getSDKContext()));
		stringBuffer.append("/os");
		stringBuffer.append("2");
		stringBuffer.append("?ord=");
		stringBuffer.append(String.valueOf(new Random().nextInt(1000)));
		return stringBuffer.toString();
	}


	public String buildClickUrl(AdBean adBean,boolean isCovered) {
		StringBuffer stringBuffer=new StringBuffer();
		stringBuffer.append(Constants.YC_EXPOSE_URL);
		stringBuffer.append("/sy");
		stringBuffer.append("8");
		stringBuffer.append("/pc");
		stringBuffer.append(adBean.getPid());
		stringBuffer.append("/mt");
		stringBuffer.append(adBean.getCreativeId());
		stringBuffer.append("/vi");
		if(isCovered){
			stringBuffer.append("0");
		}else{
			stringBuffer.append("100");
		}
		stringBuffer.append("/ip");
		stringBuffer.append("");
		stringBuffer.append("/ti");
		stringBuffer.append(String.valueOf(System.currentTimeMillis()));
		stringBuffer.append("/dv");
		stringBuffer.append(DeviceId.getDeviceID(YCPlatformInternal.getInstance().getSDKContext()));
		stringBuffer.append("/os");
		stringBuffer.append("2");
		stringBuffer.append("?ord=");
		stringBuffer.append(String.valueOf(new Random().nextInt(1000)));
		return stringBuffer.toString();
	}


}
