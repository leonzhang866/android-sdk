package com.yiche.ycanalytics.json;

import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.os.Build;
import com.yiche.ycanalytics.YCPlatformInternal;
import com.yiche.ycanalytics.bean.EventBundle;
import com.yiche.ycanalytics.net.ConnectManager;
import com.yiche.ycanalytics.utils.Constants;
import com.yiche.ycanalytics.utils.DeviceId;
import com.yiche.ycanalytics.utils.MyLogger;
import com.yiche.ycanalytics.utils.Utils;

/**
 * 
 * json构造类 用途：发起联网前准备需要上传到中间层的参数
 * 
 * @author wanglirong
 * 
 */
public final class JSONBuilder{
	private MyLogger mLogger = MyLogger.getLogger(JSONBuilder.class.getName());
	
	public JSONObject buildBaseJsonObject() {
		JSONObject mJsonHeader = new JSONObject();
		try {
			mJsonHeader.put(JsonConstants.JSON_APPKEY, Utils.getAppKey(YCPlatformInternal.getInstance().getSDKContext()));
			mJsonHeader.put(JsonConstants.JSON_CID, Utils.getChannel(YCPlatformInternal.getInstance().getSDKContext()));
			mJsonHeader.put(JsonConstants.JSON_USERID, DeviceId.getDeviceID(YCPlatformInternal.getInstance().getSDKContext()));
			mJsonHeader.put(JsonConstants.JSON_MODEL, Build.MODEL);
			mJsonHeader.put(JsonConstants.JSON_SYSTEMVERSION,Build.VERSION.RELEASE);
			mJsonHeader.put(JsonConstants.JSON_SCALE, Utils.getResolution(YCPlatformInternal.getInstance().getSDKContext()));
			mJsonHeader.put(JsonConstants.JSON_BUILD, Utils.getGameVersion(YCPlatformInternal.getInstance().getSDKContext()));
			mJsonHeader.put(JsonConstants.JSON_TELECARRIER, Utils.getOperator(YCPlatformInternal.getInstance().getSDKContext()));
			mJsonHeader.put(JsonConstants.JSON_CONNECTTYPE, ConnectManager.GetNetworkType(YCPlatformInternal.getInstance().getSDKContext()));
			mJsonHeader.put(JsonConstants.JSON_PACKAGENAME, Utils.getPackageName(YCPlatformInternal.getInstance().getSDKContext()));
			mJsonHeader.put(JsonConstants.JSON_SDK_VERSION,Constants.YC_SDK_VERSION);
			mJsonHeader.put(JsonConstants.JSON_ISJAILBREAK, "0");
			mJsonHeader.put(JsonConstants.JSON_APPCRACK, "0");
			mJsonHeader.put(JsonConstants.JSON_PLATFORM, Constants.ANDROID);
			//1.0.1新增
			mJsonHeader.put(JsonConstants.JSON_UPDATETIME, Utils.getDataFromMillis(System.currentTimeMillis()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mJsonHeader;
	}

	public JSONObject buildInitJsonObject() {
		JSONObject mJson = new JSONObject();
		JSONObject mJsonHeader = buildBaseJsonObject();
		try {
			mJson.put(JsonConstants.JSON_DEVICEINFO, mJsonHeader);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mJson;
	}
	
	public JSONObject buildYCLoginJsonObject(String accountType,String userName) {
		JSONObject mJson = buildBaseJsonObject();
		JSONObject mJsonHeader = new JSONObject();
		try {
			mJson.put(JsonConstants.JSON_USERNAME, userName);
			mJson.put(JsonConstants.JSON_ACCOUNTTYPE, accountType);
			mJsonHeader.put(JsonConstants.JSON_YCLOGININFO, mJson);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return mJsonHeader;
	}
	
	public JSONObject buildThirdLoginJsonObject(String thirdType,String userName) {
		JSONObject mJson = buildBaseJsonObject();
		JSONObject mJsonHeader = new JSONObject();
		try {
			mJson.put(JsonConstants.JSON_USERNAME, userName);
			mJson.put(JsonConstants.JSON_THIRDTYPE, thirdType);
			mJsonHeader.put(JsonConstants.JSON_THIRDPARTYLOGININFO, mJson);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return mJsonHeader;
	}
	
	public JSONObject buildLocationInfoJsonObject(String latitude, String longitude) {
		JSONObject mJson = buildBaseJsonObject();
		JSONObject mJsonHeader = new JSONObject();
		try {
			mJson.put(JsonConstants.JSON_LATITUDE, latitude);
			mJson.put(JsonConstants.JSON_LONGITUDE, longitude);
			mJsonHeader.put(JsonConstants.JSON_LOCATIONINFO, mJson);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return mJsonHeader;
	}

	public Object buildBehaviourInfoJsonObject(JSONObject jsonObject) {
		JSONObject mJson = buildBaseJsonObject();
		JSONObject mJson2 = buildBaseJsonObject();//netinfo
		JSONObject mJson3 = buildBaseJsonObject();//apkinfo
		JSONObject mJsonHeader = new JSONObject();
		JSONArray jsonArray_net=jsonObject.optJSONArray(Constants.NETINFO);
		JSONArray jsonArray_behavior=jsonObject.optJSONArray(Constants.BEHAVIORINFO);
		JSONArray jsonArray_app=jsonObject.optJSONArray(Constants.APPINFO);
		try {
			mJson.put(JsonConstants.JSON_LIST, jsonArray_net);
			mJson2.put(JsonConstants.JSON_LIST,jsonArray_behavior);
			mJson3.put(JsonConstants.JSON_LIST,jsonArray_app);
			if(jsonArray_net.length()>0){
				mJsonHeader.put(JsonConstants.JSON_REQUESTINFO, mJson);
			}
			if(jsonArray_behavior.length()>0){
				mJsonHeader.put(JsonConstants.JSON_BEHAVIOURINFO, mJson2);
			}
			if(jsonArray_app.length()>0){
				mJsonHeader.put(JsonConstants.JSON_APPDURATION, mJson3);
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return mJsonHeader;
	}


	public Object buildNativeViewContentJsonObject(String pageName, Long duringTime) {
		JSONObject mJson = new JSONObject();
		try {
			mJson.put(JsonConstants.JSON_DURING , String.valueOf(duringTime/1000.000));
			mJson.put(JsonConstants.JSON_PAGENAME, pageName);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return mJson;
	}

	public Object buildStatisticsContentJsonObject(String eventName) {
		JSONObject mJson = new JSONObject();
		try {
			mJson.put(JsonConstants.JSON_STATISTICSNAME , eventName);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return mJson;
	}

	public Object buildCustomContentJsonObject(String eventName, List<EventBundle> eventList) {
		JSONObject mJson = new JSONObject();
		try {
			mJson.put(JsonConstants.JSON_STATISTICSNAME , eventName);
			for(int i=0;i<eventList.size();i++){
				mJson.put(eventList.get(i).getKey(), eventList.get(i).getValue());
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return mJson;
	}

	public Object buildWebViewContentJsonObject(String url, long duringTime) {
		JSONObject mJson = new JSONObject();
		try {
			mJson.put(JsonConstants.JSON_WEBURL , url);
			mJson.put(JsonConstants.JSON_DURING , String.valueOf(duringTime/1000.000));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return mJson;
	}

	public Object buildAPPRuntimeContentJsonObject(long duringTime) {
		JSONObject mJson = new JSONObject();
		try {
			mJson.put(JsonConstants.JSON_DURING , String.valueOf(duringTime/1000.000));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return mJson;
	}

	public JSONObject buildLoadParameterJsonObject() {
		JSONObject mJson = new JSONObject();
		try {
			mJson.put("ak",Utils.getAppKey(YCPlatformInternal.getInstance().getSDKContext()));
			mJson.put("id",DeviceId.getDeviceID(YCPlatformInternal.getInstance().getSDKContext()));
			mJson.put("sdkv",Constants.YC_SDK_VERSIONNAME );
			mJson.put("pf", "2");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return mJson;
	}

	public JSONObject buildAllAppJsonObject(JSONArray string) {
		JSONObject mJson = buildBaseJsonObject();
		JSONObject mJsonHeader = new JSONObject();
		try {
			mJson.put(JsonConstants.JSON_LIST, string);
			mJsonHeader.put(JsonConstants.JSON_ALLAPPINFO, mJson);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return mJsonHeader;
	}



}
