package com.yiche.ycanalytics.json;

import org.json.JSONException;
import org.json.JSONObject;
import com.yiche.ycanalytics.YCErrorCode;
import com.yiche.ycanalytics.netresponse.BaseResult;
import com.yiche.ycanalytics.netresponse.ConfigResult;

/**
 * 数据解析类，sdk所有的tag数据解析都集中在此类中
 * 
 * @author wanglirong
 * 
 */
public final class JSONParser {

	public BaseResult parseDefault(String resData) throws JSONException {
		BaseResult baseResult = new BaseResult();
		do {
			JSONObject jsonObj = new JSONObject(resData);
			int errorcode = jsonObj.optInt(JsonConstants.JSON_ERROR_CODE);
			baseResult.setErrorCode(errorcode);
			if (YCErrorCode.YC_OK != errorcode) {
				break;
			}
		} while (false);
		return baseResult;
	}
	public ConfigResult parseConfig(String resData) throws JSONException {
		ConfigResult configResult = new ConfigResult();
		do {
			JSONObject jsonObj = new JSONObject(resData);
			int errorcode = Integer.parseInt(jsonObj.optString("status"));
			configResult.setErrorCode(errorcode);
			if (YCErrorCode.YC_OK != errorcode) {
				break;
			}
			JSONObject jo=jsonObj.optJSONObject("result");
			if(jo!=null){
				configResult.setInterval(Integer.parseInt(jo.optString("upload_interval")));
				configResult.setScan_interval(Integer.parseInt(jo.optString("scan_interval")));
			}
		} while (false);
		return configResult;
	}

}
