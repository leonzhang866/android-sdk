package com.yiche.bigdata.ad.json;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.text.TextUtils;
import com.yiche.bigdata.ad.YCErrorCode;
import com.yiche.bigdata.ad.bean.AdBean;
import com.yiche.bigdata.ad.netresponse.AdResult;
import com.yiche.bigdata.ad.netresponse.BaseResult;
import com.yiche.bigdata.ad.utils.Utils;

/**
 * 数据解析类，sdk所有的tag数据解析都集中在此类中
 * 
 * @author wanglirong
 * 
 */
public final class JSONParser {

	
	public AdResult parseAD(String resData) throws JSONException{
		AdResult result = new AdResult();
        do
        {
            JSONObject jsonObj = new JSONObject(resData);
            int errorcode = jsonObj.optInt(JSONConstants.JSON_ERROR_CODE);
            String errorStr = jsonObj.optString(JSONConstants.JSON_ERROR_MESSAGE);
            result.setErrorCode(errorcode);
            result.setErrorString(errorStr);
            if (YCErrorCode.YC_OK != errorcode)
            {
                break;
            }
            JSONObject jsonContent=jsonObj.optJSONObject(JSONConstants.JSON_ERROR_CONTENT);
            String dvid=jsonContent.optString(JSONConstants.JSON_PARSE_DVID);
            result.setDvid(dvid);
            
            JSONArray jsonArray=jsonContent.optJSONArray(JSONConstants.JSON_PARSE_RESULT);
            if(jsonArray.length()>0){
            	List<AdBean> list=new ArrayList<AdBean>();
            	for(int i=0;i<jsonArray.length();i++){
            		JSONObject joAD=(JSONObject)jsonArray.get(i);
            		int errCode = joAD.optInt(JSONConstants.JSON_PARSE_STATUSCODE);
            		if(errCode==YCErrorCode.YC_AD_OK){
            			AdBean adBean =new AdBean();
                		adBean.setErrorCode(errCode);
                		adBean.setErrorString(joAD.optString(JSONConstants.JSON_PARSE_STATUSMSG));
                		adBean.setDvid(dvid);
                		adBean.setPid(joAD.optInt(JSONConstants.JSON_PARSE_PID));
                		adBean.setType(joAD.optInt(JSONConstants.JSON_PARSE_TYPE));
                		JSONObject joResult=joAD.optJSONObject(JSONConstants.JSON_PARSE_RESULT);
                		adBean.setCreativeId(joResult.optInt(JSONConstants.JSON_PARSE_CREATIVEID));
                		adBean.setTitle(joResult.optString(JSONConstants.JSON_PARSE_TITLE));
                		adBean.setUrl(joResult.optString(JSONConstants.JSON_PARSE_URL));
                		adBean.setExposureTp(joResult.optString(JSONConstants.JSON_PARSE_EXPOSURETP));
                		adBean.setClickTp(joResult.optString(JSONConstants.JSON_PARSE_CLICKTP));
                		String picUrls =joResult.optString(JSONConstants.JSON_PARSE_PICURL);
                		if(!TextUtils.isEmpty(picUrls)){
                			adBean.setPicUrls(picUrls.split(";"));
                		}
                		adBean.setResourceId(Utils.string2MD5(String.valueOf(System.nanoTime()+i)));
                		list.add(adBean);
            		}
            		result.setList(list);
            	}
            }
        } while (false);

        return result;

	}
	public BaseResult parseEffectiveExpose(String resData) throws JSONException{
		BaseResult result = new BaseResult();
		result.setErrorCode(2000);
        return result;

	}
	

}
