package com.yiche.bigdata.ad.json;

import org.json.JSONException;

import com.yiche.bigdata.ad.netresponse.BaseResult;
import com.yiche.bigdata.ad.utils.Constants;

/**
 * 解析帮助类,主要用于根据不同的tag调用各自的解析方法
 * 
 * @author wanglirong
 * 
 */
public final class JSONHelper
{
    public static BaseResult parserWithTag(int requestTag, String resData) throws JSONException
    {
        BaseResult res = null;

        switch (requestTag)
        {
            case Constants.NET_TAG_DEFAULT: 
                res = JSONManager.getJsonParser().parseAD(resData);
                break;
            case Constants.NET_TAG_EXPOSE:
            	res = JSONManager.getJsonParser().parseEffectiveExpose(resData);
            	break;
            
            default:
                break;
        }

        return res;
    }
}
